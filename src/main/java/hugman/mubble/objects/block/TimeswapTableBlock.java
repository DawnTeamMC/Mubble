package hugman.mubble.objects.block;

import hugman.mubble.Mubble;
import hugman.mubble.objects.container.TimeswapTableContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientDummyContainerProvider;
import net.minecraft.container.BlockContext;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeswapTableBlock extends Block
{
	private static final TranslatableText CONTAINER_NAME = new TranslatableText("container." + Mubble.MOD_ID + ".timeswap_table");
	
    public TimeswapTableBlock(Block.Settings builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
    {
        player.openContainer(state.createContainerProvider(worldIn, pos));
        return ActionResult.SUCCESS;
    }
    
    @Override
    public NameableContainerProvider createContainerProvider(BlockState state, World worldIn, BlockPos pos)
    {
    	return new ClientDummyContainerProvider((syncId, inventory, playerIn) ->
    	{
    		return new TimeswapTableContainer(syncId, inventory, BlockContext.create(worldIn, pos));
    	}, CONTAINER_NAME);
    }
}