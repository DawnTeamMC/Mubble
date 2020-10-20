package com.hugman.mubble.object.block.block_entity;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PlacerBlockEntity extends DispenserBlockEntity {
	public PlacerBlockEntity() {
		super(MubbleBlocks.PLACER_ENTITY);
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container." + Mubble.MOD_DATA.getModName() + ".placer");
	}
}