package fr.hugman.mubble.platformer.mixin;

import fr.hugman.mubble.platformer.PlatformerController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityPlatformerMixin {
    /** The platformer layer has jumps of its own, vanilla's would only get in their way. */
    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void mubble$noVanillaJump(CallbackInfo ci) {
        if ((Object) this instanceof Player player && PlatformerController.of(player).isDriving()) {
            ci.cancel();
        }
    }

    /** Nor vanilla's strokes in water: the platformer swim rises and sinks on its own, see {@link fr.hugman.mubble.platformer.move.SwimMove}. */
    @Inject(method = {"jumpInLiquid", "goDownInWater"}, at = @At("HEAD"), cancellable = true)
    private void mubble$noVanillaStrokes(CallbackInfo ci) {
        if ((Object) this instanceof Player player && PlatformerController.of(player).isDriving()) {
            ci.cancel();
        }
    }
}
