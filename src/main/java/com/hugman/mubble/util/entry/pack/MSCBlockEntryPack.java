package com.hugman.mubble.util.entry.pack;

import com.google.common.collect.ImmutableMap;
import com.hugman.mubble.util.MoreWordUtils;
import com.hugman.mubble.util.entry.BlockEntry;
import com.hugman.mubble.util.entry.BlockTemplate;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MSCBlockEntryPack {
	private final Map<Pair<BlockTemplate, DyeColor>, Block> BLOCK_MAP;

	public MSCBlockEntryPack(String name, FabricBlockSettings settings, Block baseBlock, BlockTemplate... templates) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<DyeColor, Block>();
		for(BlockTemplate template : templates) {
			for(DyeColor color : DyeColor.values()) {
				Block entry = new BlockEntry.Builder(color.getName() + "_" + MoreWordUtils.parseShapeName(name, template), template, settings.materialColor(color.getMaterialColor())).copy(baseBlock).copy(template).build();
				builder.put(Pair.of(template, color), entry);
			}
		}
		BLOCK_MAP = builder.build();
	}

	public MSCBlockEntryPack(String name, FabricBlockSettings settings, Block baseBlock, BlockTemplate template, boolean excludeColors, DyeColor... colors) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<DyeColor, Block>();
		List<DyeColor> colorList;
		if(excludeColors) {
			colorList = Arrays.asList(DyeColor.values());
			colorList.removeAll(Arrays.asList(colors));
		}
		else {
			colorList = Arrays.asList(colors);
		}
		for(DyeColor color : colorList) {
			Block entry = new BlockEntry.Builder(color.getName() + "_" + MoreWordUtils.parseShapeName(name, template), template, settings.materialColor(color.getMaterialColor())).copy(baseBlock).copy(template).build();
			builder.put(Pair.of(template, color), entry);
		}
		BLOCK_MAP = builder.build();
	}

	public MSCBlockEntryPack(String name, Block baseBlock, BlockTemplate... shapes) {
		this(name, FabricBlockSettings.copyOf(baseBlock), baseBlock, shapes);
	}

	public Block getBlock(BlockTemplate shape, DyeColor color) {
		return BLOCK_MAP.get(Pair.of(shape, color));
	}
}
