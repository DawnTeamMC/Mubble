package com.hugman.mubble.object.item.costume;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class MaskItem extends CostumeItem {
	public MaskItem(Settings settings, SoundEvent equipSound, Identifier shader, StatusEffectInstance... potionEffects) {
		super(SlotGroups.HEAD, Slots.MASK, settings, equipSound, shader, potionEffects);
	}

	public MaskItem(Settings settings, SoundEvent equipSound, StatusEffectInstance... potionEffects) {
		this(settings, equipSound, null, potionEffects);
	}

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vcp, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		Trinket.translateToFace(matrixStack, model, player, headYaw, headPitch);
		matrixStack.scale(0.625F, 0.625F, 0.625F);
		matrixStack.translate(0.0D, 0.0D, 0.48D);
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Trinket", 1);
		itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
	}
}
