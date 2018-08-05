package hugman.mod;

import hugman.mod.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class NintendoItemsTab extends CreativeTabs
{
	public NintendoItemsTab(String label) { super("nintendo_items_tab"); }
	@Override
	public ItemStack createIcon() { return new ItemStack(ItemInit.SUPER_MUSHROOM); }
}