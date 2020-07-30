package com.hugman.mubble.object.block.block_entity;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.init.MubbleSounds;
import com.hugman.mubble.object.block.PresentBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;

public class PresentBlockEntity extends LootableContainerBlockEntity {
	private DefaultedList<ItemStack> inventory;
	private int viewerCount;

	public PresentBlockEntity() {
		super(MubbleBlocks.PRESENT_ENTITY);
		this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if(!this.serializeLootTable(tag)) {
			Inventories.toTag(tag, this.inventory);
		}
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if(!this.deserializeLootTable(tag)) {
			Inventories.fromTag(tag, this.inventory);
		}

	}

	@Override
	public int size() {
		return 9 * 2;
	}

	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X2, syncId, playerInventory, this, 2);
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public void onOpen(PlayerEntity player) {
		if(!player.isSpectator()) {
			if(this.viewerCount < 0) {
				this.viewerCount = 0;
			}
			++this.viewerCount;
			BlockState blockstate = this.getCachedState();
			boolean flag1 = blockstate.get(PresentBlock.OPEN);
			boolean flag2 = blockstate.get(PresentBlock.EMPTY);
			if(!flag1) {
				if(!flag2) {
					this.playSound(blockstate, MubbleSounds.BLOCK_PRESENT_OPEN);
				}
				this.setOpenProperty(blockstate, true);
			}
			this.scheduleTick();
		}
	}

	private void scheduleTick() {
		this.world.getBlockTickScheduler().schedule(this.getPos(), this.getCachedState().getBlock(), 5);
	}

	public void tick() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		this.viewerCount = ChestBlockEntity.countViewers(this.world, this, i, j, k);
		BlockState blockstate = this.getCachedState();
		boolean flag1 = blockstate.get(PresentBlock.OPEN);
		boolean flag2 = this.isEmpty() && this.lootTableId == null;
		this.setEmptyProperty(blockstate, flag2);
		if(this.viewerCount > 0) {
			this.scheduleTick();
		}
		else {
			if(!(blockstate.getBlock() instanceof PresentBlock)) {
				this.markRemoved();
				return;
			}
			if(flag1) {
				if(!flag2) {
					this.playSound(blockstate, MubbleSounds.BLOCK_PRESENT_CLOSE);
				}
				this.setOpenProperty(blockstate.with(PresentBlock.EMPTY, flag2), false);
			}
		}
	}

	@Override
	public void onClose(PlayerEntity player) {
		if(!player.isSpectator()) {
			--this.viewerCount;
		}
	}

	private void setOpenProperty(BlockState state, boolean open) {
		this.world.setBlockState(this.getPos(), state.with(PresentBlock.OPEN, Boolean.valueOf(open)), 3);
	}

	private void setEmptyProperty(BlockState state, boolean empty) {
		this.world.setBlockState(this.getPos(), state.with(PresentBlock.EMPTY, Boolean.valueOf(empty)), 3);
	}

	private void playSound(BlockState state, SoundEvent sound) {
		double d0 = (double) this.pos.getX() + 0.5D;
		double d1 = (double) this.pos.getY() + 0.5D;
		double d2 = (double) this.pos.getZ() + 0.5D;
		this.world.playSound(null, d0, d1, d2, sound, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container." + Mubble.MOD_ID + ".present");
	}
}