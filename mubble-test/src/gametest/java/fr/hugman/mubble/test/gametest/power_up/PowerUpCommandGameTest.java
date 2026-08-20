package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;

/**
 * {@code /powerup}, the way a power-up is handed out without an item. It is also the only user of
 * {@code PowerUpArgumentType}, so a broken argument shows up here first.
 */
public class PowerUpCommandGameTest {
    @GameTest
    public void setGivesThePowerUp(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);

        run(helper, player, "power_up set @s super_mario:fire");

        helper.assertTrue(player.getPowerUp().isPresent(), "the command handed out nothing");
        helper.assertTrue(player.getPowerUp().orElseThrow().is(SuperMarioPowerUpIds.FIRE), "the command handed out the wrong power-up");

        helper.succeed();
    }

    @GameTest
    public void setReachesADataPackPowerUpToo(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);

        run(helper, player, "power_up set @s mubble-gametest:shooter");

        helper.assertTrue(player.getPowerUp().orElseThrow().is(PowerUpFixtures.SHOOTER),
                "the argument does not reach the power-ups added by a data pack");

        helper.succeed();
    }

    @GameTest
    public void removeTakesThePowerUpBack(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);
        player.setPowerUp(PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.FIRE));

        run(helper, player, "power_up remove @s");

        helper.assertTrue(player.getPowerUp().isEmpty(), "the command left the power-up in place");
        helper.succeed();
    }

    @GameTest
    public void handingOutTheSamePowerUpTwiceFails(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);
        run(helper, player, "power_up set @s super_mario:fire");

        // The command refuses a no-op rather than reporting a success that changed nothing.
        helper.assertFalse(succeeds(helper, player, "power_up set @s super_mario:fire"),
                "handing out a power-up the player already holds should fail");

        helper.assertTrue(player.getPowerUp().orElseThrow().is(SuperMarioPowerUpIds.FIRE), "and the power-up should be untouched");
        helper.succeed();
    }

    @GameTest
    public void removingNothingFails(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);

        helper.assertFalse(succeeds(helper, player, "power_up remove @s"),
                "removing a power-up from a player who has none should fail");

        helper.succeed();
    }

    @GameTest
    public void anUnknownPowerUpIsRefused(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);

        helper.assertFalse(succeeds(helper, player, "power_up set @s super_mario:not_a_real_power_up"),
                "an unknown power-up id should be refused");
        helper.assertTrue(player.getPowerUp().isEmpty(), "and nothing should have been handed out");

        helper.succeed();
    }

    private static void run(GameTestHelper helper, ServerPlayer player, String command) {
        var outcome = perform(helper, player, command);
        helper.assertTrue(outcome.succeeded, "`/" + command + "` failed: " + outcome.message);
    }

    private static boolean succeeds(GameTestHelper helper, ServerPlayer player, String command) {
        return perform(helper, player, command).succeeded;
    }

    /** Runs {@code command} as the server, on behalf of {@code player}, keeping whatever it answered. */
    private static Outcome perform(GameTestHelper helper, ServerPlayer player, String command) {
        var server = helper.getLevel().getServer();
        var outcome = new Outcome();

        CommandSourceStack source = new CommandSourceStack(
                new CommandSource() {
                    @Override
                    public void sendSystemMessage(Component message) {
                        outcome.message = outcome.message + " | " + message.getString();
                    }

                    @Override
                    public boolean acceptsSuccess() {
                        return true;
                    }

                    @Override
                    public boolean acceptsFailure() {
                        return true;
                    }

                    @Override
                    public boolean shouldInformAdmins() {
                        return false;
                    }
                },
                player.position(),
                player.getRotationVector(),
                helper.getLevel(),
                PermissionSet.ALL_PERMISSIONS,
                "gametest",
                Component.literal("gametest"),
                server,
                player
        );

        server.getCommands().performPrefixedCommand(source.withCallback((success, result) -> outcome.succeeded = success), command);
        return outcome;
    }

    private static final class Outcome {
        boolean succeeded;
        String message = "";
    }
}
