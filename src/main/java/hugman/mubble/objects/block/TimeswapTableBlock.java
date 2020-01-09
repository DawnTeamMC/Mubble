package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleContainerTypes;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeswapTableBlock extends Block
{
    public TimeswapTableBlock(Block.Settings builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
    {
    	if (!worldIn.isClient)
    	{
    		ContainerProviderRegistry.INSTANCE.openContainer(MubbleContainerTypes.TIMESWAP_TABLE, player,
    	    		buf -> buf.writeBlockPos(pos));
	        return ActionResult.SUCCESS;
    	}
    	return ActionResult.PASS;
    }
}