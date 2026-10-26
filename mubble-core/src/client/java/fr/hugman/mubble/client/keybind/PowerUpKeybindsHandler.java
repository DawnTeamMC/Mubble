package fr.hugman.mubble.client.keybind;

import fr.hugman.mubble.network.protocol.common.custom.PowerUpTriggerPayload;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionResult;

public class PowerUpKeybindsHandler {
    /**
     * Called on the client right before the power-up of the player is triggered, and the server told
     * so: what turns the player first does it here, the server shooting where they then look.
     */
    public static final Event<BeforeTrigger> BEFORE_TRIGGER = EventFactory.createArrayBacked(BeforeTrigger.class, callbacks -> (player, powerUp) -> {
        for (var callback : callbacks) {
            callback.beforeTrigger(player, powerUp);
        }
    });

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
                BEFORE_TRIGGER.invoker().beforeTrigger(player, powerUp);
                if (powerUp.trigger(player) == InteractionResult.SUCCESS) {
                    ClientPlayNetworking.send(PowerUpTriggerPayload.INSTANCE);
                }
            }
        }
    }

    @FunctionalInterface
    public interface BeforeTrigger {
        void beforeTrigger(LocalPlayer player, PowerUp powerUp);
    }
}
