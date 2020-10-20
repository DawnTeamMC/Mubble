package com.hugman.mubble.mixin;

import com.hugman.dawn.mod.init.DawnEffects;
import com.hugman.mubble.init.data.MubbleTags;
import com.hugman.mubble.object.item.LightsaberItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
	private void mubble_tick(CallbackInfo info) {
		LivingEntity entity = (LivingEntity) (Object) this;
		World world = entity.getEntityWorld();
		ItemStack headItem = entity.getEquippedStack(EquipmentSlot.HEAD);
		if(!world.isClient) {
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(headItem.getItem())) {
				entity.addStatusEffect(new StatusEffectInstance(DawnEffects.HEAVINESS, 200, 0, false, false, true));
			}
		}
	}

	@Inject(method = "swingHand", at = @At(value = "TAIL"), cancellable = true)
	private void mubble_swingHand(Hand hand, CallbackInfo info) {
		LivingEntity entity = (LivingEntity) (Object) this;
		ItemStack stack = entity.getMainHandStack();
		if(stack.getItem() instanceof LightsaberItem) {
			((LightsaberItem) stack.getItem()).onSwing(entity, false);
		}
	}
}
