package fr.hugman.mubble.super_mario.client.keybind;

import fr.hugman.mubble.super_mario.network.protocol.common.custom.StruggleFreePayload;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * Lets a frozen player smash their way out of the ice a little sooner by hammering the movement
 * keys.
 * <p>
 * Only the presses themselves count, never the keys being held down: holding a direction is what a
 * player does anyway when they run into the ice ball that froze them.
 */
@Environment(EnvType.CLIENT)
public class FreezeStruggleHandler {
    public static void tick(Minecraft client) {
        var player = client.player;
        var options = client.options;
        if (player == null) {
            return;
        }

        int presses = consumeClicks(options.keyUp) + consumeClicks(options.keyDown)
                + consumeClicks(options.keyLeft) + consumeClicks(options.keyRight);
        // the keys are consumed either way: a press held over from before the freeze is not a struggle
        if (presses == 0 || !Freezing.isFrozen(player)) {
            return;
        }
        for (int i = 0; i < presses; i++) {
            ClientPlayNetworking.send(StruggleFreePayload.INSTANCE);
        }
    }

    private static int consumeClicks(KeyMapping key) {
        int presses = 0;
        while (key.consumeClick()) {
            presses++;
        }
        return presses;
    }
}
