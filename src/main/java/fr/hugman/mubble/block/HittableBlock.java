package fr.hugman.mubble.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//TODO: move this to the Dawn API

/**
 * Represents blocks that can be hit physically. The hit method will be triggered by:
 * <ul>
 *     <li>An entity hitting the block by under, with the superior part of the hitbox (generally the head)</li>
 * </ul>
 * Projectile hits are not handled by this interface.
 *
 * @author Hugman
 * @see Block#onProjectileHit
 * @since v4.0.0
 */
public interface HittableBlock {
	void onHit(World world, BlockPos pos, BlockState state, Entity entity, BlockHitResult hit);
}
