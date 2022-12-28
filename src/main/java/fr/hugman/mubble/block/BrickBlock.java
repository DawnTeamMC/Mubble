package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * @author Hugman
 * @since v4.0.0
 */
public class BrickBlock extends MarioBumpableBlock {
	public BrickBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState onBumpCompleted(BumpedBlockEntity entity) {
		return SuperMarioContent.EMPTY_BLOCK.getDefaultState();
	}

	@Override
	public void onBumpPeak(BumpedBlockEntity entity) {
		World world = entity.getWorld();
		if(world != null && !world.isClient()) {
			Random random = world.getRandom();
			if(random.nextBoolean()) {
				world.breakBlock(entity.getPos(), false);
				Vec3d soundPos = entity.getPos().toCenterPos();
				world.playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), SuperMarioContent.BRICK_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		}
	}
}
