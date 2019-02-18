package hugman.mod.objects.item;

import hugman.mod.Reference;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import net.minecraft.item.Item;

public class ItemSimple extends Item
{    
    public ItemSimple(String name)
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Reference.MOD_ID, name);
        
		MubbleItems.ITEMS.add(this);
    }
}
