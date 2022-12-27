package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
}
