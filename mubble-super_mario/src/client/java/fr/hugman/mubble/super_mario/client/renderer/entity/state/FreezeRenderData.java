package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;

/**
 * What the renderers need to know about the block of ice an entity is trapped in.
 *
 * @param frozenFor how long the entity has been frozen for, in ticks, interpolated within the tick.
 *                  Subtracting it from an age gives the very same value on every single frame, which
 *                  is what holds the animations of a frozen entity still.
 * @param iceCube   the ice block filling the entity hitbox, ready to be handed to the block renderer
 */
@Environment(EnvType.CLIENT)
public record FreezeRenderData(float frozenFor, MovingBlockRenderState iceCube) {
    /**
     * @return what to render around the entity, or {@code null} when it is not frozen
     */
    @Nullable
    public static FreezeRenderData of(Entity entity, float partialTicks) {
        var freeze = Freezing.getState(entity);
        if (freeze == null) {
            return null;
        }

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

        return new FreezeRenderData(freeze.elapsed(entity.level().getGameTime()) + partialTicks, iceCube);
    }
}
