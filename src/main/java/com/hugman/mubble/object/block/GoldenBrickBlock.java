package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.init.MubbleSounds;
import com.hugman.mubble.init.data.MubbleLootTables;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class GoldenBrickBlock extends HittableBlock {
	private final Block emptyBlock;
	private final SoundEvent coinSound;

	public GoldenBrickBlock(Block emptyBlock, SoundEvent coinSound, Settings settings) {
		super(settings);
		this.emptyBlock = emptyBlock;
		this.coinSound = coinSound;
	}

	@Override
	public void onBlockAdded(BlockState p_220082_1_, World worldIn, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
		if(worldIn.isReceivingRedstonePower(pos)) {
			loot(worldIn, pos);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if(worldIn.isReceivingRedstonePower(pos)) {
			loot(worldIn, pos);
		}
	}

	@Override
	public void onHit(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(!worldIn.isClient) {
			loot(worldIn, pos);
		}
	}

	public void loot(World worldIn, BlockPos pos) {
		final double x = pos.getX() + 0.5D;
		final double y = pos.getY() + 0.5D + 0.6D;
		final double z = pos.getZ() + 0.5D;
		LootTable lootTable = worldIn.getServer().getLootManager().getTable(MubbleLootTables.BRICK_BLOCK);
		LootContext lootContext = new LootContext.Builder((ServerWorld) worldIn).parameter(LootContextParameters.BLOCK_STATE, this.getDefaultState()).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).build(LootContextTypes.BLOCK);
		List<ItemStack> items = lootTable.generateLoot(lootContext);
		for(ItemStack item : items) {
			worldIn.spawnEntity(new ItemEntity(worldIn, x, y, z, item));
			worldIn.playSound(null, x, y - 0.6D, z, this.coinSound, SoundCategory.BLOCKS, 1f, 1f);
		}
		worldIn.setBlockState(pos, this.emptyBlock.getDefaultState());
	}
}
