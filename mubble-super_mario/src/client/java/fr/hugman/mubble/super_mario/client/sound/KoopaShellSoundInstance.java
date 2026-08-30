package fr.hugman.mubble.super_mario.client.sound;

import fr.hugman.mubble.super_mario.world.entity.projectile.KoopaShell;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

/**
 * Looping sound that follows a koopa shell around, fading in with its horizontal speed.
 *
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public abstract class KoopaShellSoundInstance extends AbstractTickableSoundInstance {
    /** Horizontal speed under which the shell is quiet, in blocks per tick. */
    private static final float MIN_SPEED = 0.01F;
    /** Volume the sound reaches once the shell slides at its cruising speed. */
    private static final float MAX_VOLUME = 0.7F;

    private final KoopaShell shell;

    protected KoopaShellSoundInstance(SoundEvent sound, KoopaShell shell) {
        super(sound, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.shell = shell;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public boolean canPlaySound() {
        return !this.shell.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.shell.isRemoved()) {
            this.stop();
            return;
        }

        this.x = (float) this.shell.getX();
        this.y = (float) this.shell.getY();
        this.z = (float) this.shell.getZ();

        float speed = (float) this.shell.getDeltaMovement().horizontalDistance();
        if (speed < MIN_SPEED || !this.shell.level().tickRateManager().runsNormally()) {
            this.volume = 0.0F;
            return;
        }
        this.volume = Mth.lerp(Mth.clamp(speed / KoopaShell.TARGET_SPEED, 0.0F, 1.0F), 0.0F, MAX_VOLUME);
    }
}
