package hugman.mod.objects.item;

import hugman.mod.Reference;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import net.minecraft.item.Item;

public class ItemFood extends net.minecraft.item.ItemFood
{    
    public ItemFood(String name, int heal, float saturation, boolean isMeat)
    {
        super(heal, saturation, isMeat, new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Reference.MOD_ID, name);
		MubbleItems.register(this);
    }
}
