package net.smileycorp.phantoms.common.entities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.smileycorp.phantoms.common.ConfigHandler;

import java.util.List;

public class EntityAIPhantomTarget extends EntityAIBase {

    private final EntityPhantom phantom;
    private int targetTicks = 10;

    public EntityAIPhantomTarget(EntityPhantom phantom) {
        this.phantom = phantom;
    }

    @Override
    public boolean shouldExecute() {
        if (targetTicks > 0) {
            targetTicks--;
            return false;
        }
        targetTicks = 60;
        double range = phantom.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
        List<EntityPlayer> players = phantom.world.getEntitiesWithinAABB(EntityPlayer.class,
                phantom.getEntityBoundingBox().grow(range, 64, range), phantom::canTarget);
        if (players.isEmpty()) return false;
        EntityPlayer target = players.stream().min((p1, p2) -> Double.compare(p2.posY, p1.posY)).get();
        phantom.setAttackTarget(target);
        phantom.setTargetPos(target.getPosition().up(ConfigHandler.phantomCircleHeight));
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (phantom.canTarget(phantom.getAttackTarget())) return true;
        phantom.setAttackTarget(null);
        return false;
    }

}
