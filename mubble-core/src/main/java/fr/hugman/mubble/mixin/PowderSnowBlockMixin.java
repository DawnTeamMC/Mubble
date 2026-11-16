package fr.hugman.mubble.mixin;

import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void mubble$walkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && PowerUpHolder.holdsTagged(entity, MubblePowerUpTags.LIGHT_FOOTED)) {
            cir.setReturnValue(true);
        }
    }
}
