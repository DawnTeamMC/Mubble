package hugman.mod;

import hugman.mod.init.BlockInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MubbleBlocksTab extends CreativeTabs
{
	public MubbleBlocksTab(String label) { super("mubble_blocks_tab"); }
	@Override
	public ItemStack createIcon() { return new ItemStack(Item.getItemFromBlock(BlockInit.QUESTION_BLOCK)); }
}