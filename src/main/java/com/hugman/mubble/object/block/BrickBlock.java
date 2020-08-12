package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlockPack;
import com.hugman.mubble.init.MubbleSoundPack;
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
import java.util.Random;

public class BrickBlock extends Block {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);

	public BrickBlock(BlockSoundGroup soundType) {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.RED).strength(2.0F, 6.0F).sounds(soundType));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean moved) {
		if(!worldIn.isClient && worldIn.isReceivingRedstonePower(pos)) {
			Random rand = new Random();
			switch(rand.nextInt(2)) {
				case 0:
					loot(worldIn, pos);
					break;
				case 1:
					worldIn.removeBlock(pos, false);
					break;
			}
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
		if(!worldIn.isClient && worldIn.isReceivingRedstonePower(pos)) {
			Random rand = new Random();
			switch(rand.nextInt(2)) {
				case 0:
					loot(worldIn, pos);
					break;
				case 1:
					worldIn.removeBlock(pos, false);
					break;
			}
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(!worldIn.isClient && entityIn.getVelocity().y > 0.0D) {
			Random rand = new Random();
			switch(rand.nextInt(2)) {
				case 0:
					loot(worldIn, pos);
					break;
				case 1:
					worldIn.removeBlock(pos, false);
					break;
			}
		}
	}

	public void loot(World worldIn, BlockPos pos) {
		if(!worldIn.isClient) {
			BlockState emptyBlock = MubbleBlockPack.SMB_EMPTY_BLOCK.getDefaultState();
			SoundEvent coinLootSound = MubbleSoundPack.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB;
			if(this == MubbleBlockPack.SMB_GROUND_BRICK_BLOCK
					|| this == MubbleBlockPack.SMB_UNDERGROUND_BRICK_BLOCK
					|| this == MubbleBlockPack.SMB_CASTLE_BRICK_BLOCK) {
				coinLootSound = MubbleSoundPack.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB;
				emptyBlock = MubbleBlockPack.SMB_EMPTY_BLOCK.getDefaultState();
			}
			else if(this == MubbleBlockPack.SMB3_BRICK_BLOCK) {
				coinLootSound = MubbleSoundPack.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB3;
				emptyBlock = MubbleBlockPack.SMB3_EMPTY_BLOCK.getDefaultState();
			}
			else if(this == MubbleBlockPack.SMW_BRICK_BLOCK) {
				coinLootSound = MubbleSoundPack.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMW;
				emptyBlock = MubbleBlockPack.SMW_EMPTY_BLOCK.getDefaultState();
			}
			else if(this == MubbleBlockPack.NSMBU_BRICK_BLOCK) {
				coinLootSound = MubbleSoundPack.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_NSMBU;
				emptyBlock = MubbleBlockPack.NSMBU_EMPTY_BLOCK.getDefaultState();
			}
			final double x = pos.getX() + 0.5D;
			final double y = pos.getY() + 0.5D + 0.6D;
			final double z = pos.getZ() + 0.5D;
			LootTable lootTable = worldIn.getServer().getLootManager().getTable(MubbleLootTables.BRICK_BLOCK);
			LootContext lootContext = new LootContext.Builder((ServerWorld) worldIn)
					.parameter(LootContextParameters.BLOCK_STATE, this.getDefaultState())
					.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
					.parameter(LootContextParameters.TOOL, ItemStack.EMPTY)
					.build(LootContextTypes.BLOCK);
			List<ItemStack> items = lootTable.generateLoot(lootContext);
			for(ItemStack item : items) {
				worldIn.spawnEntity(new ItemEntity(worldIn, x, y, z, item));
				worldIn.playSound(null, x, y - 0.6D, z, coinLootSound, SoundCategory.BLOCKS, 1f, 1f);
			}
			worldIn.setBlockState(pos, emptyBlock);
		}
	}
}
