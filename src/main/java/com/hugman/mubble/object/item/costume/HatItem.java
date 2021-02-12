package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.init.MubbleCostumes;
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
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class HatItem extends WearableItem {
	protected final boolean isHeadSet;
	public HatItem(Settings settings, SoundEvent equipSound, boolean isHeadSet, Identifier shader, StatusEffectInstance... potionEffects) {
		super(SlotGroups.HEAD, MubbleSlots.HAT, settings, equipSound, shader);
		this.isHeadSet = isHeadSet;
	}

	public HatItem(Settings settings, SoundEvent equipSound, boolean isHeadSet, StatusEffectInstance... potionEffects) {
		this(settings, equipSound, isHeadSet, null, potionEffects);
	}

	@Override
	public void onEquip(PlayerEntity player, ItemStack stack) {
		if(this == MubbleCostumes.MAYRO_CAP && player.getGameProfile().getId().toString().equals("8cf61519-4ac2-4d60-9d65-d0c7abcf4524")) {
			player.sendMessage(new TranslatableText("item.mubble.mayro_cap.secret_status"), true);
		}
		else if(this == MubbleCostumes.NOTEBLOCK_HEAD && player.getGameProfile().getId().toString().equals("5a68af56-e293-44e9-bbf8-21d58300b3f3")) {
			player.sendMessage(new TranslatableText("item.mubble.noteblock_head.secret_status"), true);
		}
		else if(this == MubbleCostumes.BANDANA && player.getGameProfile().getId().toString().equals("1805e857-329e-463e-8ca8-122fcc686996")) {
			player.sendMessage(new TranslatableText("item.mubble.bandana.secret_status"), true);
		}
		super.onEquip(player, stack);
	}

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vcp, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		Trinket.translateToFace(matrixStack, model, player, headYaw, headPitch);
		matrixStack.scale(0.6F, 0.6F, 0.6F);
		matrixStack.translate(0.0D, 0.0D, 0.475D);
		matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		if(!isHeadSet) {
			if(player.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ArmorItem) {
				matrixStack.translate(0.0D, 0.1D, 0.0D);
				matrixStack.scale(1.1F, 1.1F, 1.1F);
			}
			if(!player.isFallFlying()) {
				matrixStack.translate(0.0D, - Math.min(0.0D, (player.getVelocity().getY() + 0.6D) * 0.45D), 0.0D);
			}
		}
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Trinket", 1);
		itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
	}
}
