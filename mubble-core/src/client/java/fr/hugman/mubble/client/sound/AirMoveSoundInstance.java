package fr.hugman.mubble.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

/**
 * The loop a mid-air move is heard as, for as long as it lasts.
 * <p>
 * It follows the player rather than the move: what a flutter turning into a float should sound like is one
 * loop giving way to another, so the instance stops as soon as the sound the player is owed is no longer
 * its own, and {@link AirMoveSounds} starts whichever one has taken over.
 */
@Environment(EnvType.CLIENT)
public class AirMoveSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;
    private final Holder<SoundEvent> event;

    public AirMoveSoundInstance(Player player, Holder<SoundEvent> event) {
        super(event.value(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.event = event;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.2F;
    }

    /** The loop this instance is playing, so that the handler can tell it apart from the one now owed. */
    public Holder<SoundEvent> event() {
        return this.event;
    }

    @Override
    public boolean canPlaySound() {
        return !this.player.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        var wanted = AirMoveSounds.soundFor(this.player);
        if (this.player.isRemoved() || wanted.isEmpty() || !wanted.get().equals(this.event)) {
            this.stop();
            return;
        }
        this.x = (float) this.player.getX();
        this.y = (float) this.player.getY();
        this.z = (float) this.player.getZ();
    }
}
