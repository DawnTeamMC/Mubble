package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
public abstract class MarioBumpableBlock extends Block implements BumpableBlock, UnderHittableBlock {
	private static final SoundEvent DEFAULT_BUMP_SOUND = SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL.value();

	public MarioBumpableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onHitFromUnder(BlockState state, World world, BlockPos pos, BlockHitResult hit, Entity entity) {
		if(!world.isClient()) {
			// The hit direction should always be up in this case
			BumpedBlockEntity.bump(world, pos, hit);
		}
	}

	@Override
	public void onBump(BumpedBlockEntity entity, BlockHitResult hit) {
		Vec3d soundPos = entity.getPos().toCenterPos();
		if(entity.getWorld() != null && DEFAULT_BUMP_SOUND != null) {
			entity.getWorld().playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), DEFAULT_BUMP_SOUND, SoundCategory.BLOCKS, 1F, 1F);
		}
	}

	@Override
	public void onBumpPeak(BumpedBlockEntity entity) {
		// TODO : add a gamerule for this
		this.launchEntitiesOnTop(entity.getWorld(), entity.getPos());
	}

	@Override
	public BlockState onBumpCompleted(BumpedBlockEntity entity) {
		return SuperMarioContent.EMPTY_BLOCK.getDefaultState();
	}

	/**
	 * @return the entities on top of the block
	 */
	public List<Entity> getEntitiesOnTop(World world, BlockPos pos) {
		return world.getOtherEntities(null, new Box(pos.up()));
	}

	/**
	 * Launches entities on top of the block.
	 */
	public void launchEntitiesOnTop(World world, BlockPos pos) {
		for(Entity entity : getEntitiesOnTop(world, pos)) {
			launchEntity(entity);
		}
	}

	public void launchEntity(Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		entity.setVelocity(vec3d.x, 0.3D, vec3d.z);
		entity.velocityDirty = true;
	}
}
