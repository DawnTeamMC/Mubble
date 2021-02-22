package com.hugman.mubble.object.item.costume;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

public class PropellerBoxItem extends HatItem {
	public PropellerBoxItem(Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_IRON, true);
	}

	@Override
	public int getAbilityCooldown(PlayerEntity player, ItemStack stack) {
		return 110;
	}

	@Override
	public void onAbilityUsage(PlayerEntity player, ItemStack stack) {
		stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
		player.setVelocity(0.0D, 1.4D, 0.0D);
		player.velocityModified = true;
		player.setOnGround(false);
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, getAbilityCooldown(player, stack), 4, false, false));
		player.fallDistance = 0f;
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if(!player.world.isClient() && isBeingUsed(player) && player.isOnGround()) {
			player.getItemCooldownManager().set(this, 0);
			if(player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
				if(player.getStatusEffect(StatusEffects.SLOW_FALLING).getAmplifier() == 4) {
					player.removeStatusEffect(StatusEffects.SLOW_FALLING);
				}
			}
		}
	}

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vcp, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		super.render(slot, matrixStack, vcp, light, model, player, headYaw, headPitch);
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Trinket", 2);

		int k = 1;
		if(isBeingUsed(player)) {
			k = player.getVelocity().getY() > 0 ? 7 : 3;
		}

		float j = player.age * 5 * k;

		matrixStack.push();
		matrixStack.translate(0.0D, 1.125D, 0.0D);
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));
		itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
		matrixStack.pop();
	}
}