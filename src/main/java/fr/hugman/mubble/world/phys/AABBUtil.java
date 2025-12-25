package fr.hugman.mubble.world.phys;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Utility class for box collision calculations.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class AABBUtil {
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
     * @return a {@link Vec3} representing the bouncing multiplier direction,
     * or null if no collision is detected
     */
    @Nullable
    public static Vec3 calculateHorizontalBouncingMultiplier(AABB originBox, AABB... otherBoxes) {
        double minDistanceX = Double.MAX_VALUE;
        double minDistanceZ = Double.MAX_VALUE;

        for (AABB box : otherBoxes) {
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
                ? new Vec3(-1.0, 1.0, 1.0)  // Bounce in the x-axis
                : new Vec3(1.0, 1.0, -1.0);  // Bounce in the z-axis
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
     * @return a {@link Vec3} representing the bouncing multiplier direction,
     * or null if no collision is detected
     */
    public static Vec3 calculateHorizontalBouncingMultiplier(AABB originBox, List<AABB> otherBoxes) {
        return calculateHorizontalBouncingMultiplier(originBox, otherBoxes.toArray(new AABB[0]));
    }

    /**
     * Collects potential block collisions in the level for the given origin box.
     * <p>
     * This method iterates over all block positions within the bounds of
     * the origin box and retrieves the collision shapes for each block
     * position.
     *
     * @param originBox the box to check for potential collisions
     * @return a list of {@link AABB} objects representing the potential collisions
     */
    public static List<AABB> collectPotentialBlockCollisions(Level level, AABB originBox) {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(originBox);
        List<AABB> boundingBoxes = new ArrayList<>();
        for (BlockPos pos : iterable) {
            // Collect bounding boxes from collision shapes directly
            boundingBoxes.addAll(level.getBlockState(pos).getCollisionShape(level, pos)
                    .move(Vec3.atLowerCornerOf(pos)).toAabbs());
        }
        return boundingBoxes;
    }
}
