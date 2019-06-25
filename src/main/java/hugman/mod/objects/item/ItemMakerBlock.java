package hugman.mod.objects.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMakerBlock extends BlockNamedItem
{
    public ItemMakerBlock(Block block, Item.Properties builder)
    {
        super(block, builder);
    }
    
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
    	if (this.isInGroup(group))
    	{
    		items.add(new ItemStack(this));
    	}
	}
}
