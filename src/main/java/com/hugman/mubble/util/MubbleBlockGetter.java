package com.hugman.mubble.util;

import com.hugman.dawn.api.creator.BlockCreator.Render;
import com.hugman.dawn.api.util.BlockGetter;
import com.hugman.mubble.object.block.BalloonBlock;
import com.hugman.mubble.object.block.CloudBlock;
import com.hugman.mubble.object.block.KeyDoorBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.ItemGroup;

public enum MubbleBlockGetter implements BlockGetter {
	KEY_DOOR("key_door", ItemGroup.REDSTONE, Render.CUTOUT),
	CLOUD_BLOCK("cloud_block", ItemGroup.DECORATIONS, Render.TRANSLUCENT),
	BALLOON("balloon", ItemGroup.DECORATIONS, Render.TRANSLUCENT),
	TETRIS_BLOCK("tetris_block", ItemGroup.BUILDING_BLOCKS);

	private final String suffix;
	private final ItemGroup itemGroup;
	private final Render renderLayer;

	MubbleBlockGetter(String suffix, ItemGroup itemGroup) {
		this(suffix, itemGroup, Render.SOLID);
	}

	MubbleBlockGetter(String suffix, ItemGroup itemGroup, Render render) {
		this.suffix = suffix;
		this.itemGroup = itemGroup;
		this.renderLayer = render;
	}

	public String getSuffix() {
		return suffix;
	}

	public ItemGroup getItemGroup() {
		return itemGroup;
	}

	public Render getRender() {
		return renderLayer;
	}

	public Block getBlock(AbstractBlock.Settings settings) {
		switch(this) {
			default:
				return new Block(settings);
			case KEY_DOOR:
				return new KeyDoorBlock(settings);
			case CLOUD_BLOCK:
				return new CloudBlock(settings);
			case BALLOON:
				return new BalloonBlock(settings);
			case TETRIS_BLOCK:
				return new FallingBlock(settings);
		}
	}
}
