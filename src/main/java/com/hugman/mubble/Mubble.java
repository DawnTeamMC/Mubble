package com.hugman.mubble;

import com.hugman.dawn.api.util.ModData;
import com.hugman.mubble.config.MubbleConfig;
import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.init.MubbleCostumes;
import com.hugman.mubble.init.MubbleEntities;
import com.hugman.mubble.init.MubbleItemGroups;
import com.hugman.mubble.init.MubbleItems;
import com.hugman.mubble.init.MubbleKeyBindings;
import com.hugman.mubble.init.MubblePaintings;
import com.hugman.mubble.init.MubbleSlots;
import com.hugman.mubble.init.MubbleSounds;
import com.hugman.mubble.init.data.MubbleStats;
import com.hugman.mubble.init.world.MubbleBiomes;
import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import com.hugman.mubble.init.world.MubbleFoliagePlacerTypes;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
	public static final ModData MOD_DATA = new ModData("mubble");
	public static final Logger LOGGER = LogManager.getLogger();
	public static final MubbleConfig CONFIG = AutoConfig.register(MubbleConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new)).getConfig();

	@Override
	public void onInitialize() {
		MubbleBlocks.init();
		MubbleItems.init();
		MubbleCostumes.init();
		MubbleEntities.init();
		MubblePaintings.init();
		MubbleSounds.init();
		MubbleSlots.init();
		MubbleItemGroups.init();
		MubbleStats.init();
		MubbleConfiguredFeatures.init();
		MubbleFoliagePlacerTypes.init();
		MubbleBiomes.init();
		MOD_DATA.registerCreators();
		MubbleBiomes.addToGen();
		MubbleKeyBindings.serverInit();
	}
}