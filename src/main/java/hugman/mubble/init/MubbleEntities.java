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
import hugman.mubble.objects.entity.render.CustomTNTRenderer;
import hugman.mubble.objects.entity.render.FlyingBlockRenderer;
import hugman.mubble.objects.entity.render.ToadRenderer;
import hugman.mubble.objects.entity.render.ZombieCowmanRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.Heightmap;

public class MubbleEntities
{
    public static final List<EntityType<?>> ENTITY_TYPES = new ArrayList<EntityType<? extends Entity>>();

    /* MINECRAFT */
	public static final EntityType<ZombieCowmanEntity> ZOMBIE_COWMAN = register("zombie_cowman", EntityType.Builder.create(ZombieCowmanEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F));
	public static final EntityType<CustomTNTEntity> CUSTOM_TNT = register("custom_tnt", EntityType.Builder.<CustomTNTEntity>create(CustomTNTEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).setTrackingRange(128).setUpdateInterval(1));
	public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = register("flying_block", EntityType.Builder.<FlyingBlockEntity>create(FlyingBlockEntity::new, EntityClassification.MISC).size(0.98F, 0.98F).setTrackingRange(128).setUpdateInterval(1));
	
	/* SUPER MARIO */
	public static final EntityType<ChinchoEntity> CHINCHO = register("chincho", EntityType.Builder.create(ChinchoEntity::new, EntityClassification.MONSTER).size(0.6F, 1.2F));
	public static final EntityType<GoombaEntity> GOOMBA = register("goomba", EntityType.Builder.create(GoombaEntity::new, EntityClassification.MONSTER).size(0.75F, 0.85F));
	public static final EntityType<ToadEntity> TOAD = register("toad", EntityType.Builder.create(ToadEntity::new, EntityClassification.CREATURE).size(0.6F, 1.4F));
	
	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder)
	{
	    EntityType<T> entitytype = builder.build(Mubble.MOD_PREFIX + id);
	    entitytype.setRegistryName(Mubble.MOD_ID, id);
	    ENTITY_TYPES.add(entitytype);
		return entitytype;
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
    	EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
    	manager.register(ZOMBIE_COWMAN, new ZombieCowmanRenderer(manager));
    	manager.register(CUSTOM_TNT, new CustomTNTRenderer(manager));
    	manager.register(FLYING_BLOCK, new FlyingBlockRenderer(manager));
    	//manager.register(CHINCHO, new ChinchoRenderer(manager));
    	//manager.register(GOOMBA, new GoombaRenderer(manager));
    	manager.register(TOAD, new ToadRenderer(manager));
    }
}