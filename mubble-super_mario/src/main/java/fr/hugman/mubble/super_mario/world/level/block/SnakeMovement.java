package fr.hugman.mubble.super_mario.world.level.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * What a moving snake block is doing while it stands in for an ordinary one.
 *
 * @author Hugman
 * @since v4.0.0
 */
public enum SnakeMovement implements StringRepresentable {
    /** Growing into the cell it sits in, sliding in from the block behind it. */
    HEAD("head"),
    /** Leaving the cell it sits in, sliding forward into the body in front of it. */
    TAIL("tail"),
    /** Standing still and trembling, at the end of the path and just before falling. */
    SHAKE("shake");

    public static final Codec<SnakeMovement> CODEC = StringRepresentable.fromEnum(SnakeMovement::values);

    private final String name;

    SnakeMovement(String name) {
        this.name = name;
    }

    /** Whether the block travels along its direction, or just sits there. */
    public boolean travels() {
        return this != SHAKE;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
