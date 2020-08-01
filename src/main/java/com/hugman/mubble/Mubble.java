package com.hugman.mubble;

import com.hugman.dawn.api.creator.ModData;
import com.hugman.mubble.init.*;
import com.hugman.mubble.init.data.MubbleCommands;
import com.hugman.mubble.init.data.MubbleStatPack;
import com.hugman.mubble.init.world.MubbleBiomePack;
import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import com.hugman.mubble.init.world.MubbleFeatures;
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
		new MubbleEnchantmentPack();
		new MubbleEntityPack();
		new MubbleEffectPack();
		new MubblePaintingPack();
		new MubbleSoundPack();
		// Data
		new MubbleStatPack();
		MubbleCommands.init();
		// World
		new MubbleConfiguredFeatures();
		new MubbleBiomePack();
		new MubbleFeatures();
		//MubbleGenerators.init();
	}
}