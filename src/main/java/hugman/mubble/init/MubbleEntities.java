package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleEntities
{
	/* MUBBLE */
	public static final EntityType<DuckEntity> DUCK = register("duck", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DuckEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.8F)).build());
	public static final EntityType<ZombieCowmanEntity> ZOMBIE_COWMAN = register("zombie_cowman", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ZombieCowmanEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).build());

	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register("custom_tnt", FabricEntityTypeBuilder.<CustomTNTEntity>create(SpawnGroup.MISC, CustomTNTEntity::new).fireImmune().dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(10, 20).build());
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register("flying_block", FabricEntityTypeBuilder.<FlyingBlockEntity>create(SpawnGroup.MISC, FlyingBlockEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(10, 20).build());

	/* SUPER MARIO */
	public static final EntityType<ChinchoEntity> CHINCHO = register("chincho", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ChinchoEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.2F)).build());
	public static final EntityType<GoombaEntity> GOOMBA = register("goomba", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GoombaEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.625F)).build());
	public static final EntityType<ToadEntity> TOAD = register("toad", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ToadEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.4F)).build());

	public static final EntityType<FireballEntity> FIREBALL = register("fireball", FabricEntityTypeBuilder.<FireballEntity>create(SpawnGroup.MISC, FireballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build());
	public static final EntityType<IceballEntity> ICEBALL = register("iceball", FabricEntityTypeBuilder.<IceballEntity>create(SpawnGroup.MISC, IceballEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build());

	/* KIRBY */
	public static final EntityType<KirbyBallEntity> KIRBY_BALL = register("kirby_ball", FabricEntityTypeBuilder.<KirbyBallEntity>create(SpawnGroup.MISC, KirbyBallEntity::new).dimensions(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build());

	public static <T extends Entity> EntityType<T> register(String name, EntityType<T> builder)
	{
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(Mubble.MOD_ID, name), builder);
	}

	public static void registerEntityAttributes()
	{
		FabricDefaultAttributeRegistry.register(DUCK, DuckEntity.createDuckAttributes());
		FabricDefaultAttributeRegistry.register(ZOMBIE_COWMAN, ZombieCowmanEntity.createZombieCowmanAttributes());

		FabricDefaultAttributeRegistry.register(CHINCHO, ChinchoEntity.createChinchoAttributes());
		FabricDefaultAttributeRegistry.register(GOOMBA, GoombaEntity.createGoombaAttributes());
		FabricDefaultAttributeRegistry.register(TOAD, ToadEntity.createToadAttributes());
	}
}