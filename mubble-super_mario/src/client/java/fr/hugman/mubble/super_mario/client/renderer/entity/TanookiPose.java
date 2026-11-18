package fr.hugman.mubble.super_mario.client.renderer.entity;

import fr.hugman.mubble.client.keybind.PowerUpKeybindsHandler;
import fr.hugman.mubble.client.renderer.entity.CartoonPoseCallback;
import fr.hugman.mubble.client.renderer.entity.state.CartoonPose;
import fr.hugman.mubble.super_mario.core.attachment.SuperMarioAttachmentTypes;
import fr.hugman.mubble.super_mario.tags.SuperMarioPowerUpTags;
import fr.hugman.mubble.super_mario.world.power_up.action.TailWhipPowerUpAction;
import fr.hugman.mubble.util.SquashStretch;
import fr.hugman.mubble.world.power_up.ability.GroundPoundAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * The body of a player in a Tanooki form: spun round by the tail whip, leaning into a glide, squashed and
 * stretched by a pound, and popped in and out of its statue. Worked out on every client from the synced state,
 * timed from when each beat started.
 */
@Environment(EnvType.CLIENT)
public final class TanookiPose {
    private static final float WHIP_TICKS = 7.0F;
    private static final float WHIP_SQUASH = 0.3F;
    private static final float WHIP_SQUASH_TICKS = 2.0F;
    private static final float WHIP_STRETCH = 0.12F;
    /** How long a spin the local client played ahead of the server may be taken for the one the server announces. */
    private static final float PREDICTION_TICKS = 10.0F;

    private static final float GLIDE_RAMP_TICKS = 4.0F;
    private static final float GLIDE_LEAN = 28.0F;
    private static final float GLIDE_SWAY_ROLL = 6.0F;
    private static final float GLIDE_SWAY_SPEED = 0.18F;
    private static final float GLIDE_STRETCH = 0.06F;

    private static final float POUND_SQUASH = 0.3F;
    private static final float POUND_STRETCH = 0.4F;
    private static final float POUND_STRETCH_TICKS = 2.0F;
    private static final float LAND_SQUASH = 0.55F;
    private static final float LAND_TICKS = 14.0F;
    private static final float LAND_SWINGS = 2.5F;

    private static final float POP_TICKS = 6.0F;
    private static final float POP_SCALE = 0.25F;

    private static final Map<Player, Timeline> TIMELINES = new WeakHashMap<>();

    private TanookiPose() {
    }

    public static void register() {
        CartoonPoseCallback.EVENT.register(TanookiPose::pose);
        PowerUpKeybindsHandler.BEFORE_TRIGGER.register((player, powerUp) -> {
            if (powerUp.action().isPresent() && powerUp.action().get().value() instanceof TailWhipPowerUpAction) {
                var timeline = timelineOf(player);
                timeline.whipSince = player.tickCount;
                timeline.predictedAt = player.tickCount;
            }
        });
    }

    private static Timeline timelineOf(Player player) {
        return TIMELINES.computeIfAbsent(player, key -> new Timeline(key.getAttachedOrElse(SuperMarioAttachmentTypes.TAIL_WHIP, 0)));
    }

    private static void pose(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CartoonPose pose) {
        if (!(entity instanceof Player player)) {
            return;
        }
        boolean tanooki = player.getPowerUp().map(powerUp -> powerUp.is(SuperMarioPowerUpTags.TANOOKI_FORMS)).orElse(false);
        var timeline = TIMELINES.get(player);
        if (timeline == null) {
            if (!tanooki) {
                return;
            }
            timeline = timelineOf(player);
        }
        float time = player.tickCount + partialTicks;
        boolean gliding = tanooki && player.isFloating();
        boolean pounding = player.isGroundPounding();
        boolean statue = player.isStatue();
        timeline.update(player, time, gliding, pounding, statue);

        float stretch = 0.0F;
        float whip = (time - timeline.whipSince) / WHIP_TICKS;
        if (timeline.whipSince >= 0.0F && whip < 1.0F) {
            float elapsed = time - timeline.whipSince;
            stretch = elapsed < WHIP_SQUASH_TICKS
                    ? -WHIP_SQUASH * (1.0F - elapsed / WHIP_SQUASH_TICKS)
                    : WHIP_STRETCH * SquashStretch.pulse((elapsed - WHIP_SQUASH_TICKS) / (WHIP_TICKS - WHIP_SQUASH_TICKS));
            pose.spin += 360.0F * Ease.outBack(Mth.clamp(whip, 0.0F, 1.0F));
        } else if (pounding) {
            int hang = player.getGroundPoundAbility().map(GroundPoundAbility::hangTicks).orElse(0);
            float elapsed = time - timeline.poundSince;
            stretch = elapsed < hang
                    ? -POUND_SQUASH * Ease.outQuad(elapsed / hang)
                    : POUND_STRETCH * Math.min(1.0F, (elapsed - hang) / POUND_STRETCH_TICKS);
        } else if (timeline.landSince >= 0.0F) {
            float progress = (time - timeline.landSince) / LAND_TICKS;
            stretch = -LAND_SQUASH * SquashStretch.wobble(progress, LAND_SWINGS);
        } else if (gliding) {
            float ramp = Math.min(1.0F, (time - timeline.glideSince) / GLIDE_RAMP_TICKS);
            pose.lean += GLIDE_LEAN * Ease.outQuad(ramp);
            pose.roll += Mth.sin(time * GLIDE_SWAY_SPEED) * GLIDE_SWAY_ROLL * ramp;
            pose.pivot = state.boundingBoxHeight / 2.0F;
            stretch = GLIDE_STRETCH * ramp;
        }
        pose.scale(SquashStretch.alongY(stretch));

        float pop = (time - timeline.popSince) / POP_TICKS;
        if (timeline.popSince >= 0.0F && pop < 1.0F) {
            float size = 1.0F + POP_SCALE * SquashStretch.pulse(pop) * (timeline.popping ? 1.0F : -0.6F);
            pose.scale(new Vector3f(size, size, size));
        }
        if (!tanooki && !gliding && !pounding && !statue && timeline.idle(time)) {
            TIMELINES.remove(player);
        }
    }

    private static final class Timeline {
        private float whipSince = -1.0F;
        private float predictedAt = Float.NEGATIVE_INFINITY;
        private int seenWhips;
        private float glideSince;
        private float poundSince;
        private float landSince = -1.0F;
        private float popSince = -1.0F;
        private boolean popping;
        private boolean gliding;
        private boolean pounding;
        private boolean statue;

        private Timeline(int seenWhips) {
            this.seenWhips = seenWhips;
        }

        private void update(Player player, float time, boolean gliding, boolean pounding, boolean statue) {
            int whips = player.getAttachedOrElse(SuperMarioAttachmentTypes.TAIL_WHIP, 0);
            if (whips != this.seenWhips) {
                this.seenWhips = whips;
                if (time - this.predictedAt > PREDICTION_TICKS) {
                    this.whipSince = time;
                }
            }
            if (gliding && !this.gliding) {
                this.glideSince = time;
            }
            if (pounding && !this.pounding) {
                this.poundSince = time;
                this.landSince = -1.0F;
            }
            if (!pounding && this.pounding && player.onGround()) {
                this.landSince = time;
            }
            if (this.landSince >= 0.0F && time - this.landSince >= LAND_TICKS) {
                this.landSince = -1.0F;
            }
            if (statue != this.statue) {
                this.popSince = time;
                this.popping = statue;
            }
            this.gliding = gliding;
            this.pounding = pounding;
            this.statue = statue;
        }

        private boolean idle(float time) {
            return time - this.whipSince >= WHIP_TICKS && this.landSince < 0.0F && time - this.popSince >= POP_TICKS;
        }
    }
}
