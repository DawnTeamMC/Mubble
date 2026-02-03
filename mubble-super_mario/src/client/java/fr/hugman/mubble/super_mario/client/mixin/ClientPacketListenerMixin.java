package fr.hugman.mubble.super_mario.client.mixin;

import fr.hugman.mubble.super_mario.client.sound.HomingKoopaShellSoundInstance;
import fr.hugman.mubble.super_mario.client.sound.SlidingKoopaShellSoundInstance;
import fr.hugman.mubble.super_mario.world.entity.projectile.KoopaShell;
import fr.hugman.mubble.super_mario.world.entity.projectile.RedKoopaShell;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
@Environment(EnvType.CLIENT)
public class ClientPacketListenerMixin {
    @Inject(method = "postAddEntitySoundInstance", at = @At("HEAD"))
    private void onPlaySpawnSound(Entity entity, CallbackInfo ci) {
        ClientCommonPacketListenerImplAccessor accessor = (ClientCommonPacketListenerImplAccessor) this;
        if (entity instanceof RedKoopaShell koopaShell) {
            accessor.getClient().getSoundManager().play(new HomingKoopaShellSoundInstance(koopaShell));
        }
        else if (entity instanceof KoopaShell koopaShell) {
            accessor.getClient().getSoundManager().play(new SlidingKoopaShellSoundInstance(koopaShell));
        }
    }
}