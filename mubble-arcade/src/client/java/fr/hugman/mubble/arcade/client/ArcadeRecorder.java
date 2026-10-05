package fr.hugman.mubble.arcade.client;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadePrediction;
import fr.hugman.mubble.arcade.replay.ArcadeRecording;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Records the steps of the player of this client, and plays recorded inputs back through the very
 * same driver, to replay a bug or check that a change of the simulation still lands where it used to.
 * <p>
 * Recordings go to {@code <game directory>/mubble/arcade_recordings/<name>.json}. A replay feeds the
 * recorded inputs in place of the live ones, from wherever the player stands, then tells in chat at
 * which step the trajectory first left the recorded one, if it did.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeRecorder {
    private static final String ROOT = "mubble_client";

    @Nullable
    private static Capture capture;
    @Nullable
    private static Playback playback;

    private ArcadeRecorder() {
    }

    private record Capture(Vec3 start, List<ArcadeRecording.Step> steps) {
    }

    private static final class Playback {
        final String name;
        final ArcadeRecording recording;
        final Vec3 start;
        int index;
        int divergence = -1;
        @Nullable
        ArcadeInputFrame current;

        Playback(String name, ArcadeRecording recording, Vec3 start) {
            this.name = name;
            this.recording = recording;
            this.start = start;
        }
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommands.literal(ROOT).then(ClientCommands.literal("arcade")
                .then(ClientCommands.literal("record").executes(ArcadeRecorder::record))
                .then(ClientCommands.literal("stop")
                        .then(ClientCommands.argument("name", StringArgumentType.word()).executes(ArcadeRecorder::stop)))
                .then(ClientCommands.literal("replay")
                        .then(ClientCommands.argument("name", StringArgumentType.word()).executes(ArcadeRecorder::replay)))
                .then(ClientCommands.literal("cancel").executes(ArcadeRecorder::cancel))));
    }

    private static Path file(String name) {
        return FabricLoader.getInstance().getGameDir().resolve("mubble").resolve("arcade_recordings").resolve(name + ".json");
    }

    private static int record(CommandContext<FabricClientCommandSource> context) {
        var player = context.getSource().getPlayer();
        if (!ArcadeController.of(player).isDriving()) {
            context.getSource().sendError(Component.translatable("commands.mubble_client.arcade.not_driving"));
            return 0;
        }
        playback = null;
        capture = new Capture(player.position(), new ArrayList<>());
        context.getSource().sendFeedback(Component.translatable("commands.mubble_client.arcade.record.started"));
        return 1;
    }

    private static int stop(CommandContext<FabricClientCommandSource> context) {
        var name = StringArgumentType.getString(context, "name");
        var captured = capture;
        if (captured == null) {
            context.getSource().sendError(Component.translatable("commands.mubble_client.arcade.record.none"));
            return 0;
        }
        capture = null;
        var recording = new ArcadeRecording(captured.start(), List.copyOf(captured.steps()));
        try {
            var json = ArcadeRecording.CODEC.encodeStart(JsonOps.INSTANCE, recording).getOrThrow();
            var path = file(name);
            Files.createDirectories(path.getParent());
            Files.writeString(path, new GsonBuilder().create().toJson(json));
            context.getSource().sendFeedback(Component.translatable("commands.mubble_client.arcade.record.saved", recording.steps().size(), path.toString()));
            return recording.steps().size();
        } catch (IOException | IllegalStateException e) {
            context.getSource().sendError(Component.translatable("commands.mubble_client.arcade.file_error", name, String.valueOf(e.getMessage())));
            return 0;
        }
    }

    private static int replay(CommandContext<FabricClientCommandSource> context) {
        var name = StringArgumentType.getString(context, "name");
        var player = context.getSource().getPlayer();
        if (!ArcadeController.of(player).isDriving()) {
            context.getSource().sendError(Component.translatable("commands.mubble_client.arcade.not_driving"));
            return 0;
        }
        ArcadeRecording recording;
        try {
            var json = JsonParser.parseString(Files.readString(file(name)));
            recording = ArcadeRecording.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        } catch (IOException | RuntimeException e) {
            context.getSource().sendError(Component.translatable("commands.mubble_client.arcade.file_error", name, String.valueOf(e.getMessage())));
            return 0;
        }
        capture = null;
        playback = new Playback(name, recording, player.position());
        context.getSource().sendFeedback(Component.translatable("commands.mubble_client.arcade.replay.started", name, recording.steps().size()));
        return recording.steps().size();
    }

    private static int cancel(CommandContext<FabricClientCommandSource> context) {
        boolean any = capture != null || playback != null;
        capture = null;
        playback = null;
        context.getSource().sendFeedback(Component.translatable(any ? "commands.mubble_client.arcade.cancelled" : "commands.mubble_client.arcade.record.none"));
        return any ? 1 : 0;
    }

    /** The input of the next step: the recorded one during a replay, nothing otherwise. */
    public static Optional<ArcadeInputFrame> replayFrame(int tick) {
        var replay = playback;
        if (replay == null) {
            return Optional.empty();
        }
        var recorded = replay.recording.steps().get(replay.index).frame();
        replay.current = recorded;
        // the recorded input, at the tick of this session
        return Optional.of(new ArcadeInputFrame(tick, recorded.stickX(), recorded.stickZ(), recorded.viewYaw(), recorded.coupled(),
                recorded.held(), recorded.pressed(), recorded.jumpAgeMs(), recorded.actionAgeMs()));
    }

    /** Called after every step the driver took. */
    public static void onStep(LocalPlayer player, ArcadePrediction.Step step) {
        var payload = step.payload();
        var captured = capture;
        if (captured != null) {
            captured.steps().add(new ArcadeRecording.Step(payload.frame(), payload.from().subtract(captured.start()),
                    Optional.of(payload.result()), Optional.of(payload.fingerprint())));
        }
        var replay = playback;
        if (replay != null && replay.current != null) {
            var recorded = replay.recording.steps().get(replay.index);
            var from = payload.from().subtract(replay.start);
            boolean same = from.distanceToSqr(recorded.from()) < 1.0E-12D
                    && recorded.result().map(result -> result.equals(payload.result())).orElse(true)
                    && recorded.fingerprint().map(fingerprint -> fingerprint == payload.fingerprint()).orElse(true);
            if (!same && replay.divergence < 0) {
                replay.divergence = replay.index;
            }
            replay.index++;
            replay.current = null;
            if (replay.index >= replay.recording.steps().size()) {
                playback = null;
                player.sendSystemMessage(replay.divergence < 0
                        ? Component.translatable("commands.mubble_client.arcade.replay.identical", replay.name)
                        : Component.translatable("commands.mubble_client.arcade.replay.diverged", replay.name, replay.divergence));
            }
        }
    }

    public static boolean isRecording() {
        return capture != null;
    }

    public static boolean isReplaying() {
        return playback != null;
    }
}
