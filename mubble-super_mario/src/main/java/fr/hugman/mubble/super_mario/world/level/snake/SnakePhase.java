package fr.hugman.mubble.super_mario.world.level.snake;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * What a snake that has already started is busy doing.
 *
 * @author Hugman
 * @since v4.0.0
 */
public enum SnakePhase implements StringRepresentable {
    /** Walking its path, one block at a time. */
    MOVING("moving"),
    /** At the end of its path, standing still and trembling before it comes apart. */
    SHAKING("shaking"),
    /** Coming apart, dropping one block after another starting from the tail. */
    FALLING("falling");

    public static final Codec<SnakePhase> CODEC = StringRepresentable.fromEnum(SnakePhase::values);

    private final String name;

    SnakePhase(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
