package net.smileycorp.phantoms.common;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.smileycorp.phantoms.common.entities.EntityPhantom;

import java.util.Map;
import java.util.Random;

public class PhantomSpawner {

    private static final Map<Integer, PhantomSpawner> SPAWNERS = Maps.newHashMap();
    private final World world;
    private int nextSpawn;

    public PhantomSpawner(World world) {
        this.world = world;
        nextSpawn = ConfigHandler.spawnDelay;
        if (ConfigHandler.spawnDelayVariation > 0) nextSpawn += world.rand.nextInt(ConfigHandler.spawnDelayVariation);
    }

    public void trySpawn() {
        if (nextSpawn-- > 0) return;
        if (world.provider.hasSkyLight() && world.getSkylightSubtracted() < 11 - ConfigHandler.spawnLight) return;
        Random rand = world.rand;
        nextSpawn = ConfigHandler.spawnDelay;
        if (ConfigHandler.spawnDelayVariation > 0) nextSpawn += rand.nextInt(ConfigHandler.spawnDelayVariation);
        for (EntityPlayerMP player : world.getPlayers(EntityPlayerMP.class, EntitySelectors.NOT_SPECTATING)) {
            BlockPos pos = player.getPosition();
            if (!ConfigHandler.canSpawn(world.getBiome(pos))) return;
            if (world.provider.hasSkyLight() && (pos.getY() < world.getSeaLevel() |! world.canSeeSky(pos))) continue;
            DifficultyInstance difficulty = world.getDifficultyForLocation(pos);
            if (!difficulty.isHarderThan(rand.nextFloat() * 3f)) continue;
            NBTTagCompound nbt = player.getEntityData();
            if (!nbt.hasKey("ticksSinceLastSleep")) continue;
            int ticks = rand.nextInt((int) MathHelper.clamp(nbt.getLong("ticksSinceLastSleep"), 1, Integer.MAX_VALUE));
            if ((!ConfigHandler.invertSleeplessTicks && ticks < ConfigHandler.sleeplessTicks) ||
                    (ConfigHandler.invertSleeplessTicks && ticks >= ConfigHandler.sleeplessTicks)) return;
            BlockPos pos1 = pos.add(rand.nextInt(21) - 10, rand.nextInt(15) - 10 + ConfigHandler.phantomCircleHeight, rand.nextInt(21) - 10);
            if (!world.isAirBlock(pos1)) continue;
            for (int i = 0; i < ConfigHandler.minSpawns + rand.nextInt(Math.max((int)(world.getDifficulty().getDifficultyId() * ConfigHandler.extraSpawnsPerDifficulty), 1)); i++) {
                EntityPhantom phantom = new EntityPhantom(world);
                phantom.setPosition(pos1.getX() + 0.5f, pos1.getY(), pos1.getZ() + 0.5f);
                phantom.onInitialSpawn(difficulty, null);
                world.spawnEntity(phantom);
            }
        }
    }

    public static PhantomSpawner get(World world) {
        int dim = world.provider.getDimension();
        for (int id : ConfigHandler.spawnDimensions) if (dim == id) {
            PhantomSpawner spawner = SPAWNERS.get(dim);
            if (spawner == null) {
                spawner = new PhantomSpawner(world);
                SPAWNERS.put(dim, spawner);
            }
            return spawner;
        }
        return null;
    }

    public static void clear() {
        SPAWNERS.clear();
    }
    
}
