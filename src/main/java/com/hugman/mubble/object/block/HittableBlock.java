package com.hugman.mubble.object.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class HittableBlock extends Block {
	protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0D, 0.1D, 0.0D, 16.0D, 16.0D, 16.0D);

	public HittableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		Vec3d velocity = entity.getVelocity();
		if(velocity.getY() > 0.0D) {
			entity.setVelocity(velocity.getX(), -0.2D, velocity.getZ());
			this.onHit(state, world, pos, entity);
		}
	}

	public abstract void onHit(BlockState state, World world, BlockPos pos, Entity entity);
}
