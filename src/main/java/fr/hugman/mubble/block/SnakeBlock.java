package fr.hugman.mubble.block;

import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * @author komerish
 * @since v4.0.0
 */
public class SnakeBlock extends Block {
	public SnakeBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		super.onSteppedOn(world, pos, state, entity);

		int x = 0;
		int z = 0;
		if(entity.getHorizontalFacing() == Direction.NORTH) {
			z = -1;
		}
		if(entity.getHorizontalFacing() == Direction.SOUTH) {
			z = 1;
		}
		if(entity.getHorizontalFacing() == Direction.WEST) {
			x = -1;
		}
		if(entity.getHorizontalFacing() == Direction.EAST) {
			x = 1;
		}
		if(world.getBlockState(pos.add(x, 0, z)).isAir()) {
			world.setBlockState(pos.add(x, 0, z), SuperMario.SNAKE_BLOCK.getDefaultState());
			world.playSound(null, entity.getX(), entity.getY() + 0.5, entity.getZ() + 0.5, SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, SoundCategory.RECORDS, 3.0f, 1.0F, world.random.nextLong());
		}
	}
}
