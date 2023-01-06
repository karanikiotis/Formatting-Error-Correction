package WayofTime.bloodmagic.alchemyArray;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.iface.IAlchemyArray;

public class AlchemyArrayEffectMovement extends AlchemyArrayEffect
{
    public AlchemyArrayEffectMovement(String key)
    {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, IBlockState state, Entity entity)
    {
        double motionY = 0.5;
        double speed = 3;
        EnumFacing direction = array.getRotation();

        entity.motionY = motionY;
        entity.fallDistance = 0;

        switch (direction)
        {
        case NORTH:
            entity.motionX = 0;
            entity.motionY = motionY;
            entity.motionZ = -speed;
            break;

        case SOUTH:
            entity.motionX = 0;
            entity.motionY = motionY;
            entity.motionZ = speed;
            break;

        case WEST:
            entity.motionX = -speed;
            entity.motionY = motionY;
            entity.motionZ = 0;
            break;

        case EAST:
            entity.motionX = speed;
            entity.motionY = motionY;
            entity.motionZ = 0;
            break;
        default:
            break;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {

    }

    @Override
    public AlchemyArrayEffect getNewCopy()
    {
        return new AlchemyArrayEffectMovement(key);
    }
}
