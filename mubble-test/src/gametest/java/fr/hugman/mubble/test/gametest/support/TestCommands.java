package fr.hugman.mubble.test.gametest.support;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;

/**
 * Runs a command on behalf of a test player and keeps what it answered.
 *
 * <p>A command's refusal is half of its behaviour — "this is not a real id" and "you are already in
 * one" are the parts a player actually meets — so the tests need the message and the success flag,
 * not just whether an exception escaped.
 */
public final class TestCommands {
    private TestCommands() {
    }

    /** Runs {@code command} and fails the test if it did not succeed. */
    public static void run(GameTestHelper helper, ServerPlayer player, String command) {
        Outcome outcome = perform(helper, player, command);
        helper.assertTrue(outcome.succeeded(), "`/" + command + "` failed: " + outcome.message());
    }

    /** {@return whether {@code command} succeeded}, without failing the test either way. */
    public static boolean succeeds(GameTestHelper helper, ServerPlayer player, String command) {
        return perform(helper, player, command).succeeded();
    }

    /** Runs {@code command} as the server, on behalf of {@code player}. */
    public static Outcome perform(GameTestHelper helper, ServerPlayer player, String command) {
        MinecraftServer server = helper.getLevel().getServer();
        Outcome outcome = new Outcome();

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

        server.getCommands().performPrefixedCommand(
                source.withCallback((success, result) -> outcome.succeeded = success), command);
        return outcome;
    }

    /** What a command did: whether it worked, and everything it said while doing it. */
    public static final class Outcome {
        private boolean succeeded;
        private String message = "";

        public boolean succeeded() {
            return this.succeeded;
        }

        /** Every line the command sent, joined, so a test can assert on what a player would read. */
        public String message() {
            return this.message;
        }
    }
}
