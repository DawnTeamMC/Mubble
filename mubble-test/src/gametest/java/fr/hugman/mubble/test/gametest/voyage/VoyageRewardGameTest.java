package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.test.gametest.support.TestCommands;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.voyage.VoyageReward;
import fr.hugman.mubble.world.voyage.session.VoyageControl;
import fr.hugman.mubble.world.voyage.session.VoyageSessions;
import java.util.List;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Completion rewards.
 *
 * <p>{@code mubble-gametest:two_trials} pays two carrots, so every test here is a count of carrots
 * after an ending. The interesting ones are the zeros: a reward that survives a forfeit is a reward
 * nobody has to earn.
 */
public class VoyageRewardGameTest {
    private static final String VOYAGE = "mubble-gametest:two_trials";

    @GameTest
    public void completingPaysTheReward(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);

        complete(helper, player);

        helper.assertValueEqual(count(player, Items.CARROT), 2, "carrots after finishing the voyage");
        helper.assertTrue(sessions(helper).sessionOf(player) == null, "the voyage did not end");

        helper.succeed();
    }

    /**
     * The ordering the issue asks for, and the one that is easy to get wrong.
     *
     * <p>Rewards are granted after the stash is restored. Granting them first would put them into an
     * inventory that the restore then overwrites, and the reward would vanish with no error anywhere
     * — so this checks the reward and the player's own belongings are both there at the end.
     */
    @GameTest
    public void theRewardOutlivesTheInventoryRestore(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        player.getInventory().setItem(20, new ItemStack(Items.DIAMOND_SWORD));

        complete(helper, player);

        helper.assertTrue(ItemStack.matches(player.getInventory().getItem(20), new ItemStack(Items.DIAMOND_SWORD)),
                "the player's own sword did not come back");
        helper.assertValueEqual(count(player, Items.CARROT), 2, "carrots after finishing the voyage");

        helper.succeed();
    }

    @GameTest
    public void forfeitingPaysNothing(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);

        TestCommands.run(helper, player, "voyage start " + VOYAGE + " 5");
        sessions(helper).useControl(player, VoyageControl.FAIL);

        helper.assertValueEqual(count(player, Items.CARROT), 0, "carrots after forfeiting");
        helper.assertTrue(sessions(helper).sessionOf(player) == null, "forfeiting did not end the voyage");

        helper.succeed();
    }

    @GameTest
    public void abandoningPaysNothing(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);

        TestCommands.run(helper, player, "voyage start " + VOYAGE + " 5");
        TestCommands.run(helper, player, "voyage abandon");

        helper.assertValueEqual(count(player, Items.CARROT), 0, "carrots after abandoning");

        helper.succeed();
    }

    /** Leaving partway pays nothing either, and the reward is not banked for the next attempt. */
    @GameTest
    public void quittingHalfwayPaysNothing(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);

        TestCommands.run(helper, player, "voyage start " + VOYAGE + " 5");
        sessions(helper).useControl(player, VoyageControl.ADVANCE);
        helper.assertValueEqual(sessions(helper).sessionOf(player).trialNumber(), 2, "the trial reached");
        TestCommands.run(helper, player, "voyage abandon");

        helper.assertValueEqual(count(player, Items.CARROT), 0, "carrots after abandoning on the last trial");

        helper.succeed();
    }

    /**
     * The drop path, exercised on the reward itself rather than through a whole voyage.
     *
     * <p>Two things force that. The framework's mock player is creative, and
     * {@code Inventory#add} <em>deletes</em> an overflow for a player with infinite materials rather
     * than leaving it to be dropped — so the ability has to be taken away first. And restoring the
     * stash reapplies the saved game mode, which would put it back, so the grant has to be reached
     * without a restore in between. The voyage plumbing that calls this is covered by the tests
     * above.
     */
    @GameTest
    public void afullInventoryGetsTheRewardAtItsFeet(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < 36; slot++) {
            inventory.setItem(slot, new ItemStack(Items.STONE, 64));
        }
        player.getAbilities().instabuild = false;
        player.onUpdateAbilities();

        new VoyageReward(Items.CARROT, 2).grantTo(player);

        helper.assertValueEqual(count(player, Items.CARROT), 0, "carrots that fitted into a full inventory");

        // A tick late on purpose: an entity spawned this tick is queued rather than added, so asking
        // the level for it immediately finds nothing whether or not the drop happened.
        helper.runAfterDelay(1, () -> {
            List<ItemEntity> dropped = helper.getLevel().getEntitiesOfClass(
                    ItemEntity.class, player.getBoundingBox().inflate(4.0D),
                    entity -> entity.getItem().is(Items.CARROT));
            helper.assertValueEqual(dropped.size(), 1, "carrot stacks lying at the player's feet");
            helper.assertValueEqual(dropped.getFirst().getItem().getCount(), 2, "carrots in the dropped stack");

            // The test's own level is shared with everything after it, so do not leave items in it.
            dropped.forEach(ItemEntity::discard);

            helper.succeed();
        });
    }

    /** Runs the whole of {@code two_trials}: in, advance, advance, out. */
    private static void complete(GameTestHelper helper, ServerPlayer player) {
        TestCommands.run(helper, player, "voyage start " + VOYAGE + " 5");
        sessions(helper).useControl(player, VoyageControl.ADVANCE);
        sessions(helper).useControl(player, VoyageControl.ADVANCE);
    }

    private static VoyageSessions sessions(GameTestHelper helper) {
        return VoyageSessions.get(helper.getLevel().getServer());
    }

    private static int count(ServerPlayer player, Item item) {
        Inventory inventory = player.getInventory();
        int total = 0;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.is(item)) {
                total += stack.getCount();
            }
        }
        return total;
    }
}
