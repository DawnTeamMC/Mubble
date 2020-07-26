package com.hugman.mubble.object.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PuyoBlock extends DirectionalBlock {
	public PuyoBlock(Block.Settings builder) {
		super(builder);
	}

	@Override
	public void onLandedUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if(entityIn.isSneaking()) {
			super.onLandedUpon(worldIn, pos, entityIn, fallDistance);
		}
		else {
			entityIn.handleFallDamage(fallDistance, 0.0F);
		}
	}

	@Override
	public void onEntityLand(BlockView worldIn, Entity entityIn) {
		if(entityIn.isSneaking()) {
			super.onEntityLand(worldIn, entityIn);
		}
		else {
			Vec3d vec3d = entityIn.getVelocity();
			if(vec3d.y < 0.0D) {
				double d0 = entityIn instanceof LivingEntity ? 1.0D : 0.8D;
				entityIn.setVelocity(vec3d.x, -vec3d.y * d0, vec3d.z);
			}
		}
	}

	@Override
	public void onSteppedOn(World worldIn, BlockPos pos, Entity entityIn) {
		double d0 = Math.abs(entityIn.getVelocity().y);
		if(d0 < 0.1D && !entityIn.isSneaking()) {
			double d1 = 0.4D + d0 * 0.2D;
			entityIn.setVelocity(entityIn.getVelocity().multiply(d1, 1.0D, d1));
		}
		super.onSteppedOn(worldIn, pos, entityIn);
	}
}
