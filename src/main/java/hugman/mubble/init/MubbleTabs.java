package hugman.mubble.init;

import hugman.mubble.Mubble;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MubbleTabs 
{
    public static final ItemGroup COSTUMES = new ItemGroup(Mubble.MOD_ID + ".costumes")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleCostumes.CAPPY);
    	}
    };
    
    public static final ItemGroup INSTRUMENTS = new ItemGroup(Mubble.MOD_ID + ".instruments")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(MubbleItems.JINGLE_BELLS);
    	}
    };
}