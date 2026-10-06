package fr.hugman.mubble.arcade.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.arcade.client.cue.ArcadeCuePlayer;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadePlayer;
import fr.hugman.mubble.arcade.cue.CueEvent;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.sim.ArcadeState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

/**
 * Plays the arcade animations on players.
 * <p>
 * Every player gets a track following its move: its own client reads the predicted state, everyone
 * else reads the move synced with the entity. The limbs are animated by the vanilla keyframe API in
 * {@code setupAnim}, the whole body by a rotation of the pose stack in the renderer, the way vanilla
 * turns a swimming or gliding player. Outside an arcade move the render state carries
 * {@link ArcadeRenderData#NONE} and nothing here touches the model.
 */
@Environment(EnvType.CLIENT)
public final class ArcadePlayerAnimator {
    public static final RenderStateDataKey<ArcadeRenderData> DATA = RenderStateDataKey.create(() -> "Mubble arcade animation");

    private static final Map<Player, Track> TRACKS = new WeakHashMap<>();
    private static final Map<ModelPart, Map<Identifier, Baked>> BAKED = new WeakHashMap<>();
    private static final Map<Identifier, KeyframeAnimation> BAKED_BODIES = new HashMap<>();
    /** How long the model takes to ease back into the vanilla pose when the arcade animations stop, in seconds. */
    private static final float BLEND_OUT_SECONDS = 0.15F;

    private ArcadePlayerAnimator() {
    }

    private static final class Track {
        @Nullable
        ArcadeMove move;
        int seq = Integer.MIN_VALUE;
        int ticksInMove;
        final AnimationState state = new AnimationState();
        @Nullable
        ArcadeAnimation animation;
        /** Whether the animation plays on after the end of its move, until its own end. */
        boolean lingering;
        /** The animation the model eases out of, {@code null} for the vanilla pose, and when it started. */
        @Nullable
        ArcadeAnimation previous;
        final AnimationState previousState = new AnimationState();
        long blendStart;
        float blendSeconds;

        /** Whether a lingering animation reached its end, past which it would hold its last pose for good. */
        boolean lingeredOut(Player player) {
            return this.lingering && this.animation != null && this.state.getTimeInMillis(player.tickCount) >= this.animation.lengthInSeconds() * 1000.0F;
        }

        void clear() {
            this.play(null, 0);
        }

        /** Plays {@code next} from now on, easing into it from the pose the model has. */
        void play(@Nullable ArcadeAnimation next, int tickCount) {
            if (this.animation != null || next != null) {
                this.previous = this.animation;
                this.previousState.copyFrom(this.state);
                this.blendStart = Util.getMillis();
                this.blendSeconds = next != null ? next.blendIn() : BLEND_OUT_SECONDS;
            }
            this.animation = next;
            this.lingering = false;
            if (next != null) {
                this.state.start(tickCount);
            } else {
                this.state.stop();
            }
        }

        /** How far the model is into the animation it plays, eased in and out, from 0 to 1. */
        float weight() {
            if (this.blendSeconds <= 0.0F) {
                return 1.0F;
            }
            float t = (Util.getMillis() - this.blendStart) / (this.blendSeconds * 1000.0F);
            return t >= 1.0F ? 1.0F : Ease.inOutSine(Math.max(0.0F, t));
        }
    }

    /** A limb animation baked against one model, with the parts it moves. */
    private record Baked(KeyframeAnimation animation, List<ModelPart> parts) {
        void play(AnimationState state, float ageInTicks) {
            for (var part : this.parts) {
                part.resetPose();
            }
            this.animation.apply(state, ageInTicks);
        }
    }

    static void clearBakedCache() {
        BAKED.clear();
        BAKED_BODIES.clear();
    }

    /** Called after every predicted step of the player of this client. */
    public static void onLocalStep(Player player, ArcadeState state) {
        update(player, state.move, state.moveSeq);
    }

    /**
     * Called every client tick: follows the synced move of every other player, and plays its cues,
     * since only its own client simulated it.
     */
    public static void tickRemotePlayers(Minecraft minecraft) {
        var level = minecraft.level;
        if (level == null) {
            return;
        }
        for (Player player : level.players()) {
            if (player == minecraft.player) {
                continue;
            }
            var visual = ((ArcadePlayer) player).mubble$arcadeVisual();
            var track = TRACKS.get(player);
            var previous = track == null ? null : track.move;
            if (update(player, visual.move(), visual.seq()) && visual.move() != null) {
                ArcadeCuePlayer.playOne(player, null, visual.move(), CueEvent.START, visual.intensity());
                if (previous != null && previous.isAirborne() && !visual.move().isAirborne()) {
                    ArcadeCuePlayer.playOne(player, null, visual.move(), CueEvent.LAND, visual.intensity());
                }
            } else if (track != null && track.move != null) {
                track.ticksInMove++;
                ArcadeCuePlayer.playTick(player, null, track.move, track.ticksInMove);
            }
        }
    }

    /** @return whether the move changed */
    private static boolean update(Player player, @Nullable ArcadeMove move, int seq) {
        var track = TRACKS.computeIfAbsent(player, p -> new Track());
        if (move == track.move && seq == track.seq) {
            return false;
        }
        track.move = move;
        track.seq = seq;
        track.ticksInMove = 0;
        var next = move == null ? null : animationFor(player, move);
        var current = track.animation;
        boolean lingering = next == null && move != null && current != null && current.linger()
                && track.state.getTimeInMillis(player.tickCount) < current.lengthInSeconds() * 1000.0F;
        if (lingering) {
            track.lingering = true;
        } else {
            track.play(next, player.tickCount);
        }
        return true;
    }

    @Nullable
    private static ArcadeAnimation animationFor(Player player, ArcadeMove move) {
        var profile = ArcadeController.of(player).profile();
        var id = profile == null ? move.id() : profile.settingsOrDefault(move).animation().orElse(move.id());
        return ArcadeAnimations.get(id).orElse(null);
    }

    /** The animation {@code player} plays, for the debug HUD. */
    public static String describe(Player player) {
        var track = TRACKS.get(player);
        if (track == null) {
            return "vanilla";
        }
        float weight = track.weight();
        var blend = weight < 1.0F ? String.format(java.util.Locale.ROOT, ", easing out of %s (%d%%)", track.previous == null ? "vanilla" : track.previous.id(), Math.round(weight * 100.0F)) : "";
        if (track.animation == null || !track.state.isStarted()) {
            return "vanilla" + blend;
        }
        return track.animation.id() + (track.lingering ? " (lingering)" : "") + String.format(java.util.Locale.ROOT, " %.2fs", track.state.getTimeInMillis(player.tickCount) / 1000.0F) + blend;
    }

    /** What the render state of {@code player} carries this frame. */
    public static ArcadeRenderData renderData(Player player) {
        boolean local = player == Minecraft.getInstance().player;
        boolean active = local ? ArcadeController.of(player).isDriving() : ((ArcadePlayer) player).mubble$arcadeVisual().isActive();
        var track = TRACKS.get(player);
        if (!active && track != null && track.move != null) {
            update(player, null, Integer.MIN_VALUE);
        }
        if (track != null && track.lingeredOut(player)) {
            // the landing played out over the first steps of a run: vanilla has the legs again
            track.clear();
        }
        if (track == null) {
            return ArcadeRenderData.NONE;
        }
        float weight = track.weight();
        var animation = track.animation != null && track.state.isStarted() ? track.animation : null;
        var previous = weight < 1.0F ? track.previous : null;
        if (animation == null && previous == null) {
            return ArcadeRenderData.NONE;
        }
        var state = new AnimationState();
        state.copyFrom(track.state);
        var previousState = new AnimationState();
        previousState.copyFrom(track.previousState);
        return new ArcadeRenderData(animation, state, previous, previousState, weight);
    }

    /**
     * Animates the limbs of a humanoid model, once vanilla posed it: the bones the animation drives are
     * reset to their rest pose, then played. Between two animations, or between an animation and the
     * vanilla pose, the model is posed both ways and eased from one into the other.
     */
    public static void applyLimbs(ModelPart root, ArcadeRenderData data, float ageInTicks) {
        var current = data.animation() == null ? null : baked(root, data.animation());
        if (!data.blending()) {
            if (current != null) {
                current.play(data.state(), ageInTicks);
            }
            return;
        }
        var previous = data.previous() == null ? null : baked(root, data.previous());
        var parts = new LinkedHashSet<ModelPart>();
        if (current != null) {
            parts.addAll(current.parts);
        }
        if (previous != null) {
            parts.addAll(previous.parts);
        }
        var vanilla = PartTransform.of(parts);
        if (previous != null) {
            previous.play(data.previousState(), ageInTicks);
        }
        var from = PartTransform.of(parts);
        PartTransform.load(parts, vanilla);
        if (current != null) {
            current.play(data.state(), ageInTicks);
        }
        var to = PartTransform.of(parts);
        PartTransform.load(parts, PartTransform.lerp(from, to, data.weight()));
    }

    private static Baked baked(ModelPart root, ArcadeAnimation animation) {
        return BAKED.computeIfAbsent(root, r -> new HashMap<>()).computeIfAbsent(animation.id(), id -> bake(root, animation.limbs()));
    }

    /** Bakes {@code definition} against {@code root}, leaving out the bones the model does not have: armor models have fewer than the player. */
    private static Baked bake(ModelPart root, AnimationDefinition definition) {
        var lookup = root.createPartLookup();
        var bones = new HashMap<String, List<net.minecraft.client.animation.AnimationChannel>>();
        var parts = new ArrayList<ModelPart>();
        definition.boneAnimations().forEach((bone, channels) -> {
            var part = lookup.apply(bone);
            if (part != null) {
                bones.put(bone, channels);
                parts.add(part);
            }
        });
        var filtered = new AnimationDefinition(definition.lengthInSeconds(), definition.looping(), bones);
        return new Baked(filtered.bake(root), List.copyOf(parts));
    }

    /**
     * Moves the whole body for the frame: squashed or stretched from the feet, turned around the pivot
     * of the animation. Between two animations, the body eases from the one into the other, a body the
     * animation leaves alone standing as vanilla has it.
     *
     * @return whether the animation drives the whole body, in which case vanilla's own body rotations stay out of it
     */
    public static boolean applyBody(PoseStack poseStack, ArcadeRenderData data, float ageInTicks) {
        if (!data.drivesBody()) {
            return false;
        }
        var to = bodyTransform(data.animation(), data.state(), ageInTicks);
        var transform = data.blending()
                ? BodyTransform.lerp(bodyTransform(data.previous(), data.previousState(), ageInTicks), to, data.weight())
                : to;
        transform.apply(poseStack);
        return true;
    }

    /** {@code angle} brought within half a turn either way, so that easing between two angles goes the short way. */
    private static float wrapRadians(float angle) {
        return Mth.wrapDegrees(angle * Mth.RAD_TO_DEG) * Mth.DEG_TO_RAD;
    }

    private static BodyTransform bodyTransform(@Nullable ArcadeAnimation animation, AnimationState state, float ageInTicks) {
        if (animation == null || animation.body().isEmpty()) {
            return BodyTransform.IDENTITY;
        }
        var baked = BAKED_BODIES.computeIfAbsent(animation.id(), id -> animation.bakedBody());
        var part = ArcadeAnimation.BODY_PART;
        part.resetPose();
        baked.apply(state, ageInTicks);
        return BodyTransform.of(part, animation.pivot());
    }

    /** The pose of model parts, to ease from one pose into another. */
    private record PartTransform(float[] values) {
        private static final int SIZE = 9;

        static PartTransform of(Collection<ModelPart> parts) {
            var values = new float[parts.size() * SIZE];
            int i = 0;
            for (var part : parts) {
                values[i++] = part.x;
                values[i++] = part.y;
                values[i++] = part.z;
                values[i++] = part.xRot;
                values[i++] = part.yRot;
                values[i++] = part.zRot;
                values[i++] = part.xScale;
                values[i++] = part.yScale;
                values[i++] = part.zScale;
            }
            return new PartTransform(values);
        }

        static void load(Collection<ModelPart> parts, PartTransform transform) {
            var values = transform.values;
            int i = 0;
            for (var part : parts) {
                part.x = values[i++];
                part.y = values[i++];
                part.z = values[i++];
                part.xRot = values[i++];
                part.yRot = values[i++];
                part.zRot = values[i++];
                part.xScale = values[i++];
                part.yScale = values[i++];
                part.zScale = values[i++];
            }
        }

        static PartTransform lerp(PartTransform from, PartTransform to, float t) {
            var values = new float[from.values.length];
            for (int i = 0; i < values.length; i++) {
                int field = i % SIZE;
                values[i] = field >= 3 && field < 6
                        ? from.values[i] + wrapRadians(to.values[i] - from.values[i]) * t
                        : Mth.lerp(t, from.values[i], to.values[i]);
            }
            return new PartTransform(values);
        }
    }

    /**
     * Where the whole body goes for a frame: an offset in model pixels, a squash and stretch from the
     * feet, and a turn around a pivot in blocks above the feet.
     */
    private record BodyTransform(Vector3f offset, Vector3f scale, Vector3f rotation, Vector3f pivot) {
        static final BodyTransform IDENTITY = new BodyTransform(new Vector3f(), new Vector3f(1.0F), new Vector3f(), new Vector3f(0.0F, 0.9F, 0.0F));

        static BodyTransform of(ModelPart part, Vector3f pivot) {
            return new BodyTransform(new Vector3f(part.x, part.y, part.z), new Vector3f(part.xScale, part.yScale, part.zScale),
                    new Vector3f(part.xRot, part.yRot, part.zRot), new Vector3f(pivot));
        }

        static BodyTransform lerp(BodyTransform from, BodyTransform to, float t) {
            // turns go the short way: a flip ends where it started, 360 degrees around
            var rotation = new Vector3f(
                    from.rotation.x + wrapRadians(to.rotation.x - from.rotation.x) * t,
                    from.rotation.y + wrapRadians(to.rotation.y - from.rotation.y) * t,
                    from.rotation.z + wrapRadians(to.rotation.z - from.rotation.z) * t);
            var pivot = from == IDENTITY ? to.pivot : to == IDENTITY ? from.pivot : new Vector3f(from.pivot).lerp(to.pivot, t);
            return new BodyTransform(new Vector3f(from.offset).lerp(to.offset, t), new Vector3f(from.scale).lerp(to.scale, t), rotation, pivot);
        }

        void apply(PoseStack poseStack) {
            // positions are model pixels, y pointing down like every model part
            poseStack.translate(this.offset.x / 16.0F, -this.offset.y / 16.0F, this.offset.z / 16.0F);
            poseStack.scale(this.scale.x, this.scale.y, this.scale.z);
            poseStack.translate(this.pivot.x, this.pivot.y, this.pivot.z);
            poseStack.mulPose(new Quaternionf().rotationZYX(this.rotation.z, this.rotation.y, this.rotation.x));
            poseStack.translate(-this.pivot.x, -this.pivot.y, -this.pivot.z);
        }
    }
}
