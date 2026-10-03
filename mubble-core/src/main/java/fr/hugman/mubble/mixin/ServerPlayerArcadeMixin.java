package fr.hugman.mubble.mixin;

import fr.hugman.mubble.world.arcade.ArcadeController;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerArcadeMixin {
    /**
     * The server calls this itself when a client leaves the ground going up, to count the jump and
     * charge for it. Under the arcade layer, the validated step already did both.
     */
    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void mubble$noVanillaJump(CallbackInfo ci) {
        if (ArcadeController.of((ServerPlayer) (Object) this).isDriving()) {
            ci.cancel();
        }
    }
}
