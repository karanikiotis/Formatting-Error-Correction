package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualJumping extends Ritual
{
    public static final String JUMP_RANGE = "jumpRange";

    public RitualJumping()
    {
        super("ritualJump", 0, 5000, "ritual." + Constants.Mod.MODID + ".jumpRitual");
        addBlockRange(JUMP_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3, 1, 3));
        setMaximumVolumeAndDistanceOfRange(JUMP_RANGE, 0, 5, 5);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor jumpRange = getBlockRange(JUMP_RANGE);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, jumpRange.getAABB(masterRitualStone.getBlockPos()));
        if (entities != null)
        {
            for (EntityLivingBase entity : entities)
            {
                if (totalEffects >= maxEffects)
                {
                    break;
                }

                double motionY = 1.5;

                entity.fallDistance = 0;
                if (entity.isSneaking())
                {
                    continue;
                }

                entity.motionY = motionY;
                totalEffects++;

                if (entity instanceof EntityPlayer)
                {
                    Utils.setPlayerSpeedFromServer((EntityPlayer) entity, entity.motionX, entity.motionY, entity.motionZ);
                }
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(getRefreshCost() * totalEffects);
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 5;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        for (int i = -1; i <= 1; i++)
            this.addCornerRunes(components, 1, i, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualJumping();
    }
}
