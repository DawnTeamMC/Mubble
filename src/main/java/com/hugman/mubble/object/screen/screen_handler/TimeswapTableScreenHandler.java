package com.hugman.mubble.object.screen.screen_handler;

import com.google.common.collect.Lists;
import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.init.MubbleSounds;
import com.hugman.mubble.init.data.MubbleTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class TimeswapTableScreenHandler extends ScreenHandler {
	private final ScreenHandlerContext context;
	private final Property selectedRecipe;
	private List<Item> availableRecipes;
	private ItemStack inputStack;
	private long lastTakeTime;
	final Slot inputSlot;
	final Slot outputSlot;
	private Runnable contentsChangedListener;
	public final Inventory input;
	private final CraftingResultInventory output;

	public TimeswapTableScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
	}

	public TimeswapTableScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
		super(null, syncId);
		this.selectedRecipe = Property.create();
		this.availableRecipes = Lists.newArrayList();
		this.inputStack = ItemStack.EMPTY;
		this.contentsChangedListener = () -> {
		};
		this.input = new SimpleInventory(1) {
			public void markDirty() {
				super.markDirty();
				TimeswapTableScreenHandler.this.onContentChanged(this);
				TimeswapTableScreenHandler.this.contentsChangedListener.run();
			}
		};
		this.output = new CraftingResultInventory();
		this.context = context;
		this.inputSlot = this.addSlot(new Slot(this.input, 0, 20, 33));
		this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
			public boolean canInsert(ItemStack stack) {
				return false;
			}

			public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
				ItemStack itemStack = TimeswapTableScreenHandler.this.inputSlot.takeStack(1);
				if(!itemStack.isEmpty()) {
					TimeswapTableScreenHandler.this.populateResult();
				}
				stack.getItem().onCraft(stack, player.world, player);
				context.run((world, blockPos) -> {
					long l = world.getTime();
					if(TimeswapTableScreenHandler.this.lastTakeTime != l) {
						world.playSound(null, blockPos, MubbleSounds.UI_TIMESWAP_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
						TimeswapTableScreenHandler.this.lastTakeTime = l;
					}

				});
				return super.onTakeItem(player, stack);
			}
		});
		int k;
		for(k = 0; k < 3; ++k) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
			}
		}
		for(k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
		this.addProperty(this.selectedRecipe);
	}

	@Environment(EnvType.CLIENT)
	public int getSelectedRecipe() {
		return this.selectedRecipe.get();
	}

	@Environment(EnvType.CLIENT)
	public List<Item> getAvailableRecipes() {
		return this.availableRecipes;
	}

	@Environment(EnvType.CLIENT)
	public int getAvailableRecipeCount() {
		return this.availableRecipes.size();
	}

	@Environment(EnvType.CLIENT)
	public boolean canCraft() {
		return this.inputSlot.hasStack() && !this.availableRecipes.isEmpty();
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(this.context, player, MubbleBlocks.TIMESWAP_TABLE);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return MubbleBlocks.TIMESWAP_TABLE_SCREEN_HANDLER;
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if(id >= 0 && id < this.availableRecipes.size()) {
			this.selectedRecipe.set(id);
			this.populateResult();
		}
		return true;
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		ItemStack itemStack = this.inputSlot.getStack();
		if(itemStack.getItem() != this.inputStack.getItem()) {
			this.inputStack = itemStack.copy();
			this.updateInput(inventory, itemStack);
		}

	}

	private void updateInput(Inventory input, ItemStack stack) {
		this.availableRecipes.clear();
		this.selectedRecipe.set(-1);
		this.outputSlot.setStack(ItemStack.EMPTY);
		if(!stack.isEmpty()) {
			Tag<Item> tag = correspondingTag(stack.getItem());
			if(tag != null) {
				this.availableRecipes = Lists.newArrayList();
				for(Item item : Registry.ITEM) {
					if(item.isIn(tag)) {
						this.availableRecipes.add(item);
					}
				}
			}
		}
	}

	private void populateResult() {
		if(!this.availableRecipes.isEmpty()) {
			Item item = this.availableRecipes.get(this.selectedRecipe.get());
			this.outputSlot.setStack(new ItemStack(item));
		}
		else {
			this.outputSlot.setStack(ItemStack.EMPTY);
		}
		this.sendContentUpdates();
	}

	@Environment(EnvType.CLIENT)
	public void setContentsChangedListener(Runnable runnable) {
		this.contentsChangedListener = runnable;
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if(slot != null && slot.hasStack()) {
			ItemStack itemstack1 = slot.getStack();
			Item item = itemstack1.getItem();
			itemstack = itemstack1.copy();
			Tag<Item> tag = correspondingTag(item);
			if(index == 1) {
				item.onCraft(itemstack1, playerIn.world, playerIn);
				if(!this.insertItem(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
				slot.onStackChanged(itemstack1, itemstack);
			}
			else if(index == 0) {
				if(!this.insertItem(itemstack1, 2, 38, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if(tag != null) {
				if(!this.insertItem(itemstack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if(index >= 2 && index < 29) {
				if(!this.insertItem(itemstack1, 29, 38, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if(index >= 29 && index < 38 && !this.insertItem(itemstack1, 2, 29, false)) {
				return ItemStack.EMPTY;
			}
			if(itemstack1.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			}
			slot.markDirty();
			if(itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(playerIn, itemstack1);
			this.sendContentUpdates();
		}
		return itemstack;
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		this.output.removeStack(1);
		this.context.run((world, blockPos) -> {
			this.dropInventory(player, player.world, this.input);
		});
	}

	private static Tag<Item> correspondingTag(Item item) {
		for(Tag<Item> tag : MubbleTags.Items.TIMESWAP_TAGS) {
			if(item.isIn(tag)) {
				return tag;
			}
			else {
				continue;
			}
		}
		return null;
	}
}