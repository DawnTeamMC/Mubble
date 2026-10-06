package fr.hugman.mubble.splatoon.world.level.ink;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The ink painted on one face of one block.
 *
 * <p>Each cell of the {@linkplain InkGrid grid} holds either nothing ({@code 0}) or one plus the index of its style in
 * a small palette, so a face painted with a single color costs 64 bytes and one palette entry, however many times it
 * gets painted over. The palette never holds styles that no cell uses.
 *
 * <p>The face also remembers when it was last painted, which is what makes it dry off eventually. That time only
 * matters to the server, and is not sent to clients.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkFace {
    public static final MapCodec<InkFace> MAP_CODEC = RecordCodecBuilder.<InkFace>mapCodec(instance -> instance.group(
            InkStyle.CODEC.listOf().fieldOf("palette").forGetter(face -> List.of(face.palette)),
            Codec.BYTE_BUFFER.xmap(InkFace::toArray, ByteBuffer::wrap).fieldOf("cells").forGetter(face -> face.cells),
            Codec.LONG.optionalFieldOf("painted_at", 0L).forGetter(face -> face.paintedAt)
    ).apply(instance, InkFace::new)).validate(InkFace::validate);
    public static final Codec<InkFace> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<ByteBuf, InkFace> STREAM_CODEC = StreamCodec.composite(
            InkStyle.STREAM_CODEC.apply(ByteBufCodecs.list(InkGrid.CELLS)), face -> List.of(face.palette),
            ByteBufCodecs.BYTE_ARRAY, face -> face.cells,
            (palette, cells) -> new InkFace(palette, cells, 0L)
    );

    private InkStyle[] palette;
    private final byte[] cells;
    private long paintedAt;

    public InkFace() {
        this.palette = new InkStyle[0];
        this.cells = new byte[InkGrid.CELLS];
    }

    private InkFace(List<InkStyle> palette, byte[] cells, long paintedAt) {
        this.palette = palette.toArray(InkStyle[]::new);
        this.cells = cells;
        this.paintedAt = paintedAt;
    }

    private static byte[] toArray(ByteBuffer buffer) {
        var array = new byte[buffer.remaining()];
        buffer.duplicate().get(array);
        return array;
    }

    private static DataResult<InkFace> validate(InkFace face) {
        if (face.cells.length != InkGrid.CELLS) {
            return DataResult.error(() -> "Ink faces have " + InkGrid.CELLS + " cells, got " + face.cells.length);
        }
        for (byte cell : face.cells) {
            if (cell < 0 || cell > face.palette.length) {
                return DataResult.error(() -> "Ink cell points outside of its palette: " + cell);
            }
        }
        return DataResult.success(face);
    }

    /**
     * @return the style of the given cell, or {@code null} if it is clean
     */
    @Nullable
    public InkStyle get(int cell) {
        int index = this.cells[cell];
        return index == 0 ? null : this.palette[index - 1];
    }

    /**
     * @return the index of the style of the given cell in {@link #palette()} plus one, or {@code 0} if it is clean
     */
    public int paletteIndex(int cell) {
        return this.cells[cell];
    }

    public List<InkStyle> palette() {
        return List.of(this.palette);
    }

    public long paintedAt() {
        return this.paintedAt;
    }

    /**
     * @return the cells holding ink
     */
    public long cells() {
        long mask = 0L;
        for (int cell = 0; cell < InkGrid.CELLS; cell++) {
            if (this.cells[cell] != 0) {
                mask |= 1L << cell;
            }
        }
        return mask;
    }

    public boolean isEmpty() {
        return this.palette.length == 0;
    }

    /**
     * Paints the given cells, and marks the face as freshly painted.
     *
     * @return whether any cell changed
     */
    public boolean paint(long cells, InkStyle style, long time) {
        this.paintedAt = time;

        int index = Arrays.asList(this.palette).indexOf(style);
        if (index < 0) {
            this.palette = Arrays.copyOf(this.palette, this.palette.length + 1);
            this.palette[this.palette.length - 1] = style;
            index = this.palette.length - 1;
        }
        byte value = (byte) (index + 1);

        boolean changed = false;
        for (int cell = 0; cell < InkGrid.CELLS; cell++) {
            if (InkGrid.has(cells, cell) && this.cells[cell] != value) {
                this.cells[cell] = value;
                changed = true;
            }
        }
        this.trimPalette();
        return changed;
    }

    /**
     * Cleans the given cells.
     *
     * @return whether any cell changed
     */
    public boolean erase(long cells) {
        boolean changed = false;
        for (int cell = 0; cell < InkGrid.CELLS; cell++) {
            if (InkGrid.has(cells, cell) && this.cells[cell] != 0) {
                this.cells[cell] = 0;
                changed = true;
            }
        }
        if (changed) {
            this.trimPalette();
        }
        return changed;
    }

    /**
     * Drops the styles no cell uses anymore, keeping the palette as small as the face.
     */
    private void trimPalette() {
        var used = new boolean[this.palette.length];
        for (byte cell : this.cells) {
            if (cell != 0) {
                used[cell - 1] = true;
            }
        }

        var remap = new byte[this.palette.length + 1];
        List<InkStyle> trimmed = new ArrayList<>(this.palette.length);
        for (int i = 0; i < this.palette.length; i++) {
            if (used[i]) {
                trimmed.add(this.palette[i]);
                remap[i + 1] = (byte) trimmed.size();
            }
        }
        if (trimmed.size() == this.palette.length) {
            return;
        }

        for (int cell = 0; cell < InkGrid.CELLS; cell++) {
            this.cells[cell] = remap[this.cells[cell]];
        }
        this.palette = trimmed.toArray(InkStyle[]::new);
    }

    public InkFace copy() {
        return new InkFace(List.of(this.palette), this.cells.clone(), this.paintedAt);
    }
}
