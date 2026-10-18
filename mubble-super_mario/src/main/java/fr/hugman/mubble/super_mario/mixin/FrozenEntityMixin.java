package fr.hugman.mubble.super_mario.mixin;

import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.minecraft.sounds.SoundEvent;
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

    /**
     * Muffles a frozen entity, whether it is growling, hurting or walking into a wall.
     * <p>
     * It is caught in the one method every sound an entity makes of its own accord goes through,
     * rather than in {@code isSilent()} right below it: that flag is written back out when the entity
     * is saved, and a mob that happened to be frozen at the time would come back mute for good.
     */
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    private void super_mario$muteWhileFrozen(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        if (Freezing.isFrozen((Entity) (Object) this)) {
            ci.cancel();
        }
    }

    /**
     * Keeps a frozen entity from catching fire. Putting one out is left to
     * {@link Freezing#freezeFor}, which does it the moment the ice takes hold.
     */
    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    private void super_mario$stayUnburntWhileFrozen(int ticks, CallbackInfo ci) {
        if (ticks > 0 && Freezing.isFrozen((Entity) (Object) this)) {
            ci.cancel();
        }
    }
}
