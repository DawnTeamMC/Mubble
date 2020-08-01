package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlockPack;
import com.hugman.mubble.init.MubbleItemPack;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.item.ItemConvertible;
import net.minecraft.sound.BlockSoundGroup;

public class CropsBlock extends CropBlock {
	/* Extension for internal publicity
	 * + Missing features */
	public CropsBlock() {
		super(FabricBlockSettings.of(Material.LEAVES).collidable(true).ticksRandomly().hardness(0f).sounds(BlockSoundGroup.CROP).nonOpaque());
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		if(this == MubbleBlockPack.TOMATOES) {
			return MubbleItemPack.TOMATO;
		}
		if(this == MubbleBlockPack.SALAD) {
			return MubbleItemPack.SALAD;
		}
		else {
			return null;
		}
	}
}
