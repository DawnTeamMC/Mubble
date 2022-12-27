package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BumpableBlock extends Block implements UnderHittableBlock {
	public BumpableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		//TODO: should we keep this?
		if(!world.isClient()) {
			BumpedBlockEntity.bump(world, pos, hit);
		}
		return ActionResult.success(world.isClient());
	}

	@Override
	public void onHitFromUnder(BlockState state, World world, BlockPos pos, BlockHitResult hit, Entity entity) {
		if(!world.isClient()) {
			// The hit direction should always be up in this case
			BumpedBlockEntity.bump(world, pos, hit);
		}
	}

	/**
	 * @return the sound to play to when the block is bumped
	 */
	@Nullable
	protected abstract SoundEvent getBumpSound(BumpedBlockEntity entity);

	/**
	 * @return the block state to convert to after the block is bumped
	 */
	@Nullable
	protected abstract BlockState getBumpedState(BumpedBlockEntity entity);


	/**
	 * @return the velocity on the y-axis to apply to entities when the block is bumped
	 * @see #launchEntitiesOnTop
	 */
	protected double getBumpYVelocity(World world, BlockPos pos, BlockState state, BumpedBlockEntity blockEntity, Entity entity) {
		return 0.5D;
	}

	/**
	 * Called when a block is at the peak of being bumped.
	 */
	public void onBump(World world, BlockPos pos, BlockState state, BumpedBlockEntity entity, BlockHitResult hit) {
		Vec3d soundPos = pos.toCenterPos();
		SoundEvent sound = this.getBumpSound(entity);
		if(sound != null) {
			world.playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), sound, SoundCategory.BLOCKS, 1F, 1F);
		}
	}

	/**
	 * Called when a block is at the peak of being bumped. (middle of the animation)
	 */
	public void onBumpPeak(World world, BlockPos pos, BlockState state, BumpedBlockEntity entity) {
		if(entity.getBumpDirection() == Direction.DOWN) {
			// TODO : add a gamerule for this
			this.launchEntitiesOnTop(world, pos, state, entity);
		}
	}

	/**
	 * Called when a block finishes being bumped.
	 */
	public void onBumpCompleted(World world, BlockPos pos, BlockState state, BumpedBlockEntity entity) {
		if(!world.isClient()) {
			BlockState bumpedState = getBumpedState(entity);
			if(bumpedState == null) {
				bumpedState = entity.getBlockState();
			}
			world.setBlockState(pos, bumpedState);
		}
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
	public void launchEntitiesOnTop(World world, BlockPos pos, BlockState state, BumpedBlockEntity blockEntity) {
		for(Entity entity : getEntitiesOnTop(world, pos)) {
			Vec3d vec3d = entity.getVelocity();
			entity.setVelocity(vec3d.x, this.getBumpYVelocity(world, pos, state, blockEntity, entity), vec3d.z);
			entity.velocityDirty = true;
		}
	}
}
