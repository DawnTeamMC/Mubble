package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import hugman.mubble.objects.tile_entity.PresentTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PresentBlock extends ContainerBlock
{
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty EMPTY = MubbleBlockStateProperties.EMPTY;
	protected static final VoxelShape EMPTY_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D);
	protected static final VoxelShape FULL_SHAPE = VoxelShapes.or(EMPTY_SHAPE, Block.makeCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D));
	
    public PresentBlock(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(OPEN, Boolean.valueOf(false)).with(EMPTY, Boolean.valueOf(true)));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
		if(!state.get(EMPTY) && !state.get(OPEN))
		{
    		return FULL_SHAPE;
		}
		else
		{
			return EMPTY_SHAPE;
		}
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
    	if(!worldIn.isRemote)
    	{
    		TileEntity tileEntity = worldIn.getTileEntity(pos);
    		if(tileEntity instanceof PresentTileEntity)
    		{
    			player.openContainer((PresentTileEntity)tileEntity);
    			//TODO player.addStat(Stats.OPEN_BARREL);
    		}
    	}
		return true;
    }

	@Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
    	if(state.getBlock() != newState.getBlock())
    	{
    		TileEntity tileEntity = worldIn.getTileEntity(pos);
    		if(tileEntity instanceof IInventory)
    		{
    			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileEntity);
    			worldIn.updateComparatorOutputLevel(pos, this);
    		}
    	}
    	super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof PresentTileEntity)
		{
			((PresentTileEntity)tileEntity).presentTick();
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(stack.hasDisplayName())
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof PresentTileEntity)
			{
				((PresentTileEntity)tileEntity).setCustomName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(OPEN, EMPTY);
	}
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
    	return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
    	return new PresentTileEntity();
    }

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		return new PresentTileEntity();
	}
}
