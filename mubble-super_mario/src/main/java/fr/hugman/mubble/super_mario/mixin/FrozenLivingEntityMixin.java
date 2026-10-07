package fr.hugman.mubble.super_mario.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeSnapshot;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Everything a block of ice changes about a living entity: the say it has over where it goes, the
 * pose it holds while it is in there, what reaches it through the ice — and the footing it gives
 * whoever climbs on top of it.
 *
 * @see Freezing
 */
@Mixin(LivingEntity.class)
public class FrozenLivingEntityMixin implements FreezeSnapshot {
    @Unique
    private float super_mario$frozenWalkPos;
    @Unique
    private float super_mario$frozenWalkSpeed;

    @Inject(method = "tick", at = @At("HEAD"))
    private void super_mario$rememberThePose(CallbackInfo ci) {
        LivingEntity this_ = (LivingEntity) (Object) this;
        if (Freezing.isFrozen(this_)) {
            return;
        }
        this.super_mario$frozenWalkPos = this_.walkAnimation.position();
        this.super_mario$frozenWalkSpeed = this_.walkAnimation.speed();
    }

    /**
     * Hands the ground the ice covered this tick on to whoever is riding it. It sits at the end of
     * {@code LivingEntity}'s tick rather than {@code Entity}'s, which runs before the {@code aiStep}
     * that does the moving and would hand on a tick that had not happened yet.
     */
    @Inject(method = "tick", at = @At("RETURN"))
    private void super_mario$carryRiders(CallbackInfo ci) {
        Freezing.carryRiders((LivingEntity) (Object) this);
    }

    /** Sends whoever jumps off a block of ice on the way it was already going. */
    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void super_mario$jumpOffTheIce(CallbackInfo ci) {
        Freezing.jumpOffFrozen((LivingEntity) (Object) this);
    }

    @Override
    public float frozenWalkPos() {
        return this.super_mario$frozenWalkPos;
    }

    @Override
    public float frozenWalkSpeed() {
        return this.super_mario$frozenWalkSpeed;
    }

    /**
     * Keeps a block of ice out of the shouldering match entities have when they overlap, which would
     * otherwise throw a rider off the very thing carrying it. On a client that shove picks out the
     * local player alone, so it is players it lands on, and their own client makes it stick.
     */
    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void super_mario$notJostledWhileFrozen(CallbackInfoReturnable<Boolean> cir) {
        if (Freezing.isFrozen((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    /**
     * And the other way about: a block of ice shoulders nobody aside either.
     *
     * @see #super_mario$notJostledWhileFrozen
     */
    @Inject(method = "pushEntities", at = @At("HEAD"), cancellable = true)
    private void super_mario$shoulderNobodyWhileFrozen(CallbackInfo ci) {
        if (Freezing.isFrozen((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }

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

    /**
     * Keeps a punch from lifting a block of ice off the floor: it is sent skidding along it instead.
     * <p>
     * The lift is the one thing vanilla only adds to a knockback when the target is standing on
     * something, so telling it the ice is mid-air leaves the horizontal shove untouched and the
     * vertical speed exactly as it was.
     */
    @ModifyExpressionValue(
            method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z"))
    private boolean super_mario$noLiftWhileFrozen(boolean onGround) {
        return onGround && !Freezing.isFrozen((LivingEntity) (Object) this);
    }

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void super_mario$shieldWhileFrozen(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Freezing.absorb(level, (LivingEntity) (Object) this, source, amount);
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void super_mario$shieldedWhileFrozen(ServerLevel level, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (Freezing.shields((LivingEntity) (Object) this, source)) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Gives whoever climbs on top of a block of ice the footing of one.
     * <p>
     * Friction is a property of the block underfoot, and there is no block underfoot here: standing on
     * an entity leaves vanilla reading the air below it and handing out ordinary ground. Reading the
     * ice off the entity instead is what makes the top of a frozen mob as slippery as it looks.
     */
    @ModifyExpressionValue(
            method = "travelInAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float super_mario$slipperyOnTopOfIce(float friction) {
        return Freezing.isStandingOnFrozen((LivingEntity) (Object) this) ? Blocks.ICE.getFriction() : friction;
    }
}
