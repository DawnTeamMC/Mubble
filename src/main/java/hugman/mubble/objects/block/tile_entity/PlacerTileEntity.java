package hugman.mubble.objects.block.tile_entity;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleTileEntityTypes;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PlacerTileEntity extends DispenserTileEntity
{
	public PlacerTileEntity()
	{
		super(MubbleTileEntityTypes.PLACER);
	}
	
	@Override
	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container." + Mubble.MOD_ID + ".placer");
	}
}