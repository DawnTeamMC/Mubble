package com.hugman.mubble;

import com.hugman.dawn.api.creator.ModData;
import com.hugman.mubble.init.*;
import com.hugman.mubble.init.data.MubbleStatPack;
import com.hugman.mubble.init.world.MubbleBiomePack;
import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
	public static final ModData MOD_DATA = new ModData("mubble");
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		new MubbleBlockPack();
		new MubbleItemPack();
		new MubbleCostumePack();
		new MubbleEntityPack();
		new MubblePaintingPack();
		new MubbleSoundPack();
		// Data
		new MubbleStatPack();
		// World
		new MubbleConfiguredFeatures();
		new MubbleBiomePack();
		//MubbleGenerators.init();
	}
}