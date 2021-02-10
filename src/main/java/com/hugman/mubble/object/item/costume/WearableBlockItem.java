package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.init.MubbleSlots;
import com.hugman.mubble.mixin.client.GameRendererAccessor;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class WearableBlockItem extends BlockItem implements Trinket {
	protected final SoundEvent equipSound;
	protected final StatusEffectInstance[] effects;
	protected final Identifier shader;

	public WearableBlockItem(Block baseBlock, Settings settings, SoundEvent equipSound, Identifier shader, StatusEffectInstance... potionEffects) {
		super(baseBlock, settings);
		this.equipSound = equipSound;
		this.effects = potionEffects;
		this.shader = shader;
		DispenserBlock.registerBehavior(this, TrinketItem.TRINKET_DISPENSER_BEHAVIOR);
	}

	public WearableBlockItem(Block baseBlock, Settings settings, SoundEvent equipSound, StatusEffectInstance... potionEffects) {
		this(baseBlock, settings, equipSound, null, potionEffects);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return Trinket.equipTrinket(player, hand);
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.HEAD) && slot.equals(MubbleSlots.HAT);
	}

	@Override
	public void onEquip(PlayerEntity player, ItemStack stack) {
		World world = player.getEntityWorld();
		player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(), this.equipSound, SoundCategory.PLAYERS, 1f, 1f);
		if(world.isClient) {
			GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;
			ShaderEffect shaderEffect = renderer.getShader();
			Identifier shader = this.getShader();
			if(shader != null) {
				if(shaderEffect != null) {
					if(!shaderEffect.getName().equals(shader.toString())) {
						((GameRendererAccessor) renderer).invokeLoadShader(shader);
					}
				}
				else {
					((GameRendererAccessor) renderer).invokeLoadShader(shader);
				}
			}
		}
	}

	@Override
	public void onUnequip(PlayerEntity player, ItemStack stack) {
		World world = player.getEntityWorld();
		if(world.isClient) {
			GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;
			renderer.disableShader();
		}
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		World world = player.getEntityWorld();
		if(!world.isClient && effects != null) {
			for(StatusEffectInstance effect : effects) {
				player.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), 200, effect.getAmplifier(), false, false, true));
			}
		}
	}

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vcp, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		Trinket.translateToFace(matrixStack, model, player, headYaw, headPitch);
		matrixStack.scale(0.6F, 0.6F, 0.6F);
		matrixStack.translate(0.0D, 0.0D, 0.475D);
		matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		itemRenderer.renderItem(new ItemStack(this), ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, matrixStack, vcp);
	}

	public static boolean isUsable(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage() - 1;
	}

	public SoundEvent getEquipSound() {
		return equipSound;
	}

	public Identifier getShader() {
		return this.shader;
	}
}
