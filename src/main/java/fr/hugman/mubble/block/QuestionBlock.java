package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class QuestionBlock extends BumpableBlock {
	public QuestionBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected SoundEvent getBumpSound(BumpedBlockEntity entity) {
		//TODO
		return SoundEvents.BLOCK_NOTE_BLOCK_BANJO.value();
	}

	@Override
	public BlockState getBumpedState(BumpedBlockEntity entity) {
		return SuperMarioContent.EMPTY_BLOCK.getDefaultState();
	}
}
