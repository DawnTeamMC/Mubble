package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.world.phys.AABBUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Pure geometry checks for {@link AABBUtil}, the class deciding on which axis a koopa shell bounces
 * back when it runs into a wall. No Minecraft bootstrap is needed here: only {@link AABB} maths.
 */
public class AABBUtilTest {
    /** Bouncing back on the X axis: the X component of the movement gets mirrored. */
    private static final Vec3 BOUNCE_ON_X = new Vec3(-1.0D, 1.0D, 1.0D);
    /** Bouncing back on the Z axis: the Z component of the movement gets mirrored. */
    private static final Vec3 BOUNCE_ON_Z = new Vec3(1.0D, 1.0D, -1.0D);

    /** A one block wide box sitting at the origin, standing in for the moving entity. */
    private static final AABB ORIGIN = new AABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    @Test
    @DisplayName("a box that touches nothing does not bounce")
    void noCollisionYieldsNoMultiplier() {
        assertNull(AABBUtil.calculateHorizontalBouncingMultiplier(ORIGIN, new AABB(2.0D, 0.0D, 2.0D, 3.0D, 1.0D, 3.0D)));
        assertNull(AABBUtil.calculateHorizontalBouncingMultiplier(ORIGIN));
    }

    @Test
    @DisplayName("a wall barely overlapped on X bounces the box on X")
    void shallowestOverlapOnXBouncesOnX() {
        // Overlaps by 0.1 on X and by a full block on Z, so X is the axis that got hit.
        var wall = new AABB(0.9D, 0.0D, -5.0D, 3.0D, 1.0D, 5.0D);

        assertEquals(BOUNCE_ON_X, AABBUtil.calculateHorizontalBouncingMultiplier(ORIGIN, wall));
    }

    @Test
    @DisplayName("a wall barely overlapped on Z bounces the box on Z")
    void shallowestOverlapOnZBouncesOnZ() {
        var wall = new AABB(-5.0D, 0.0D, 0.9D, 5.0D, 1.0D, 3.0D);

        assertEquals(BOUNCE_ON_Z, AABBUtil.calculateHorizontalBouncingMultiplier(ORIGIN, wall));
    }

    @Test
    @DisplayName("with several walls, the shallowest overlap of all of them wins")
    void closestEdgeAcrossAllBoxesWins() {
        var overlappingOnXBy10Percent = new AABB(0.9D, 0.0D, -5.0D, 3.0D, 1.0D, 5.0D);
        var overlappingOnZBy5Percent = new AABB(-5.0D, 0.0D, 0.95D, 5.0D, 1.0D, 3.0D);

        assertEquals(BOUNCE_ON_Z, AABBUtil.calculateHorizontalBouncingMultiplier(ORIGIN, overlappingOnXBy10Percent, overlappingOnZBy5Percent));
    }

    @Test
    @DisplayName("the list overload behaves like the varargs one")
    void listOverloadMatchesVarargsOverload() {
        var walls = List.of(
                new AABB(0.9D, 0.0D, -5.0D, 3.0D, 1.0D, 5.0D),
                new AABB(-5.0D, 0.0D, 0.95D, 5.0D, 1.0D, 3.0D)
        );

        assertEquals(
                AABBUtil.calculateHorizontalBouncingMultiplier(ORIGIN, walls.toArray(new AABB[0])),
                AABBUtil.calculateHorizontalBouncingMultiplier(ORIGIN, walls)
        );
    }
}
