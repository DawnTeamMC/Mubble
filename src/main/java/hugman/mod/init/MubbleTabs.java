package hugman.mod.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/** 
 * Init class - used to initialize creative tabs.
 */
public class MubbleTabs 
{
    public static final ItemGroup MUBBLE_BLOCKS = new ItemGroup("mubble_blocks")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleBlocks.QUESTION_BLOCK);
    	}
    };
    public static final ItemGroup MUBBLE_ITEMS = new ItemGroup("mubble_items")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleItems.SUPER_MUSHROOM);
    	}
    };
    public static final ItemGroup MUBBLE_COSTUMES = new ItemGroup("mubble_costumes")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleCostumes.CAPPY);
    	}
    };
}