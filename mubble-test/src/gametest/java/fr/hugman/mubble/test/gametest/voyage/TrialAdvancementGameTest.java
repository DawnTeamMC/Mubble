package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.test.gametest.support.TestCommands;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.voyage.session.VoyageControl;
import fr.hugman.mubble.world.voyage.session.VoyageSessions;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;

/**
 * Earning an advancement by completing a trial.
 *
 * <p>The point of the design is that no Java knows which trial earns what — an advancement names the
 * trial it wants, so content adds one by adding a JSON file. These tests use advancements from the
 * game test data pack, which is exactly the route a third party would take.
 */
public class TrialAdvancementGameTest {
    private static final String VOYAGE = "mubble-gametest:two_trials";
    private static final Identifier ANY_TRIAL = Identifier.fromNamespaceAndPath("mubble-gametest", "any_trial");
    private static final Identifier SEEDED_CLEARED = Identifier.fromNamespaceAndPath("mubble-gametest", "seeded_cleared");
    private static final Identifier SEEDED_NO_JUMP = Identifier.fromNamespaceAndPath("mubble-gametest", "seeded_no_jump");

    @GameTest
    public void completingATrialEarnsItsAdvancement(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        try {
            helper.assertTrue(!hasAdvancement(helper, player, SEEDED_CLEARED), "the player started with the advancement");

            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");
            sessions.useControl(player, VoyageControl.ADVANCE);

            helper.assertTrue(hasAdvancement(helper, player, SEEDED_CLEARED),
                    "completing the trial did not earn the advancement naming it");
            helper.assertTrue(hasAdvancement(helper, player, ANY_TRIAL),
                    "the advancement naming no trial in particular was not earned");
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.succeed();
    }

    /** Entering is not completing, or "clear every trial" would be satisfied by walking in. */
    @GameTest
    public void enteringATrialEarnsNothing(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");

            helper.assertTrue(!hasAdvancement(helper, player, SEEDED_CLEARED),
                    "standing in the trial earned its completion advancement");
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.succeed();
    }

    /** A statistic condition asks about the trial, not about the save. */
    @GameTest
    public void aStatConditionMeasuresTheTrialOnly(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        try {
            // Jumped before the voyage, which must not count against "completed without jumping".
            player.awardStat(Stats.JUMP);

            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");
            sessions.useControl(player, VoyageControl.ADVANCE);

            helper.assertTrue(hasAdvancement(helper, player, SEEDED_NO_JUMP),
                    "a jump from before the trial counted against it, so the condition is a total and not a delta");
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.succeed();
    }

    @GameTest
    public void aStatConditionFailsWhenItIsBroken(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        VoyageSessions sessions = sessions(helper);

        try {
            TestCommands.run(helper, player, "voyage start " + VOYAGE + " 1");
            player.awardStat(Stats.JUMP);
            sessions.useControl(player, VoyageControl.ADVANCE);

            helper.assertTrue(!hasAdvancement(helper, player, SEEDED_NO_JUMP),
                    "jumping inside the trial still earned the no-jumping advancement");
            // The unconditional one is the control: the trial was completed either way.
            helper.assertTrue(hasAdvancement(helper, player, SEEDED_CLEARED),
                    "the trial was not completed at all");
        } finally {
            abandonIfRunning(player, sessions);
        }

        helper.succeed();
    }

    private static boolean hasAdvancement(GameTestHelper helper, ServerPlayer player, Identifier id) {
        AdvancementHolder holder = helper.getLevel().getServer().getAdvancements().get(id);
        if (holder == null) {
            throw new AssertionError(id + " is missing; the game test data pack did not load");
        }
        return player.getAdvancements().getOrStartProgress(holder).isDone();
    }

    private static VoyageSessions sessions(GameTestHelper helper) {
        return VoyageSessions.get(helper.getLevel().getServer());
    }

    private static void abandonIfRunning(ServerPlayer player, VoyageSessions sessions) {
        if (sessions.sessionOf(player) != null) {
            sessions.abandon(player);
        }
    }
}
