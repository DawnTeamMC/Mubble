package hugman.mod;

import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NintendoBlocksTab extends CreativeTabs
{
	public NintendoBlocksTab(String label) { super("nintendo_blocks_tab"); }
	@Override
	public ItemStack createIcon() { return new ItemStack(Item.getItemFromBlock(BlockInit.QUESTION_BLOCK)); }
}