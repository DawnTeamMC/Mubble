package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.test.gametest.support.TestCommands;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.voyage.VoyageSeeds;
import fr.hugman.mubble.world.voyage.session.VoyageControl;
import fr.hugman.mubble.world.voyage.session.VoyageSession;
import fr.hugman.mubble.world.voyage.session.VoyageSessions;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;

/**
 * Branching routes and waystations.
 *
 * <p>{@code mubble-gametest:branching} is {@code seeded → fork → (left | right) → finish}, where
 * {@code fork} is a waystation and both routes are one trial long.
 */
public class VoyageRoutesGameTest {
    private static final String VOYAGE = "mubble-gametest:branching";

    @GameTest
    public void aWaystationIsEnteredLikeAnyOtherNode(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");
            sessions.useControl(player, VoyageControl.ADVANCE);

            VoyageSession session = sessions.sessionOf(player);
            helper.assertValueEqual(session.nodeKey(), "fork", "the node reached after the first trial");
            helper.assertTrue(!session.node().isTrial(), "the fork should be a waystation");
            // Waystations do not count towards the trial number, or "trial 2 of 3" would drift every
            // time content put a rest stop in.
            helper.assertValueEqual(session.trialNumber(), 1, "trials entered while standing in a waystation");
            helper.assertTrue(player.level() != helper.getLevel(), "the waystation opened no level");
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.succeed();
    }

    @GameTest
    public void aRouteItemPicksItsOwnRoute(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");
            sessions.useControl(player, VoyageControl.ADVANCE);
            sessions.useControl(player, VoyageControl.route("right"));

            helper.assertValueEqual(sessions.sessionOf(player).nodeKey(), "right", "the route taken");
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.succeed();
    }

    @GameTest
    public void aBranchWillNotChooseForYou(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");
            sessions.useControl(player, VoyageControl.ADVANCE);

            // A plain advance at a fork names no route. Picking one would silently decide the run.
            sessions.useControl(player, VoyageControl.ADVANCE);
            helper.assertValueEqual(sessions.sessionOf(player).nodeKey(), "fork", "the node after a routeless advance");

            // Nor does a route belonging to some other node.
            sessions.useControl(player, VoyageControl.route("seeded"));
            helper.assertValueEqual(sessions.sessionOf(player).nodeKey(), "fork", "the node after a stale route item");
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.succeed();
    }

    /**
     * The property that makes branching safe to seed.
     *
     * <p>A node's seed comes from its key, so the route not taken was already decided and the route
     * taken does not shift it. Reaching {@code finish} the long way or the short way must give the
     * same seed, or a shared run code would mean two different things.
     */
    @GameTest
    public void aNodeKeepsItsSeedWhicheverRouteReachesIt(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        long viaLeft;
        long viaRight;
        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 4242");
            sessions.useControl(player, VoyageControl.ADVANCE);
            sessions.useControl(player, VoyageControl.route("left"));
            sessions.useControl(player, VoyageControl.ADVANCE);
            viaLeft = VoyageSeeds.node(sessions.sessionOf(player).seed(), sessions.sessionOf(player).nodeKey());
            helper.assertValueEqual(sessions.sessionOf(player).nodeKey(), "finish", "where the left route ends");
            sessions.abandon(player);

            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 4242");
            sessions.useControl(player, VoyageControl.ADVANCE);
            sessions.useControl(player, VoyageControl.route("right"));
            sessions.useControl(player, VoyageControl.ADVANCE);
            viaRight = VoyageSeeds.node(sessions.sessionOf(player).seed(), sessions.sessionOf(player).nodeKey());
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.assertValueEqual(viaLeft, viaRight, "the seed of the node both routes rejoin at");

        helper.succeed();
    }

    @GameTest
    public void bothRoutesReachTheEnd(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        for (String route : new String[]{"left", "right"}) {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 9");
            sessions.useControl(player, VoyageControl.ADVANCE);
            sessions.useControl(player, VoyageControl.route(route));
            sessions.useControl(player, VoyageControl.ADVANCE);
            sessions.useControl(player, VoyageControl.ADVANCE);

            helper.assertTrue(sessions.sessionOf(player) == null, "the " + route + " route did not finish the voyage");
            helper.assertTrue(player.level() == helper.getLevel(), "the " + route + " route left the player elsewhere");
        }

        helper.succeed();
    }

    private static VoyageSessions sessions(GameTestHelper helper) {
        return VoyageSessions.get(helper.getLevel().getServer());
    }

    /** Node levels are real runtime dimensions, so a test that leaves one open leaks a dimension. */
    private static void abandonIfRunning(ServerPlayer player, VoyageSessions sessions) {
        if (sessions.sessionOf(player) != null) {
            sessions.abandon(player);
        }
    }
}
