package fr.hugman.mubble.super_mario.client.renderer.entity;

import fr.hugman.mubble.client.renderer.entity.CartoonPoseCallback;
import fr.hugman.mubble.client.renderer.entity.state.CartoonPose;
import fr.hugman.mubble.util.SquashStretch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * The body of a player on a launch, its slow fall or a drill: squashed, stretched and spun around, aim and
 * camera left alone. Worked out on every client from the synced flags, timed from when each one turned on.
 */
@Environment(EnvType.CLIENT)
public final class PropellerPose {
    /** Degrees per tick. */
    private static final float LAUNCH_SPIN = 70.0F;
    private static final float SLOW_FALL_SPIN = 14.0F;
    private static final float DRILL_SPIN = 95.0F;
    /** The slowest an unwinding spin turns, so that it always gets back to facing forwards. */
    private static final float UNWIND_SPIN = 10.0F;
    private static final float SPIN_DECAY = 0.85F;

    private static final float LAUNCH_SQUASH = 0.35F;
    private static final float LAUNCH_SQUASH_TICKS = 2.0F;
    private static final float LAUNCH_STRETCH = 0.45F;
    /** The vertical speed, in blocks per tick, the launch stretch is at its fullest at. */
    private static final double LAUNCH_FULL_SPEED = 1.0D;
    private static final float APEX_WOBBLE = 0.25F;
    private static final float APEX_TICKS = 12.0F;
    private static final float APEX_SWINGS = 2.0F;

    private static final float SLOW_FALL_STRETCH = 0.08F;
    private static final float SWAY_ROLL = 7.0F;
    private static final float SWAY_LEAN = 4.0F;
    private static final float SWAY_SPEED = 0.16F;

    private static final float DRILL_STRETCH = 0.4F;
    private static final float DRILL_STRETCH_TICKS = 3.0F;
    private static final float LAND_SQUASH = 0.45F;
    private static final float LAND_TICKS = 12.0F;
    private static final float LAND_SWINGS = 2.5F;

    private static final Map<Player, Timeline> TIMELINES = new WeakHashMap<>();

    private PropellerPose() {
    }

    public static void register() {
        CartoonPoseCallback.EVENT.register(PropellerPose::pose);
    }

    private static void pose(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CartoonPose pose) {
        if (!(entity instanceof Player player)) {
            return;
        }
        boolean launching = player.isLaunching();
        boolean slowFalling = player.isSlowFalling();
        boolean drilling = player.isDrilling();
        Timeline timeline = TIMELINES.get(player);
        if (timeline == null) {
            if (!launching && !slowFalling && !drilling) {
                return;
            }
            timeline = new Timeline();
            TIMELINES.put(player, timeline);
        }
        float time = player.tickCount + partialTicks;
        timeline.update(time, launching, slowFalling, drilling);

        float stretch = 0.0F;
        if (launching) {
            float elapsed = time - timeline.launchSince;
            stretch = elapsed < LAUNCH_SQUASH_TICKS
                    ? -LAUNCH_SQUASH * (1.0F - elapsed / LAUNCH_SQUASH_TICKS)
                    : SquashStretch.fromSpeed(player.getY() - player.yo, LAUNCH_FULL_SPEED, LAUNCH_STRETCH);
        } else if (slowFalling) {
            float sinceApex = Math.min(1.0F, (time - timeline.slowFallSince) / APEX_TICKS);
            // Starts from no deformation at all, the launch stretch having died down by the apex.
            float overshoot = Mth.sin(sinceApex * APEX_SWINGS * Mth.TWO_PI) * (1.0F - sinceApex) * (1.0F - sinceApex);
            stretch = SLOW_FALL_STRETCH * sinceApex + APEX_WOBBLE * overshoot;
            float sway = time * SWAY_SPEED;
            pose.roll += Mth.sin(sway) * SWAY_ROLL * sinceApex;
            pose.lean += Mth.cos(sway * 0.7F) * SWAY_LEAN * sinceApex;
            pose.pivot = state.boundingBoxHeight / 2.0F;
        } else if (drilling) {
            stretch = DRILL_STRETCH * Math.min(1.0F, (time - timeline.drillSince) / DRILL_STRETCH_TICKS);
        } else if (timeline.landSince >= 0.0F) {
            float progress = (time - timeline.landSince) / LAND_TICKS;
            stretch = -LAND_SQUASH * SquashStretch.wobble(progress, LAND_SWINGS);
            if (progress >= 1.0F) {
                timeline.landSince = -1.0F;
            }
        }

        pose.spin += timeline.spin;
        pose.scale(SquashStretch.alongY(stretch));
        if (!launching && !slowFalling && !drilling && timeline.spin == 0.0F && timeline.landSince < 0.0F) {
            TIMELINES.remove(player);
        }
    }

    private static final class Timeline {
        private float lastTime = Float.NaN;
        private float launchSince;
        private float slowFallSince;
        private float drillSince;
        private float landSince = -1.0F;
        private boolean launching;
        private boolean slowFalling;
        private boolean drilling;
        private float spin;
        private float spinRate;

        private void update(float time, boolean launching, boolean slowFalling, boolean drilling) {
            if (launching && !this.launching) {
                this.launchSince = time;
            }
            if (slowFalling && !this.slowFalling) {
                this.slowFallSince = time;
            }
            if (drilling && !this.drilling) {
                this.drillSince = time;
            }
            if (!drilling && this.drilling) {
                this.landSince = time;
            }
            this.launching = launching;
            this.slowFalling = slowFalling;
            this.drilling = drilling;

            float delta = Float.isNaN(this.lastTime) ? 0.0F : Mth.clamp(time - this.lastTime, 0.0F, 1.0F);
            this.lastTime = time;
            if (drilling) {
                this.spinRate = DRILL_SPIN;
            } else if (launching) {
                this.spinRate = LAUNCH_SPIN;
            } else if (slowFalling) {
                this.spinRate = Mth.lerp(delta * 0.3F, this.spinRate, SLOW_FALL_SPIN);
            } else if (this.spin != 0.0F) {
                this.spinRate = Math.max(UNWIND_SPIN, this.spinRate * (float) Math.pow(SPIN_DECAY, delta));
            }
            this.spin += this.spinRate * delta;
            if (this.spin >= 360.0F) {
                if (!launching && !slowFalling && !drilling) {
                    this.spin = 0.0F;
                    this.spinRate = 0.0F;
                } else {
                    this.spin -= 360.0F;
                }
            }
        }
    }
}
