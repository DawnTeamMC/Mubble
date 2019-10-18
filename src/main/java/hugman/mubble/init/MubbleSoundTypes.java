package hugman.mubble.init;

import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvents;

public class MubbleSoundTypes 
{
	public static final SoundType SMB_BRICK_BLOCK = new SoundType(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_SMB, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final SoundType SMB3_BRICK_BLOCK = new SoundType(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_SMB3, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final SoundType SMW_BRICK_BLOCK = new SoundType(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_SMW, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final SoundType NSMBU_BRICK_BLOCK = new SoundType(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_NSMBU, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	
	public static final SoundType DREAM_BLOCK = new SoundType(0.4F, 1.0F, MubbleSounds.BLOCK_DREAM_BLOCK_BREAK, MubbleSounds.BLOCK_DREAM_BLOCK_STEP, MubbleSounds.BLOCK_DREAM_BLOCK_PLACE, MubbleSounds.BLOCK_DREAM_BLOCK_HIT, MubbleSounds.BLOCK_DREAM_BLOCK_FALL);
}