package fr.hugman.mubble.screen;

import fr.hugman.mubble.block.BumpableDropMode;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BumpableScreenHandler extends ScreenHandler {
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;

	public BumpableScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(1));
	}

	public BumpableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(SuperMario.BUMPABLE_BLOCK_SCREEN_HANDLER, syncId);
		ScreenHandler.checkSize(inventory, 1);
		ScreenHandler.checkDataCount(propertyDelegate, 1);
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		this.inventory.onOpen(playerInventory.player);

		// block inventory
		this.addSlot(new Slot(inventory, 0, 26, 18));

		int playerInventoryOffset = 52;

		// player inventory
		for(int line = 0; line < 3; line++) {
			for(int column = 0; column < 9; column++) {
				this.addSlot(new Slot(playerInventory, column + line * 9 + 9, 8 + column * 18, playerInventoryOffset + line * 18));
			}
		}
		// player hotbar
		for(int column = 0; column < 9; column++) {
			this.addSlot(new Slot(playerInventory, column, 8 + column * 18, playerInventoryOffset + 58));
		}

		this.addProperties(propertyDelegate);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		ItemStack ogStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if(slot.hasStack()) {
			ItemStack stack = slot.getStack();
			ogStack = stack.copy();
			if(index < this.inventory.size() ? !this.insertItem(stack, this.inventory.size(), this.slots.size(), true) : !this.insertItem(stack, 0, this.inventory.size(), false)) {
				return ItemStack.EMPTY;
			}
			if(stack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			}
			else {
				slot.markDirty();
			}
		}
		return ogStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if(id == 0) {
			this.setDropMode(this.getDropMode().next());
			return true;
		}
		return false;
	}

	public void setDropMode(BumpableDropMode mode) {
		this.setProperty(0, mode.getIndex());
	}

	public BumpableDropMode getDropMode() {
		return BumpableDropMode.get(this.propertyDelegate.get(0));
	}

	@Override
	public void setProperty(int id, int value) {
		super.setProperty(id, value);
		this.sendContentUpdates();
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.inventory.onClose(player);
	}
}
