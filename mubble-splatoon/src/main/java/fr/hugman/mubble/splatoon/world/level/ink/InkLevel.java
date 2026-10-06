package fr.hugman.mubble.splatoon.world.level.ink;

import fr.hugman.mubble.splatoon.tags.SplatoonBlockTags;
import fr.hugman.mubble.splatoon.world.attribute.SplatoonEnvironmentAttributes;
import fr.hugman.mubble.splatoon.world.level.attachment.SplatoonAttachmentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

/**
 * Reading and changing the ink of a level. Every change goes through here, so that the chunk gets saved, the
 * players watching it get told, and the ink gets to dry off.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkLevel {
    private static final Direction[] DIRECTIONS = Direction.values();

    private InkLevel() {
    }

    @Nullable
    public static LevelChunk loadedChunk(Level level, BlockPos pos) {
        return level.getChunkSource().getChunkNow(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
    }

    @Nullable
    public static ChunkInk get(@Nullable LevelChunk chunk) {
        return chunk == null ? null : chunk.getAttached(SplatoonAttachmentTypes.CHUNK_INK);
    }

    @Nullable
    public static InkFace get(Level level, BlockPos pos, Direction side) {
        var ink = get(loadedChunk(level, pos));
        return ink == null ? null : ink.get(pos, side);
    }

    @Nullable
    public static InkCoat getCoat(Level level, BlockPos pos) {
        var ink = get(loadedChunk(level, pos));
        return ink == null ? null : ink.getCoat(pos);
    }

    /**
     * Whether ink sticks to the given block at all. Blocks without collision (plants, torches, rails...) are left
     * alone, and so are the ones in {@link SplatoonBlockTags#UNINKABLE}.
     */
    public static boolean isInkable(BlockState state) {
        return !state.isAir()
                && !state.is(SplatoonBlockTags.UNINKABLE)
                && !state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty();
    }

    /**
     * Whether ink covers the given block as a whole, as an {@link InkCoat}: the blocks of
     * {@link SplatoonBlockTags#INK_COATED} that have no collision, and so no surface to paint.
     */
    public static boolean isCoatable(BlockState state) {
        return state.is(SplatoonBlockTags.INK_COATED)
                && !state.is(SplatoonBlockTags.UNINKABLE)
                && state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty();
    }

    /**
     * @return the cells of the neighbor's face that the given block covers, on the boundary they share
     */
    public static long covers(BlockState state, Direction towardsNeighbor) {
        // what ink covers as a whole lets ink through to the surface under it
        return isCoatable(state) ? 0L : InkSurfaces.of(state).flush(towardsNeighbor);
    }

    public static boolean liquidsWashInk(Level level, BlockPos pos) {
        return level.environmentAttributes().getValue(SplatoonEnvironmentAttributes.LIQUIDS_WASH_INK, pos);
    }

    /**
     * @return the cells of the given face that can hold ink right now: the ones with a surface under them, minus the
     * ones covered by the neighboring block and, when liquids wash ink, the ones under a liquid
     */
    public static long inkableCells(Level level, BlockPos pos, Direction side) {
        if (!level.isLoaded(pos)) {
            return 0L;
        }
        var state = level.getBlockState(pos);
        if (!isInkable(state)) {
            return 0L;
        }
        if (!state.getFluidState().isEmpty() && liquidsWashInk(level, pos)) {
            return 0L;
        }
        var surfaces = InkSurfaces.of(state);
        long cells = surfaces.surfaces(side);
        if (cells == 0L) {
            return 0L;
        }

        var neighborPos = pos.relative(side);
        if (!level.isLoaded(neighborPos)) {
            // looking would load the chunk, for a face nobody can see yet
            return 0L;
        }
        var neighbor = level.getBlockState(neighborPos);
        long flush = surfaces.flush(side);
        if (!neighbor.getFluidState().isEmpty() && liquidsWashInk(level, neighborPos)) {
            cells &= ~flush;
        }
        return cells & ~(flush & covers(neighbor, side.getOpposite()));
    }

    /**
     * Whether the given block can be coated with ink right now.
     */
    public static boolean canCoat(Level level, BlockPos pos) {
        if (!level.isLoaded(pos)) {
            return false;
        }
        var state = level.getBlockState(pos);
        return isCoatable(state) && !(!state.getFluidState().isEmpty() && liquidsWashInk(level, pos));
    }

    /**
     * Coats a whole block with ink, if it {@linkplain #canCoat can be}.
     *
     * @return whether the block changed color
     */
    public static boolean coat(ServerLevel level, BlockPos pos, InkStyle style) {
        if (!canCoat(level, pos)) {
            return false;
        }
        var chunk = loadedChunk(level, pos);
        if (chunk == null) {
            return false;
        }
        var ink = chunk.getAttachedOrCreate(SplatoonAttachmentTypes.CHUNK_INK);
        var previous = ink.getCoat(pos);
        ink.setCoat(pos.immutable(), new InkCoat(style, level.getGameTime()));
        chunk.markUnsaved();
        InkWeathering.track(level, chunk.getPos());
        boolean changed = previous == null || !previous.style().equals(style);
        if (changed) {
            InkSync.markDirty(level, pos, null);
        }
        return changed;
    }

    /**
     * Takes the coat of ink off a block.
     *
     * @return whether the block had one
     */
    public static boolean uncoat(ServerLevel level, BlockPos pos) {
        return uncoat(level, loadedChunk(level, pos), pos);
    }

    static boolean uncoat(ServerLevel level, @Nullable LevelChunk chunk, BlockPos pos) {
        var ink = get(chunk);
        if (ink == null || ink.getCoat(pos) == null) {
            return false;
        }
        ink.setCoat(pos, null);
        if (ink.isEmpty()) {
            chunk.removeAttached(SplatoonAttachmentTypes.CHUNK_INK);
        }
        chunk.markUnsaved();
        InkSync.markDirty(level, pos, null);
        return true;
    }

    /**
     * Paints the given cells of a face, leaving out the ones that {@linkplain #inkableCells cannot hold ink}.
     *
     * @return whether any cell changed
     */
    public static boolean paint(ServerLevel level, BlockPos pos, Direction side, long cells, InkStyle style) {
        cells &= inkableCells(level, pos, side);
        if (cells == 0L) {
            return false;
        }
        var chunk = loadedChunk(level, pos);
        if (chunk == null) {
            return false;
        }

        var ink = chunk.getAttachedOrCreate(SplatoonAttachmentTypes.CHUNK_INK);
        boolean changed = ink.getOrCreate(pos, side).paint(cells, style, level.getGameTime());
        // even without a visible change, the face was refreshed and dries later
        chunk.markUnsaved();
        InkWeathering.track(level, chunk.getPos());
        if (changed) {
            InkSync.markDirty(level, pos, side);
        }
        return changed;
    }

    /**
     * Cleans the given cells of a face.
     *
     * @return whether any cell changed
     */
    public static boolean erase(ServerLevel level, BlockPos pos, Direction side, long cells) {
        return erase(level, loadedChunk(level, pos), pos, side, cells);
    }

    static boolean erase(ServerLevel level, @Nullable LevelChunk chunk, BlockPos pos, Direction side, long cells) {
        var ink = get(chunk);
        if (ink == null) {
            return false;
        }
        var face = ink.get(pos, side);
        if (face == null || !face.erase(cells)) {
            return false;
        }

        if (face.isEmpty()) {
            ink.remove(pos, side);
        }
        if (ink.isEmpty()) {
            chunk.removeAttached(SplatoonAttachmentTypes.CHUNK_INK);
        }
        chunk.markUnsaved();
        InkSync.markDirty(level, pos, side);
        return true;
    }

    /**
     * Cleans every face of the given block, and its coat.
     *
     * @return whether any cell changed
     */
    public static boolean clear(ServerLevel level, BlockPos pos) {
        return clear(level, loadedChunk(level, pos), pos);
    }

    static boolean clear(ServerLevel level, @Nullable LevelChunk chunk, BlockPos pos) {
        var ink = get(chunk);
        if (ink == null) {
            return false;
        }
        boolean changed = uncoat(level, chunk, pos);
        if (!ink.has(pos)) {
            return changed;
        }
        for (var side : DIRECTIONS) {
            changed |= erase(level, chunk, pos, side, InkGrid.ALL_CELLS);
        }
        return changed;
    }
}
