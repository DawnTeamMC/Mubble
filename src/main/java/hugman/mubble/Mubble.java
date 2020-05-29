package hugman.mubble;

import hugman.mubble.init.*;
import hugman.mubble.init.data.MubbleCommands;
import hugman.mubble.init.data.MubbleContainerTypes;
import hugman.mubble.init.data.MubbleTileEntityTypes;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleGenerators;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import hugman.mubble.objects.event.LightsaberEvents;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer
{
	public static final String MOD_ID = "mubble";
	public static final String MOD_PREFIX = MOD_ID + ":";
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize()
	{
		new MubbleBlocks();
		new MubbleCostumes();
		new MubbleEffects();
		new MubbleEnchantments();
		new MubbleEntities();
		new MubbleItems();
		new MubblePaintingTypes();
		new MubbleSounds();
		// Data
		new MubbleTileEntityTypes();
		MubbleCommands.init();
		MubbleContainerTypes.init();
		// World
		new MubbleBiomes();
		new MubbleSurfaceBuilders();
		MubbleBiomes.initBiomeGeneration();
		initGenerators();
		// Events
		LightsaberEvents.init();
	}

	private void initGenerators()
	{
		MubbleGenerators.registerOres();
		MubbleGenerators.registerTrees();
		MubbleGenerators.registerSpawns();
	}
	
	/*private void initSpawnRestrictions()
	{
		EntitySpawnPlacementRegistry.register(MubbleEntities.CHINCHO, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChinchoEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.TOAD, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ToadEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.GOOMBA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GoombaEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.ZOMBIE_COWMAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieCowmanEntity::canSpawn);
	}*/
}