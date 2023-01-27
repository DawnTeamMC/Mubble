package fr.hugman.mubble.screen;

import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BumpableBlockScreenHandler extends ScreenHandler {
	private final Inventory inventory;

	public BumpableBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(1));
	}

	public BumpableBlockScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(SuperMario.BUMPABLE_BLOCK_SCREEN_HANDLER, syncId);
		this.inventory = inventory;
		ScreenHandler.checkSize(this.inventory, 1);
		this.inventory.onOpen(playerInventory.player);

		// block inventory
		this.addSlot(new Slot(inventory, 0, 8 + 4 * 18, 20));

		int playerInventoryOffset = 51;

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
	public void close(PlayerEntity player) {
		super.close(player);
		this.inventory.onClose(player);
	}
}
