package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.init.MubbleSlots;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class HatItem extends CostumeItem {
	protected final boolean isHeadSet;

	public HatItem(Settings settings, SoundEvent equipSound, boolean isHeadSet, Identifier shader, StatusEffectInstance... potionEffects) {
		super(SlotGroups.HEAD, MubbleSlots.HAT, settings, equipSound, shader, potionEffects);
		this.isHeadSet = isHeadSet;
	}

	public HatItem(Settings settings, SoundEvent equipSound, boolean isHeadSet, StatusEffectInstance... potionEffects) {
		this(settings, equipSound, isHeadSet, null, potionEffects);
	}

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vcp, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		Trinket.translateToFace(matrixStack, model, player, headYaw, headPitch);
		matrixStack.scale(0.625F, 0.625F, 0.625F);
		matrixStack.translate(0.0D, 0.0D, 0.48D);
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		if(!isHeadSet) {
			if(player.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ArmorItem) {
				matrixStack.scale(1.253F, 1.253F, 1.253F);
			}
		}
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Trinket", 1);
		itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
	}
}
