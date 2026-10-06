package fr.hugman.mubble.super_mario.world.level.block;

import fr.hugman.mubble.world.level.block.PoundableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Represents blocks that can be hit physically. The hit method will be triggered by:
 * <ul>
 *     <li>An entity hitting the block by under, with the superior part of the hitbox (generally the head)</li>
 *     <li>An entity pounding the block from above, such as a ground pound landing on it, see {@link PoundableBlock}</li>
 * </ul>
 * Projectile hits are not handled by this interface.
 *
 * @author Hugman
 * @see Block#onProjectileHit
 * @since v4.0.0
 */
public interface HittableBlock extends PoundableBlock {
    double HIT_Y_OFFSET = 0.001;

    void onHit(Level level, BlockState state, Entity entity, BlockHitResult hit);

    /** A pound from above hits the top of the block. */
    @Override
    default void onPounded(Level level, BlockState state, BlockPos pos, Entity entity) {
        this.onHit(level, state, entity, new BlockHitResult(Vec3.upFromBottomCenterOf(pos, 1.0D), Direction.UP, pos, false));
    }
}
