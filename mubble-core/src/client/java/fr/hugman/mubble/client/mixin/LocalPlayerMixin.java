package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.world.entity.Fluttering;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Where a client reads its own jump key.
 * <p>
 * The server is handed the key of every player through their input packets, but a client only ever has one
 * to read: the one under the keyboard in front of it. That is enough, since a client only ever simulates the
 * flutter of the player it controls.
 */
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin implements Fluttering {
    @Override
    public boolean isJumpKeyHeld() {
        return ((LocalPlayer) (Object) this).input.keyPresses.jump();
    }
}
