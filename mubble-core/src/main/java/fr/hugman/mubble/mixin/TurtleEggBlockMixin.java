package fr.hugman.mubble.mixin;

import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.TurtleEggBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {
    @Inject(method = "canDestroyEgg", at = @At("HEAD"), cancellable = true)
    private void mubble$spareEggs(ServerLevel level, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (PowerUpHolder.holdsTagged(entity, MubblePowerUpTags.LIGHT_FOOTED)) {
            cir.setReturnValue(false);
        }
    }
}
