package com.hugman.mubble.object.block;

import com.hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CloudFlowerBlock extends FlowerBlock {
	public CloudFlowerBlock(StatusEffect stewEffect, int stewEffectDuration, Block.Settings builder) {
		super(stewEffect, stewEffectDuration, builder);
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		Vec3d vec3d = entityIn.getVelocity();
		if(entityIn instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) entityIn;
			ItemStack armor = entity.getEquippedStack(EquipmentSlot.HEAD);
			if(MubbleTags.Items.CROWNS.contains(armor.getItem()) && !entity.isSneaking()) {
				if(!entity.isSprinting()) {
					entity.setVelocity(vec3d.x, (entity.getRandom().nextInt(31) + 40) / 100D, vec3d.z);
				}
				else {
					entity.setVelocity(vec3d.x, 0.7D, vec3d.z);
				}
				entity.fallDistance = 0f;
			}
		}
		if(entityIn instanceof ItemEntity) {
			ItemEntity entity = (ItemEntity) entityIn;
			if(MubbleTags.Items.CROWNS.contains(entity.getStack().getItem())) {
				entity.setVelocity(vec3d.x, 0.3D, vec3d.z);
			}
			if(MubbleTags.Items.WEIGHT_LIGHT.contains(entity.getStack().getItem())) {
				entity.setVelocity(vec3d.x, 0.1D, vec3d.z);
			}
		}
	}
}
