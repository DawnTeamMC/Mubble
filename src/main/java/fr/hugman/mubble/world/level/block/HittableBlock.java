package fr.hugman.mubble.world.level.block;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
    double HIT_Y_OFFSET = 0.001;

    void onHit(Level level, BlockState state, Entity entity, BlockHitResult hit);
}
