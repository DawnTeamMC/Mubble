package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.test.gametest.support.TestCommands;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.voyage.session.VoyageSession;
import fr.hugman.mubble.world.voyage.session.VoyageSessions;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * {@code /voyage}, which is the only way a player touches any of this.
 *
 * <p>The command is thin — every branch is one call into {@link VoyageSessions} — so what is worth
 * testing is the refusals. "You are already in one" and "that id does not exist" are the parts a
 * player actually meets, and a command that quietly succeeds at neither is worse than one that
 * throws.
 *
 * <p>{@code mubble-gametest:two_trials} is used rather than the testmod's voyage, so that
 * rebalancing content cannot break a test about a command.
 */
public class VoyageCommandGameTest {
    private static final String VOYAGE = "mubble-gametest:two_trials";

    @GameTest
    public void startPutsThePlayerInTheFirstTrial(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = VoyageSessions.get(helper.getLevel().getServer());

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1234");

            VoyageSession session = sessions.sessionOf(player);
            helper.assertTrue(session != null, "the command started no session");
            helper.assertValueEqual(session.trialNumber(), 1, "the trial the player landed in");
            helper.assertValueEqual(session.seed(), 1234L, "the seed the command passed through");
            helper.assertTrue(player.level() != helper.getLevel(), "the player was not moved into a trial level");
        } finally {
            abandonIfRunning(helper, player, sessions);
        }

        helper.succeed();
    }

    @GameTest
    public void anOmittedSeedIsGeneratedAndReported(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = VoyageSessions.get(helper.getLevel().getServer());

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE);

            VoyageSession session = sessions.sessionOf(player);
            helper.assertTrue(session != null, "the command started no session");
            // Reported, not just generated: a seed the player cannot read is a seed they cannot share.
            String status = TestCommands.perform(helper, player, "voyage status").message();
            helper.assertTrue(status.contains(Long.toString(session.seed())),
                    "status did not name the generated seed, it said: " + status);
        } finally {
            abandonIfRunning(helper, player, sessions);
        }

        helper.succeed();
    }

    @GameTest
    public void startingASecondVoyageIsRefused(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = VoyageSessions.get(helper.getLevel().getServer());

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");
            long seed = sessions.sessionOf(player).seed();

            helper.assertFalse(TestCommands.succeeds(helper, player, "voyage start " + VOYAGE + " 2"),
                    "starting a second voyage should be refused");
            // The point of refusing: a second start would strand the first stash.
            helper.assertValueEqual(sessions.sessionOf(player).seed(), seed, "the seed of the original voyage");
        } finally {
            abandonIfRunning(helper, player, sessions);
        }

        helper.succeed();
    }

    @GameTest
    public void anUnknownVoyageIsRefused(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);

        var outcome = TestCommands.perform(helper, player, "voyage start mubble-gametest:not_a_real_voyage");

        helper.assertFalse(outcome.succeeded(), "an unknown voyage id should be refused");
        helper.assertTrue(outcome.message().contains("not_a_real_voyage"),
                "the refusal should name the id that was wrong, it said: " + outcome.message());
        helper.assertTrue(VoyageSessions.get(helper.getLevel().getServer()).sessionOf(player) == null,
                "a refused start left a session behind");

        helper.succeed();
    }

    @GameTest
    public void statusAndAbandonNeedAVoyage(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);

        helper.assertFalse(TestCommands.succeeds(helper, player, "voyage status"),
                "status should refuse when the player is not in a voyage");
        helper.assertFalse(TestCommands.succeeds(helper, player, "voyage abandon"),
                "abandon should refuse when the player is not in a voyage");

        helper.succeed();
    }

    @GameTest
    public void abandonReturnsThePlayerIntact(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        player.getInventory().setItem(0, new ItemStack(Items.DIAMOND_SWORD));
        Vec3 where = player.position();

        TestCommands.run(helper, player, "voyage start " + VOYAGE + " 7");
        // Not "slot 0 is empty" — slot 0 is where the advance control item goes.
        helper.assertFalse(carries(player, Items.DIAMOND_SWORD), "the sword came into the trial");

        TestCommands.run(helper, player, "voyage abandon");

        helper.assertTrue(VoyageSessions.get(helper.getLevel().getServer()).sessionOf(player) == null,
                "abandon left the session running");
        helper.assertTrue(player.level() == helper.getLevel(), "abandon did not bring the player back");
        helper.assertValueEqual(player.position(), where, "the position the player was returned to");
        helper.assertTrue(ItemStack.matches(player.getInventory().getItem(0), new ItemStack(Items.DIAMOND_SWORD)),
                "abandon did not give the sword back");

        helper.succeed();
    }

    private static boolean carries(ServerPlayer player, net.minecraft.world.item.Item item) {
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).is(item)) {
                return true;
            }
        }
        return false;
    }

    /** Trial levels are real runtime dimensions, so a test that leaves one open leaks a dimension. */
    private static void abandonIfRunning(GameTestHelper helper, ServerPlayer player, VoyageSessions sessions) {
        if (sessions.sessionOf(player) != null) {
            sessions.abandon(player);
        }
    }
}
