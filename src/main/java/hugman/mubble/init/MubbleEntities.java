package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.ChinchoEntity;
import hugman.mubble.objects.entity.CustomTNTEntity;
import hugman.mubble.objects.entity.FlyingBlockEntity;
import hugman.mubble.objects.entity.GoombaEntity;
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
    public static final List<EntityType<? extends Entity>> ENTITY_TYPES = new ArrayList<EntityType<? extends Entity>>();
    
	public static final EntityType<ChinchoEntity> CHINCHO = register("chincho", FabricEntityTypeBuilder.create(EntityCategory.MONSTER, ChinchoEntity::new).size(EntityDimensions.fixed(0.6F, 1.2F)).build());
	public static final EntityType<GoombaEntity> GOOMBA = register("goomba", FabricEntityTypeBuilder.create(EntityCategory.MONSTER, GoombaEntity::new).size(EntityDimensions.fixed(0.75F, 0.85F)).build());
	public static final EntityType<ToadEntity> TOAD = register("toad", FabricEntityTypeBuilder.create(EntityCategory.CREATURE, ToadEntity::new).size(EntityDimensions.fixed(0.6F, 1.4F)).build());
	public static final EntityType<ZombieCowmanEntity> ZOMBIE_COWMAN = register("zombie_cowman", FabricEntityTypeBuilder.create(EntityCategory.MONSTER, ZombieCowmanEntity::new).size(EntityDimensions.fixed(0.6F, 1.95F)).build());

	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register("custom_tnt", FabricEntityTypeBuilder.<CustomTNTEntity>create(EntityCategory.MISC, CustomTNTEntity::new).setImmuneToFire().size(EntityDimensions.fixed(0.98F, 0.98F)).trackable(128, 1).build());
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register("flying_block", FabricEntityTypeBuilder.<FlyingBlockEntity>create(EntityCategory.MISC, FlyingBlockEntity::new).size(EntityDimensions.fixed(0.98F, 0.98F)).trackable(128, 1).build());
	
	private static <T extends Entity> EntityType<T> register(String name, EntityType<T> builder)
	{
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(Mubble.MOD_ID, name), builder);
	}
}