package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * What the renderers need to know about the block of ice an entity is trapped in.
 *
 * @param frozenFor how long the entity has been frozen for, in ticks, interpolated within the tick.
 *                  Subtracting it from an age gives the very same value on every single frame, which
 *                  is what holds the animations of a frozen entity still.
 * @param rattle    how far the block of ice is off its resting place this frame, which is nothing at
 *                  all until it is nearly out of time
 * @param iceCube   the ice block filling the entity hitbox, ready to be handed to the block renderer
 */
@Environment(EnvType.CLIENT)
public record FreezeRenderData(float frozenFor, Vec3 rattle, MovingBlockRenderState iceCube) {
    /** How far the ice throws itself around, in blocks, by the time it is about to give. */
    private static final double RATTLE_AMPLITUDE = 0.06D;
    /** How fast it does so, in radians per tick. Fast enough to read as a shudder rather than a sway. */
    private static final float RATTLE_FREQUENCY = 2.7F;
    /** The two axes are run at different rates so that the shudder never settles into a straight line. */
    private static final float RATTLE_CROSS_FREQUENCY = 3.9F;

    /**
     * @return what to render around the entity, or {@code null} when it is not frozen
     */
    @Nullable
    public static FreezeRenderData of(Entity entity, float partialTicks) {
        var freeze = Freezing.getState(entity);
        if (freeze == null) {
            return null;
        }
        long gameTime = entity.level().getGameTime();

        var iceCube = new MovingBlockRenderState();
        var pos = entity.blockPosition();
        iceCube.randomSeedPos = pos;
        iceCube.blockPos = pos;
        iceCube.blockState = Blocks.ICE.defaultBlockState();
        if (entity.level() instanceof ClientLevel level) {
            iceCube.biome = level.getBiome(pos);
            iceCube.cardinalLighting = level.cardinalLighting();
            iceCube.lightEngine = level.getLightEngine();
        }

        float frozenFor = freeze.elapsed(gameTime) + partialTicks;
        return new FreezeRenderData(frozenFor, rattleOf(freeze.remaining(gameTime) - partialTicks, frozenFor), iceCube);
    }

    /**
     * Works out how hard the ice is shaking, which is the only warning anyone gets that it is about to
     * let go.
     *
     * @param remaining how much of the freeze is left, in ticks, interpolated within the tick
     * @param frozenFor how long the freeze has run for, in ticks, interpolated within the tick. It is
     *                  what the shudder is driven off, so that it keeps going rather than restarting
     *                  every frame.
     */
    private static Vec3 rattleOf(float remaining, float frozenFor) {
        if (remaining >= Freezing.RATTLE_DURATION) {
            return Vec3.ZERO;
        }
        // it starts as a barely-there tremor and works itself up to the moment the ice gives
        double amplitude = RATTLE_AMPLITUDE * (1.0D - Math.max(remaining, 0.0F) / Freezing.RATTLE_DURATION);
        return new Vec3(
                Mth.sin(frozenFor * RATTLE_FREQUENCY) * amplitude,
                0.0D,
                Mth.sin(frozenFor * RATTLE_CROSS_FREQUENCY) * amplitude);
    }
}
