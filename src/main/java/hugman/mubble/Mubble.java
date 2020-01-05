package hugman.mubble;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleCommands;
import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleContainerTypes;
import hugman.mubble.init.data.MubbleTileEntityTypes;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleCarvers;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.init.world.MubbleGenerators;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import net.fabricmc.api.ModInitializer;

public class Mubble implements ModInitializer
{
	public static final String MOD_ID = "mubble";
	public static final String MOD_PREFIX = MOD_ID + ":";
	public static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public void onInitialize()
	{
		new MubbleBlocks();
		new MubbleCommands();
		new MubbleCostumes();
		new MubbleEffects();
		new MubbleEntities();
		new MubbleItems();
		new MubbleSounds();
		
		// Data
		new MubbleTileEntityTypes();
		MubbleContainerTypes.init();
		
		// World
		new MubbleBiomes();
		new MubbleCarvers();
		new MubbleDimensions();
		new MubbleSurfaceBuilders();
		MubbleBiomes.initBiomeGeneration();
		initGenerators();
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