package org.zstack.sdk;

public class ChangePortForwardingRuleStateResult {
    public PortForwardingRuleInventory inventory;
    public void setInventory(PortForwardingRuleInventory inventory) {
        this.inventory = inventory;
    }
    public PortForwardingRuleInventory getInventory() {
        return this.inventory;
    }

}
