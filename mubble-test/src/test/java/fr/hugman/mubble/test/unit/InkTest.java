package fr.hugman.mubble.test.unit;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.network.protocol.common.custom.InkSyncPayload;
import fr.hugman.mubble.splatoon.world.level.ink.ChunkInk;
import fr.hugman.mubble.splatoon.world.level.ink.InkFace;
import fr.hugman.mubble.splatoon.world.level.ink.InkGrid;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import fr.hugman.mubble.splatoon.world.level.ink.InkSurfaces;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The ink layer of the Splatoon module, away from any level: how a face keeps its cells, how ink is saved and sent,
 * and where blocks let ink sit.
 */
public class InkTest {
    private static final InkStyle RED = new InkStyle(0xFF0000);
    private static final InkStyle BLUE = new InkStyle(0x0000FF, Splatoon.id("glitter"));

    /** The four cells in the middle of a face, which is where a torch stands. */
    private static final long MIDDLE = cells(3, 3) | cells(4, 3) | cells(3, 4) | cells(4, 4);

    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    private static long cells(int u, int v) {
        return 1L << InkGrid.cell(u, v);
    }

    @Test
    @DisplayName("a face painted over only keeps the styles it still shows")
    void paintingOverDropsUnusedStyles() {
        var face = new InkFace();
        face.paint(InkGrid.ALL_CELLS, RED, 10L);
        face.paint(InkGrid.ALL_CELLS, BLUE, 20L);

        assertEquals(List.of(BLUE), face.palette(), "red is not shown anywhere anymore");
        assertEquals(BLUE, face.get(0));
        assertEquals(20L, face.paintedAt(), "painting refreshes the face");

        face.paint(MIDDLE, RED, 30L);
        assertEquals(2, face.palette().size());
        assertEquals(RED, face.get(InkGrid.cell(3, 3)));
        assertEquals(BLUE, face.get(InkGrid.cell(0, 0)));
    }

    @Test
    @DisplayName("erasing every cell leaves an empty face")
    void erasingEverythingEmptiesTheFace() {
        var face = new InkFace();
        assertTrue(face.paint(MIDDLE, RED, 0L));
        assertEquals(MIDDLE, face.cells());

        assertFalse(face.erase(~MIDDLE), "erasing clean cells changes nothing");
        assertTrue(face.erase(InkGrid.ALL_CELLS));
        assertTrue(face.isEmpty());
        assertNull(face.get(InkGrid.cell(3, 3)));
    }

    @Test
    @DisplayName("an ink style reads both hex strings and integers, and defaults to the normal type")
    void styleCodecAcceptsBothColorFormats() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var fromHex = InkStyle.COLOR_CODEC.parse(ops, new JsonPrimitive("#FF00FF")).getOrThrow();
        var fromInt = InkStyle.COLOR_CODEC.parse(ops, new JsonPrimitive(0xFF00FF)).getOrThrow();
        assertEquals(0xFF00FF, fromHex);
        assertEquals(0xFF00FF, fromInt);

        CodecAssertions.assertJsonRoundTrip(InkStyle.CODEC, BLUE);
        assertEquals(InkStyle.NORMAL, new InkStyle(0x123456).type());
        CodecAssertions.assertStreamRoundTrip(InkStyle.STREAM_CODEC, BLUE);
    }

    @Test
    @DisplayName("the ink of a chunk survives being saved with it")
    void chunkInkRoundTripsThroughNbt() {
        var ink = new ChunkInk();
        var pos = new BlockPos(3, 64, -7);
        ink.getOrCreate(pos, Direction.UP).paint(InkGrid.ALL_CELLS, RED, 1234L);
        ink.getOrCreate(pos, Direction.NORTH).paint(MIDDLE, BLUE, 99L);

        var nbt = ChunkInk.CODEC.encodeStart(NbtOps.INSTANCE, ink).getOrThrow();
        var decoded = ChunkInk.CODEC.parse(NbtOps.INSTANCE, nbt).getOrThrow();

        assertFaceEquals(ink.get(pos, Direction.UP), decoded.get(pos, Direction.UP), true);
        assertFaceEquals(ink.get(pos, Direction.NORTH), decoded.get(pos, Direction.NORTH), true);
        assertNull(decoded.get(pos, Direction.SOUTH));
    }

    @Test
    @DisplayName("the ink sent to clients carries cells and styles, and cleaned faces")
    void syncPayloadRoundTrips() {
        var painted = new InkFace();
        painted.paint(MIDDLE, BLUE, 77L);
        var payload = new InkSyncPayload(new ChunkPos(2, -1), false, List.of(
                new ChunkInk.Entry(new BlockPos(32, 70, -10), Direction.EAST, Optional.of(painted)),
                new ChunkInk.Entry(new BlockPos(33, 70, -10), Direction.UP, Optional.empty())
        ));

        var buf = new FriendlyByteBuf(Unpooled.buffer());
        InkSyncPayload.STREAM_CODEC.encode(buf, payload);
        var decoded = InkSyncPayload.STREAM_CODEC.decode(buf);

        assertEquals(0, buf.readableBytes(), "the decoder left bytes behind");
        assertEquals(payload.chunk(), decoded.chunk());
        assertEquals(2, decoded.faces().size());
        assertFaceEquals(painted, decoded.faces().get(0).face().orElseThrow(), false);
        assertTrue(decoded.faces().get(1).face().isEmpty(), "a cleaned face must arrive as no face");
    }

    @Test
    @DisplayName("a full block has a flush surface on every cell of every face")
    void fullBlocksAreFlushEverywhere() {
        var surfaces = InkSurfaces.of(Blocks.STONE.defaultBlockState());
        for (var side : Direction.values()) {
            assertEquals(InkGrid.ALL_CELLS, surfaces.flush(side), side + " is not flush");
            assertEquals(0.0F, surfaces.depth(side, 0));
        }
    }

    @Test
    @DisplayName("the top of a bottom slab holds ink halfway down, and its bottom on the boundary")
    void slabsHoldInkWhereTheirSurfaceIs() {
        var slab = Blocks.STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM);
        var surfaces = InkSurfaces.of(slab);

        assertEquals(InkGrid.ALL_CELLS, surfaces.surfaces(Direction.UP));
        assertEquals(0L, surfaces.flush(Direction.UP), "the top of a bottom slab is not on the boundary");
        assertEquals(0.5F, surfaces.depth(Direction.UP, 0));
        assertEquals(InkGrid.ALL_CELLS, surfaces.flush(Direction.DOWN));
    }

    @Test
    @DisplayName("a torch only stands on the middle of the face under it")
    void aTorchCoversTheMiddleOfTheFaceUnderIt() {
        var surfaces = InkSurfaces.of(Blocks.TORCH.defaultBlockState());

        assertEquals(MIDDLE, surfaces.flush(Direction.DOWN));
        assertEquals(0L, surfaces.flush(Direction.UP), "a torch does not reach the top of its block");
    }

    @Test
    @DisplayName("air holds no ink at all")
    void airHasNoSurface() {
        var surfaces = InkSurfaces.of(Blocks.AIR.defaultBlockState());
        for (var side : Direction.values()) {
            assertEquals(0L, surfaces.surfaces(side));
        }
    }

    private static void assertFaceEquals(InkFace expected, InkFace actual, boolean withTime) {
        assertEquals(expected.palette(), actual.palette());
        var expectedCells = new int[InkGrid.CELLS];
        var actualCells = new int[InkGrid.CELLS];
        for (int cell = 0; cell < InkGrid.CELLS; cell++) {
            expectedCells[cell] = expected.paletteIndex(cell);
            actualCells[cell] = actual.paletteIndex(cell);
        }
        assertArrayEquals(expectedCells, actualCells);
        if (withTime) {
            assertEquals(expected.paintedAt(), actual.paintedAt());
        }
    }
}
