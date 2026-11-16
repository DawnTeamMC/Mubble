package fr.hugman.mubble.mixin;

import fr.hugman.mubble.world.entity.PickupKeeper;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    // Mobs only look at the pickup delay; players go through Player#touch, see PlayerMixin.
    @Inject(method = "hasPickUpDelay", at = @At("HEAD"), cancellable = true)
    private void mubble$keptByVehicle(CallbackInfoReturnable<Boolean> cir) {
        if (PickupKeeper.isKeptFrom((ItemEntity) (Object) this, null)) {
            cir.setReturnValue(true);
        }
    }
}
