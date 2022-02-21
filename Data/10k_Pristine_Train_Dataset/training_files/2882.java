package io.mewbase.example.retail;

import io.mewbase.bson.BsonObject;
import io.mewbase.bson.BsonPath;
import io.mewbase.server.Binder;
import io.mewbase.server.Channel;
import io.mewbase.server.Mewbase;
import io.mewbase.server.Mewblet;
import io.vertx.core.http.HttpMethod;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Created by tim on 11/01/17.
 */
public class OrderServiceMewblet implements Mewblet {

    private static class Event {

        protected final BsonObject bsonObject;

        public Event(BsonObject bsonObject) {
            this.bsonObject = bsonObject;
        }

        public Event(String eventType) {
            bsonObject = new BsonObject();
            bsonObject.put("eventType", eventType);
        }

        public String getEventType() {
            return bsonObject.getString("eventType");
        }

        public BsonObject getBsonObject() {
            return bsonObject;
        }

    }

    private static class AddItemEvent extends Event {


        public AddItemEvent(BsonObject bsonObject) {
            super(bsonObject);
        }

        public AddItemEvent() {
            super(ADD_ITEM_EVENT_TYPE);
        }

        public String getCustomerID() {
            return bsonObject.getString("customerID");
        }

        public String getProductID() {
            return bsonObject.getString("productID");
        }

        public Integer getQuantity() {
            return bsonObject.getInteger("quantity");
        }


        public void setCustomerID(String customerID) {
            bsonObject.put("customerID", customerID);
        }

        public void setProductID(String productID) {
            bsonObject.put("productID", productID);
        }

        public void setQuantity(Integer quantity) {
            bsonObject.put("quantity", quantity);
        }

    }

    private static class OrderPlacedEvent extends Event {


        public OrderPlacedEvent(BsonObject bsonObject) {
            super(bsonObject);
        }

        public OrderPlacedEvent() {
            super(ADD_ITEM_EVENT_TYPE);
        }

        public void setCustomerID(String customerID) {
            bsonObject.put("customerID", customerID);
        }

        public void setOrder(BsonObject order) {
            bsonObject.put("order", order);
        }

        public void setOrderID(String orderID) {
            bsonObject.put("orderID", orderID);
        }
    }

    private static class Command {

        protected final BsonObject bsonObject;

        public Command(BsonObject bsonObject) {
            this.bsonObject = bsonObject;
        }

        public Command() {
            bsonObject = new BsonObject();
        }

    }

    private static class AddItemCommand extends Command {

        public AddItemCommand(BsonObject bsonObject) {
            super(bsonObject);
        }

        public String getCustomerID() {
            return bsonObject.getString("customerID");
        }

        public String getProductID() {
            return bsonObject.getString("productID");
        }

        public Integer getQuantity() {
            return bsonObject.getInteger("quantity");
        }
    }

    private static class PlaceOrderCommand extends Command {

        public PlaceOrderCommand(BsonObject bsonObject) {
            super(bsonObject);
        }

        public String getCustomerID() {
            return bsonObject.getString("customerID");
        }
    }


    private class Basket {

        private BsonObject bsonObject;

        public Basket(BsonObject bsonObject) {
            this.bsonObject = bsonObject;
        }

        public String getBasketID(BsonObject basket) {
            return bsonObject.getString("basketID");
        }

        public BsonObject incrementQuantity(String productID, Integer quantity) {
            return BsonPath.add(bsonObject, quantity, "products", productID);
        }
    }

    private static final String ORDERS_CHANNEL_NAME = "orders";
    private static final String BASKETS_BINDER_NAME = "baskets";

    private static final String ADD_ITEM_EVENT_TYPE = "addItem";
    private static final String ORDER_PLACED_EVENT_TYPE = "orderPlaced";


    @Override
    public void setup(Mewbase mewbase) throws Exception {

        mewbase.createChannel(ORDERS_CHANNEL_NAME).get();
        mewbase.createBinder(BASKETS_BINDER_NAME).get();

        Channel ordersChannel = mewbase.getChannel(ORDERS_CHANNEL_NAME);
        Binder basketsBinder = mewbase.getBinder(BASKETS_BINDER_NAME);

        mewbase.buildProjection("maintain_basket")                    // projection name
                .projecting(ordersChannel.getName())                               // channel name
                .filteredBy(ev -> new Event(ev).getEventType().equals(ADD_ITEM_EVENT_TYPE)) // event filter
                .onto(basketsBinder.getName())                                     // binder name
                .identifiedBy(ev -> new AddItemEvent(ev).getCustomerID())          // document id selector; how to obtain the doc id from the event bson
                .as((b, del) -> {
                    // projection function
                    Basket basket = new Basket(b);
                    AddItemEvent aie = new AddItemEvent(del.event());
                    return basket.incrementQuantity(aie.getProductID(), aie.getQuantity());
                })
                .create();


        mewbase.buildCommandHandler("addItem")
                .emittingTo(ORDERS_CHANNEL_NAME)
                .as((c, ctx) -> {
                    AddItemCommand command = new AddItemCommand(c);
                    AddItemEvent ev = new AddItemEvent();
                    ev.setCustomerID(command.getCustomerID());
                    ev.setProductID(command.getProductID());
                    ev.setQuantity(command.getQuantity());
                    ctx.publishEvent(ev.getBsonObject()).complete();
                })
                .create();

        mewbase.buildCommandHandler("placeOrder")
                .emittingTo(ORDERS_CHANNEL_NAME)
                .as((c, ctx) -> {
                    PlaceOrderCommand command = new PlaceOrderCommand(c);
                    OrderPlacedEvent event = new OrderPlacedEvent();
                    event.setCustomerID(command.getCustomerID());
                    //CommandContext.putFields(command, event, "customerID");
                    // Retrieve the basket and add it to the event
                    basketsBinder.get("customerID").whenComplete((basket, t) -> {
                        if (t == null) {
                            String orderID = UUID.randomUUID().toString();
                            event.setOrderID(orderID);
                            event.setOrder(basket);
                            ctx.publishEvent(event.getBsonObject()).complete();
                        } else {
                            // TODO fail context
                        }
                    });
                })
                .create();

        mewbase.buildQuery("allBaskets")
                .from("baskets")
                .documentFilter((doc, ctx) -> true)
                .create();

        mewbase
                .exposeCommand("addItem", "/baskets/:customerID/", HttpMethod.PATCH)
                .exposeCommand("placeOrder", "/orders/:customerID/", HttpMethod.POST)
                .exposeQuery("allBaskets", "/baskets/")
                .exposeFindByID("baskets", "/baskets/:customerID/");

    }

    private CompletableFuture<Void> sendPickCommand(BsonObject state) {
        // Send a pick order command
        // TODO
        return null;
    }

    private CompletableFuture<Void> sendDeliverCommand(BsonObject state) {
        // Send a pick order command
        // TODO
        return null;
    }
}
