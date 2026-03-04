package fr.hugman.mubble.mixin;

import fr.hugman.mubble.entity.BubbleEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Suppresses AI goals and movement for mob entities that are trapped inside a {@link BubbleEntity}.
 */
@Mixin(MobEntity.class)
public class MobEntityMixin {
	@Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
	private void mubble$cancelAiInBubble(CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.hasVehicle() && self.getVehicle() instanceof BubbleEntity) {
			ci.cancel();
		}
	}
}
