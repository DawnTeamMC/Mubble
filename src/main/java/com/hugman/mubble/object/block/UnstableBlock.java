package com.hugman.mubble.object.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UnstableBlock extends Block {
	public UnstableBlock(Block.Settings builder) {
		super(builder);
	}

	@Override
	public void onSteppedOn(World worldIn, BlockPos pos, Entity entityIn) {
		if(!worldIn.isClient && worldIn.random.nextInt(8) == 0) {
			worldIn.removeBlock(pos, false);
		}
	}
}
