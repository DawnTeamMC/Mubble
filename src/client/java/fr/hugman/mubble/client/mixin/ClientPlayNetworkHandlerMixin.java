package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.sound.HomingKoopaShellSoundInstance;
import fr.hugman.mubble.client.sound.SlidingKoopaShellSoundInstance;
import fr.hugman.mubble.entity.KoopaShellEntity;
import fr.hugman.mubble.entity.RedKoopaShellEntity;
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
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "postAddEntitySoundInstance", at = @At("HEAD"))
    private void onPlaySpawnSound(Entity entity, CallbackInfo ci) {
        ClientCommonNetworkHandlerAccessor accessor = (ClientCommonNetworkHandlerAccessor) this;
        if (entity instanceof RedKoopaShellEntity koopaShell) {
            accessor.getClient().getSoundManager().play(new HomingKoopaShellSoundInstance(koopaShell));
        }
        else if (entity instanceof KoopaShellEntity koopaShell) {
            accessor.getClient().getSoundManager().play(new SlidingKoopaShellSoundInstance(koopaShell));
        }
    }
}