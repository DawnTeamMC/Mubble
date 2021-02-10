package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.mixin.client.GameRendererAccessor;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class WearableItem extends TrinketItem {
	protected final String group;
	protected final String slot;
	protected final SoundEvent equipSound;
	protected final StatusEffectInstance[] effects;
	protected final Identifier shader;

	public WearableItem(String group, String slot, Settings settings, SoundEvent equipSound, Identifier shader, StatusEffectInstance... potionEffects) {
		super(settings);
		this.group = group;
		this.slot = slot;
		this.equipSound = equipSound;
		this.effects = potionEffects;
		this.shader = shader;
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(this.group) && slot.equals(this.slot);
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
