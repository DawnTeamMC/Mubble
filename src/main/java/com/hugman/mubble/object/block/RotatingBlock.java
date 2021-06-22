package com.hugman.mubble.object.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RotatingBlock extends PunchBlock {
	public RotatingBlock(BlockSoundGroup soundType) {
		super(FabricBlockSettings.of(Material.STONE, MapColor.STONE_GRAY).strength(1.5F, 6.0F).sounds(soundType));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	public void onEntityLand(BlockView world, Entity entity) {
		Vec3d velocity = entity.getVelocity();
		if(entity.isSneaking() && velocity.y < -0.1) {
			if(!entity.world.isClient) {
				entity.world.breakBlock(entity.getBlockPos().down(), false, entity);
			}
			entity.setVelocity(velocity.x, 0.625D, velocity.z);
		}
		else {
			super.onEntityLand(world, entity);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean moved) {
		if(!world.isClient && world.isReceivingRedstonePower(pos)) {
			world.breakBlock(pos, false);
		}
	}

	@Override
	public void onPunch(BlockState state, World world, BlockPos pos, Entity entity) {
		if(!world.isClient) {
			world.breakBlock(pos, false, entity);
		}
	}
}
