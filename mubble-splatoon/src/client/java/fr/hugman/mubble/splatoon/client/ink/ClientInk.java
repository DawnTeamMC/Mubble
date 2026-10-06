package fr.hugman.mubble.splatoon.client.ink;

import fr.hugman.mubble.splatoon.network.protocol.common.custom.InkSyncPayload;
import fr.hugman.mubble.splatoon.world.level.ink.ChunkInk;
import fr.hugman.mubble.splatoon.world.level.ink.InkFace;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ink the client knows about, as the server sends it.
 *
 * <p>Payloads are applied on the client thread, but sections are meshed on worker threads. Those only ever read
 * {@linkplain #section(long) per-section snapshots}: lists that are rebuilt and swapped whenever a face of their
 * section changes, and that only hold faces fresh from the network, which nothing modifies afterwards.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class ClientInk {
    private static final Long2ObjectMap<ChunkInk> CHUNKS = new Long2ObjectOpenHashMap<>();
    private static final Map<Long, Section> SECTIONS = new ConcurrentHashMap<>();

    private ClientInk() {
    }

    /**
     * @return the ink of the given section, or {@code null} if there is none
     */
    @Nullable
    public static Section section(long sectionKey) {
        return SECTIONS.get(sectionKey);
    }

    @Nullable
    public static InkFace get(BlockPos pos, Direction side) {
        var ink = CHUNKS.get(ChunkPos.containing(pos).pack());
        return ink == null ? null : ink.get(pos, side);
    }

    public static void apply(ClientLevel level, InkSyncPayload payload) {
        var chunkPos = payload.chunk();
        if (level.getChunkSource().getChunkNow(chunkPos.x(), chunkPos.z()) == null) {
            // the chunk itself will come with all of its ink
            return;
        }
        long key = chunkPos.pack();
        LongSet changedSections = new LongOpenHashSet();

        var ink = CHUNKS.get(key);
        if (payload.replace() && ink != null) {
            ink.forEach((pos, side, face) -> changedSections.add(SectionPos.asLong(pos)));
            ink.forEachCoat((pos, coat) -> changedSections.add(SectionPos.asLong(pos)));
            ink.clear();
        }
        if (ink == null) {
            ink = new ChunkInk();
            CHUNKS.put(key, ink);
        }
        for (var entry : payload.faces()) {
            ink.set(entry.pos(), entry.side(), entry.face().orElse(null));
            changedSections.add(SectionPos.asLong(entry.pos()));
        }
        for (var entry : payload.coats()) {
            ink.setCoat(entry.pos(), entry.coat().orElse(null));
            changedSections.add(SectionPos.asLong(entry.pos()));
        }
        if (ink.isEmpty()) {
            CHUNKS.remove(key);
        }

        for (long section : changedSections) {
            rebuild(ink, section);
            var pos = SectionPos.of(section);
            level.setSectionRangeDirty(pos.x(), pos.y(), pos.z(), pos.x(), pos.y(), pos.z());
        }
    }

    private static void rebuild(ChunkInk ink, long sectionKey) {
        List<Face> faces = new ArrayList<>();
        ink.forEach((pos, side, face) -> {
            if (SectionPos.asLong(pos) == sectionKey) {
                faces.add(new Face(pos, side, face));
            }
        });
        Long2ObjectMap<InkStyle> coats = new Long2ObjectOpenHashMap<>();
        ink.forEachCoat((pos, coat) -> {
            if (SectionPos.asLong(pos) == sectionKey) {
                coats.put(pos.asLong(), coat.style());
            }
        });
        if (faces.isEmpty() && coats.isEmpty()) {
            SECTIONS.remove(sectionKey);
        } else {
            SECTIONS.put(sectionKey, new Section(List.copyOf(faces), Long2ObjectMaps.unmodifiable(coats)));
        }
    }

    public static void forgetChunk(ClientLevel level, ChunkPos chunkPos) {
        if (CHUNKS.remove(chunkPos.pack()) == null) {
            return;
        }
        for (int y = level.getMinSectionY(); y <= level.getMaxSectionY(); y++) {
            SECTIONS.remove(SectionPos.asLong(chunkPos.x(), y, chunkPos.z()));
        }
    }

    public static void clear() {
        CHUNKS.clear();
        SECTIONS.clear();
    }

    public record Face(BlockPos pos, Direction side, InkFace ink) {
    }

    /**
     * The ink of one section, as meshing threads see it.
     *
     * @param faces the faces holding ink
     * @param coats the style of the coated blocks, by {@linkplain BlockPos#asLong() position}
     */
    public record Section(List<Face> faces, Long2ObjectMap<InkStyle> coats) {
    }
}
