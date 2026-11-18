package fr.hugman.mubble.splatoon.world.level.ink;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

/**
 * The grid ink is painted on. Every face of a block is split into {@value #SIZE}×{@value #SIZE} cells, so one cell
 * covers 2×2 pixels of a vanilla texture (12.5 cm). With 64 cells per face, any set of cells of a face fits in a
 * single {@code long}, which is how cells are passed around ({@code 1L << cell}).
 *
 * <p>Cells are addressed by {@code (u, v)}, both axes being world axes so that two faces sharing a plane (the top of
 * a block and the bottom of the block above it) agree on what a cell is:
 * <ul>
 *     <li>faces on the X axis (west, east): {@code u} follows Z, {@code v} follows Y</li>
 *     <li>faces on the Y axis (down, up): {@code u} follows X, {@code v} follows Z</li>
 *     <li>faces on the Z axis (north, south): {@code u} follows X, {@code v} follows Y</li>
 * </ul>
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkGrid {
    public static final int SIZE = 8;
    public static final int CELLS = SIZE * SIZE;
    public static final long ALL_CELLS = -1L;

    private InkGrid() {
    }

    public static int cell(int u, int v) {
        return v * SIZE + u;
    }

    public static int u(int cell) {
        return cell % SIZE;
    }

    public static int v(int cell) {
        return cell / SIZE;
    }

    /**
     * @return the position of the center of the {@code i}-th row or column, from 0 to 1 across the face
     */
    public static float center(int i) {
        return (i + 0.5F) / SIZE;
    }

    /**
     * @return the row or column holding the given position, from 0 to 1 across the face
     */
    public static int index(double local) {
        return Mth.clamp(Mth.floor(local * SIZE), 0, SIZE - 1);
    }

    public static double u(Direction.Axis axis, double x, double y, double z) {
        return axis == Direction.Axis.X ? z : x;
    }

    public static double v(Direction.Axis axis, double x, double y, double z) {
        return axis == Direction.Axis.Y ? z : y;
    }

    public static double w(Direction.Axis axis, double x, double y, double z) {
        return axis.choose(x, y, z);
    }

    /**
     * @return the cell holding the given point, in coordinates local to the block
     */
    public static int cellAt(Direction.Axis axis, double x, double y, double z) {
        return cell(index(u(axis, x, y, z)), index(v(axis, x, y, z)));
    }

    public static boolean has(long cells, int cell) {
        return (cells & (1L << cell)) != 0;
    }
}
