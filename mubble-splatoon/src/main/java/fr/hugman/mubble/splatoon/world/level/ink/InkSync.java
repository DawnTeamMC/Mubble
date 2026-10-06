package fr.hugman.mubble.splatoon.world.level.ink;

import fr.hugman.mubble.splatoon.network.protocol.common.custom.InkSyncPayload;
import fr.hugman.mubble.splatoon.network.protocol.common.custom.SplatoonPayloadTypes;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Sends ink to the clients: all of a chunk's ink when a player starts watching it, then the faces that changed, at
 * most once per tick and per chunk however many shots landed.
 *
 * <p>Faces are copied when the payload is built, since payloads are encoded off the server thread.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkSync {
    private static final Map<ServerLevel, Long2ObjectMap<Set<Face>>> DIRTY = new IdentityHashMap<>();

    private InkSync() {
    }

    static void markDirty(ServerLevel level, BlockPos pos, Direction side) {
        DIRTY.computeIfAbsent(level, l -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(ChunkPos.containing(pos).pack(), key -> new LinkedHashSet<>())
                .add(new Face(pos.immutable(), side));
    }

    /**
     * Sends the faces that changed since the last call to the players watching them.
     */
    public static void flush(ServerLevel level) {
        var dirty = DIRTY.remove(level);
        if (dirty == null) {
            return;
        }
        for (var entry : dirty.long2ObjectEntrySet()) {
            var chunkPos = ChunkPos.unpack(entry.getLongKey());
            var players = PlayerLookup.tracking(level, chunkPos);
            if (players.isEmpty()) {
                continue;
            }

            List<ChunkInk.Entry> faces = new ArrayList<>(entry.getValue().size());
            for (var face : entry.getValue()) {
                var ink = InkLevel.get(level, face.pos(), face.side());
                faces.add(new ChunkInk.Entry(face.pos(), face.side(), Optional.ofNullable(ink).map(InkFace::copy)));
            }
            var payload = new InkSyncPayload(chunkPos, false, faces);
            for (var player : players) {
                send(player, payload);
            }
        }
    }

    /**
     * Sends all the ink of a chunk, right after the chunk itself.
     */
    public static void sendChunk(ServerPlayer player, LevelChunk chunk) {
        var ink = InkLevel.get(chunk);
        if (ink == null || ink.isEmpty()) {
            return;
        }
        List<ChunkInk.Entry> faces = new ArrayList<>();
        ink.forEach((pos, side, face) -> faces.add(new ChunkInk.Entry(pos, side, Optional.of(face.copy()))));
        send(player, new InkSyncPayload(chunk.getPos(), true, faces));
    }

    public static void forget(ServerLevel level) {
        DIRTY.remove(level);
    }

    private static void send(ServerPlayer player, InkSyncPayload payload) {
        if (ServerPlayNetworking.canSend(player, SplatoonPayloadTypes.INK_SYNC)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    private record Face(BlockPos pos, Direction side) {
    }
}
