package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
public interface BumpableBlock {
	/**
	 * Called when a block is at the peak of being bumped.
	 */
	void onBump(BumpedBlockEntity entity, BlockHitResult hit);

	/**
	 * Called when a block is at the peak of being bumped. (middle of the animation)
	 */
	void onBumpPeak(BumpedBlockEntity entity);

	/**
	 * Called when a block finishes being bumped.
	 *
	 * @return the new state of the block
	 */
	@Nullable
	BlockState onBumpCompleted(BumpedBlockEntity entity);
}
