package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.init.MubbleCostumes;
import com.hugman.mubble.init.MubbleShaders;
import com.hugman.mubble.mixin.client.GameRendererAccessor;
import com.hugman.mubble.util.CalendarEvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.world.World;

import java.util.Random;

public class Costume extends Item {
	public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
		@Override
		public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
			return ArmorItem.dispenseArmor(source, stack) ? stack : super.dispenseSilently(source, stack);
		}
	};
	protected final EquipmentSlot armorType;
	protected final SoundEvent equipSound;
	protected final StatusEffectInstance[] effects;
	protected final Identifier shader;

	public Costume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, StatusEffectInstance... potionEffects) {
		super(builder);
		this.equipSound = sound;
		this.armorType = armorType;
		this.effects = potionEffects;
		this.shader = null;
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	public Costume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, Identifier shader, StatusEffectInstance... potionEffects) {
		super(builder);
		this.equipSound = sound;
		this.armorType = armorType;
		this.effects = potionEffects;
		if(MubbleShaders.RETRO_SHADERS.contains(shader) && CalendarEvents.isAprilFools) {
			Random rand = new Random();
			this.shader = MubbleShaders.RETRO_SHADERS.get(rand.nextInt(MubbleShaders.RETRO_SHADERS.size()));
		}
		else {
			this.shader = shader;
		}
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	public static boolean isUsable(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage() - 1;
	}

	public EquipmentSlot getArmorType() {
		return this.armorType;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(world.isClient && entity instanceof PlayerEntity) {
			GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;
			PlayerEntity player = (PlayerEntity) entity;
			ShaderEffect shaderEffect = renderer.getShader();
			Identifier shader = this.getShader();
			if(player.inventory.getArmorStack(3).equals(stack)) {
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
		if(!world.isClient && effects != null) {
			for(StatusEffectInstance effect : effects) {
				((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(effect.getEffectType(), 200, effect.getAmplifier(), false, false, true));
			}
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		ItemStack itemStack1 = player.getEquippedStack(this.armorType);
		if(itemStack1.isEmpty()) {
			player.equipStack(armorType, itemStack.copy());
			itemStack.decrement(1);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), this.equipSound, SoundCategory.PLAYERS, 1f, 1f);
			if(this == MubbleCostumes.MAYRO_CAP && player.getGameProfile().getId().toString() == "8cf61519-4ac2-4d60-9d65-d0c7abcf4524") {
				player.sendMessage(new TranslatableText("item.mubble.mayro_cap.secret_status", new Object[0]), true);
			}
			else if(this == MubbleCostumes.NOTEBLOCK_HEAD && player.getGameProfile().getId().toString() == "5a68af56-e293-44e9-bbf8-21d58300b3f3") {
				player.sendMessage(new TranslatableText("item.mubble.noteblock_head.secret_status", new Object[0]), true);
			}
			else if(this == MubbleCostumes.BANDANA && player.getGameProfile().getId().toString() == "1805e857-329e-463e-8ca8-122fcc686996") {
				player.sendMessage(new TranslatableText("item.mubble.bandana.secret_status", new Object[0]), true);
			}
			return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
		}
		else {
			return new TypedActionResult<>(ActionResult.FAIL, itemStack);
		}
	}

	public SoundEvent getEquipSound() {
		return equipSound;
	}

	public Identifier getShader() {
		return this.shader;
	}
}