package hugman.mubble.objects.block;

import hugman.mubble.objects.block.dispenser_behavior.PlaceBlockBehavior;
import hugman.mubble.objects.block.tile_entity.PlacerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class PlacerBlock extends DispenserBlock
{
	private static final IDispenseItemBehavior PLACE_BEHAVIOR = new PlaceBlockBehavior();
	
    public PlacerBlock(Properties builder)
    {
        super(builder);
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
    	return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
    	return new PlacerTileEntity();
    }
    
    @Override
    protected IDispenseItemBehavior getBehavior(ItemStack stack)
    {
    	return PLACE_BEHAVIOR;
    }
}
