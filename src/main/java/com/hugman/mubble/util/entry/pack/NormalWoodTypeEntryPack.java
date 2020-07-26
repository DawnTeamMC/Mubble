package com.hugman.mubble.util.entry.pack;

import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.object.block.SaplingBlock;
import com.hugman.mubble.util.entry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.sapling.SaplingGenerator;

public class NormalWoodTypeEntryPack extends WoodTypeEntryPack {
	private final PottedPlantEntryPack saplingCreator;
	private final LeavesEntryPack leavesEntryPack;

	public NormalWoodTypeEntryPack(String name, SaplingGenerator saplingGenerator, MaterialColor planksColor, MaterialColor barkColor) {
		this(name, saplingGenerator, planksColor, planksColor, barkColor);
	}

	public NormalWoodTypeEntryPack(String name, SaplingGenerator saplingGenerator, MaterialColor planksColor, MaterialColor insideColor, MaterialColor barkColor) {
		super(name, planksColor, insideColor, barkColor, false);
		this.saplingCreator = new PottedPlantEntryPack(new BlockEntry.Builder(name + "_sapling", new SaplingBlock(saplingGenerator, MubbleBlocks.Settings.SAPLING)).build());
		this.leavesEntryPack = new LeavesEntryPack(name);
	}

	public Block getSapling() {
		return saplingCreator.getPlant();
	}

	public Block getPottedSapling() {
		return saplingCreator.getPottedPlant();
	}

	public Block getLeaves() {
		return leavesEntryPack.getLeaves();
	}

	public Block getLeafPile() {
		return leavesEntryPack.getLeafPile();
	}
}
