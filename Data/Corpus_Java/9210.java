package org.zstack.test.applianceVm;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zstack.appliancevm.*;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.componentloader.ComponentLoader;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.header.configuration.InstanceOfferingInventory;
import org.zstack.header.core.ReturnValueCompletion;
import org.zstack.header.errorcode.ErrorCode;
import org.zstack.header.exception.CloudRuntimeException;
import org.zstack.header.identity.AccountConstant;
import org.zstack.header.image.ImageInventory;
import org.zstack.header.network.l3.L3NetworkInventory;
import org.zstack.header.vm.VmInstanceState;
import org.zstack.header.vm.VmNicInventory;
import org.zstack.test.Api;
import org.zstack.test.ApiSenderException;
import org.zstack.test.DBUtil;
import org.zstack.test.deployer.Deployer;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestDestroyApplianceVm {
    Deployer deployer;
    Api api;
    ComponentLoader loader;
    CloudBus bus;
    DatabaseFacade dbf;
    ApplianceVmFacade apvmf;
    ApplianceVmType type = new ApplianceVmType("TestApplianceVmType");
    CountDownLatch latch = new CountDownLatch(1);
    boolean success = false;
    ApplianceVmInventory target;

    @Before
    public void setUp() throws Exception {
        DBUtil.reDeployDB();
        deployer = new Deployer("deployerXml/applianceVm/TestCreateApplianceVm.xml");
        deployer.build();
        api = deployer.getApi();
        loader = deployer.getComponentLoader();
        bus = loader.getComponent(CloudBus.class);
        dbf = loader.getComponent(DatabaseFacade.class);
        apvmf = loader.getComponent(ApplianceVmFacade.class);
    }

    @Test
    public void test() throws ApiSenderException, InterruptedException {
        ApplianceVmSpec spec = new ApplianceVmSpec();

        L3NetworkInventory l31 = deployer.l3Networks.get("TestL3Network1");
        L3NetworkInventory l32 = deployer.l3Networks.get("TestL3Network2");
        L3NetworkInventory l33 = deployer.l3Networks.get("TestL3Network3");
        ImageInventory image = deployer.images.get("TestImage");
        InstanceOfferingInventory io = deployer.instanceOfferings.get("TestInstanceOffering");

        final ApplianceVmNicSpec mgmtNic = new ApplianceVmNicSpec();
        mgmtNic.setL3NetworkUuid(l31.getUuid());

        spec.setManagementNic(mgmtNic);
        spec.setName("testApplianceVm");
        spec.setAccountUuid(AccountConstant.INITIAL_SYSTEM_ADMIN_UUID);
        spec.setApplianceVmType(type);
        spec.setInstanceOffering(io);
        spec.setTemplate(image);
        spec.setApplianceVmType(type);

        final ApplianceVmNicSpec nic1 = new ApplianceVmNicSpec();
        nic1.setL3NetworkUuid(l32.getUuid());
        nic1.setAcquireOnNetwork(false);
        nic1.setGateway("10.10.2.1");
        nic1.setIp("10.10.2.10");
        nic1.setNetmask("255.0.0.0");
        spec.getAdditionalNics().add(nic1);

        final ApplianceVmNicSpec nic2 = new ApplianceVmNicSpec();
        nic2.setL3NetworkUuid(l33.getUuid());
        spec.getAdditionalNics().add(nic2);

        apvmf.createApplianceVm(spec, new ReturnValueCompletion<ApplianceVmInventory>(null) {
            private VmNicInventory findNic(List<VmNicInventory> lst, String l3Uuid) {
                for (VmNicInventory nic : lst) {
                    if (l3Uuid.equals(nic.getL3NetworkUuid())) {
                        return nic;
                    }
                }

                throw new CloudRuntimeException(String.format("cannot find nic on L3Network[uuid:%s]", l3Uuid));
            }

            @Override
            public void success(ApplianceVmInventory vm) {
                try {
                    findNic(vm.getVmNics(), mgmtNic.getL3NetworkUuid());
                    findNic(vm.getVmNics(), nic2.getL3NetworkUuid());
                    VmNicInventory vnic1 = findNic(vm.getVmNics(), nic1.getL3NetworkUuid());
                    Assert.assertEquals(nic1.getIp(), vnic1.getIp());
                    Assert.assertEquals(nic1.getGateway(), vnic1.getGateway());
                    Assert.assertEquals(nic1.getNetmask(), vnic1.getNetmask());
                    Assert.assertEquals(ApplianceVmConstant.APPLIANCE_VM_TYPE, vm.getType());
                    Assert.assertEquals(type.toString(), vm.getApplianceVmType());
                    Assert.assertEquals(VmInstanceState.Running.toString(), vm.getState());
                    success = true;
                    target = vm;
                } finally {
                    latch.countDown();
                }
            }

            @Override
            public void fail(ErrorCode errorCode) {
                success = false;
                latch.countDown();
            }
        });

        latch.await(30, TimeUnit.SECONDS);
        Assert.assertTrue(success);

        final CountDownLatch slatch = new CountDownLatch(1);
        apvmf.destroyApplianceVm(target.getUuid(), new ReturnValueCompletion<ApplianceVmInventory>(null) {
            @Override
            public void success(ApplianceVmInventory vm) {
                Assert.assertEquals(VmInstanceState.Destroyed.toString(), vm.getState());
                success = true;
                slatch.countDown();
            }

            @Override
            public void fail(ErrorCode errorCode) {
                success = false;
                slatch.countDown();
            }
        });
        slatch.await(30, TimeUnit.SECONDS);
        Assert.assertTrue(success);
    }
}
