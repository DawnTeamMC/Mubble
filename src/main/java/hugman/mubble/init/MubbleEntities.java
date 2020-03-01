package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

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
import hugman.mubble.objects.entity.render.ChinchoRenderer;
import hugman.mubble.objects.entity.render.CustomTNTRenderer;
import hugman.mubble.objects.entity.render.DuckRenderer;
import hugman.mubble.objects.entity.render.FlyingBlockRenderer;
import hugman.mubble.objects.entity.render.GoombaRenderer;
import hugman.mubble.objects.entity.render.ToadRenderer;
import hugman.mubble.objects.entity.render.ZombieCowmanRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MubbleEntities
{
    public static final List<EntityType<?>> ENTITY_TYPES = new ArrayList<EntityType<? extends Entity>>();

    /* MUBBLE */
	public static final EntityType<DuckEntity> DUCK = register("duck", EntityType.Builder.create(DuckEntity::new, EntityClassification.CREATURE).size(0.4F, 0.8F));
	public static final EntityType<ZombieCowmanEntity> ZOMBIE_COWMAN = register("zombie_cowman", EntityType.Builder.create(ZombieCowmanEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F));
	
	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register("custom_tnt", EntityType.Builder.<CustomTNTEntity>create(CustomTNTEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).setTrackingRange(10).setUpdateInterval(20));
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register("flying_block", EntityType.Builder.<FlyingBlockEntity>create(FlyingBlockEntity::new, EntityClassification.MISC).size(0.98F, 0.98F).setTrackingRange(10).setUpdateInterval(20));
	
	/* SUPER MARIO */
	public static final EntityType<ChinchoEntity> CHINCHO = register("chincho", EntityType.Builder.create(ChinchoEntity::new, EntityClassification.MONSTER).size(0.6F, 1.2F));
	public static final EntityType<GoombaEntity> GOOMBA = register("goomba", EntityType.Builder.create(GoombaEntity::new, EntityClassification.MONSTER).size(0.5F, 0.625F));
	public static final EntityType<ToadEntity> TOAD = register("toad", EntityType.Builder.create(ToadEntity::new, EntityClassification.CREATURE).size(0.6F, 1.4F));

	public static final EntityType<FireballEntity> FIREBALL = register("fireball", EntityType.Builder.<FireballEntity>create(FireballEntity::new, EntityClassification.MISC).size(0.98F, 0.98F).setTrackingRange(4).setUpdateInterval(10));
	public static final EntityType<IceballEntity> ICEBALL = register("iceball", EntityType.Builder.<IceballEntity>create(IceballEntity::new, EntityClassification.MISC).size(0.98F, 0.98F).setTrackingRange(4).setUpdateInterval(10));
	
	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder)
	{
	    EntityType<T> entitytype = builder.build(Mubble.MOD_PREFIX + id);
	    entitytype.setRegistryName(Mubble.MOD_ID, id);
	    ENTITY_TYPES.add(entitytype);
		return entitytype;
	}
    
    public static void registerPlacements()
    {
    	EntitySpawnPlacementRegistry.register(DUCK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
    	EntitySpawnPlacementRegistry.register(ZOMBIE_COWMAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieCowmanEntity::canSpawn);
    	
    	EntitySpawnPlacementRegistry.register(CHINCHO, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChinchoEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(TOAD, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ToadEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(GOOMBA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GoombaEntity::canSpawn);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void registerRenders()
    {
    	EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
    	ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    	manager.register(DUCK, new DuckRenderer(manager));
    	manager.register(ZOMBIE_COWMAN, new ZombieCowmanRenderer(manager));
    	manager.register(CUSTOM_TNT, new CustomTNTRenderer(manager));
    	manager.register(FLYING_BLOCK, new FlyingBlockRenderer(manager));
    	
    	manager.register(CHINCHO, new ChinchoRenderer(manager));
    	manager.register(GOOMBA, new GoombaRenderer(manager));
    	manager.register(TOAD, new ToadRenderer(manager));
    	manager.register(FIREBALL, new SpriteRenderer<>(manager, itemRenderer));
    	manager.register(ICEBALL, new SpriteRenderer<>(manager, itemRenderer));
    }
}