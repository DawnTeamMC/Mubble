package fr.hugman.mubble.super_mario.world.level.block;

import net.minecraft.util.StringRepresentable;

/**
 * Where a snake block is in its life: being built, running along its path, or knocked out.
 *
 * @author Hugman
 * @since v4.0.0
 */
public enum SnakeBlockState implements StringRepresentable {
    /** The snake is being built. Blocks can be added to its tail and a path drawn from its head. */
    IDLE("idle"),
    /** The snake has started and is walking its path. */
    MOVING("moving"),
    /** The block has broken off a snake. Grayed out, and only a redstone pulse brings it back. */
    DEACTIVATED("deactivated");

    private final String name;

    SnakeBlockState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
