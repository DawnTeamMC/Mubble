package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.init.MubbleBlocks;
import com.hugman.mubble.object.block.KoretatoBlock;
import com.hugman.mubble.object.block.block_state_property.Princess;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrownItem extends HatItem {
	protected final Princess princess;

	public CrownItem(Settings builder, Princess princess) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_IRON, false);
		this.princess = princess;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() == MubbleBlocks.KORETATO_BLOCK && state.get(KoretatoBlock.PRINCESS) == Princess.NONE) {
			world.addParticle(ParticleTypes.HEART, (float) pos.getX() + 0.5F, (float) pos.getY() + 1.1F, (float) pos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D);
			if(!world.isClient) {
				BlockState blockState1 = state.with(KoretatoBlock.PRINCESS, princess);
				world.setBlockState(pos, blockState1, 2);
				context.getStack().decrement(1);
				world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.playSound(null, pos, this.equipSound, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return ActionResult.SUCCESS;
		}
		else {
			return super.useOnBlock(context);
		}
	}
}