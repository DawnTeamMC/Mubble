package hugman.mod.objects.block;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockSimple extends Block
{    
    public BlockSimple(String name, Properties properties)
    {
        super(properties);
        setRegistryName(Reference.MOD_ID, name);
        Item.Properties blocks = new Item.Properties().group(MubbleTabs.MUBBLE_BLOCKS);
        
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this, blocks).setRegistryName(this.getRegistryName()));
    }
}
