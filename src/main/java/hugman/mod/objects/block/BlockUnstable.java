package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlockStateProperties;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUnstable extends Block
{	
	public static final BooleanProperty UNSTABLE = MubbleBlockStateProperties.UNSTABLE;
	
    public BlockUnstable()
    {
        super(Properties.from(Blocks.STONE).hardnessAndResistance(0.1F).needsRandomTick());
        this.setDefaultState(this.stateContainer.getBaseState().with(UNSTABLE, false));
        setRegistryName(Mubble.MOD_ID, "unstable_stone");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
    	builder.add(UNSTABLE);
	}
    
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
    	if(worldIn.isRemote && worldIn.rand.nextInt(8) == 0)
    	{
    		worldIn.destroyBlock(pos, false);
    	}
    }
}
