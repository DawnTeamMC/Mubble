package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.init.MubbleSounds;
import com.hugman.mubble.init.data.MubbleLootTables;
import com.hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class QuestionBlock extends Block {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);

	public QuestionBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean moved) {
		if(worldIn.isReceivingRedstonePower(pos)) {
			loot(worldIn, pos);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean moved) {
		if(worldIn.isReceivingRedstonePower(pos)) {
			loot(worldIn, pos);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		loot(worldIn, pos);
	}

	public void loot(World worldIn, BlockPos pos) {
		if(!worldIn.isClient) {
			BlockState emptyBlock = MubbleBlocks.SMB_EMPTY_BLOCK.getDefaultState();
			SoundEvent coinLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN_SMB;
			SoundEvent powerUpLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB;
			if(this == MubbleBlocks.SMB_QUESTION_BLOCK) {
				coinLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN_SMB;
				powerUpLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB;
				emptyBlock = MubbleBlocks.SMB_EMPTY_BLOCK.getDefaultState();
			}
			else if(this == MubbleBlocks.SMB3_QUESTION_BLOCK) {
				coinLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN_SMB3;
				powerUpLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB3;
				emptyBlock = MubbleBlocks.SMB3_EMPTY_BLOCK.getDefaultState();
			}
			else if(this == MubbleBlocks.SMW_QUESTION_BLOCK) {
				coinLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN_SMW;
				powerUpLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMW;
				emptyBlock = MubbleBlocks.SMW_EMPTY_BLOCK.getDefaultState();
			}
			else if(this == MubbleBlocks.NSMBU_QUESTION_BLOCK) {
				coinLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN_NSMBU;
				powerUpLootSound = MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_NSMBU;
				emptyBlock = MubbleBlocks.NSMBU_EMPTY_BLOCK.getDefaultState();
			}
			final double x = pos.getX() + 0.5D;
			final double y = pos.getY() + 0.5D + 0.6D;
			final double z = pos.getZ() + 0.5D;
			LootTable lootTable = worldIn.getServer().getLootManager().getTable(MubbleLootTables.QUESTION_BLOCK);
			LootContext lootContext = new LootContext.Builder((ServerWorld) worldIn)
					.parameter(LootContextParameters.BLOCK_STATE, this.getDefaultState())
					.parameter(LootContextParameters.POSITION, pos)
					.parameter(LootContextParameters.TOOL, ItemStack.EMPTY)
					.build(LootContextTypes.BLOCK);
			List<ItemStack> items = lootTable.generateLoot(lootContext);
			for(ItemStack item : items) {
				worldIn.spawnEntity(new ItemEntity(worldIn, x, y, z, item));
				if(item.getItem().isIn(MubbleTags.Items.COINS)) {
					worldIn.playSound(null, x, y - 0.6D, z, coinLootSound, SoundCategory.BLOCKS, 1f, 1f);
				}
				else {
					worldIn.playSound(null, x, y - 0.6D, z, powerUpLootSound, SoundCategory.BLOCKS, 1f, 1f);
				}
			}
			worldIn.setBlockState(pos, emptyBlock);
		}
	}
}
