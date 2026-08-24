package fr.hugman.mubble.super_mario.mixin;

import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Turns whatever is trapped in a block of ice into the block of ice it looks like.
 *
 * @see Freezing
 */
@Mixin(Entity.class)
public class FrozenEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void super_mario$tickFreeze(CallbackInfo ci) {
        Freezing.tick((Entity) (Object) this);
    }

    /**
     * Makes a frozen entity as solid as the ice around it, so that others can walk into it and stand
     * on top of it.
     */
    @Inject(method = "canBeCollidedWith", at = @At("HEAD"), cancellable = true)
    private void super_mario$collideWhileFrozen(@Nullable Entity other, CallbackInfoReturnable<Boolean> cir) {
        if (Freezing.isFrozen((Entity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }
}
