package fr.hugman.mubble.test.gametest.support;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;

/**
 * Running a command the way a test wants it: as the server, on behalf of a player, with whatever it
 * answered kept rather than printed.
 */
public final class TestCommands {
    private TestCommands() {
    }

    /** Runs {@code command} and fails the test when it does not go through. */
    public static void run(GameTestHelper helper, ServerPlayer player, String command) {
        var outcome = perform(helper, player, command);
        helper.assertTrue(outcome.succeeded, "`/" + command + "` failed: " + outcome.message);
    }

    /** @return whether {@code command} went through, without minding what it answered */
    public static boolean succeeds(GameTestHelper helper, ServerPlayer player, String command) {
        return perform(helper, player, command).succeeded;
    }

    /** Runs {@code command} as the server, on behalf of {@code player}, keeping whatever it answered. */
    public static Outcome perform(GameTestHelper helper, ServerPlayer player, String command) {
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

        server.getCommands().performPrefixedCommand(source.withCallback((success, result) -> {
            outcome.succeeded = success;
            outcome.result = result;
        }), command);
        return outcome;
    }

    /** What a command left behind: whether it went through, what it returned, and what it said. */
    public static final class Outcome {
        public boolean succeeded;
        /** The number the command returned, which is what {@code execute if} hangs off. */
        public int result;
        public String message = "";
    }
}
