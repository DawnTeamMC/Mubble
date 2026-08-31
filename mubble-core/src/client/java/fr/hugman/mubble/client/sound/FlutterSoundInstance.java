package fr.hugman.mubble.client.sound;

import fr.hugman.mubble.world.entity.Fluttering;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

/**
 * The loop a flutter is heard as, for as long as it lasts.
 */
@Environment(EnvType.CLIENT)
public class FlutterSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;

    public FlutterSoundInstance(Player player, SoundEvent event) {
        super(event, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.2F;
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
        if (this.player.isRemoved() || !((Fluttering) this.player).isFluttering()) {
            this.stop();
            return;
        }
        this.x = (float) this.player.getX();
        this.y = (float) this.player.getY();
        this.z = (float) this.player.getZ();
    }
}
