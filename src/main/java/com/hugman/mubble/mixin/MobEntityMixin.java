package com.hugman.mubble.mixin;

import com.hugman.mubble.init.MubbleCostumes;
import com.hugman.mubble.init.data.MubbleTags;
import com.hugman.mubble.util.CalendarEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(MobEntity.class)
public class MobEntityMixin {
	@Inject(method = "initialize", at = @At(value = "TAIL"), cancellable = true)
	private void mubble_initialize(ServerWorldAccess arg, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag, CallbackInfoReturnable<EntityData> info) {
		MobEntity entity = (MobEntity) (Object) this;
		Random rand = new Random();
		if(MubbleTags.EntityTypes.CAN_WEAR_HELMET.contains(entity.getType())) {
			if(entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && CalendarEvents.isChristmasSeason) {
				if(rand.nextFloat() < (float) CalendarEvents.getDayToday() / 25.0f) {
					entity.equipStack(EquipmentSlot.HEAD, new ItemStack(MubbleCostumes.CHRISTMAS_HAT));
					entity.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0F);
				}
			}
		}
		info.setReturnValue(entityData);
	}
}
