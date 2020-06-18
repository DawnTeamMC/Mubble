package hugman.mubble.init.data;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class MubbleSoundTypes {
	public static final BlockSoundGroup SMB_BRICK_BLOCK = new BlockSoundGroup(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_SMB, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final BlockSoundGroup SMB3_BRICK_BLOCK = new BlockSoundGroup(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_SMB3, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final BlockSoundGroup SMW_BRICK_BLOCK = new BlockSoundGroup(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_SMW, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final BlockSoundGroup NSMBU_BRICK_BLOCK = new BlockSoundGroup(1.0F, 1.0F, MubbleSounds.BLOCK_BRICK_BLOCK_BREAK_NSMBU, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

	public static final BlockSoundGroup DREAM_BLOCK = new BlockSoundGroup(1.0F, 1.0F, MubbleSounds.BLOCK_DREAM_BLOCK_BREAK, MubbleSounds.BLOCK_DREAM_BLOCK_STEP, MubbleSounds.BLOCK_DREAM_BLOCK_PLACE, MubbleSounds.BLOCK_DREAM_BLOCK_HIT, MubbleSounds.BLOCK_DREAM_BLOCK_FALL);
}