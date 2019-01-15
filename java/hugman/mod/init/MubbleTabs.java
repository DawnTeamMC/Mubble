package hugman.mod.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/** 
 * Init class - used to initialize creative tabs.
 */
public class MubbleTabs 
{
    public static final CreativeTabs MUBBLE_BLOCKS = new CreativeTabs("mubble_blocks")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleBlocks.QUESTION_BLOCK);
    	}
    };
    public static final CreativeTabs MUBBLE_ITEMS = new CreativeTabs("mubble_items")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleItems.SUPER_MUSHROOM);
    	}
    };
    public static final CreativeTabs MUBBLE_COSTUMES = new CreativeTabs("mubble_costumes")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleCostumes.CAPPY);
    	}
    };
}