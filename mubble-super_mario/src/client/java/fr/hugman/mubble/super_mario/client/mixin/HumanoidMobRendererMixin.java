package fr.hugman.mubble.super_mario.client.mixin;

import fr.hugman.mubble.super_mario.world.entity.projectile.Bubble;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HumanoidMobRenderer.class)
public class HumanoidMobRendererMixin {
    /**
     * {@link net.minecraft.client.model.HumanoidModel} folds a humanoid's legs into a sitting pose as soon as it
     * rides anything. An entity floating inside a bubble should keep standing.
     */
    @Redirect(
            method = "extractHumanoidRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FLnet/minecraft/client/renderer/item/ItemModelResolver;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isPassenger()Z")
    )
    private static boolean super_mario$standUpInsideBubbles(LivingEntity entity) {
        return entity.isPassenger() && !(entity.getVehicle() instanceof Bubble);
    }
}
