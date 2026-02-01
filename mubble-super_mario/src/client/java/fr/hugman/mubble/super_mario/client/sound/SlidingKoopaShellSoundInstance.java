package fr.hugman.mubble.super_mario.client.sound;

import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.projectile.KoopaShell;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class SlidingKoopaShellSoundInstance extends AbstractTickableSoundInstance {
    private final KoopaShell shell;
    private float distance = 0.0F;

    public SlidingKoopaShellSoundInstance(KoopaShell shell) {
        super(SuperMarioSounds.KOOPA_SHELL_SLIDE, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.shell = shell;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.3F;
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
        } else {
            this.x = ((float) this.shell.getX());
            this.y = ((float) this.shell.getY());
            this.z = ((float) this.shell.getZ());
            float f = (float) this.shell.getDeltaMovement().horizontalDistance();
            if (f >= 0.01F && this.shell.level().tickRateManager().runsNormally()) {
                this.distance = Mth.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
                this.volume = Mth.lerp(Mth.clamp(f, 0.0F, 0.5F), 0.0F, 0.7F);
            } else {
                this.distance = 0.0F;
                this.volume = 0.0F;
            }
        }
    }
}
