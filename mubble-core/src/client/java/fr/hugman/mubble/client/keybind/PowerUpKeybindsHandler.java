package fr.hugman.mubble.client.keybind;

import fr.hugman.mubble.network.protocol.common.custom.PowerUpTriggerPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionResult;

public class PowerUpKeybindsHandler {
    public static void tick(Minecraft client) {
        if (null != client.player) {
            while (MubbleKeyBindings.TRIGGER_POWER_UP.consumeClick()) {
                trigger(client.player);
            }
        }
    }

    /** Triggers the power-up of {@code player}, if it has one that can be triggered: what the key does. */
    public static void trigger(LocalPlayer player) {
        var powerUpOpt = player.getPowerUp();
        if (powerUpOpt.isPresent()) {
            var powerUp = powerUpOpt.get().value();
            // it's great to check the power-up allows certain actions on the client first
            // to avoid unnecessary network traffic.
            // let's utilize Minecraft's registry sync to my advantage
            if (powerUp.canBeTriggered(player)) {
                if (powerUp.trigger(player) == InteractionResult.SUCCESS) {
                    ClientPlayNetworking.send(PowerUpTriggerPayload.INSTANCE);
                }
            }
        }
    }
}
