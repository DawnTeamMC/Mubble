package hugman.mubble.object.block.block_entity;

import hugman.mubble.Mubble;
import hugman.mubble.init.data.MubbleTileEntityTypes;
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