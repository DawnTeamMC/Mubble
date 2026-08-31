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

    /**
     * Keeps the walk animation of the tick just gone within reach, and stops keeping it the moment the
     * ice takes hold — which leaves behind exactly what the entity was doing when it froze.
     * <p>
     * Trailing it a tick behind rather than reading it once the freeze lands is what makes the pose
     * right from the very first frame: the freeze reaches a client between two ticks, and by the head
     * of the next one the walk animation has already started running itself down. Both sides keep
     * their own reading, since the position is a running total that server and client drift apart on
     * over an entity's life, and handing one over to the other would snap the limbs somewhere else at
     * the moment the entity froze.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void super_mario$rememberThePose(CallbackInfo ci) {
        LivingEntity this_ = (LivingEntity) (Object) this;
        if (Freezing.isFrozen(this_)) {
            return;
        }
        this.super_mario$frozenWalkPos = this_.walkAnimation.position();
        this.super_mario$frozenWalkSpeed = this_.walkAnimation.speed();
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

    /**
     * Hands the hit to the block of ice, which stands in for whoever is inside it.
     * <p>
     * It runs ahead of the invulnerability the shield rests on, so that fire has already melted the
     * ice by the time that check is reached and reaches what was inside it after all.
     */
    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void super_mario$shieldWhileFrozen(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Freezing.absorb(level, (LivingEntity) (Object) this, source, amount);
    }

    /**
     * Turns away whatever the ice took on the entity's behalf.
     */
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
