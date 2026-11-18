package fr.hugman.mubble.platformer.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * While the orbit camera is in use, whatever turns the view (the mouse, the right stick of a
 * controller) turns the camera instead of the player. Only the rotation of the player is left alone,
 * rather than the whole turn, so that whatever else other mods do with a turn still happens.
 */
@Mixin(Entity.class)
public class PlatformerEntityTurnMixin {
    @Inject(method = "turn", at = @At("HEAD"))
    private void mubble$turnTheOrbit(double xo, double yo, CallbackInfo ci) {
        if (this.mubble$orbits()) {
            PlatformerCamera.turn(xo, yo);
        }
    }

    @WrapOperation(method = "turn", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setXRot(F)V"),
            @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setYRot(F)V"),
            @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;xRotO:F", opcode = Opcodes.PUTFIELD),
            @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;yRotO:F", opcode = Opcodes.PUTFIELD)
    })
    private void mubble$keepThePlayerRotation(Entity entity, float rotation, Operation<Void> original) {
        if (!this.mubble$orbits()) {
            original.call(entity, rotation);
        }
    }

    @Unique
    private boolean mubble$orbits() {
        return (Object) this == Minecraft.getInstance().player && PlatformerCamera.isOrbiting();
    }
}
