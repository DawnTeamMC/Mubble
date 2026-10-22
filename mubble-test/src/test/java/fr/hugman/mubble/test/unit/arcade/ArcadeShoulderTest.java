package fr.hugman.mubble.test.unit.arcade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.hugman.mubble.arcade.ArcadeShoulder;
import fr.hugman.mubble.test.unit.support.Registrations;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Where the eyes of a player aiming over the shoulder look: at what the crosshair points at. When
 * they aim is held against real items by {@code ArcadeAimGameTest}.
 */
public class ArcadeShoulderTest {
    @BeforeAll
    static void bootstrap() {
        Registrations.registerEverything();
    }

    @Test
    @DisplayName("the eyes look at what the crosshair points at, as vanilla's lookAt does")
    void lookAt() {
        var eye = new Vec3(10.0D, 64.0D, 10.0D);
        var south = ArcadeShoulder.lookAt(eye, eye.add(0.0D, 0.0D, 5.0D));
        assertEquals(0.0F, south.y, 1.0E-4F, "south is a yaw of 0");
        assertEquals(0.0F, south.x, 1.0E-4F, "level with the eyes");
        var east = ArcadeShoulder.lookAt(eye, eye.add(5.0D, 0.0D, 0.0D));
        assertEquals(-90.0F, east.y, 1.0E-4F, "east is a yaw of -90");
        var up = ArcadeShoulder.lookAt(eye, eye.add(0.0D, 5.0D, 5.0D));
        assertEquals(-45.0F, up.x, 1.0E-4F, "looking up is a negative pitch");
        // and back: the direction of the rotation points at the target
        var target = eye.add(3.0D, -2.0D, -7.0D);
        var rotation = ArcadeShoulder.lookAt(eye, target);
        var direction = Vec3.directionFromRotation(rotation.x, rotation.y);
        assertEquals(0.0D, direction.cross(target.subtract(eye).normalize()).length(), 1.0E-4D, "the rotation points at the target");
    }
}
