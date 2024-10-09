package fr.hugman.mubble.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for box collision calculations.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class BoxUtil {
    /**
     * Calculates the horizontal bouncing multiplier vector based on the collision of
     * the given origin box with a list of other boxes.
     * <p>
     * This method checks for collisions in both the X and Z axes. If a
     * collision is detected, it computes the minimum distance to the
     * closest edge of the colliding boxes in both axes. The method returns
     * a vector indicating the bounce direction. If there is no collision,
     * it returns null.
     *
     * @param originBox  the box for which the bouncing multiplier is calculated
     * @param otherBoxes other boxes to check for collisions against
     * @return a {@link Vec3d} representing the bouncing multiplier direction,
     * or null if no collision is detected
     */
    @Nullable
    public static Vec3d calculateHorizontalBouncingMultiplier(Box originBox, Box... otherBoxes) {
        double minDistanceX = Double.MAX_VALUE;
        double minDistanceZ = Double.MAX_VALUE;

        for (Box box : otherBoxes) {
            // Check for collision between the origin box and the other box
            if (originBox.maxX > box.minX && originBox.minX < box.maxX &&
                    originBox.maxZ > box.minZ && originBox.minZ < box.maxZ) {

                // Calculate the distances to the closest edges of the box
                double distanceX = Math.min(originBox.maxX - box.minX, box.maxX - originBox.minX);
                double distanceZ = Math.min(originBox.maxZ - box.minZ, box.maxZ - originBox.minZ);

                // Update the minimum distances for collision response
                if (Math.abs(distanceX) < Math.abs(minDistanceX)) {
                    minDistanceX = distanceX;
                }
                if (Math.abs(distanceZ) < Math.abs(minDistanceZ)) {
                    minDistanceZ = distanceZ;
                }
            }
        }

        // Check if a collision was detected
        if (minDistanceX == Double.MAX_VALUE && minDistanceZ == Double.MAX_VALUE) {
            return null;  // No collision detected
        }

        // Determine which axis the collision is on
        return (minDistanceX < minDistanceZ)
                ? new Vec3d(-1.0, 1.0, 1.0)  // Bounce in the x-axis
                : new Vec3d(1.0, 1.0, -1.0);  // Bounce in the z-axis
    }

    /**
     * Calculates the horizontal bouncing multiplier vector based on the collision of
     * the given origin box with a list of other boxes.
     * <p>
     * This method checks for collisions in both the X and Z axes. If a
     * collision is detected, it computes the minimum distance to the
     * closest edge of the colliding boxes in both axes. The method returns
     * a vector indicating the bounce direction. If there is no collision,
     * it returns null.
     *
     * @param originBox  the box for which the bouncing multiplier is calculated
     * @param otherBoxes a list of other boxes to check for collisions against
     * @return a {@link Vec3d} representing the bouncing multiplier direction,
     * or null if no collision is detected
     */
    public static Vec3d calculateHorizontalBouncingMultiplier(Box originBox, List<Box> otherBoxes) {
        return calculateHorizontalBouncingMultiplier(originBox, otherBoxes.toArray(new Box[0]));
    }

    /**
     * Collects potential block collisions in the world for the given origin box.
     * <p>
     * This method iterates over all block positions within the bounds of
     * the origin box and retrieves the collision shapes for each block
     * position.
     *
     * @param originBox the box to check for potential collisions
     * @return a list of {@link Box} objects representing the potential collisions
     */
    public static List<Box> collectPotentialBlockCollisions(World world, Box originBox) {
        Iterable<BlockPos> iterable = BlockPos.iterate(originBox);
        List<Box> boundingBoxes = new ArrayList<>();
        for (BlockPos pos : iterable) {
            // Collect bounding boxes from collision shapes directly
            boundingBoxes.addAll(world.getBlockState(pos).getCollisionShape(world, pos)
                    .offset(Vec3d.of(pos)).getBoundingBoxes());
        }
        return boundingBoxes;
    }
}
