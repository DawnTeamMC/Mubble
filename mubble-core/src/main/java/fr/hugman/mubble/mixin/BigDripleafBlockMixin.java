package fr.hugman.mubble.mixin;

import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.BigDripleafBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BigDripleafBlock.class)
public class BigDripleafBlockMixin {
    @Inject(method = "canEntityTilt", at = @At("HEAD"), cancellable = true)
    private static void mubble$stayLevel(BlockPos pos, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (PowerUpHolder.holdsTagged(entity, MubblePowerUpTags.LIGHT_FOOTED)) {
            cir.setReturnValue(false);
        }
    }
}
