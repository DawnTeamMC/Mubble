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
	public void onSteppedOn(World world, BlockPos pos, Entity entity) {
		if(!world.isClient && world.random.nextInt(8) == 0) {
			world.breakBlock(pos, false, entity);
		}
	}
}
