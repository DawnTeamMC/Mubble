package hugman.mod.objects.item;

import hugman.mod.Reference;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public class ItemRecord extends net.minecraft.item.ItemRecord
{    
    public ItemRecord(String name, SoundEvent soundIn, int comparatorValue)
    {
        super(comparatorValue, soundIn, new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Reference.MOD_ID, name);
		MubbleItems.register(this);
    }
}
