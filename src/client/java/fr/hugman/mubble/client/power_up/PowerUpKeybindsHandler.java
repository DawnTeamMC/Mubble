package fr.hugman.mubble.client.power_up;

import fr.hugman.mubble.client.keybind.MubbleKeyBindings;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpTriggerPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

public class PowerUpKeybindsHandler {
    public static void tick(Minecraft client) {
        if (null != client.player) {
            var powerUpOpt = client.player.getPowerUp();
            if (powerUpOpt.isPresent()) {
                var powerUp = powerUpOpt.get().value();
                // it's great to check the power-up allows certain actions on the client first
                // to avoid unnecessary network traffic.
                // let's utilize Minecraft's registry sync to my advantage
                if(powerUp.canBeTriggered()) {
                    while(MubbleKeyBindings.TRIGGER_POWER_UP.consumeClick()) {
                        if(powerUp.trigger(client.player) == InteractionResult.SUCCESS) {
                            ClientPlayNetworking.send(PowerUpTriggerPayload.INSTANCE);
                        }
                    }
                }
            }
        }
    }
}
