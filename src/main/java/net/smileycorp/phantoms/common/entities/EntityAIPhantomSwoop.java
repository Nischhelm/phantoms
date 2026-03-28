package net.smileycorp.phantoms.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;
import net.smileycorp.phantoms.common.ConfigHandler;
import net.smileycorp.phantoms.common.PhantomsSoundEvents;

public class EntityAIPhantomSwoop extends EntityAIBase {

    private final EntityPhantom phantom;
    private int attackTimer = 0;
    private boolean playedSound = false;

    public EntityAIPhantomSwoop(EntityPhantom phantom) {
        this.phantom = phantom;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (!phantom.canTarget(phantom.getAttackTarget())) return false;
        return attackTimer-- <= 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!phantom.canTarget(phantom.getAttackTarget())) {
            phantom.setAttackTarget(null);
            return false;
        }
        return phantom.getAttackState() == EntityPhantom.AttackState.SWOOPING;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        phantom.setAttackState(EntityPhantom.AttackState.SWOOPING);
        playedSound = false;
        attackTimer = 20;
    }

    @Override
    public void resetTask() {
        phantom.setAttackState(EntityPhantom.AttackState.CIRCLING);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        EntityLivingBase target = phantom.getAttackTarget();
        double disSq = phantom.getDistanceSq(target);
        if (disSq < 225 &! playedSound) {
            phantom.playSound(PhantomsSoundEvents.PHANTOM_SWOOP, 10, 0.95f + phantom.getRNG().nextFloat() * 0.1f);
            playedSound = true;
        }
        if (!phantom.world.getEntitiesWithinAABB(Entity.class, phantom.getEntityBoundingBox().grow(8), ConfigHandler::repelsPhantoms).isEmpty()) {
            resetTask();
            return;
        }
        if (disSq < 4) {
            target.attackEntityFrom(DamageSource.causeMobDamage(phantom), (float) phantom.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            phantom.playSound(PhantomsSoundEvents.PHANTOM_BITE, 0.3f, 0.9f + phantom.getRNG().nextFloat() * 0.1f);
            resetTask();
            return;
        }
        if (!phantom.world.isAirBlock(phantom.getPosition().down())) {
            resetTask();
            return;
        }
        if (phantom.posY - target.posY + target.height * 0.5 < 0) {
            resetTask();
            return;
        }
        phantom.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 0.1);
    }

}
