package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.voyage.session.VoyageControl;
import fr.hugman.mubble.world.voyage.session.VoyageControlItems;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * The two items a player carries through a trial.
 *
 * <p>What is being defended is the gate. The behaviour keys off a data component rather than an item
 * type, so the test that matters is the negative one: an ordinary emerald must be inert, and must
 * survive a voyage that strips the marked ones.
 */
public class VoyageControlItemsGameTest {
    @GameTest
    public void controlItemsAreMarked(GameTestHelper helper) {
        helper.assertValueEqual(VoyageControlItems.advance().get(MubbleDataComponents.VOYAGE_CONTROL),
                VoyageControl.ADVANCE, "the marker on the advance item");
        helper.assertValueEqual(VoyageControlItems.fail().get(MubbleDataComponents.VOYAGE_CONTROL),
                VoyageControl.FAIL, "the marker on the fail item");
        helper.assertValueEqual(VoyageControlItems.route("left", Component.literal("Left")).get(MubbleDataComponents.VOYAGE_CONTROL),
                VoyageControl.route("left"), "the marker on a route item");

        helper.succeed();
    }

    @GameTest
    public void givingPutsBothInTheHotbar(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);

        giveFor(helper, player);

        long marked = countMarked(player);
        helper.assertValueEqual(marked, 2L, "the number of control items in the inventory");
        helper.assertValueEqual(player.getInventory().getSelectedSlot(), 0, "the selected slot");

        helper.succeed();
    }

    @GameTest
    public void strippingRemovesThemFromAnywhere(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        giveFor(helper, player);
        // Moved out of the hotbar, because a player can do that and a control item outliving its
        // voyage is an item that ends somebody else's.
        player.getInventory().setItem(0, ItemStack.EMPTY);
        player.getInventory().setItem(30, VoyageControlItems.advance());

        VoyageControlItems.strip(player);

        helper.assertValueEqual(countMarked(player), 0L, "the number of control items after stripping");

        helper.succeed();
    }

    @GameTest
    public void anOrdinaryItemIsNeitherMarkedNorStripped(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        Inventory inventory = player.getInventory();
        inventory.setItem(5, new ItemStack(Items.EMERALD, 12));
        giveFor(helper, player);

        VoyageControlItems.strip(player);

        helper.assertTrue(ItemStack.matches(inventory.getItem(5), new ItemStack(Items.EMERALD, 12)),
                "stripping the control items took an ordinary emerald with them");
        helper.assertTrue(!inventory.getItem(5).has(MubbleDataComponents.VOYAGE_CONTROL),
                "an ordinary emerald came out marked");

        helper.succeed();
    }

    /** Hands out the set for a plain one-way-out node, which is what most of these tests want. */
    private static void giveFor(GameTestHelper helper, ServerPlayer player) {
        VoyageDefinition voyage = helper.getLevel().registryAccess()
                .lookupOrThrow(MubbleRegistries.VOYAGE)
                .getValue(Identifier.fromNamespaceAndPath("mubble-gametest", "two_trials"));
        VoyageControlItems.give(player, voyage, voyage.node("second"));
    }

    private static long countMarked(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        long count = 0;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).has(MubbleDataComponents.VOYAGE_CONTROL)) {
                count++;
            }
        }
        return count;
    }
}
