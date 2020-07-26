package com.hugman.mubble.util.entry.pack;

import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.object.block.FungusBlock;
import com.hugman.mubble.util.entry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;

import java.util.function.Supplier;

public class FungusEntryPack extends PottedPlantEntryPack {
	/**
	 * Creates an entry pack containing a fungus entry and its potted variant.
	 *
	 * @param suffix   The suffix of the fungus plant.
	 * @param supplier The supplier for the huge fungus feature.
	 */
	public FungusEntryPack(String suffix, Supplier<ConfiguredFeature<HugeFungusFeatureConfig, ?>> supplier) {
		super(new BlockEntry.Builder(suffix + "_fungus", new FungusBlock(MubbleBlocks.Settings.FUNGUS, supplier)).build());
	}

	public Block getFungus() {
		return getPlant();
	}
}
