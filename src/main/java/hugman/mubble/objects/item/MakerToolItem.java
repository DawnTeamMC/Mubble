package hugman.mubble.objects.item;

import hugman.mubble.util.MakerSwapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MakerToolItem extends Item
{    
    public MakerToolItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Block block = world.getBlockState(pos).getBlock();
        Block nextStyleBlock = MakerSwapper.getNextStyle(block);
        Block nextThemeBlock = MakerSwapper.getNextTheme(block);
        
        if(nextStyleBlock != null && !context.isPlacerSneaking())
        {
        	world.setBlockState(pos, nextStyleBlock.getDefaultState(), 11);
        	return ActionResultType.SUCCESS;
        }
        else if(nextThemeBlock != null && context.isPlacerSneaking())
        {
        	world.setBlockState(pos, nextThemeBlock.getDefaultState(), 11);
        	return ActionResultType.SUCCESS;
        }
        else
        {
        	//new SPacketTitle(SPacketTitle.Type.ACTIONBAR, new TextComponentString("You cannot use the Maker Tool on " + block.getNameTextComponent()));
        	return ActionResultType.PASS;
        }
    }
}
