package hugman.mod;

import hugman.mod.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MubbleItemsTab extends CreativeTabs
{
	public MubbleItemsTab(String label)
	{
		super("mubble_items_tab");
	}
	
	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ItemInit.SUPER_MUSHROOM);
	}
}