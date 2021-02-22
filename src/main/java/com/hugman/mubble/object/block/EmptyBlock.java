package com.hugman.mubble.object.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyBlock extends HittableBlock {
	private final SoundEvent hitSound;

	public EmptyBlock(SoundEvent hitSound, AbstractBlock.Settings settings) {
		super(settings);
		this.hitSound = hitSound;
	}

	@Override
	public void onHit(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(!worldIn.isClient) {
			worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, this.hitSound, SoundCategory.BLOCKS, 1f, 1f);
		}
	}
}
