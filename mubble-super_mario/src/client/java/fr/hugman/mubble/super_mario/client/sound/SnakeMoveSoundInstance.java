package fr.hugman.mubble.super_mario.client.sound;

import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.level.block.SnakeSpeed;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

/**
 * The one rumble a running snake makes, following its head the way a minecart sound follows the
 * cart, rather than one sound per block of it.
 * <p>
 * Pitched by {@link SnakeSpeed}, so the slow, normal and fast variants are the same recording
 * played at three speeds rather than three recordings that have to be made to sit together.
 *
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class SnakeMoveSoundInstance extends AbstractTickableSoundInstance {
    /**
     * How long the sound carries on without hearing from the snake before it gives up.
     * <p>
     * The head reports in from the renderer, which runs per frame rather than per tick, so a couple
     * of quiet ticks mean nothing: a chunk being rebuilt or a frame being dropped should not cut a
     * snake off mid-run.
     */
    private static final int GRACE_TICKS = 10;

    private int silentTicks;

    SnakeMoveSoundInstance(SnakeSpeed speed, double x, double y, double z) {
        super(SuperMarioSounds.SNAKE_BLOCK_MOVE.value(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
        // One recording covers all three variants, played faster or slower to match the one running.
        this.pitch = speed.pitch();
        this.moveTo(x, y, z);
    }

    void moveTo(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.silentTicks = 0;
    }

    /** {@link #stop} is final and protected; this lets the manager silence a leftover sound. */
    void silence() {
        this.stop();
    }

    @Override
    public void tick() {
        if (++this.silentTicks > GRACE_TICKS) {
            this.stop();
        }
    }
}
