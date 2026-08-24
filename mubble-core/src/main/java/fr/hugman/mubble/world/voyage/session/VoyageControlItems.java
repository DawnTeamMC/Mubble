package fr.hugman.mubble.world.voyage.session;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.VoyageNode;
import java.util.List;

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
 * The items a player holds inside a voyage node.
 *
 * <p>Ordinary vanilla items carrying a {@link VoyageControl} marker and a name. Nothing subclasses
 * {@link net.minecraft.world.item.Item} and nothing is registered, because the marker is what the
 * behaviour reads — an emerald that has been through a furnace hopper is still just an emerald.
 *
 * <p>A node with one way out gives one emerald. A node with several gives one per route, named after
 * where it goes, and the player picks by using one. Both are placeholders: the real thing ends a
 * trial with an objective and picks a route by walking through a door.
 */
public final class VoyageControlItems {
    private static final int FAIL_SLOT = 8;

    private VoyageControlItems() {
    }

    /** Hooks up right-clicking a control item. Ordinary items fall straight through. */
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

    /** An item that finishes this node and takes the route to {@code destination}. */
    public static ItemStack route(String destination, Component where) {
        return marked(new ItemStack(Items.ENDER_PEARL), VoyageControl.route(destination),
                Component.literal("Go to ").append(where).withStyle(ChatFormatting.AQUA));
    }

    /**
     * Puts a fresh set in the hotbar: one way out per route, and the forfeit.
     *
     * <p>Called on entering each node, so what the player is holding always matches where they can
     * actually go from where they are standing.
     */
    public static void give(ServerPlayer player, VoyageDefinition voyage, VoyageNode node) {
        Inventory inventory = player.getInventory();
        List<String> routes = node.next();

        if (routes.size() <= 1) {
            // One way on, or none and this is the last node — either way the emerald ends the node
            // and the session works out what that means.
            inventory.setItem(0, advance());
        } else {
            for (int slot = 0; slot < routes.size(); slot++) {
                String destination = routes.get(slot);
                inventory.setItem(slot, route(destination, voyage.node(destination).content().displayName()));
            }
        }

        inventory.setItem(FAIL_SLOT, fail());
        inventory.setSelectedSlot(0);
        player.containerMenu.broadcastChanges();
    }

    /**
     * Removes every control item the player is holding.
     *
     * <p>Sweeps the whole inventory rather than the slots they were put in, because a player can
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
