package net.smileycorp.phantoms.common.entities;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.entity.ai.FlyingMoveControl;
import net.smileycorp.phantoms.common.ConfigHandler;
import net.smileycorp.phantoms.common.Constants;
import net.smileycorp.phantoms.common.PhantomsSoundEvents;

public class EntityPhantom extends EntityFlying implements IMob {

    private static final DataParameter<Integer> SIZE = EntityDataManager.<Integer>createKey(EntityPhantom.class, DataSerializers.VARINT);

    private AttackState attackState = AttackState.CIRCLING;
    private BlockPos targetPos = BlockPos.ORIGIN;

    public EntityPhantom(World world) {
        super(world);
        moveHelper = new FlyingMoveControl(this);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SIZE, 0);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(1, new EntityAIPhantomSwoop(this));
        tasks.addTask(2, new EntityAIPhantomCircle(this));
        targetTasks.addTask(1, new EntityAIPhantomTarget(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        ConfigHandler.phantomAttributes.applyAttributes(this);
        setSize(0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            float offset = getEntityId() * 3f + ticksExisted;
            float flap = (float) Math.cos(offset * 7.448451f * (float)Math.PI / 180f + (float)Math.PI);
            float nextFlap = (float) Math.cos((offset + 1)* 7.448451f * (float)Math.PI / 180f + (float)Math.PI);
            if (flap > 0 && nextFlap <= 0) world.playSound(posX, posY, posZ, PhantomsSoundEvents.PHANTOM_FLAP, getSoundCategory(),
                    0.95f + rand.nextFloat() * 0.05f, 0.95f + rand.nextFloat() * 0.05f, false);
            float angle = rotationYawHead * (float) Math.PI / 180f;
            float wingX = (float) Math.cos(angle) * (1.3f + 0.21f * getSize());
            float wingY = (0.3f - flap * 0.45f) * (getSize() * 0.2f + 1f);
            float wingZ = (float) Math.sin(angle) * (1.3f + 0.21f * getSize());
            world.spawnParticle(EnumParticleTypes.TOWN_AURA, posX + wingX, posY + wingY, posZ + wingZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.TOWN_AURA, posX - wingX, posY + wingY, posZ - wingZ, 0, 0, 0);
            return;
        }
        getLookHelper().setLookPosition(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ(), 180, 90);
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL) setDead();
        if (!isEntityAlive() |! world.isDaytime() |! ConfigHandler.phantomsBurn) return;
        float light = getBrightness();
        if (light <= 0.5 |! world.canSeeSky(new BlockPos(posX, posY + getEyeHeight(), posZ))) return;
        if (rand.nextFloat() * 30f >= (light - 0.4f) * 2f);
        ItemStack helm = getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helm.isEmpty()) {
            setFire(8);
            return;
        }
        if (helm.isItemStackDamageable()) helm.setItemDamage(helm.getItemDamage() + rand.nextInt(2));
    }

    public boolean canTarget(EntityLivingBase target) {
        if (target == null) return false;
        return target instanceof EntityPlayer && getDistanceSq(target) < 4096 && target.isEntityAlive() &! ((EntityPlayer) target).isSpectator()
                &! ((EntityPlayer) target).isCreative() && canEntityBeSeen(target) &! (getTeam() != null && getTeam().isSameTeam(target.getTeam()));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (getAttackState() == AttackState.SWOOPING) setAttackState(AttackState.CIRCLING);
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {

        return super.attackEntityAsMob(entity);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return source != DamageSource.DROWN && super.isEntityInvulnerable(source);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() { return EnumCreatureAttribute.UNDEAD; }

    @Override
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        return type == EnumCreatureType.MONSTER;
    }


    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return PhantomsSoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PhantomsSoundEvents.PHANTOM_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return PhantomsSoundEvents.PHANTOM_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {}

    @Override
    protected ResourceLocation getLootTable() {
        return Constants.PHANTOM_DROPS;
    }

    public void setSize(int size) {
        size = MathHelper.clamp(size, 0, 64);
        dataManager.set(SIZE, size);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(size == 0 ? 2 : 6 + size);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 + size * 4);
        float scale = 0.9f + (0.2f * size / 0.9f);
        setSize(0.9f * scale, 0.5f * scale);
        experienceValue = (size + 1) * 5;
    }

    public int getSize() {
        return dataManager.get(SIZE);
    }

    public AttackState getAttackState() {
        return attackState;
    }

    public void setAttackState(AttackState attackState) {
        this.attackState = attackState;
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(BlockPos targetPos) {
        this.targetPos = targetPos;
    }


    public enum AttackState {
        CIRCLING,
        SWOOPING;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("Size")) setSize(nbt.getInteger("Size"));
        int ax = 0, ay = 0, az = 0;
        if (nbt.hasKey("AX")) ax = nbt.getInteger("AX");
        if (nbt.hasKey("AY")) ay = nbt.getInteger("AY");
        if (nbt.hasKey("AZ")) az = nbt.getInteger("AZ");
        targetPos = new BlockPos(ax, ay, az);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Size", getSize());
        nbt.setInteger("AX", targetPos.getX());
        nbt.setInteger("AY", targetPos.getY());
        nbt.setInteger("AZ", targetPos.getZ());
    }

}
