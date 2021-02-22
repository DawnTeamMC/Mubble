package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.EntityCreator;
import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.mubble.object.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class MubbleEntities extends MubblePack {
	/* MUBBLE */
	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register(new EntityCreator.Builder<>("custom_tnt", FabricEntityTypeBuilder.<CustomTNTEntity>create(SpawnGroup.MISC, CustomTNTEntity::new).fireImmune().dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(10).trackedUpdateRate(10).forceTrackedVelocityUpdates(true).build()));
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register(new EntityCreator.Builder<>("flying_block", FabricEntityTypeBuilder.<FlyingBlockEntity>create(SpawnGroup.MISC, FlyingBlockEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(10).trackedUpdateRate(20).forceTrackedVelocityUpdates(true).build()));

	/* SUPER MARIO */
	public static final EntityType<ChinchoEntity> CHINCHO = register(new EntityCreator.Builder<>("chincho", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ChinchoEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.2F)).trackRangeChunks(8).trackedUpdateRate(3).build()).attributes(ChinchoEntity.createChinchoAttributes()));
	public static final Item CHINCHO_SPAWN_EGG = register(new ItemCreator.Builder("chincho_spawn_egg", new SpawnEggItem(MubbleEntities.CHINCHO, 7527671, 4903, MubbleItems.Settings.SPAWN_EGG)));
	public static final EntityType<GoombaEntity> GOOMBA = register(new EntityCreator.Builder<>("goomba", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GoombaEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.625F)).trackRangeChunks(8).trackedUpdateRate(3).build()).attributes(GoombaEntity.createGoombaAttributes()));
	public static final Item GOOMBA_SPAWN_EGG = register(new ItemCreator.Builder("goomba_spawn_egg", new SpawnEggItem(MubbleEntities.GOOMBA, 10839375, 12097909, MubbleItems.Settings.SPAWN_EGG)));
	public static final EntityType<ToadEntity> TOAD = register(new EntityCreator.Builder<>("toad", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ToadEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.625F)).trackRangeChunks(8).trackedUpdateRate(3).build()).attributes(ToadEntity.createToadAttributes()));
	public static final Item TOAD_SPAWN_EGG = register(new ItemCreator.Builder("toad_spawn_egg", new SpawnEggItem(MubbleEntities.TOAD, 14671839, 16722728, MubbleItems.Settings.SPAWN_EGG)));
	public static final EntityType<FireballEntity> FIREBALL = register(new EntityCreator.Builder<>("fireball", FabricEntityTypeBuilder.<FireballEntity>create(SpawnGroup.MISC, FireballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(4).trackedUpdateRate(10).build()));
	public static final EntityType<IceballEntity> ICEBALL = register(new EntityCreator.Builder<>("iceball", FabricEntityTypeBuilder.<IceballEntity>create(SpawnGroup.MISC, IceballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(4).trackedUpdateRate(10).build()));

	/* KIRBY */
	public static final EntityType<KirbyBallEntity> KIRBY_BALL = register(new EntityCreator.Builder<>("kirby_ball", FabricEntityTypeBuilder.<KirbyBallEntity>create(SpawnGroup.MISC, KirbyBallEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackRangeChunks(4).trackedUpdateRate(10).build()));

	public static void init() {

	}
}