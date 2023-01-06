package WayofTime.bloodmagic.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.api.ritual.CapabilityRuneType;
import WayofTime.bloodmagic.api.ritual.IRitualStone;
import WayofTime.bloodmagic.api.teleport.TeleportQueue;
import WayofTime.bloodmagic.fuel.FuelHandler;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelperV2;

import com.google.common.collect.ImmutableMap;

public class CommonProxy
{
    @Deprecated
    public InventoryRenderHelper getRenderHelper()
    {
        return null;
    }

    public InventoryRenderHelperV2 getRenderHelperV2()
    {
        return null;
    }

    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(TeleportQueue.getInstance());
        GameRegistry.registerFuelHandler(new FuelHandler());
        registerRenderers();
    }

    public void init()
    {
        CapabilityManager.INSTANCE.register(IRitualStone.Tile.class, new CapabilityRuneType.RuneTypeStorage(), new CapabilityRuneType.Factory());
    }

    public void postInit()
    {

    }

    public void registerRenderers()
    {

    }

    public Object beamCont(World worldObj, double xi, double yi, double zi, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void tryHandleBlockModel(Block block, String name)
    {
        // NO-OP
    }

    public void tryHandleItemModel(Item item, String name)
    {
        // NO-OP
    }

    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters)
    {
        return null;
    }
}
