package hugman.mod.init;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;

public class MubbleSoundTypes 
{
	public static final SoundType BRICK_BLOCK = new SoundType(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final SoundType SPACE_JAM = new SoundType(1.0F, 1.0F, MubbleSounds.BLOCK_SPACE_JAM_BREAK, MubbleSounds.BLOCK_SPACE_JAM_STEP, MubbleSounds.BLOCK_SPACE_JAM_PLACE, MubbleSounds.BLOCK_SPACE_JAM_HIT, MubbleSounds.BLOCK_SPACE_JAM_FALL);
}