package com.hugman.mubble.object.block;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.init.MubbleItems;
import com.hugman.mubble.init.MubbleSounds;
import com.hugman.mubble.init.data.MubbleTags;
import com.hugman.mubble.object.block.block_entity.LootablePunchBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.List;

public class LootablePunchBlock extends PunchBlock implements BlockEntityProvider {
	private final Block emptyBlock;
	private final SoundEvent coinSound;
	private final SoundEvent powerUpSound;

	public LootablePunchBlock(Block emptyBlock, SoundEvent coinSound, SoundEvent powerUpSound, Settings settings) {
		super(settings);
		this.emptyBlock = emptyBlock;
		this.coinSound = coinSound;
		this.powerUpSound = powerUpSound;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if(world.isReceivingRedstonePower(pos)) {
			loot(state, world, pos, null);
		}
		super.onBlockAdded(state, world, pos, oldState, notify);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if(world.isReceivingRedstonePower(pos)) {
			loot(state, world, pos, null);
		}
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		CompoundTag itemTag = stack.getOrCreateTag();
		if(!itemTag.contains("BlockEntityTag")) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof LootablePunchBlockEntity) {
				LootablePunchBlockEntity lootablePunchBlockEntity = (LootablePunchBlockEntity) blockEntity;
				lootablePunchBlockEntity.setLootItemStack(new ItemStack(MubbleItems.YELLOW_COIN));
				lootablePunchBlockEntity.setBreakChance(0.0F);
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if(!stack.isEmpty()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof LootablePunchBlockEntity) {
				LootablePunchBlockEntity lootablePunchBlockEntity = (LootablePunchBlockEntity) blockEntity;
				if(lootablePunchBlockEntity.getLootItemStack() != null) {
					ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, lootablePunchBlockEntity.getLootItemStack());
					itemEntity.setToDefaultPickupDelay();
					world.spawnEntity(itemEntity);
				}
				ItemStack stackCopy = stack.copy();
				lootablePunchBlockEntity.setLootItemStack(stackCopy);
				lootablePunchBlockEntity.setLootTable(null, 0L);
				stack.decrement(stackCopy.getCount());
				if(world.isClient) {
					return ActionResult.SUCCESS;
				}
				else {
					world.playSound(null, pos.add(0.5D, 0.5D, 0.5D), MubbleSounds.BLOCK_LOOTABLE_PUNCH_BLOCK_CHANGE_LOOT, SoundCategory.BLOCKS, 1f, 1f);
				}
			}
		}
		return ActionResult.CONSUME;
	}

	@Override
	public void onPunch(BlockState state, World world, BlockPos pos, Entity entity) {
		loot(state, world, pos, entity);
	}

	public void loot(BlockState state, World world, BlockPos pos, @Nullable Entity entity) {
		if(!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof LootablePunchBlockEntity) {
				if(((LootablePunchBlockEntity) blockEntity).canBreak(world.random)) {
					world.breakBlock(pos, false);
					return;
				}
				List<ItemStack> items = ((LootablePunchBlockEntity) blockEntity).getLoot(state, world, entity);
				for(ItemStack item : items) {
					world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, item));
					if(item.getItem().isIn(MubbleTags.Items.COINS)) {
						world.playSound(null, pos.add(0.5D, 0.5D, 0.5D), coinSound, SoundCategory.BLOCKS, 1f, 1f);
					}
					else {
						world.playSound(null, pos.add(0.5D, 0.5D, 0.5D), powerUpSound, SoundCategory.BLOCKS, 1f, 1f);
					}
				}
				world.setBlockState(pos, emptyBlock.getDefaultState());
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
		CompoundTag tag = stack.getSubTag("BlockEntityTag");
		if(tag != null) {
			if(tag.contains("LootItem")) {
				ItemStack lootItemStack = ItemStack.fromTag(tag.getCompound("LootItem"));
				MutableText mutableText = lootItemStack.getName().shallowCopy();
				mutableText.append(" x").append(String.valueOf(lootItemStack.getCount()));
				tooltip.add(mutableText);
			}
		}
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		super.onSyncedBlockEvent(state, world, pos, type, data);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new LootablePunchBlockEntity();
	}
}
