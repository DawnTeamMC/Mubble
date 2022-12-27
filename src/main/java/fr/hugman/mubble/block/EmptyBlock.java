package fr.hugman.mubble.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EmptyBlock extends Block implements UnderHittableBlock {
	public EmptyBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onHitFromUnder(BlockState state, World world, BlockPos blockPos, BlockHitResult hit, Entity entity) {
		if(!world.isClient()) {
			Vec3d pos = hit.getPos();
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_BANJO.value(), SoundCategory.BLOCKS, 1F, 1F);
		}
	}
}
