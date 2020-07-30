package com.hugman.mubble;

import com.hugman.dawn.api.creator.ModData;
import com.hugman.mubble.init.*;
import com.hugman.mubble.init.data.MubbleCommands;
import com.hugman.mubble.init.data.MubbleScreenHandlers;
import com.hugman.mubble.init.data.MubbleStats;
import com.hugman.mubble.init.world.MubbleBiomes;
import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import com.hugman.mubble.init.world.MubbleFeatures;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
	public static final ModData MOD_DATA = new ModData("mubble");
	public static final String MOD_ID = "mubble";
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		new MubbleBlocks();
		new MubbleConfiguredFeatures();
		new MubbleCostumes();
		new MubbleEffects();
		new MubbleEnchantments();
		new MubbleEntities();
		new MubbleItems();
		new MubblePaintingTypes();
		new MubbleSounds();
		// Data
		new MubbleStats();
		MubbleCommands.init();
		new MubbleScreenHandlers();
		// World
		new MubbleBiomes();
		new MubbleFeatures();
		//MubbleGenerators.init();
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