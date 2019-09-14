package hugman.mubble.objects.block;

import javax.annotation.Nullable;

import hugman.mubble.Mubble;
import hugman.mubble.objects.container.TimeswapTableContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TimeswapTableBlock extends Block
{
	private static final TranslationTextComponent CONTAINER_NAME = new TranslationTextComponent("container." + Mubble.MOD_ID + ".timeswap_table");
	
    public TimeswapTableBlock(Block.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        player.openContainer(state.getContainer(worldIn, pos));
        return true;
    }
    
    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
    	return new SimpleNamedContainerProvider((windowId, inventory, playerIn) ->
    	{
    		return new TimeswapTableContainer(windowId, inventory, IWorldPosCallable.of(worldIn, pos));
    	}, CONTAINER_NAME);
    }
}