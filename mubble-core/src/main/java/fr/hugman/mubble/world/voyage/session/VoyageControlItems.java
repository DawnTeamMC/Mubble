package fr.hugman.mubble.world.voyage.session;

import fr.hugman.mubble.core.component.MubbleDataComponents;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * The two items a player holds during a trial.
 *
 * <p>Ordinary vanilla items carrying a {@link VoyageControl} marker and a name. Nothing subclasses
 * {@link net.minecraft.world.item.Item} and nothing is registered, because the marker is what the
 * behaviour reads — an emerald that has been through a furnace hopper is still just an emerald.
 *
 * <p>A placeholder for real trial objectives. Phase 2 left objectives deliberately unbuilt, so
 * "the player decides when the trial is over" is the stand-in, and these two items are how they say
 * so.
 */
public final class VoyageControlItems {
    private static final int ADVANCE_SLOT = 0;
    private static final int FAIL_SLOT = 8;

    private VoyageControlItems() {
    }

    /** Hooks up right-clicking either item. Ordinary items fall straight through. */
    public static void register() {
        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            VoyageControl control = stack.get(MubbleDataComponents.VOYAGE_CONTROL);
            if (control == null) {
                return InteractionResult.PASS;
            }
            if (player instanceof ServerPlayer serverPlayer) {
                VoyageSessions.get(serverPlayer.level().getServer()).useControl(serverPlayer, control);
            }
            // Consumed either way, so a control item never also places a block or eats itself.
            return InteractionResult.SUCCESS;
        });
    }

    public static ItemStack advance() {
        return marked(new ItemStack(Items.EMERALD), VoyageControl.ADVANCE,
                Component.literal("Complete Trial").withStyle(ChatFormatting.GREEN));
    }

    public static ItemStack fail() {
        return marked(new ItemStack(Items.REDSTONE), VoyageControl.FAIL,
                Component.literal("Forfeit Voyage").withStyle(ChatFormatting.RED));
    }

    /** Puts a fresh pair in the hotbar. Called on entering each trial. */
    public static void give(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        inventory.setItem(ADVANCE_SLOT, advance());
        inventory.setItem(FAIL_SLOT, fail());
        inventory.setSelectedSlot(ADVANCE_SLOT);
        player.containerMenu.broadcastChanges();
    }

    /**
     * Removes every control item the player is holding.
     *
     * <p>Sweeps the whole inventory rather than the two slots they were put in, because a player can
     * move them, and a control item surviving a voyage is an item that ends someone else's.
     */
    public static void strip(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).has(MubbleDataComponents.VOYAGE_CONTROL)) {
                inventory.setItem(slot, ItemStack.EMPTY);
            }
        }
        player.containerMenu.broadcastChanges();
    }

    private static ItemStack marked(ItemStack stack, VoyageControl control, Component name) {
        stack.set(MubbleDataComponents.VOYAGE_CONTROL, control);
        stack.set(DataComponents.CUSTOM_NAME, name);
        return stack;
    }
}
