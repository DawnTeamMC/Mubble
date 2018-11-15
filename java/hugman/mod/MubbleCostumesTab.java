package hugman.mod;

import hugman.mod.init.CostumeInit;
import hugman.mod.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MubbleCostumesTab extends CreativeTabs
{
	public MubbleCostumesTab(String label)
	{
		super("mubble_costumes_tab");
	}
	
	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(CostumeInit.CAPPY);
	}
}