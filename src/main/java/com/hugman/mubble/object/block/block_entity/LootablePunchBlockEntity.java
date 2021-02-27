package com.hugman.mubble.object.block.block_entity;

import com.hugman.mubble.init.MubbleBlocks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootablePunchBlockEntity extends BlockEntity {
	protected ItemStack lootItemStack;
	protected Identifier lootTableId;
	protected long lootTableSeed;
	protected float breakChance;

	public LootablePunchBlockEntity() {
		super(MubbleBlocks.LOOTABLE_PUNCH_BLOCK_ENTITY);
		this.breakChance = 0.0F;
	}

	public static void setLootTable(BlockView world, Random random, BlockPos pos, Identifier id) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if(blockEntity instanceof LootablePunchBlockEntity) {
			((LootablePunchBlockEntity) blockEntity).setLootTable(id, random.nextLong());
		}
	}

	public void setLootItemStack(ItemStack stack) {
		this.lootItemStack = stack;
	}

	public ItemStack getLootItemStack() {
		return lootItemStack;
	}

	public void setLootTable(Identifier id, long seed) {
		this.lootTableId = id;
		this.lootTableSeed = seed;
	}

	public Identifier getLootTableId() {
		return this.lootTableId;
	}

	public long getLootTableSeed() {
		return this.lootTableSeed;
	}

	public void setBreakChance(float breakChance) {
		this.breakChance = breakChance;
	}

	public float getBreakChance() {
		return breakChance;
	}

	public boolean canBreak(Random random) {
		return random.nextFloat() < this.breakChance;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if(this.lootTableId != null) {
			tag.putString("LootTable", this.lootTableId.toString());
			if(this.lootTableSeed != 0L) {
				tag.putLong("LootTableSeed", this.lootTableSeed);
			}
		}
		if(this.lootItemStack != null) {
			tag.put("LootItem", lootItemStack.toTag(new CompoundTag()));
		}
		tag.putFloat("BreakChance", this.breakChance);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		if(tag.contains("LootTable", 8)) {
			this.lootTableId = new Identifier(tag.getString("LootTable"));
			this.lootTableSeed = tag.getLong("LootTableSeed");
		}
		if(tag.contains("LootItem", 10)) {
			this.lootItemStack = ItemStack.fromTag(tag.getCompound("LootItem"));
		}
		this.breakChance = tag.getFloat("BreakChance");
	}

	public List<ItemStack> getLoot(BlockState state, World world, @Nullable Entity entity) {
		List<ItemStack> itemStacks = new ArrayList<>();
		if (this.lootTableId != null && this.world.getServer() != null) {
			LootTable lootTable = world.getServer().getLootManager().getTable(this.lootTableId);
			if (entity instanceof ServerPlayerEntity) {
				Criteria.PLAYER_GENERATES_CONTAINER_LOOT.test((ServerPlayerEntity)entity, this.lootTableId);
			}
			LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.BLOCK_STATE, state).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos)).random(this.lootTableSeed).parameter(LootContextParameters.TOOL, ItemStack.EMPTY);
			if (entity != null) {
				if (entity instanceof PlayerEntity) {
					builder.luck(((PlayerEntity) entity).getLuck());
				}
				builder.parameter(LootContextParameters.THIS_ENTITY, entity);
			}
			itemStacks.addAll(lootTable.generateLoot(builder.build(LootContextTypes.BLOCK)));
		}
		if(this.lootItemStack != null) {
			itemStacks.add(lootItemStack);
		}
		return itemStacks;
	}
}