package fr.hugman.mubble.client.network;

import fr.hugman.mubble.client.sound.PowerUpEmitSoundInstance;
import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpChangePayload;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class MubbleClientReceivers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.POWER_UP_CHANGE, ((payload, context) -> context.client().execute(() -> changePowerUp(payload, context))));
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.COLLECT_COLLECTIBLE, CollectCollectiblePayloadReceiver.INSTANCE);
    }

    private static void changePowerUp(PowerUpChangePayload payload, ClientPlayNetworking.Context context) {
        PowerUp.onChange(context.player(), payload.previous(), payload.next());
        var newEmit =  payload.next().flatMap(powerUpHolder -> powerUpHolder.value().cosmectics().emitSound());
        if(newEmit.isEmpty()) {
            // no new sound
            return;
        }
        var oldEmit = payload.previous().flatMap(powerUpHolder -> powerUpHolder.value().cosmectics().emitSound());
        if(oldEmit.isPresent() && oldEmit.get().is(newEmit.get().value().location())) {
            // same sound, don't cancel
            return;
        }
        Minecraft.getInstance().getSoundManager().play(new PowerUpEmitSoundInstance<>(context.player(), newEmit.get().value()));
    }
}
