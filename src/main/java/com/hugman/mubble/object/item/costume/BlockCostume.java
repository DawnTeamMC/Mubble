package com.hugman.mubble.object.item.costume;

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
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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
import net.minecraft.world.World;

public class BlockCostume extends BlockItem implements Trinket {
	public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
		@Override
		public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
			return ArmorItem.dispenseArmor(source, stack) ? stack : super.dispenseSilently(source, stack);
		}
	};
	protected final EquipmentSlot armorType;
	protected final SoundEvent sound;
	protected final Identifier shader;

	public BlockCostume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, Block baseBlock) {
		super(baseBlock, builder);
		this.sound = sound;
		this.armorType = armorType;
		this.shader = null;
		DispenserBlock.registerBehavior(this, TrinketItem.TRINKET_DISPENSER_BEHAVIOR);
	}

	public BlockCostume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, Block baseBlock, Identifier shader) {
		super(baseBlock, builder);
		this.sound = sound;
		this.armorType = armorType;
		this.shader = shader;
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	public static boolean isUsable(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage() - 1;
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
	}

	public EquipmentSlot getArmorType() {
		return this.armorType;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return Trinket.equipTrinket(player, hand);
	}

	public Identifier getShader() {
		return this.shader;
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.HEAD) && slot.equals(Slots.MASK);
	}
}
