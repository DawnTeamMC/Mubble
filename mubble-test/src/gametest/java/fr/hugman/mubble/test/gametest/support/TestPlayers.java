package fr.hugman.mubble.test.gametest.support;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

/**
 * The one place that decides how a test gets a player.
 * <p>
 * The framework offers three mock players and they are not interchangeable here:
 * {@code makeMockServerPlayer} comes without a {@code connection}, and the power-up code sends a
 * packet whenever it changes, so it dies on a {@link NullPointerException}. The two below are the
 * ones that work; going through this class keeps the next test from picking the wrong one.
 */
public final class TestPlayers {
    private TestPlayers() {
    }

    /** A survival player that can hold a power-up. Not placed in the arena. */
    public static Player mock(GameTestHelper helper) {
        return helper.makeMockPlayer(GameType.SURVIVAL);
    }

    /** A real server player standing in the arena, for anything needing an actual position. */
    public static ServerPlayer inLevel(GameTestHelper helper) {
        return helper.makeMockServerPlayerInLevel();
    }

    /** A server player standing on {@code pos}, in structure-relative coordinates. */
    public static ServerPlayer at(GameTestHelper helper, net.minecraft.core.BlockPos pos) {
        var player = inLevel(helper);
        var absolute = helper.absolutePos(pos);
        player.teleportTo(absolute.getX() + 0.5D, absolute.getY(), absolute.getZ() + 0.5D);
        return player;
    }

    /**
     * Advances {@code player} by one tick with a client behind them: the keys they hold down going in, and
     * the movement they made coming back out.
     * <p>
     * Both halves normally arrive as packets, and both are read by the server rather than worked out by it:
     * {@code getLastClientInput()} is the only place the jump key exists server-side, and
     * {@code getKnownMovement()} the only honest reading of how a player is actually moving. A mock player
     * has nobody sending either, so a test standing in for the client has to send both itself.
     */
    public static void tick(ServerPlayer player, Input keys) {
        player.setLastClientInput(keys);
        var before = player.position();
        tick(player);
        player.setKnownMovement(player.position().subtract(before));
    }

    /** The keys of a player leaning on the jump button, and on nothing else. */
    public static Input holdingJump() {
        return new Input(false, false, false, false, true, false, false);
    }

    /**
     * Advances {@code player} by one tick, the way a connected client would.
     * <p>
     * {@link ServerPlayer#tick()} is not it: that one only does the server-side bookkeeping, and the
     * real entity tick — physics, fluid state, and every mixin hooked on {@code Player.tick} — hangs
     * off {@link ServerPlayer#doTick()}, which the connection normally drives. A mock player has no
     * client feeding it, so the tests drive it themselves.
     */
    public static void tick(ServerPlayer player) {
        player.doTick();
    }
}
