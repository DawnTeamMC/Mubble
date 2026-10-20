package fr.hugman.mubble.super_mario.world.level.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * How fast a snake walks its path, in ticks spent on each block.
 * <p>
 * The values are picked around how fast a player moves: walking covers a block in about 4.6 ticks and
 * sprinting in about 3.6. {@link #NORMAL} therefore matches a walk — keep moving and the tail never
 * catches you — {@link #FAST} outruns a sprint, and {@link #SLOW} is comfortable enough to stand on.
 *
 * @author Hugman
 * @since v4.0.0
 */
public enum SnakeSpeed implements StringRepresentable {
    SLOW("slow", 10),
    NORMAL("normal", 5),
    FAST("fast", 3);

    public static final Codec<SnakeSpeed> CODEC = StringRepresentable.fromEnum(SnakeSpeed::values);

    private final String name;
    private final int ticksPerBlock;

    SnakeSpeed(String name, int ticksPerBlock) {
        this.name = name;
        this.ticksPerBlock = ticksPerBlock;
    }

    /** Ticks the snake takes to move one block forward. */
    public int ticksPerBlock() {
        return this.ticksPerBlock;
    }

    /** How far the snake moves in one tick, as a fraction of a block. */
    public float stepPerTick() {
        return 1.0F / this.ticksPerBlock;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
