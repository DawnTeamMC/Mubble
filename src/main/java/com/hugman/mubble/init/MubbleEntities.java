package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.EntityCreator;
import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.mubble.object.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.Heightmap;

public class MubbleEntities extends MubblePack {
	/* SUPER MARIO */
	public static final EntityType<GoombaEntity> GOOMBA = register(new EntityCreator.Builder<>("goomba", FabricEntityTypeBuilder.createMob().entityFactory(GoombaEntity::new).dimensions(EntityDimensions.fixed(0.6F, 0.9F)).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn).trackRangeChunks(8).trackedUpdateRate(3).defaultAttributes(GoombaEntity::createGoombaAttributes).build()));
	public static final Item GOOMBA_SPAWN_EGG = register(new ItemCreator.Builder("goomba_spawn_egg", new SpawnEggItem(MubbleEntities.GOOMBA, 10839375, 12097909, MubbleItems.Settings.SPAWN_EGG)));
	public static final EntityType<ToadEntity> TOAD = register(new EntityCreator.Builder<>("toad", FabricEntityTypeBuilder.createMob().entityFactory(ToadEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.625F)).spawnGroup(SpawnGroup.CREATURE).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn).trackRangeChunks(8).trackedUpdateRate(3).defaultAttributes(ToadEntity::createToadAttributes).build()));
	public static final Item TOAD_SPAWN_EGG = register(new ItemCreator.Builder("toad_spawn_egg", new SpawnEggItem(MubbleEntities.TOAD, 14671839, 16722728, MubbleItems.Settings.SPAWN_EGG)));
	public static final EntityType<ChinchoEntity> CHINCHO = register(new EntityCreator.Builder<>("chincho", FabricEntityTypeBuilder.createMob().entityFactory(ChinchoEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.2F)).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn).trackRangeChunks(8).trackedUpdateRate(3).defaultAttributes(ChinchoEntity::createChinchoAttributes).build()));
	public static final Item CHINCHO_SPAWN_EGG = register(new ItemCreator.Builder("chincho_spawn_egg", new SpawnEggItem(MubbleEntities.CHINCHO, 7527671, 4903, MubbleItems.Settings.SPAWN_EGG)));
	public static final EntityType<FireballEntity> FIREBALL = register(new EntityCreator.Builder<>("fireball", FabricEntityTypeBuilder.<FireballEntity>create(SpawnGroup.MISC, FireballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(4).trackedUpdateRate(10).build()));
	public static final EntityType<IceballEntity> ICEBALL = register(new EntityCreator.Builder<>("iceball", FabricEntityTypeBuilder.<IceballEntity>create(SpawnGroup.MISC, IceballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(4).trackedUpdateRate(10).build()));

	/* KIRBY */
	public static final EntityType<KirbyBallEntity> KIRBY_BALL = register(new EntityCreator.Builder<>("kirby_ball", FabricEntityTypeBuilder.<KirbyBallEntity>create(SpawnGroup.MISC, KirbyBallEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(4).trackedUpdateRate(10).build()));

	public static void init() {
	}
}