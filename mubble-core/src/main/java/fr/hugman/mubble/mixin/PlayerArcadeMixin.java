package fr.hugman.mubble.mixin;

import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import fr.hugman.mubble.world.arcade.ArcadeController;
import fr.hugman.mubble.world.arcade.ArcadeLocalDriver;
import fr.hugman.mubble.world.arcade.ArcadePlayer;
import fr.hugman.mubble.world.arcade.ArcadeVisual;
import fr.hugman.mubble.world.arcade.move.ArcadeMoves;
import fr.hugman.mubble.world.entity.ai.attributes.MubbleAttributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Plugs the arcade movement layer into the player. Every hook here starts by asking whether the layer
 * drives the movement, and does nothing at all when it does not.
 */
@Mixin(Player.class)
public abstract class PlayerArcadeMixin implements ArcadePlayer {
    @Unique
    private static final EntityDataAccessor<ArcadeVisual> ARCADE_VISUAL = SynchedEntityData.defineId(Player.class, MubbleEntityDataSerializers.ARCADE_VISUAL);

    @Unique
    private ArcadeController mubble$arcade;

    @Override
    public ArcadeController mubble$arcade() {
        if (this.mubble$arcade == null) {
            this.mubble$arcade = new ArcadeController((Player) (Object) this);
        }
        return this.mubble$arcade;
    }

    @Override
    public ArcadeVisual mubble$arcadeVisual() {
        return ((Player) (Object) this).getEntityData().get(ARCADE_VISUAL);
    }

    @Override
    public void mubble$setArcadeVisual(ArcadeVisual visual) {
        ((Player) (Object) this).getEntityData().set(ARCADE_VISUAL, visual);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void mubble$addArcadeAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        MubbleAttributes.ARCADE.forEach(attribute -> cir.getReturnValue().add(attribute));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void mubble$defineArcadeVisual(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(ARCADE_VISUAL, ArcadeVisual.NONE);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void mubble$tickArcade(CallbackInfo ci) {
        this.mubble$arcade().tick();
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void mubble$arcadeTravel(Vec3 input, CallbackInfo ci) {
        var player = (Player) (Object) this;
        var controller = this.mubble$arcade();
        var driver = ArcadeLocalDriver.Holder.instance;
        if (driver != null && controller.isDriving() && player.isLocalInstanceAuthoritative()) {
            driver.travel(player, controller);
            ci.cancel();
        }
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    private void mubble$arcadePose(CallbackInfo ci) {
        var controller = this.mubble$arcade();
        if (controller.isDriving()) {
            ((Player) (Object) this).setPose(controller.state().pose);
            ci.cancel();
        }
    }

    @Inject(method = "isStayingOnGroundSurface", at = @At("RETURN"), cancellable = true)
    private void mubble$arcadeEdgeGuard(CallbackInfoReturnable<Boolean> cir) {
        var controller = this.mubble$arcade();
        if (controller.isDriving()) {
            // the edge guard of sneaking only belongs to the crouch: a long jump or a roll must be able to leave the edge
            cir.setReturnValue(cir.getReturnValueZ() && controller.state().move == ArcadeMoves.CROUCH);
        }
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void mubble$arcadeNegatedFall(double fallDistance, float damageModifier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        var controller = this.mubble$arcade();
        if (controller.isDriving() && controller.state().negateFallDamage) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "causeFallDamage", at = @At("HEAD"), argsOnly = true)
    private double mubble$arcadeSafeFall(double fallDistance) {
        var controller = this.mubble$arcade();
        var profile = controller.profile();
        return controller.isDriving() && profile != null ? Math.max(0.0D, fallDistance - profile.fallDamage().safeDistanceBonus()) : fallDistance;
    }

    @ModifyVariable(method = "causeFallDamage", at = @At("HEAD"), argsOnly = true)
    private float mubble$arcadeFallMultiplier(float damageModifier) {
        var controller = this.mubble$arcade();
        var profile = controller.profile();
        return controller.isDriving() && profile != null ? (float) (damageModifier * profile.fallDamage().multiplier()) : damageModifier;
    }
}
