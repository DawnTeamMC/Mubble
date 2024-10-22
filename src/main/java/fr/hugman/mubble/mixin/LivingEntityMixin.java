package fr.hugman.mubble.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends EntityMixin {
    @Override
    public boolean canBeStomped() {
        return ((LivingEntity) (Object) this).getHealth() > 0.0f && super.canBeStomped();
    }
}
