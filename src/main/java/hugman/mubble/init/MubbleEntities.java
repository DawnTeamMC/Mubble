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
import hugman.mubble.objects.entity.render.ChinchoRender;
import hugman.mubble.objects.entity.render.CustomTNTRender;
import hugman.mubble.objects.entity.render.FlyingBlockRender;
import hugman.mubble.objects.entity.render.GoombaRender;
import hugman.mubble.objects.entity.render.ToadRender;
import hugman.mubble.objects.entity.render.ZombieCowmanRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class MubbleEntities
{
    public static final List<EntityType<? extends Entity>> ENTITY_TYPES = new ArrayList<EntityType<? extends Entity>>();
    
	public static final EntityType<ChinchoEntity> CHINCHO = register("chincho", EntityType.Builder.create(ChinchoEntity::new, EntityClassification.MONSTER).size(0.6F, 1.2F));
	public static final EntityType<GoombaEntity> GOOMBA = register("goomba", EntityType.Builder.create(GoombaEntity::new, EntityClassification.MONSTER).size(0.75F, 0.85F));
	public static final EntityType<ToadEntity> TOAD = register("toad", EntityType.Builder.create(ToadEntity::new, EntityClassification.CREATURE).size(0.6F, 1.4F));
	public static final EntityType<ZombieCowmanEntity> ZOMBIE_COWMAN = register("zombie_cowman", EntityType.Builder.create(ZombieCowmanEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F));

	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register("custom_tnt", EntityType.Builder.<CustomTNTEntity>create(CustomTNTEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).setTrackingRange(128).setUpdateInterval(1));
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register("flying_block", EntityType.Builder.<FlyingBlockEntity>create(FlyingBlockEntity::new, EntityClassification.MISC).size(0.98F, 0.98F).setTrackingRange(128).setUpdateInterval(1));
	
	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder)
	{
	    EntityType<T> entitytype = builder.build(Mubble.MOD_PREFIX + id);
	    entitytype.setRegistryName(Mubble.MOD_ID, id);
	    ENTITY_TYPES.add(entitytype);
		return entitytype;
	}
	
    public static void registerEntities(IForgeRegistry<EntityType<?>> registry)
    {
    	registry.register(CHINCHO);
    	registry.register(TOAD);
    	registry.register(GOOMBA);
    	registry.register(ZOMBIE_COWMAN);

    	registry.register(CUSTOM_TNT);
    	registry.register(FLYING_BLOCK);
    }
    
    public static void registerPlacements()
    {
    	EntitySpawnPlacementRegistry.register(MubbleEntities.CHINCHO, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChinchoEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.TOAD, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ToadEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.GOOMBA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GoombaEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.ZOMBIE_COWMAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieCowmanEntity::canSpawn);
    }
    
    public static void registerRenders()
    {
    	RenderingRegistry.registerEntityRenderingHandler(ChinchoEntity.class, ChinchoRender::new);
    	RenderingRegistry.registerEntityRenderingHandler(GoombaEntity.class, GoombaRender::new);
    	RenderingRegistry.registerEntityRenderingHandler(ToadEntity.class, ToadRender::new);
    	RenderingRegistry.registerEntityRenderingHandler(ZombieCowmanEntity.class, ZombieCowmanRender::new);

    	RenderingRegistry.registerEntityRenderingHandler(CustomTNTEntity.class, CustomTNTRender::new);
    	RenderingRegistry.registerEntityRenderingHandler(FlyingBlockEntity.class, FlyingBlockRender::new);
    }
}