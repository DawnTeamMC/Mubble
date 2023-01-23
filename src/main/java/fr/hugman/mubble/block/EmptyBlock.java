package fr.hugman.mubble.block;

import fr.hugman.mubble.registry.MubbleSounds;
import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Hugman
 * @since v4.0.0
 */
public class EmptyBlock extends Block implements HittableBlock {
	public EmptyBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onHit(World world, BlockPos blockPos, BlockState state, Entity entity, BlockHitResult hit) {
		var pos = hit.getPos();
		if(world != null) {
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), MubbleSounds.BUMPABLE_BLOCK_BUMP, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
}
