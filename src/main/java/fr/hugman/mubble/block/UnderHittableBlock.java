package fr.hugman.mubble.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Hugman
 * @since v4.0.0
 */
public interface UnderHittableBlock {
	void onHitFromUnder(BlockState state, World world, BlockPos pos, BlockHitResult hit, Entity entity);
}
