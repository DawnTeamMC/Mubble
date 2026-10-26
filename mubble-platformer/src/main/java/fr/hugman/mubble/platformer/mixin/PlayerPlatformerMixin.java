package fr.hugman.mubble.platformer.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.hugman.mubble.platformer.network.PlatformerEntityDataSerializers;
import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerLocalDriver;
import fr.hugman.mubble.platformer.PlatformerPlayer;
import fr.hugman.mubble.platformer.PlatformerVisual;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.PlatformerAttributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Plugs the platformer movement layer into the player. Every hook here starts by asking whether the layer
 * drives the movement, and does nothing at all when it does not.
 */
@Mixin(Player.class)
public abstract class PlayerPlatformerMixin implements PlatformerPlayer {
    @Unique
    private static final EntityDataAccessor<PlatformerVisual> PLATFORMER_VISUAL = SynchedEntityData.defineId(Player.class, PlatformerEntityDataSerializers.PLATFORMER_VISUAL);

    @Unique
    private PlatformerController mubble$platformer;

    @Override
    public PlatformerController mubble$platformer() {
        if (this.mubble$platformer == null) {
            this.mubble$platformer = new PlatformerController((Player) (Object) this);
        }
        return this.mubble$platformer;
    }

    @Override
    public PlatformerVisual mubble$platformerVisual() {
        return ((Player) (Object) this).getEntityData().get(PLATFORMER_VISUAL);
    }

    @Override
    public void mubble$setPlatformerVisual(PlatformerVisual visual) {
        ((Player) (Object) this).getEntityData().set(PLATFORMER_VISUAL, visual);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void mubble$addPlatformerAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        PlatformerAttributes.PLATFORMER.forEach(attribute -> cir.getReturnValue().add(attribute));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void mubble$definePlatformerVisual(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(PLATFORMER_VISUAL, PlatformerVisual.NONE);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void mubble$tickPlatformer(CallbackInfo ci) {
        this.mubble$platformer().tick();
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void mubble$platformerTravel(Vec3 input, CallbackInfo ci) {
        var player = (Player) (Object) this;
        var controller = this.mubble$platformer();
        var driver = PlatformerLocalDriver.Holder.instance;
        if (driver != null && controller.isDriving() && player.isLocalInstanceAuthoritative()) {
            driver.travel(player, controller);
            ci.cancel();
        }
    }

    @WrapOperation(method = "updatePlayerPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPose(Lnet/minecraft/world/entity/Pose;)V"))
    private void mubble$platformerPose(Player player, Pose pose, Operation<Void> original) {
        var controller = this.mubble$platformer();
        // the move decides the pose, rather than what the player does in vanilla terms (swimming, sneaking...)
        original.call(player, controller.isDriving() ? controller.state().pose : pose);
    }

    @ModifyReturnValue(method = "isStayingOnGroundSurface", at = @At("RETURN"))
    private boolean mubble$platformerEdgeGuard(boolean staying) {
        var controller = this.mubble$platformer();
        // the edge guard of sneaking only belongs to the crouch: a long jump or a roll must be able to leave the edge
        return controller.isDriving() ? staying && controller.state().move == PlatformerMoves.CROUCH : staying;
    }
}
