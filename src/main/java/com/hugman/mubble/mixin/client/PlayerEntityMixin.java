package com.hugman.mubble.mixin.client;

import com.hugman.mubble.object.item.LightsaberItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Shadow
	private ItemStack selectedItem;

	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	private void mubble_tick(CallbackInfo info) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		World world = player.getEntityWorld();
		ItemStack mainHandStack = player.getMainHandStack();
		ItemStack headStack = player.getEquippedStack(EquipmentSlot.HEAD);
		if(!ItemStack.areEqual(selectedItem, mainHandStack)) {
			if(!ItemStack.areItemsEqual(selectedItem, mainHandStack)) {
				if(mainHandStack.getItem() instanceof LightsaberItem) {
					((LightsaberItem) mainHandStack.getItem()).onPullOut(player, world);
				}
				if(selectedItem.getItem() instanceof LightsaberItem) {
					((LightsaberItem) selectedItem.getItem()).onPullIn(player, world);
				}
			}
		}
	}
}
