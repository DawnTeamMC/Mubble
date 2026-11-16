package fr.hugman.mubble.super_mario.client.renderer.entity;

import fr.hugman.mubble.client.renderer.entity.state.CartoonPose;
import fr.hugman.mubble.util.SquashStretch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * The squash and stretch of a player flying and clinging: a stretch at take-off, a hovering bob leaning into
 * the way they go, a stutter while the gauge sputters, a squash at the landing and one against the wall.
 */
@Environment(EnvType.CLIENT)
public final class FlightPoses {
    private static final float TAKE_OFF_TICKS = 10.0F;
    private static final float TAKE_OFF_STRETCH = 0.35F;
    private static final float TAKE_OFF_SWINGS = 1.5F;
    private static final float LANDING_TICKS = 8.0F;
    private static final float LANDING_SQUASH = 0.3F;
    private static final float LANDING_SWINGS = 1.0F;
    private static final float CLING_TICKS = 8.0F;
    private static final float CLING_SQUASH = 0.25F;
    private static final float CLING_SWINGS = 1.0F;
    private static final float BOB_SPEED = 0.6F;
    private static final float BOB_AMOUNT = 0.05F;
    private static final double FULL_LEAN_SPEED = 0.3D;
    private static final float MAX_LEAN = 25.0F;
    private static final float MAX_ROLL = 15.0F;
    private static final float SPUTTER_ROLL = 5.0F;

    private static final Map<Player, State> STATES = new WeakHashMap<>();

    private FlightPoses() {
    }

    public static void tick(Minecraft client) {
        if (client.level == null) {
            STATES.clear();
            return;
        }
        for (Player player : client.level.players()) {
            STATES.computeIfAbsent(player, _ -> new State()).tick(player);
        }
    }

    public static void pose(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CartoonPose pose) {
        if (!(entity instanceof Player player)) {
            return;
        }
        State flight = STATES.get(player);
        if (flight == null) {
            return;
        }
        float now = player.tickCount + partialTicks;
        pose.pivot = player.getBbHeight() / 2.0F;

        float stretch = TAKE_OFF_STRETCH * wobbleSince(flight.takeOffTick, now, TAKE_OFF_TICKS, TAKE_OFF_SWINGS)
                - LANDING_SQUASH * wobbleSince(flight.landingTick, now, LANDING_TICKS, LANDING_SWINGS);
        if (player.isFlying()) {
            stretch += BOB_AMOUNT * Mth.sin(now * BOB_SPEED);
            double dx = player.getX() - player.xo;
            double dz = player.getZ() - player.zo;
            float yaw = state.bodyRot * Mth.DEG_TO_RAD;
            double forward = -dx * Mth.sin(yaw) + dz * Mth.cos(yaw);
            double side = dx * Mth.cos(yaw) + dz * Mth.sin(yaw);
            pose.lean += Math.signum(forward) * SquashStretch.fromSpeed(Math.abs(forward), FULL_LEAN_SPEED, MAX_LEAN);
            pose.roll += Math.signum(side) * SquashStretch.fromSpeed(Math.abs(side), FULL_LEAN_SPEED, MAX_ROLL);
            if (player.isFlightSputtering()) {
                pose.roll += (player.tickCount % 2 == 0 ? 1.0F : -1.0F) * SPUTTER_ROLL;
            }
        }
        if (stretch != 0.0F) {
            pose.scale(SquashStretch.alongY(stretch));
        }
        float cling = wobbleSince(flight.clingTick, now, CLING_TICKS, CLING_SWINGS);
        if (cling != 0.0F) {
            pose.scale(SquashStretch.alongZ(-CLING_SQUASH * cling));
        }
    }

    private static float wobbleSince(int tick, float now, float duration, float swings) {
        float elapsed = now - tick;
        if (tick == Integer.MIN_VALUE || elapsed < 0.0F || elapsed >= duration) {
            return 0.0F;
        }
        return SquashStretch.wobble(elapsed / duration, swings);
    }

    private static final class State {
        private boolean flying;
        private boolean clinging;
        private boolean flewThisJump;
        private int takeOffTick = Integer.MIN_VALUE;
        private int landingTick = Integer.MIN_VALUE;
        private int clingTick = Integer.MIN_VALUE;

        private void tick(Player player) {
            boolean flying = player.isFlying();
            if (flying && !this.flying) {
                this.takeOffTick = player.tickCount;
                this.flewThisJump = true;
            }
            this.flying = flying;
            if (this.flewThisJump && player.onGround()) {
                this.flewThisJump = false;
                this.landingTick = player.tickCount;
            }
            boolean clinging = player.isClinging();
            if (clinging && !this.clinging) {
                this.clingTick = player.tickCount;
                this.flewThisJump = false;
            }
            this.clinging = clinging;
        }
    }
}
