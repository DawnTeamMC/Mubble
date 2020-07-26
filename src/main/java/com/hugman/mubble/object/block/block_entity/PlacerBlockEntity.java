package com.hugman.mubble.object.block.block_entity;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.init.data.MubbleTileEntityTypes;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PlacerBlockEntity extends DispenserBlockEntity {
	public PlacerBlockEntity() {
		super(MubbleTileEntityTypes.PLACER);
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container." + Mubble.MOD_ID + ".placer");
	}
}