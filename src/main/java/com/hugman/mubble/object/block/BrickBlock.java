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
import java.util.Random;

public class BrickBlock extends HittableBlock {
	private final Block emptyBlock;
	private final SoundEvent coinSound;

	public BrickBlock(Block emptyBlock, SoundEvent coinSound, Settings settings) {
		super(settings);
		this.emptyBlock = emptyBlock;
		this.coinSound = coinSound;
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
					worldIn.breakBlock(pos, false);
					break;
			}
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if(!world.isClient && world.isReceivingRedstonePower(pos)) {
			Random rand = new Random();
			switch(rand.nextInt(2)) {
				case 0:
					loot(world, pos);
					break;
				case 1:
					world.breakBlock(pos, false);
					break;
			}
		}
	}

	@Override
	public void onHit(BlockState state, World world, BlockPos pos, Entity entity) {
		if(!world.isClient) {
			Random rand = new Random();
			switch(rand.nextInt(2)) {
				case 0:
					loot(world, pos);
					break;
				case 1:
					world.breakBlock(pos, false, entity);
					break;
			}
		}
	}

	public void loot(World worldIn, BlockPos pos) {
		if(!worldIn.isClient) {
			final double x = pos.getX() + 0.5D;
			final double y = pos.getY() + 0.5D;
			final double z = pos.getZ() + 0.5D;
			LootTable lootTable = worldIn.getServer().getLootManager().getTable(MubbleLootTables.BRICK_BLOCK);
			LootContext lootContext = new LootContext.Builder((ServerWorld) worldIn).parameter(LootContextParameters.BLOCK_STATE, this.getDefaultState()).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).build(LootContextTypes.BLOCK);
			List<ItemStack> items = lootTable.generateLoot(lootContext);
			for(ItemStack item : items) {
				worldIn.spawnEntity(new ItemEntity(worldIn, x, y + 0.6D, z, item));
				worldIn.playSound(null, x, y, z, this.coinSound, SoundCategory.BLOCKS, 1f, 1f);
			}
			worldIn.setBlockState(pos, this.emptyBlock.getDefaultState());
		}
	}
}
