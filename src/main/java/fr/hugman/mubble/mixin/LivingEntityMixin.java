package fr.hugman.mubble.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travelMidAir(Lnet/minecraft/util/math/Vec3d;)V", shift = At.Shift.AFTER))
    private void mubble$float(Vec3d movementInput, CallbackInfo ci) {
        var this_ = (LivingEntity) (Object) this;
        var velocity = this_.getVelocity();
        var d = velocity.y;
        d += (0.05 * 1 - velocity.y) * 0.2;
        //this_.setVelocity(velocity.x, d, velocity.z);
    }
}
