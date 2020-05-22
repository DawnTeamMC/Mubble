package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.ChinchoEntity;
import hugman.mubble.objects.entity.CustomTNTEntity;
import hugman.mubble.objects.entity.DuckEntity;
import hugman.mubble.objects.entity.FireballEntity;
import hugman.mubble.objects.entity.FlyingBlockEntity;
import hugman.mubble.objects.entity.GoombaEntity;
import hugman.mubble.objects.entity.IceballEntity;
import hugman.mubble.objects.entity.ToadEntity;
import hugman.mubble.objects.entity.ZombieCowmanEntity;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleEntities
{
    /* MUBBLE */
	public static final EntityType<DuckEntity> DUCK = register("duck", FabricEntityTypeBuilder.create(DuckEntity::new, EntityCategory.CREATURE).size(0.4F, 0.8F));
	public static final EntityType<ZombieCowmanEntity> ZOMBIE_COWMAN = register("zombie_cowman", FabricEntityTypeBuilder.create(EntityCategory.MONSTER, ZombieCowmanEntity::new).size(EntityDimensions.fixed(0.6F, 1.95F)).build());
	
	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register("custom_tnt", FabricEntityTypeBuilder.<CustomTNTEntity>create(EntityCategory.MISC, CustomTNTEntity::new).setImmuneToFire().size(EntityDimensions.fixed(0.98F, 0.98F)).trackable(10, 20).build());
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register("flying_block", FabricEntityTypeBuilder.<FlyingBlockEntity>create(EntityCategory.MISC, FlyingBlockEntity::new).size(EntityDimensions.fixed(0.98F, 0.98F)).trackable(10, 20).build());
	
	/* SUPER MARIO */
	public static final EntityType<ChinchoEntity> CHINCHO = register("chincho", FabricEntityTypeBuilder.create(EntityCategory.MONSTER, ChinchoEntity::new).size(EntityDimensions.fixed(0.6F, 1.2F)).build());
	public static final EntityType<GoombaEntity> GOOMBA = register("goomba", FabricEntityTypeBuilder.create(EntityCategory.MONSTER, GoombaEntity::new).size(EntityDimensions.fixed(0.5F, 0.625F)).build());
	public static final EntityType<ToadEntity> TOAD = register("toad", FabricEntityTypeBuilder.create(EntityCategory.CREATURE, ToadEntity::new).size(EntityDimensions.fixed(0.6F, 1.4F)).build());
	
	public static final EntityType<FireballEntity> FIREBALL = register("fireball", FabricEntityTypeBuilder.<FireballEntity>create(EntityCategory.MISC, FireballEntity::new).size(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build());
	public static final EntityType<IceballEntity> ICEBALL = register("iceball", FabricEntityTypeBuilder.<IceballEntity>create(EntityCategory.MISC, IceballEntity::new).size(EntityDimensions.fixed(0.98F, 0.98F)).trackable(4, 10).build());
	
	public static <T extends Entity> EntityType<T> register(String name, EntityType<T> builder)
	{
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(Mubble.MOD_ID, name), builder);
	}
}