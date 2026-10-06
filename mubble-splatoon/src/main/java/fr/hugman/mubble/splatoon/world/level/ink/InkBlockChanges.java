package fr.hugman.mubble.splatoon.world.level.ink;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * Keeps ink in line with the blocks it is painted on, whenever a block changes:
 * <ul>
 *     <li>ink on the block itself goes away if the block is replaced, and shrinks to what is left of its surface
 *     if only its state changes (a door opening, a cake being eaten);</li>
 *     <li>ink on the neighbors goes away where the new block covers them, and only there: a torch placed on the
 *     floor cleans the middle of the face it stands on, while grass growing on it cleans nothing;</li>
 *     <li>the coat of a plant goes away with the plant;</li>
 *     <li>a liquid washes away the ink it touches, unless {@link fr.hugman.mubble.splatoon.world.attribute.SplatoonEnvironmentAttributes#LIQUIDS_WASH_INK}
 *     says otherwise.</li>
 * </ul>
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkBlockChanges {
    private static final Direction[] DIRECTIONS = Direction.values();

    private InkBlockChanges() {
    }

    public static void onBlockChanged(ServerLevel level, LevelChunk chunk, BlockPos pos, BlockState oldState, BlockState newState) {
        if (!hasInkAround(level, chunk, pos)) {
            return;
        }
        boolean liquid = !newState.getFluidState().isEmpty() && InkLevel.liquidsWashInk(level, pos);

        // the block itself
        var ink = InkLevel.get(chunk);
        if (ink != null && ink.getCoat(pos) != null
                && (liquid || oldState.getBlock() != newState.getBlock() || !InkLevel.isCoatable(newState))) {
            InkLevel.uncoat(level, chunk, pos);
        }
        if (ink != null && ink.has(pos)) {
            if (liquid || oldState.getBlock() != newState.getBlock() || !InkLevel.isInkable(newState)) {
                InkLevel.clear(level, chunk, pos);
            } else {
                var surfaces = InkSurfaces.of(newState);
                for (var side : DIRECTIONS) {
                    InkLevel.erase(level, chunk, pos, side, ~surfaces.surfaces(side));
                }
            }
        }

        // its neighbors, on the boundary they share with it
        for (var direction : DIRECTIONS) {
            var neighborPos = pos.relative(direction);
            var side = direction.getOpposite();
            var neighborChunk = sameChunk(pos, neighborPos) ? chunk : InkLevel.loadedChunk(level, neighborPos);
            var neighborInk = InkLevel.get(neighborChunk);
            if (neighborInk == null || neighborInk.get(neighborPos, side) == null) {
                continue;
            }

            long covered = liquid ? InkGrid.ALL_CELLS : InkLevel.covers(newState, direction);
            if (covered == 0L) {
                continue;
            }
            // recessed surfaces (the top of a slab) do not touch the boundary, so nothing there covers them
            covered &= InkSurfaces.of(level.getBlockState(neighborPos)).flush(side);
            InkLevel.erase(level, neighborChunk, neighborPos, side, covered);
        }
    }

    private static boolean hasInkAround(ServerLevel level, LevelChunk chunk, BlockPos pos) {
        if (InkLevel.get(chunk) != null) {
            return true;
        }
        int localX = SectionPos.sectionRelative(pos.getX());
        int localZ = SectionPos.sectionRelative(pos.getZ());
        return (localX == 0 && InkLevel.get(InkLevel.loadedChunk(level, pos.west())) != null)
                || (localX == 15 && InkLevel.get(InkLevel.loadedChunk(level, pos.east())) != null)
                || (localZ == 0 && InkLevel.get(InkLevel.loadedChunk(level, pos.north())) != null)
                || (localZ == 15 && InkLevel.get(InkLevel.loadedChunk(level, pos.south())) != null);
    }

    private static boolean sameChunk(BlockPos a, BlockPos b) {
        return SectionPos.blockToSectionCoord(a.getX()) == SectionPos.blockToSectionCoord(b.getX())
                && SectionPos.blockToSectionCoord(a.getZ()) == SectionPos.blockToSectionCoord(b.getZ());
    }
}
