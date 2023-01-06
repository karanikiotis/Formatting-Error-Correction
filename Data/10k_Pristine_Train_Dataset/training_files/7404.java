package org.zstack.network.service.virtualrouter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.zstack.core.cascade.AbstractAsyncCascadeExtension;
import org.zstack.core.cascade.CascadeAction;
import org.zstack.core.cascade.CascadeConstant;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.cloudbus.CloudBusListCallBack;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.core.db.SimpleQuery;
import org.zstack.core.db.SimpleQuery.Op;
import org.zstack.header.configuration.ConfigurationConstant;
import org.zstack.header.configuration.InstanceOfferingDeletionMsg;
import org.zstack.header.core.Completion;
import org.zstack.header.image.ImageDeletionStruct;
import org.zstack.header.image.ImageVO;
import org.zstack.header.message.MessageReply;
import org.zstack.header.network.l3.L3NetworkInventory;
import org.zstack.header.network.l3.L3NetworkVO;
import org.zstack.utils.CollectionUtils;
import org.zstack.utils.function.Function;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

/**
 */
public class VirtualRouterOfferingCascadeExtension extends AbstractAsyncCascadeExtension {
    @Autowired
    private DatabaseFacade dbf;
    @Autowired
    private CloudBus bus;

    private static String NAME = VirtualRouterOfferingVO.class.getSimpleName();

    public void asyncCascade(CascadeAction action, Completion completion) {
        if (action.isActionCode(CascadeConstant.DELETION_CHECK_CODE)) {
            handleDeletionCheck(action, completion);
        } else if (action.isActionCode(CascadeConstant.DELETION_DELETE_CODE, CascadeConstant.DELETION_FORCE_DELETE_CODE)) {
            handleDeletion(action, completion);
        } else if (action.isActionCode(CascadeConstant.DELETION_CLEANUP_CODE)) {
            handleDeletionCleanup(action, completion);
        } else {
            completion.success();
        }
    }

    @Transactional
    private void deleteInstanceOfferingEONotReferredByVm() {
        String sql = "delete from InstanceOfferingEO i" +
                " where i.deleted is not null" +
                " and i.uuid not in" +
                " (" +
                " select vm.instanceOfferingUuid" +
                " from VmInstanceVO vm" +
                " where vm.instanceOfferingUuid is not null" +
                " )";
        Query q = dbf.getEntityManager().createQuery(sql);
        q.executeUpdate();
    }

    private void handleDeletionCleanup(CascadeAction action, Completion completion) {
        deleteInstanceOfferingEONotReferredByVm();
        completion.success();
    }

    private List<VirtualRouterOfferingInventory> offeringFromAction(CascadeAction action) {
        List<VirtualRouterOfferingInventory> ret = null;
        if (L3NetworkVO.class.getSimpleName().equals(action.getParentIssuer())) {
            List<L3NetworkInventory> l3s = action.getParentIssuerContext();
            List<String> l3uuids = CollectionUtils.transformToList(l3s, new Function<String, L3NetworkInventory>() {
                @Override
                public String call(L3NetworkInventory arg) {
                    return arg.getUuid();
                }
            });

            SimpleQuery<VirtualRouterOfferingVO> q = dbf.createQuery(VirtualRouterOfferingVO.class);
            q.add(VirtualRouterOfferingVO_.publicNetworkUuid, Op.IN, l3uuids);
            List<VirtualRouterOfferingVO> offeringVOs = q.list();

            q = dbf.createQuery(VirtualRouterOfferingVO.class);
            q.add(VirtualRouterOfferingVO_.managementNetworkUuid, Op.IN, l3uuids);
            List<VirtualRouterOfferingVO> lst = q.list();
            offeringVOs.addAll(lst);
            ret = VirtualRouterOfferingInventory.valueOf1(offeringVOs);
        } else if (ImageVO.class.getSimpleName().equals(action.getParentIssuer())) {
            List<String> imgUuids = CollectionUtils.transformToList(
                    (List<ImageDeletionStruct>) action.getParentIssuerContext(), new Function<String, ImageDeletionStruct>() {
                        @Override
                        public String call(ImageDeletionStruct arg) {
                            return arg.getDeleteAll() ? arg.getImage().getUuid() : null;
                        }
                    });

            SimpleQuery<VirtualRouterOfferingVO> q = dbf.createQuery(VirtualRouterOfferingVO.class);
            q.add(VirtualRouterOfferingVO_.imageUuid, Op.IN, imgUuids);
            List<VirtualRouterOfferingVO> offeringVOs = q.list();
            ret = VirtualRouterOfferingInventory.valueOf1(offeringVOs);
        }

        if (ret != null && !ret.isEmpty()) {
            return ret;
        } else {
            return null;
        }
    }

    private void handleDeletion(final CascadeAction action, final Completion completion) {
        List<VirtualRouterOfferingInventory> offering = offeringFromAction(action);
        if (offering == null) {
            completion.success();
            return;
        }

        List<String> offeringUuids = CollectionUtils.transformToList(offering, new Function<String, VirtualRouterOfferingInventory>() {
            @Override
            public String call(VirtualRouterOfferingInventory arg) {
                return arg.getUuid();
            }
        });

        List<InstanceOfferingDeletionMsg> msgs = CollectionUtils.transformToList(
                offeringUuids, new Function<InstanceOfferingDeletionMsg, String>() {
                    @Override
                    public InstanceOfferingDeletionMsg call(String arg) {
                        InstanceOfferingDeletionMsg msg = new InstanceOfferingDeletionMsg();
                        msg.setInstanceOfferingUuid(arg);
                        msg.setForceDelete(action.isActionCode(CascadeConstant.DELETION_FORCE_DELETE_CODE));
                        bus.makeTargetServiceIdByResourceUuid(msg, ConfigurationConstant.SERVICE_ID, arg);
                        return msg;
                    }
                });

        bus.send(msgs, new CloudBusListCallBack(completion) {
            @Override
            public void run(List<MessageReply> replies) {
                if (!action.isActionCode(CascadeConstant.DELETION_FORCE_DELETE_CODE)) {
                    for (MessageReply r : replies) {
                        if (!r.isSuccess()) {
                            completion.fail(r.getError());
                            return;
                        }
                    }
                }

                completion.success();
            }
        });
    }

    private void handleDeletionCheck(CascadeAction action, Completion completion) {
        completion.success();
    }

    @Override
    public List<String> getEdgeNames() {
        return Arrays.asList(L3NetworkVO.class.getSimpleName(), ImageVO.class.getSimpleName());
    }

    @Override
    public String getCascadeResourceName() {
        return NAME;
    }

    @Override
    public CascadeAction createActionForChildResource(CascadeAction action) {
        if (CascadeConstant.DELETION_CODES.contains(action.getActionCode())) {
            List<VirtualRouterOfferingInventory> ctx = offeringFromAction(action);
            if (ctx != null) {
                return action.copy().setParentIssuer(VirtualRouterOfferingVO.class.getSimpleName()).setParentIssuerContext(ctx);
            }
        }

        return null;
    }
}
