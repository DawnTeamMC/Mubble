package hugman.mubble.init.data;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.tile_entity.PlacerTileEntity;
import hugman.mubble.objects.tile_entity.PresentTileEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class MubbleTileEntityTypes
{
	/* All Content Bag */
    public static final List<TileEntityType<?>> TILE_ENTITY_TYPES = new ArrayList<TileEntityType<?>>();

    public static final TileEntityType<DispenserTileEntity> PLACER = type("placer", TileEntityType.Builder.create(PlacerTileEntity::new, MubbleBlocks.PLACER));
    public static final TileEntityType<PresentTileEntity> PRESENT = type("present", TileEntityType.Builder.create(PresentTileEntity::new, MubbleBlocks.WHITE_PRESENT, MubbleBlocks.BLACK_PRESENT, MubbleBlocks.BLUE_PRESENT, MubbleBlocks.GREEN_PRESENT, MubbleBlocks.YELLOW_PRESENT, MubbleBlocks.RED_PRESENT, MubbleBlocks.PURPLE_PRESENT, MubbleBlocks.GOLDEN_PRESENT));
    
    private static <T extends TileEntity> TileEntityType<T> type(String name, TileEntityType.Builder<T> builder)
    {
    	TileEntityType<T> fType = builder.build(null);
    	fType.setRegistryName(Mubble.MOD_ID, name);
    	TILE_ENTITY_TYPES.add(fType);
    	return fType;
    }
}