package fr.hugman.mubble.mixin;

import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onRemoval", at = @At("HEAD"))
    private void mubble$onRemove(CallbackInfo ci) {
        Entity this_ = (Entity) (Object) this;
        if (this_ instanceof TraceableEntity ownable && ownable.getOwner() instanceof PowerUpHolder powerUpHolder) {
            // if the projectile isn't in the properties it won't set dirty so it's okay to not check for it
            var properties = powerUpHolder.getPowerUpProperties();
            if (properties != null) {
                properties.removeEntity(this_.getUUID());
            }
        }
    }
}