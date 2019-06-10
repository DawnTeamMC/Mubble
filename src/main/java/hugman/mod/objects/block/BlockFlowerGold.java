package hugman.mod.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFlowerGold extends BlockFlower
{
    public BlockFlowerGold(Block.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
    {
    	drops.add(new ItemStack(Items.GOLD_NUGGET, RANDOM.nextInt(3) + 1));
    }
}
