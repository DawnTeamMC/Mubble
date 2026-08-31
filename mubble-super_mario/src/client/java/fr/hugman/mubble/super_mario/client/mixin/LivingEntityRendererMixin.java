package fr.hugman.mubble.super_mario.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.GoombaRenderState;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.world.entity.projectile.Bubble;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Shadow
    public Identifier getTextureLocation(final S state) {
        return null;
    }

    @Redirect(method = "setupRotations", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;deathTime:F"))
    private float super_mario$blockDeathAnimation(LivingEntityRenderState state) {
        if (state instanceof GoombaRenderState goombaState && goombaState.stomped) {
            return 0;
        }
        return state.deathTime;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void super_mario$extractBubbleRide(T entity, S state, float partialTicks, CallbackInfo ci) {
        // Render states are pooled, so the key has to be cleared for entities that are not in a bubble.
        if (!(entity.getVehicle() instanceof Bubble bubble)) {
            state.setData(SuperMarioRenderStateDataKeys.BUBBLE_RIDE, null);
            return;
        }
        float ticks = entity.tickCount + partialTicks;
        // Every entity gets its own phase, so two things caught at once do not tumble in lockstep.
        float seed = entity.getId() * 0.7F;
        float absorb = bubble.getAbsorbProgress(partialTicks);
        float settle = bubble.getSettleProgress(partialTicks);
        float spin = 1.0F - settle;
        // Being swallowed spins it up on top of the idle tumble. Ramped from zero so nothing snaps.
        float whirl = absorb * absorb * 7.0F;

        // The bubble's heading when it caught this leans the tumble: something scooped up at speed rolls end
        // over end along the way the bubble was going, and rolls faster the harder it was hit.
        Vector3fc caught = bubble.getCaptureMotion();
        float heading = Mth.sqrt(caught.x() * caught.x() + caught.z() * caught.z());
        float rate = 0.06F + heading * 0.35F;
        // The roll is rounded to a whole number of turns. Landing on one at the end means the final rotation,
        // by then around the upright axis, is the identity: the captive is drawn exactly where it will be
        // standing once the bubble lets go, so nothing jumps at the moment it pops.
        int turns = Math.max(1, Math.round(rate * bubble.getFilledLifetime() * 0.5F / Mth.TWO_PI));
        // Eases in with a rate falling linearly to nothing, so it keeps advancing the whole way instead of
        // turning at full speed and then stopping dead. It never runs backwards.
        float rolled = turns * Mth.TWO_PI * (2.0F * settle - settle * settle) + whirl;

        // The axis the roll happens around leans from horizontal to vertical as the captive settles, so the
        // tumble turns into a plain spin on the spot and leaves it standing upright. Unwinding the roll
        // instead would have to pick a direction to unwind in, and that choice flips once the roll passes
        // half a turn, which reads as a snap. rotateAxis normalises, so the axis only has to point right.
        float leanX = 0.0F;
        float leanZ = 0.0F;
        if (heading > 1.0E-4F) {
            leanX = -caught.z() / heading * spin;
            leanZ = caught.x() / heading * spin;
        } else {
            leanX = spin;
        }

        // No yaw of its own: any leftover turn would have to be unwound at the end, and the whole point is
        // that the last frame of the tumble already matches the orientation the entity keeps after the pop.
        Quaternionf rotation = new Quaternionf().rotateAxis(rolled, leanX, settle, leanZ);
        // Drifts at rates that share no common period, so the tumble never settles into a visible loop. They
        // fade out as it comes to rest.
        rotation.rotateX(Mth.sin(ticks * 0.023F + seed * 1.7F) * 0.9F * spin);
        rotation.rotateZ(Mth.cos(ticks * 0.019F + seed * 0.6F) * 0.6F * spin);

        state.setData(SuperMarioRenderStateDataKeys.BUBBLE_RIDE,
                new SuperMarioRenderStateDataKeys.BubbleRide(rotation, 1.0F - absorb));
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    private void super_mario$bubbleRideRotations(S state, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci) {
        var ride = state.getData(SuperMarioRenderStateDataKeys.BUBBLE_RIDE);
        if (ride == null) {
            return;
        }
        // Tumble and shrink around the middle of the entity rather than around its feet.
        float centerY = state.boundingBoxHeight / 2.0F;
        poseStack.translate(0.0F, centerY, 0.0F);
        poseStack.mulPose(ride.rotation());
        poseStack.scale(ride.scale(), ride.scale(), ride.scale());
        poseStack.translate(0.0F, -centerY, 0.0F);
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void super_mario$getRenderType(S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing, CallbackInfoReturnable<RenderType> cir) {
        var powerUp = state.getData(fr.hugman.mubble.client.references.MubbleRenderStateDataKeys.POWER_UP);
        //TODO: make this more dynamic
        if(powerUp != null && powerUp.is(SuperMarioPowerUpIds.GOLD)) {
            Identifier texture = this.getTextureLocation(state);
            //TODO: do the vanillas checks and add translucency support
            cir.setReturnValue(SuperMarioRenderTypes.getGoldenEntity(texture));
        }
    }
}