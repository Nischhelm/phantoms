package net.smileycorp.phantoms.common.entities;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.Random;

public class EntityAIPhantomCircle extends EntityAIBase {

    private final EntityPhantom phantom;
    private Vec3d nextPos;
    private float angle;
    private float radius;
    private float height;
    private boolean clockwise;

    public EntityAIPhantomCircle(EntityPhantom phantom) {
        this.phantom = phantom;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return phantom.getAttackTarget() == null || phantom.getAttackState() == EntityPhantom.AttackState.CIRCLING;
    }

    @Override
    public void startExecuting() {
        Random rand = phantom.getRNG();
        BlockPos target = phantom.getTargetPos();
        if (target.equals(BlockPos.ORIGIN)) angle = 360 * rand.nextFloat();
        else {
            double dx = phantom.posX - target.getX() + 0.5;
            double dz = phantom.posZ - target.getZ() + 0.5;
            angle = (float) Math.atan2(dz, dx);
        }
        radius = rand.nextFloat() * 10f + 5;
        height = rand.nextFloat() * 9f - 4;
        clockwise = rand.nextBoolean();
        findNext();
    }

    @Override
    public void updateTask() {
        Random rand = phantom.getRNG();
        if (rand.nextInt(115) == 0) height = rand.nextFloat() * 9f - 4;
        if (rand.nextInt(80) == 0) {
            radius++;
            if (radius >= 15) {
                radius = 5;
                clockwise = !clockwise;
            }
        }
        if (rand.nextInt(150) == 0) {
            angle = 360 * rand.nextFloat();
            findNext();
        }
        if (phantom.getDistanceSq(nextPos.x, nextPos.y, nextPos.z) <= 4) findNext();
        World world = phantom.world;
        if (nextPos.y < phantom.posY &! world.isAirBlock(phantom.getPosition().down())) {
            height = Math.max(1, height);
            findNext();
        }
        if (nextPos.y > phantom.posY &! world.isAirBlock(phantom.getPosition().up())) {
            height = Math.min(-1, height);
            findNext();
        }
        phantom.getMoveHelper().setMoveTo(nextPos.x, nextPos.y, nextPos.z, 0.1);
    }

    private void findNext() {
        if (phantom.getTargetPos().equals(BlockPos.ORIGIN)) phantom.setTargetPos(phantom.getPosition());
        angle = MathHelper.wrapDegrees(angle + (clockwise ? -15 : 15));
        BlockPos target = phantom.getTargetPos();
        nextPos = new Vec3d(target.getX() + 0.5, target.getY() + height -4, target.getZ() + 0.5)
                .add(DirectionUtils.getDirectionVecXZDegrees(angle).scale(radius));
    }

}
