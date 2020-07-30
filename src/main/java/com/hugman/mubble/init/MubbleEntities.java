package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.EntityCreator;
import com.hugman.mubble.object.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class MubbleEntities extends MubblePack {
	/* MUBBLE */
	public static final EntityType<DuckEntity> DUCK = register(new EntityCreator.Builder("duck", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DuckEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.8F)).trackable(10, 3).build()).attributes(DuckEntity.createDuckAttributes()));
	public static final EntityType<ZombieCowmanEntity> ZOMBIE_COWMAN = register(new EntityCreator.Builder("zombie_cowman", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ZombieCowmanEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.95F)).trackable(8, 3).build()).attributes(ZombieCowmanEntity.createZombieCowmanAttributes()));

	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register(new EntityCreator.Builder("custom_tnt", FabricEntityTypeBuilder.<CustomTNTEntity>create(SpawnGroup.MISC, CustomTNTEntity::new).fireImmune().dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(10, 10).build()));
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register(new EntityCreator.Builder("flying_block", FabricEntityTypeBuilder.<FlyingBlockEntity>create(SpawnGroup.MISC, FlyingBlockEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(10, 20).build()));

	/* SUPER MARIO */
	public static final EntityType<ChinchoEntity> CHINCHO = register(new EntityCreator.Builder("chincho", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ChinchoEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.2F)).trackable(8, 3).build()).attributes(ChinchoEntity.createChinchoAttributes()));
	public static final EntityType<GoombaEntity> GOOMBA = register(new EntityCreator.Builder("goomba", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GoombaEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.625F)).trackable(8, 3).build()).attributes(GoombaEntity.createGoombaAttributes()));
	public static final EntityType<ToadEntity> TOAD = register(new EntityCreator.Builder("toad", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ToadEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.625F)).trackable(10, 3).build()).attributes(ToadEntity.createToadAttributes()));

	public static final EntityType<FireballEntity> FIREBALL = register(new EntityCreator.Builder("fireball", FabricEntityTypeBuilder.<FireballEntity>create(SpawnGroup.MISC, FireballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build()));
	public static final EntityType<IceballEntity> ICEBALL = register(new EntityCreator.Builder("iceball", FabricEntityTypeBuilder.<IceballEntity>create(SpawnGroup.MISC, IceballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build()));

	/* KIRBY */
	public static final EntityType<KirbyBallEntity> KIRBY_BALL = register(new EntityCreator.Builder("kirby_ball", FabricEntityTypeBuilder.<KirbyBallEntity>create(SpawnGroup.MISC, KirbyBallEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build()));
}