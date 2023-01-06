package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.IFuelHandler;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;

import com.google.common.base.Strings;

public class ItemLavaCrystal extends ItemBindableBase implements IFuelHandler, IVariantProvider
{
    public ItemLavaCrystal()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".lavaCrystal");
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        NetworkHelper.getSoulNetwork(this.getOwnerUUID(itemStack)).syphon(25);
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public int getBurnTime(ItemStack fuel)
    {
        if (fuel == null)
        {
            return 0;
        }

        Item fuelItem = fuel.getItem();

        if (fuelItem instanceof ItemLavaCrystal)
        {
//
//            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
//            {
//                return 200;
//            }
//            System.out.println(FMLCommonHandler.instance().getSide());

            if (NetworkHelper.canSyphonFromContainer(fuel, 25))
            {
                return 200;
            } else
            {
                if (!Strings.isNullOrEmpty(this.getOwnerUUID(fuel)))
                {
                    EntityPlayer player = PlayerHelper.getPlayerFromUUID(this.getOwnerUUID(fuel));
                    if (player != null)
                    {
                        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 99));
                    }
                }

                return 0;
            }
        }

        return 0;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }
}
