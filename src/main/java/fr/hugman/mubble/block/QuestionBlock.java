package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.BlockState;

/**
 * @author Hugman
 * @since v4.0.0
 */
public class QuestionBlock extends MarioBumpableBlock {
	public QuestionBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState onBumpCompleted(BumpedBlockEntity entity) {
		return SuperMarioContent.EMPTY_BLOCK.getDefaultState();
	}
}
