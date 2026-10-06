package fr.hugman.mubble.splatoon.world.level.ink;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * All the ink painted on the blocks of one chunk, attached to that chunk and saved with it: the faces of blocks, and
 * the {@linkplain InkCoat coats} of plants.
 *
 * <p>Only faces and blocks holding ink are stored, so a chunk nobody painted carries nothing at all.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class ChunkInk {
    private static final Codec<ChunkInk> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().optionalFieldOf("faces", List.of()).forGetter(ChunkInk::entries),
            CoatEntry.CODEC.listOf().optionalFieldOf("coats", List.of()).forGetter(ChunkInk::coatEntries)
    ).apply(instance, ChunkInk::of));
    /**
     * Also reads the first format, a plain list of faces.
     */
    public static final Codec<ChunkInk> CODEC = Codec.withAlternative(FULL_CODEC, Entry.CODEC.listOf().xmap(faces -> of(faces, List.of()), ChunkInk::entries));

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Long2ObjectMap<InkFace[]> faces = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectMap<InkCoat> coats = new Long2ObjectOpenHashMap<>();

    private static ChunkInk of(List<Entry> faces, List<CoatEntry> coats) {
        var ink = new ChunkInk();
        for (var entry : faces) {
            entry.face().ifPresent(face -> ink.set(entry.pos(), entry.side(), face));
        }
        for (var entry : coats) {
            entry.coat().ifPresent(coat -> ink.setCoat(entry.pos(), coat));
        }
        return ink;
    }

    public List<Entry> entries() {
        List<Entry> entries = new ArrayList<>();
        this.forEach((pos, side, face) -> entries.add(new Entry(pos, side, Optional.of(face))));
        return entries;
    }

    public List<CoatEntry> coatEntries() {
        List<CoatEntry> entries = new ArrayList<>(this.coats.size());
        for (var entry : this.coats.long2ObjectEntrySet()) {
            entries.add(new CoatEntry(BlockPos.of(entry.getLongKey()), Optional.of(entry.getValue())));
        }
        return entries;
    }

    @Nullable
    public InkCoat getCoat(BlockPos pos) {
        return this.coats.get(pos.asLong());
    }

    /**
     * Puts a coat, or removes it if it is {@code null}.
     */
    public void setCoat(BlockPos pos, @Nullable InkCoat coat) {
        if (coat == null) {
            this.coats.remove(pos.asLong());
        } else {
            this.coats.put(pos.asLong(), coat);
        }
    }

    public void forEachCoat(CoatConsumer consumer) {
        for (var entry : this.coats.long2ObjectEntrySet()) {
            consumer.accept(BlockPos.of(entry.getLongKey()), entry.getValue());
        }
    }

    @Nullable
    public InkFace get(BlockPos pos, Direction side) {
        var faces = this.faces.get(pos.asLong());
        return faces == null ? null : faces[side.get3DDataValue()];
    }

    public InkFace getOrCreate(BlockPos pos, Direction side) {
        var faces = this.faces.computeIfAbsent(pos.asLong(), key -> new InkFace[DIRECTIONS.length]);
        var face = faces[side.get3DDataValue()];
        if (face == null) {
            face = new InkFace();
            faces[side.get3DDataValue()] = face;
        }
        return face;
    }

    /**
     * Puts a face, or removes it if it is {@code null} or holds no ink.
     */
    public void set(BlockPos pos, Direction side, @Nullable InkFace face) {
        if (face == null || face.isEmpty()) {
            this.remove(pos, side);
            return;
        }
        this.faces.computeIfAbsent(pos.asLong(), key -> new InkFace[DIRECTIONS.length])[side.get3DDataValue()] = face;
    }

    public void remove(BlockPos pos, Direction side) {
        long key = pos.asLong();
        var faces = this.faces.get(key);
        if (faces == null) {
            return;
        }
        faces[side.get3DDataValue()] = null;
        for (var face : faces) {
            if (face != null) {
                return;
            }
        }
        this.faces.remove(key);
    }

    /**
     * Drops the faces of the given block that hold no ink anymore.
     */
    public void prune(BlockPos pos) {
        var faces = this.faces.get(pos.asLong());
        if (faces == null) {
            return;
        }
        for (var side : DIRECTIONS) {
            var face = faces[side.get3DDataValue()];
            if (face != null && face.isEmpty()) {
                this.remove(pos, side);
            }
        }
    }

    public boolean has(BlockPos pos) {
        return this.faces.containsKey(pos.asLong());
    }

    public boolean isEmpty() {
        return this.faces.isEmpty() && this.coats.isEmpty();
    }

    public void clear() {
        this.faces.clear();
        this.coats.clear();
    }

    public void forEach(FaceConsumer consumer) {
        for (var entry : this.faces.long2ObjectEntrySet()) {
            var pos = BlockPos.of(entry.getLongKey());
            var faces = entry.getValue();
            for (var side : DIRECTIONS) {
                var face = faces[side.get3DDataValue()];
                if (face != null) {
                    consumer.accept(pos, side, face);
                }
            }
        }
    }

    @FunctionalInterface
    public interface FaceConsumer {
        void accept(BlockPos pos, Direction side, InkFace face);
    }

    @FunctionalInterface
    public interface CoatConsumer {
        void accept(BlockPos pos, InkCoat coat);
    }

    /**
     * One block, with the coat of ink it holds or {@linkplain Optional#empty() none} when it was cleaned.
     */
    public record CoatEntry(BlockPos pos, Optional<InkCoat> coat) {
        public static final Codec<CoatEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(CoatEntry::pos),
                InkCoat.MAP_CODEC.forGetter(entry -> entry.coat().orElseThrow())
        ).apply(instance, (pos, coat) -> new CoatEntry(pos, Optional.of(coat))));

        public static final StreamCodec<ByteBuf, CoatEntry> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, CoatEntry::pos,
                ByteBufCodecs.optional(InkCoat.STREAM_CODEC), CoatEntry::coat,
                CoatEntry::new
        );
    }

    /**
     * One face of one block, with the ink it holds or {@linkplain Optional#empty() none} when it was cleaned.
     */
    public record Entry(BlockPos pos, Direction side, Optional<InkFace> face) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Entry::pos),
                Direction.CODEC.fieldOf("side").forGetter(Entry::side),
                InkFace.MAP_CODEC.forGetter(entry -> entry.face().orElseThrow())
        ).apply(instance, (pos, side, face) -> new Entry(pos, side, Optional.of(face))));

        public static final StreamCodec<ByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, Entry::pos,
                Direction.STREAM_CODEC, Entry::side,
                ByteBufCodecs.optional(InkFace.STREAM_CODEC), Entry::face,
                Entry::new
        );
    }
}
