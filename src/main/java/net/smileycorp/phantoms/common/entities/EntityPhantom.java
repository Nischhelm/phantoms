package net.smileycorp.phantoms.common.entities;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.smileycorp.phantoms.common.ConfigHandler;
import net.smileycorp.phantoms.common.Constants;
import net.smileycorp.phantoms.common.PhantomsSoundEvents;

public class EntityPhantom extends EntityFlying implements IMob {

    private static final DataParameter<Integer> SIZE = EntityDataManager.<Integer>createKey(EntityPhantom.class, DataSerializers.VARINT);

    public EntityPhantom(World world) {
        super(world);
        moveHelper = new EntityFlyHelper(this);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SIZE, 0);
        setSize(0);
    }

    @Override
    protected void applyEntityAttributes() {
        ConfigHandler.phantomAttributes.applyAttributes(this);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) return;
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

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return source != DamageSource.DROWN && super.isEntityInvulnerable(source);
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() { return EnumCreatureAttribute.UNDEAD; }

    @Override
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        return type == EnumCreatureType.MONSTER;
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

}
