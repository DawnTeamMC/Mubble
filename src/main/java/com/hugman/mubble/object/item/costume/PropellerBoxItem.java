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
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class PropellerBoxItem extends HatItem {
	public PropellerBoxItem(Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_IRON, true);
	}

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vcp, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		super.render(slot, matrixStack, vcp, light, model, player, headYaw, headPitch);
		float j = player.age * 5;
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Trinket", 2);

		matrixStack.push();
		matrixStack.translate(0.0D, 1.125D, 0.0D);
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));
		itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
		matrixStack.pop();
	}
}