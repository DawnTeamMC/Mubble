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
    SLOW("slow", 10, 0.8F),
    NORMAL("normal", 5, 1.0F),
    FAST("fast", 3, 1.25F);

    public static final Codec<SnakeSpeed> CODEC = StringRepresentable.fromEnum(SnakeSpeed::values);

    private final String name;
    private final int ticksPerBlock;
    private final float pitch;

    SnakeSpeed(String name, int ticksPerBlock, float pitch) {
        this.name = name;
        this.ticksPerBlock = ticksPerBlock;
        this.pitch = pitch;
    }

    /** Ticks the snake takes to move one block forward. */
    public int ticksPerBlock() {
        return this.ticksPerBlock;
    }

    /**
     * What the running loop is played back at, so that one recording covers all three variants.
     * <p>
     * Deliberately gentler than the speeds themselves: a fast snake covers ground twice as quickly
     * as a slow one, and pitching the loop by that much would drop the slow one an octave and leave
     * it sounding like a different machine. These are about four semitones either side of the
     * recording, enough to hear which variant is coming without it stopping being the same sound.
     */
    public float pitch() {
        return this.pitch;
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
