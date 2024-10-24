package fr.hugman.mubble.client.power_up;

import fr.hugman.mubble.client.keybind.MubbleKeyBindings;
import fr.hugman.mubble.network.payload.c2s.PowerUpJumpTriggerPayload;
import fr.hugman.mubble.network.payload.c2s.PowerUpTriggerPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class PowerUpKeybindsHandler {
    public static void tick(MinecraftClient client) {
        if (null != client.player) {
            var powerUpOpt = client.player.getPowerUp();
            if (powerUpOpt.isPresent()) {
                var powerUp = powerUpOpt.get().value();
                // it's great to check the power-up allows certain actions on the client first
                // to avoid unnecessary network traffic.
                // let's utilize Minecraft's registry sync to my advantage
                if (powerUp.canBeTriggered() && MubbleKeyBindings.TRIGGER_POWER_UP.isPressed()) {
                    //TODO: check for cooldown
                    ClientPlayNetworking.send(PowerUpTriggerPayload.INSTANCE);
                }
                if (client.options.jumpKey.isPressed() && powerUp.canBeTriggeredByAirborneJump() && !client.player.isOnGround()) {
                    ClientPlayNetworking.send(PowerUpJumpTriggerPayload.INSTANCE);

                }
            }
        }
    }
}
