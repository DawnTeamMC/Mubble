package fr.hugman.mubble.super_mario.mixin;

import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Takes away any say a frozen entity has over where it goes.
 *
 * @see Freezing
 */
@Mixin(LivingEntity.class)
public class FrozenLivingEntityMixin {
    /**
     * Cuts off the movement input and the AI of a frozen entity, the same way vanilla does for one
     * that is dying or asleep. It is what keeps frozen players from walking out of their own block of
     * ice: the check runs on the client that moves them just as much as on the server.
     */
    @Inject(method = "isImmobile", at = @At("HEAD"), cancellable = true)
    private void super_mario$immobileWhileFrozen(CallbackInfoReturnable<Boolean> cir) {
        if (Freezing.isFrozen((LivingEntity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Hands a frozen entity over to the ice physics, in place of the walking, swimming and flying it
     * would otherwise be doing.
     */
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void super_mario$travelWhileFrozen(Vec3 input, CallbackInfo ci) {
        LivingEntity this_ = (LivingEntity) (Object) this;
        if (Freezing.isFrozen(this_)) {
            Freezing.travelFrozen(this_);
            ci.cancel();
        }
    }
}
