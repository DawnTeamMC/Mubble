package fr.hugman.mubble.world.arcade.server;

import fr.hugman.mubble.mixin.ServerGamePacketListenerImplAccessor;
import fr.hugman.mubble.network.protocol.common.custom.ArcadeCorrectionPayload;
import fr.hugman.mubble.network.protocol.common.custom.ArcadeInputPayload;
import fr.hugman.mubble.world.arcade.ArcadeController;
import fr.hugman.mubble.world.arcade.ArcadeValidation;
import fr.hugman.mubble.world.arcade.sim.ArcadeSimulation;
import fr.hugman.mubble.world.arcade.sim.ArcadeTuning;
import fr.hugman.mubble.world.arcade.sim.MoveContext;
import fr.hugman.mubble.world.arcade.sim.MoveResult;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.Nullable;

/**
 * The server side of the arcade movement: it replays every step a client reports through the shared
 * simulation, and only lets through what the simulation could have produced.
 * <p>
 * For a player whose layer drives the movement, this takes over two vanilla checks, and only those:
 * "moved too quickly", which would trip on any launch, and the floating kick, which would trip on any
 * ledge hang or wall slide. Collisions and every other vanilla check keep running as usual. A step
 * that does not hold up sends the player back where the server last saw them, once, and is logged at
 * debug level.
 */
public final class ArcadeValidator {
    private static final Logger LOGGER = LogManager.getLogger("mubble");

    private ArcadeValidator() {
    }

    public static void handleInput(ServerPlayer player, ArcadeInputPayload payload) {
        var controller = ArcadeController.of(player);
        if (!controller.isDriving() || controller.profile() == null) {
            // the client is ahead of the server about its own sources, which the next sync settles
            return;
        }
        if (((ServerGamePacketListenerImplAccessor) player.connection).mubble$getAwaitingPositionFromClient() != null) {
            // steps taken before the client heard of the last teleport are void
            return;
        }
        var validation = controller.validation();
        var settings = controller.profile().validation();
        var frame = payload.frame().sanitized();
        var result = payload.result();
        var from = payload.from();
        if (!result.isFinite() || !Double.isFinite(from.x) || !Double.isFinite(from.y) || !Double.isFinite(from.z)) {
            reject(player, controller, frame.tick(), "non-finite numbers");
            return;
        }
        if (controller.needsReset()) {
            controller.resetFromEntity();
        }
        if (from.distanceTo(player.position()) > settings.positionTolerance()) {
            reject(player, controller, frame.tick(), "started at " + from + ", the server has the player at " + player.position());
            return;
        }

        var state = controller.state().copy();
        if (payload.impulse().isPresent()) {
            var impulse = payload.impulse().get();
            if (!impulseHoldsUp(player, controller, settings, new Vec3(state.vx, state.vy, state.vz), impulse)) {
                reject(player, controller, frame.tick(), "an unexplained change of velocity to " + impulse);
                return;
            }
            state.vx = impulse.x;
            state.vy = impulse.y;
            state.vz = impulse.z;
        }

        var tuning = controller.tuning();
        var ctx = ArcadeSimulation.plan(state, frame, tuning, controller.world(), from);
        var reason = implausibility(ctx, result, tuning, settings);
        if (reason != null) {
            reject(player, controller, frame.tick(), reason);
            return;
        }
        ArcadeSimulation.settle(ctx, result);

        controller.setState(state);
        validation.expectedPosition = from.add(result.dx(), result.dy(), result.dz());
        validation.lastTick = frame.tick();
        validation.accepted++;
        // the vanilla move packet right behind this one moves the hitbox: it has to have the pose of the step already
        player.setPose(state.pose);
        if (player.isUsingItem() && controller.handsBusy()) {
            player.stopUsingItem();
        }
        controller.applyServerEffects(ctx);
        controller.updateVisual();

        if (state.fingerprint() != payload.fingerprint()) {
            validation.corrected++;
            ServerPlayNetworking.send(player, new ArcadeCorrectionPayload(frame.tick(), state.copy(), false));
        }
    }

    /**
     * Whether a change of velocity the client did not simulate can be explained: either it is small
     * enough to come from the world (bubble columns, entities shoving), or the server pushed the
     * player recently and about that hard.
     */
    private static boolean impulseHoldsUp(ServerPlayer player, ArcadeController controller, ArcadeValidation settings, Vec3 current, Vec3 impulse) {
        double change = impulse.subtract(current).length();
        if (change <= settings.freeImpulse()) {
            return true;
        }
        var validation = controller.validation();
        long age = player.level().getGameTime() - validation.lastImpulseTime;
        return age >= 0 && age <= settings.impulseWindowTicks() && impulse.length() <= validation.lastImpulseStrength + current.length() + settings.impulseMargin();
    }

    /**
     * What is wrong with the displacement the client reports, if anything. Collisions only ever take
     * away from a displacement, along each axis; stepping up a block is the one thing vanilla adds.
     */
    @Nullable
    static String implausibility(MoveContext ctx, MoveResult result, ArcadeTuning tuning, ArcadeValidation settings) {
        var planned = ctx.total();
        double tolerance = settings.displacementTolerance();
        if (!withinAxis(result.dx(), planned.x, tolerance)) {
            return "moved " + result.dx() + " along x where " + planned.x + " was planned";
        }
        if (!withinAxis(result.dz(), planned.z, tolerance)) {
            return "moved " + result.dz() + " along z where " + planned.z + " was planned";
        }
        if (result.dy() < planned.y - tolerance) {
            return "fell " + result.dy() + " where " + planned.y + " was planned";
        }
        double maxUp = Math.max(planned.y, 0.0D) + tuning.stepHeight() + tolerance;
        if (result.dy() > maxUp) {
            return "rose " + result.dy() + " where at most " + maxUp + " was possible";
        }
        double ceiling = ctx.physics().safetyCeiling() + tolerance;
        if (result.horizontalDistance() > ceiling) {
            return "moved " + result.horizontalDistance() + " in a tick, past the safety ceiling";
        }
        if (result.onGround()) {
            var end = ctx.start().add(result.dx(), result.dy(), result.dz());
            var box = ctx.world().box(end, ctx.pose());
            if (!ctx.world().collides(box.move(0.0D, -0.05D, 0.0D))) {
                return "claims to stand on nothing at " + end;
            }
        }
        return null;
    }

    private static boolean withinAxis(double actual, double planned, double tolerance) {
        return planned >= 0.0D
                ? actual >= -tolerance && actual <= planned + tolerance
                : actual <= tolerance && actual >= planned - tolerance;
    }

    /** Sends the player back where the server last saw them, and tells them how the server sees the movement. */
    private static void reject(ServerPlayer player, ArcadeController controller, int tick, String reason) {
        var validation = controller.validation();
        validation.rejected++;
        validation.expectedPosition = null;
        LOGGER.debug("Rejected the arcade movement of {}: {}", player.getPlainTextName(), reason);
        validation.ownTeleport = true;
        player.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        validation.ownTeleport = false;
        var state = controller.state();
        state.vx = 0.0D;
        state.vy = 0.0D;
        state.vz = 0.0D;
        ServerPlayNetworking.send(player, new ArcadeCorrectionPayload(tick, state.copy(), true));
    }

    /**
     * Called for every vanilla move packet of a player: the position it carries has to be where the
     * last validated step ended. Packets no step announced, such as those of a client whose layer is
     * suspended, are left to vanilla entirely.
     *
     * @return whether the packet is vouched for, which turns the speed check and the floating kick off for it
     */
    public static boolean checkMovePacket(ServerPlayer player, double x, double y, double z) {
        var controller = ArcadeController.of(player);
        var validation = controller.validation();
        var expected = validation.expectedPosition;
        if (!controller.isDriving() || expected == null || controller.profile() == null) {
            return false;
        }
        validation.expectedPosition = null;
        double tolerance = controller.profile().validation().positionTolerance();
        if (expected.distanceToSqr(x, y, z) > tolerance * tolerance) {
            reject(player, controller, validation.lastTick, "the move packet went to " + new Vec3(x, y, z) + " instead of " + expected);
            return false;
        }
        validation.vouched = true;
        return true;
    }
}
