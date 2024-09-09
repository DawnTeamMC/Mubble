package fr.hugman.mubble.mixin;

import fr.hugman.mubble.entity.GoombaEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Redirect(method = "setupTransforms", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;deathTime:I"))
    private int redirectDeathTime(LivingEntity entity) {
        if(entity instanceof GoombaEntity) {
            // this is required to play custom death animations
            // TODO: check for damage source
            return 0;
        }
        return entity.deathTime;
    }
}