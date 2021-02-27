package com.hugman.mubble.object.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyBlock extends PunchBlock {
	private final SoundEvent hitSound;

	public EmptyBlock(SoundEvent hitSound, AbstractBlock.Settings settings) {
		super(settings);
		this.hitSound = hitSound;
	}

	@Override
	public void onPunch(BlockState state, World world, BlockPos pos, Entity entity) {
		if(!world.isClient) {
			world.playSound(null, pos.add(0.5D, 0.5D, 0.5D), this.hitSound, SoundCategory.BLOCKS, 1f, 1f);
		}
	}
}
