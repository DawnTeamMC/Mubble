package fr.hugman.mubble.mixin.client;

import fr.hugman.mubble.client.sound.MovingKoopaShellSoundInstance;
import fr.hugman.mubble.entity.KoopaShellEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
@Environment(EnvType.CLIENT)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "playSpawnSound", at = @At("HEAD"))
    private void onPlaySpawnSound(Entity entity, CallbackInfo ci) {
        ClientCommonNetworkHandlerAccessor accessor = (ClientCommonNetworkHandlerAccessor) this;
        if (entity instanceof KoopaShellEntity koopaShell) {
            accessor.getClient().getSoundManager().play(new MovingKoopaShellSoundInstance(koopaShell));
        }
    }
}