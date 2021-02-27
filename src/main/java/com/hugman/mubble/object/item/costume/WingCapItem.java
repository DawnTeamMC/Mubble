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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class WingCapItem extends HatItem {
	public WingCapItem(Item.Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Items.FEATHER;
	}

	@Override
	public int getAbilityCooldown(PlayerEntity player, ItemStack stack) {
		return 10;
	}

	@Override
	public void onAbilityUsage(PlayerEntity player, ItemStack stack) {
		if(!player.getEntityWorld().isClient()) {
			stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, (int) (getAbilityCooldown(player, stack) * 1.25D), 2, false, false));
			player.fallDistance = 0f;
		}
	}

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vcp, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		super.render(slot, matrixStack, vcp, light, model, player, headYaw, headPitch);
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		ItemStack stack = new ItemStack(this);

		int k = 5;
		if(isBeingUsed(player)) {
			k = 1;
		}

		float j = MathHelper.sin((float) player.age / k) * 10 + 12;

		matrixStack.push();
		matrixStack.translate(0.475D, 0.0D, -0.275D);
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));
		stack.getOrCreateTag().putInt("Trinket", 2);
		itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
		matrixStack.pop();

		matrixStack.push();
		matrixStack.translate(-0.475D, 0.0D, -0.275D);
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-j));
		stack.getOrCreateTag().putInt("Trinket", 3);
		itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
		matrixStack.pop();
	}
}