package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BerryBushBlock extends SweetBerryBushBlock
{
    public BerryBushBlock(Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state)
    {
    	return new ItemStack(MubbleItems.BLUEBERRIES);
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        int i = state.get(AGE);
        boolean flag = i == 3;
        if (!flag && player.getHeldItem(handIn).getItem() == Items.BONE_MEAL)
        {
           return false;
        }
        else if (i > 1)
        {
           int j = 1 + worldIn.rand.nextInt(2);
           spawnAsEntity(worldIn, pos, new ItemStack(MubbleItems.BLUEBERRIES, j + (flag ? 1 : 0)));
           worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
           worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(1)), 2);
           return true;
        }
        else
        {
           return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
    }
}