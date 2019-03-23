package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlockStateProperties;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleCostumes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockKoretato extends BlockDirectional
{
	public static final BooleanProperty PRINCESS = MubbleBlockStateProperties.PRINCESS;
	
    public BlockKoretato()
    {
        super(Properties.create(Material.GROUND, MaterialColor.YELLOW_TERRACOTTA).hardnessAndResistance(0.4f, 2f).sound(SoundType.SNOW));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.UP).with(PRINCESS, false));
        setRegistryName(Mubble.MOD_ID, "koretato_block");
        MubbleBlocks.registerWithoutItem(this);
    }
    
    @Override
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
    	return state.with(FACING, rot.rotate(state.get(FACING)));
	}
    
    @Override
    @SuppressWarnings("deprecation")
	public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
    
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
    	builder.add(PRINCESS, FACING);
	}
    
    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
    {
    	if(state.get(PRINCESS)) drops.add(new ItemStack(MubbleCostumes.PRINCESS_PEACH_CROWN));
    	drops.add(new ItemStack(Items.POTATO, 3));
    }
}
