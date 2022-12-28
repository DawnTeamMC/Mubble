package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Optional;

public class BumpedBlock extends BlockWithEntity {
	public BumpedBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BumpedBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SuperMarioContent.BUMPED_BLOCK_ENTITY_TYPE, BumpedBlockEntity::tick);
	}

	@Override
	protected void spawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state) {
		Optional<BumpedBlockEntity> blockEntity = world.getBlockEntity(pos, SuperMarioContent.BUMPED_BLOCK_ENTITY_TYPE);
		if(blockEntity.isEmpty()) {
			super.spawnBreakParticles(world, player, pos, state);
			return;
		}
		world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(blockEntity.get().getBlockState()));
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		Optional<BumpedBlockEntity> blockEntity = world.getBlockEntity(pos, SuperMarioContent.BUMPED_BLOCK_ENTITY_TYPE);
		if(blockEntity.isEmpty()) {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
			return;
		}
		BlockState state1 = blockEntity.get().getBlockState();
		state1.getBlock().onLandedUpon(world, state1, pos, entity, fallDistance);
	}
}
