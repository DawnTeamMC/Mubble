package fr.hugman.mubble.super_mario.client.sound;

import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.projectile.KoopaShell;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SlidingKoopaShellSoundInstance extends KoopaShellSoundInstance {
    public SlidingKoopaShellSoundInstance(KoopaShell shell) {
        super(SuperMarioSounds.KOOPA_SHELL_SLIDE, shell);
    }
}
