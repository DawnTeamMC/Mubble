package hugman.mod.init;

import hugman.mod.util.handlers.SoundHandler;
import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;

/** 
 * Init class - used to initialize sound types.
 */
public class MubbleSoundTypes
{
	public static final SoundType BRICK_BLOCK = new SoundType(1.0F, 1.0F, SoundHandler.BLOCK_BRICK_BLOCK_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final SoundType SPACE_MATTER = new SoundType(1.0F, 1.0F, SoundHandler.BLOCK_SPACE_MATTER_BREAK, SoundHandler.BLOCK_SPACE_MATTER_STEP, SoundHandler.BLOCK_SPACE_MATTER_PLACE, SoundHandler.BLOCK_SPACE_MATTER_HIT, SoundHandler.BLOCK_SPACE_MATTER_FALL);
}