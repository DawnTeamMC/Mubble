package fr.hugman.mubble.arcade.mixin;

import fr.hugman.mubble.arcade.ArcadeController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityArcadeMixin {
    /** The arcade layer has jumps of its own, vanilla's would only get in their way. */
    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void mubble$noVanillaJump(CallbackInfo ci) {
        if ((Object) this instanceof Player player && ArcadeController.of(player).isDriving()) {
            ci.cancel();
        }
    }
}
