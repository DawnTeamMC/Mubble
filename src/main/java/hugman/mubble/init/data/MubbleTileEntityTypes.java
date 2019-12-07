package hugman.mubble.init.data;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.tile_entity.PlacerTileEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class MubbleTileEntityTypes
{
	/* All Content Bag */
    public static final List<TileEntityType<?>> TILE_ENTITY_TYPES = new ArrayList<TileEntityType<?>>();

    public static final TileEntityType<DispenserTileEntity> PLACER = type("placer", TileEntityType.Builder.create(PlacerTileEntity::new, MubbleBlocks.PLACER));
    
    private static <T extends TileEntity> TileEntityType<T> type(String name, TileEntityType.Builder<T> builder)
    {
    	TileEntityType<T> fType = builder.build(null);
    	fType.setRegistryName(Mubble.MOD_ID, name);
    	TILE_ENTITY_TYPES.add(fType);
    	return fType;
    }
}