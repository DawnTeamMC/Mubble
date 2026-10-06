package fr.hugman.mubble.splatoon.world.level.ink;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Where ink can sit on a block, worked out once per block state from its outline shape.
 *
 * <p>For every face and every cell of the {@linkplain InkGrid grid}, this tells how deep the outermost surface of the
 * block lies under the face: {@code 0} for a full block, {@code 0.5} for the top of a bottom slab, {@code 1/16} for the
 * top of a dirt path, and no surface at all where the shape leaves the cell empty (the sides of a fence post). Ink is
 * drawn at that depth, which lets slabs, stairs and other partial blocks hold ink like any other block.
 *
 * <p>The cells whose surface is flush with the face are also the ones a block occupies on the boundary it shares with
 * its neighbor, which is how placing a torch on ink only cleans the middle of the face it stands on.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkSurfaces {
    public static final float NO_SURFACE = Float.NaN;

    private static final float EPSILON = 1.0E-4F;
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Map<BlockState, InkSurfaces> CACHE = new ConcurrentHashMap<>();
    private static final InkSurfaces EMPTY = new InkSurfaces(List.of());

    private final float[][] depths = new float[DIRECTIONS.length][InkGrid.CELLS];
    private final long[] surfaces = new long[DIRECTIONS.length];
    private final long[] flush = new long[DIRECTIONS.length];

    private InkSurfaces(List<AABB> boxes) {
        for (var side : DIRECTIONS) {
            int s = side.get3DDataValue();
            var axis = side.getAxis();
            boolean positive = side.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            Arrays.fill(this.depths[s], NO_SURFACE);

            for (int cell = 0; cell < InkGrid.CELLS; cell++) {
                float u = InkGrid.center(InkGrid.u(cell));
                float v = InkGrid.center(InkGrid.v(cell));
                double outermost = positive ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                boolean covered = false;

                for (var box : boxes) {
                    double minU = InkGrid.u(axis, box.minX, box.minY, box.minZ);
                    double maxU = InkGrid.u(axis, box.maxX, box.maxY, box.maxZ);
                    double minV = InkGrid.v(axis, box.minX, box.minY, box.minZ);
                    double maxV = InkGrid.v(axis, box.maxX, box.maxY, box.maxZ);
                    if (u <= minU || u >= maxU || v <= minV || v >= maxV) {
                        continue;
                    }
                    covered = true;
                    outermost = positive
                            ? Math.max(outermost, InkGrid.w(axis, box.maxX, box.maxY, box.maxZ))
                            : Math.min(outermost, InkGrid.w(axis, box.minX, box.minY, box.minZ));
                }
                if (!covered) {
                    continue;
                }

                float depth = (float) Math.clamp(positive ? 1.0 - outermost : outermost, 0.0, 1.0);
                if (depth < EPSILON) {
                    depth = 0.0F;
                    this.flush[s] |= 1L << cell;
                }
                this.depths[s][cell] = depth;
                this.surfaces[s] |= 1L << cell;
            }
        }
    }

    public static InkSurfaces of(BlockState state) {
        if (state.isAir()) {
            return EMPTY;
        }
        return CACHE.computeIfAbsent(state, s -> of(s.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)));
    }

    public static InkSurfaces of(VoxelShape shape) {
        return shape.isEmpty() ? EMPTY : new InkSurfaces(shape.toAabbs());
    }

    /**
     * @return how deep under the face the surface of the given cell lies, from 0 to 1, or {@link #NO_SURFACE}
     */
    public float depth(Direction side, int cell) {
        return this.depths[side.get3DDataValue()][cell];
    }

    /**
     * @return the cells of the face that have a surface under them, at whatever depth
     */
    public long surfaces(Direction side) {
        return this.surfaces[side.get3DDataValue()];
    }

    /**
     * @return the cells of the face whose surface is flush with it, which are also the cells the block occupies on
     * the boundary with its neighbor on that side
     */
    public long flush(Direction side) {
        return this.flush[side.get3DDataValue()];
    }
}
