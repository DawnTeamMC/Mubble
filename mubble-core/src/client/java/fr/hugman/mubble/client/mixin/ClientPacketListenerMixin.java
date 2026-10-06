package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.sound.PowerUpEmitSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "postAddEntitySoundInstance", at = @At("TAIL"))
    private void mubble$postAddEntitySoundInstance(Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            var newEmit = player.getPowerUp().flatMap(holder -> holder.value().cosmectics().emitSound());
            newEmit.ifPresent(soundEventHolder -> Minecraft.getInstance().getSoundManager().play(new PowerUpEmitSoundInstance<>(player, soundEventHolder.value())));
        }
    }
}