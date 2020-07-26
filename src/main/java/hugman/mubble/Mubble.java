package hugman.mubble;

import hugman.mubble.init.*;
import hugman.mubble.init.data.MubbleCommands;
import hugman.mubble.init.data.MubbleScreenHandlers;
import hugman.mubble.init.data.MubbleStats;
import hugman.mubble.init.data.MubbleTileEntityTypes;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleConfiguredFeatures;
import hugman.mubble.init.world.MubbleFeatures;
import hugman.mubble.init.world.MubbleGenerators;
import hugman.mubble.util.DataWriter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
	public static final String MOD_ID = "mubble";
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		new DataWriter();
		new MubbleBlocks();
		new MubbleConfiguredFeatures();
		new MubbleCostumes();
		new MubbleStatusEffects();
		new MubbleEnchantments();
		new MubbleEntities();
		MubbleEntities.registerEntityAttributes();
		new MubbleItems();
		new MubblePaintingTypes();
		new MubbleSounds();
		// Data
		new MubbleTileEntityTypes();
		new MubbleStats();
		MubbleCommands.init();
		new MubbleScreenHandlers();
		// World
		new MubbleBiomes();
		new MubbleFeatures();
		MubbleGenerators.init();
	}

	public static Identifier id(String name) {
		return new Identifier(Mubble.MOD_ID, name);
	}
	
	/*private void initSpawnRestrictions()
	{
		EntitySpawnPlacementRegistry.register(MubbleEntities.CHINCHO, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChinchoEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.TOAD, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ToadEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.GOOMBA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GoombaEntity::canSpawn);
    	EntitySpawnPlacementRegistry.register(MubbleEntities.ZOMBIE_COWMAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieCowmanEntity::canSpawn);
	}*/
}