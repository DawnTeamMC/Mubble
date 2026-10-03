package fr.hugman.mubble.world.arcade.sim;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

/**
 * Whatever carries out the displacement a step planned.
 * <p>
 * On the client, and in tests, it is the player entity moving through vanilla collisions. On the
 * server, it is the validator, which checks what the client reports instead of moving anything.
 */
public interface ArcadeBody {
    Vec3 position();

    /**
     * Moves by {@code first}, then by {@code second}, in the given hitbox pose.
     * <p>
     * Two moves rather than one, because vanilla always resolves the vertical axis first: a step
     * that has to go sideways before going down (staying on a step-down) or sideways before going
     * up (sliding around a corner on a head bonk) needs the horizontal part done on its own first.
     */
    MoveResult move(Pose pose, Vec3 first, Vec3 second);
}
