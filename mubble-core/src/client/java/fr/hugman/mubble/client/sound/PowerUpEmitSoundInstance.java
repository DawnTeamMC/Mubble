package fr.hugman.mubble.client.sound;

import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class PowerUpEmitSoundInstance<E extends Entity & PowerUpHolder> extends AbstractTickableSoundInstance {
    private final E entity;

    public PowerUpEmitSoundInstance(E entity, SoundEvent event) {
        super(event, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.2F;
    }

    @Override
    public boolean canPlaySound() {
        return !this.entity.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        var currentSound = entity.getPowerUp().flatMap(powerUpHolder -> powerUpHolder.value().cosmectics().emitSound());
        if(currentSound.isEmpty()) {
            this.stop();
            return;
        }
        if(!currentSound.get().is(this.identifier)) {
            this.stop();
            return;
        }
        this.x = ((float) this.entity.getX());
        this.y = ((float) this.entity.getY());
        this.z = ((float) this.entity.getZ());
    }
}
