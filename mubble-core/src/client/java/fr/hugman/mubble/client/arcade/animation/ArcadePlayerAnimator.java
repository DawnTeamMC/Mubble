package fr.hugman.mubble.client.arcade.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.client.arcade.cue.ArcadeCuePlayer;
import fr.hugman.mubble.world.arcade.ArcadeController;
import fr.hugman.mubble.world.arcade.ArcadePlayer;
import fr.hugman.mubble.world.arcade.cue.CueEvent;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import fr.hugman.mubble.world.arcade.sim.ArcadeState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
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
    }

    /** A limb animation baked against one model, with the parts it moves. */
    private record Baked(KeyframeAnimation animation, List<ModelPart> parts) {
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
        if (!lingering) {
            track.animation = next;
            if (next != null) {
                track.state.start(player.tickCount);
            } else {
                track.state.stop();
            }
        }
        return true;
    }

    @Nullable
    private static ArcadeAnimation animationFor(Player player, ArcadeMove move) {
        var profile = ArcadeController.of(player).profile();
        var id = profile == null ? move.id() : profile.settingsOrDefault(move).animation().orElse(move.id());
        return ArcadeAnimations.get(id).orElse(null);
    }

    /** What the render state of {@code player} carries this frame. */
    public static ArcadeRenderData renderData(Player player) {
        boolean local = player == Minecraft.getInstance().player;
        boolean active = local ? ArcadeController.of(player).isDriving() : ((ArcadePlayer) player).mubble$arcadeVisual().isActive();
        if (!active) {
            var track = TRACKS.get(player);
            if (track != null && track.move != null) {
                update(player, null, Integer.MIN_VALUE);
            }
            return ArcadeRenderData.NONE;
        }
        var track = TRACKS.get(player);
        if (track == null || track.animation == null || !track.state.isStarted()) {
            return ArcadeRenderData.NONE;
        }
        var copy = new AnimationState();
        copy.copyFrom(track.state);
        return new ArcadeRenderData(track.animation, copy);
    }

    /** Animates the limbs of a humanoid model: the bones the animation drives are reset to their rest pose, then played. */
    public static void applyLimbs(ModelPart root, ArcadeRenderData data, float ageInTicks) {
        var animation = data.animation();
        if (animation == null) {
            return;
        }
        var baked = BAKED.computeIfAbsent(root, r -> new HashMap<>()).computeIfAbsent(animation.id(), id -> bake(root, animation.limbs()));
        for (var part : baked.parts) {
            part.resetPose();
        }
        baked.animation.apply(data.state(), ageInTicks);
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
     * Turns the whole body for the frame, around the pivot of the animation.
     *
     * @return whether the animation drives the whole body, in which case vanilla's own body rotations stay out of it
     */
    public static boolean applyBody(PoseStack poseStack, ArcadeRenderData data, float ageInTicks) {
        var animation = data.animation();
        if (animation == null || animation.body().isEmpty()) {
            return false;
        }
        var baked = BAKED_BODIES.computeIfAbsent(animation.id(), id -> animation.bakedBody());
        var part = ArcadeAnimation.BODY_PART;
        part.resetPose();
        baked.apply(data.state(), ageInTicks);
        var pivot = animation.pivot();
        // positions are model pixels, y pointing down like every model part
        poseStack.translate(part.x / 16.0F, -part.y / 16.0F, part.z / 16.0F);
        poseStack.translate(pivot.x, pivot.y, pivot.z);
        poseStack.mulPose(new Quaternionf().rotationZYX(part.zRot, part.yRot, part.xRot));
        poseStack.translate(-pivot.x, -pivot.y, -pivot.z);
        return true;
    }
}
