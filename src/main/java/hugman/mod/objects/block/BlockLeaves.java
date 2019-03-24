package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLeaves extends net.minecraft.block.BlockLeaves
{
    public BlockLeaves(String name)
    {
        super(Properties.from(Blocks.OAK_LEAVES));
        setRegistryName(Mubble.MOD_ID, name + "_leaves");
        MubbleBlocks.register(this);
    }
    
    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
    {
        Block block = state.getBlock();
        if (block == MubbleBlocks.PALM_LEAVES)
        {
           return MubbleBlocks.PALM_SAPLING;
        }
		return block;
	}
    
    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World worldIn, BlockPos pos, int fortune)
    {
        int i = this.getSaplingDropChance(state);
        
        captureDrops(true);
    	this.dropBanana(worldIn, pos, state, i);
        drops.addAll(captureDrops(false));
        
    	super.getDrops(state, drops, worldIn, pos, fortune);
    }
    
    protected void dropBanana(World world, BlockPos pos, IBlockState state, int luck)
    {
    	if ((state.getBlock() == MubbleBlocks.PALM_LEAVES) && world.rand.nextInt(luck) == 0)
        {
           spawnAsEntity(world, pos, new ItemStack(MubbleItems.BANANA));
        }
	}
}