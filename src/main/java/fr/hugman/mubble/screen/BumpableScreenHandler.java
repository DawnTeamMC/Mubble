package fr.hugman.mubble.screen;

import fr.hugman.mubble.block.BumpableDropMode;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BumpableScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;

    public BumpableScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(1), new SimpleContainerData(2));
    }

    public BumpableScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(MubbleScreenHandlerTypes.BUMPABLE_BLOCK, syncId);
        AbstractContainerMenu.checkContainerSize(inventory, 1);
        AbstractContainerMenu.checkContainerDataCount(propertyDelegate, 2);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.inventory.startOpen(playerInventory.player);

        // block inventory
        this.addSlot(new Slot(inventory, 0, 26, 18));

        int playerInventoryOffset = 52;

        // player inventory
        for (int line = 0; line < 3; line++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory, column + line * 9 + 9, 8 + column * 18, playerInventoryOffset + line * 18));
            }
        }
        // player hotbar
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, playerInventoryOffset + 58));
        }

        this.addDataSlots(propertyDelegate);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ogStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ogStack = stack.copy();
            if (index < this.inventory.getContainerSize() ? !this.moveItemStackTo(stack, this.inventory.getContainerSize(), this.slots.size(), true) : !this.moveItemStackTo(stack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return ogStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            if (this.isDropModeLocked()) {
                return true;
            }
            this.setDropMode(this.getDropMode().next());
            return true;
        }
        return false;
    }

    public void setDropMode(BumpableDropMode mode) {
        this.setData(0, mode.getIndex());
    }

    public BumpableDropMode getDropMode() {
        return BumpableDropMode.get(this.propertyDelegate.get(0));
    }

    public boolean isDropModeLocked() {
        return this.propertyDelegate.get(1) != 0;
    }

    @Override
    public void setData(int id, int value) {
        super.setData(id, value);
        this.broadcastChanges();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }
}
