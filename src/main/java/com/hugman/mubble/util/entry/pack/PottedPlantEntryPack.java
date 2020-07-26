package com.hugman.mubble.util.entry.pack;

import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.util.entry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.registry.Registry;

public class PottedPlantEntryPack {
	private final Block plant;
	private final Block pottedPlant;

	public PottedPlantEntryPack(Block entry) {
		this.plant = entry;
		this.pottedPlant = new BlockEntry.Builder("potted_" + Registry.BLOCK.getId(entry).getPath(), new FlowerPotBlock(getPlant(), MubbleBlocks.Settings.POTTED_PLANT.lightLevel(entry.getDefaultState().getLuminance()))).setRenderLayer(RenderLayer.getCutout()).noItem().build();
	}

	public Block getPlant() {
		return this.plant;
	}

	public Block getPottedPlant() {
		return this.pottedPlant;
	}
}