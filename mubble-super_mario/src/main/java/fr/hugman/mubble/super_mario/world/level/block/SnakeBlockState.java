package fr.hugman.mubble.super_mario.world.level.block;

import net.minecraft.util.StringRepresentable;

/**
 * Whether a snake block is sitting still or walking.
 * <p>
 * There is no third state for a block that has come off a snake. A block that has fallen is an
 * ordinary snake block again, ready to be walked on the moment it has a route to take, which is
 * what makes a snake something a map can take apart and put back together.
 *
 * @author Hugman
 * @since v4.0.0
 */
public enum SnakeBlockState implements StringRepresentable {
    /** Sitting still. Blocks can be added around it and a path drawn off it. */
    IDLE("idle"),
    /** The snake has started and is walking its path. */
    MOVING("moving");

    private final String name;

    SnakeBlockState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
