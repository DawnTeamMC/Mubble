package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlockPack;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class OreBlock extends net.minecraft.block.OreBlock {
	public OreBlock(Settings properties) {
		super(properties);
	}

	@Override
	protected int getExperienceWhenMined(Random rand) {
		if(this == MubbleBlockPack.VANADIUM_ORE) {
			return MathHelper.nextInt(rand, 4, 8);
		}
		return super.getExperienceWhenMined(rand);
	}
}
