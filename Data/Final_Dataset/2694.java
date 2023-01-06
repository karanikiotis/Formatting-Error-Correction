package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockLandingPad extends ItemBlockDesc
{
    public ItemBlockLandingPad(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        String name = "";

        switch (par1ItemStack.getItemDamage())
        {
        case 0:
            name = "landing_pad";
            break;
        case 1:
            name = "buggy_fueler";
            break;
        case 2:
            name = "cargo_pad";
            break;
        }

        return this.getBlock().getUnlocalizedName() + "." + name;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote && stack.getItemDamage() == 0 && player instanceof EntityPlayerSP)
        {
            ClientProxyCore.playerClientHandler.onBuild(5, (EntityPlayerSP) player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
