package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.init.MubbleSounds;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
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
