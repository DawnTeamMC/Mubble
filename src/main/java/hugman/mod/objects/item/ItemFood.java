package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleItems;
import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.item.Item;

public class ItemFood extends net.minecraft.item.ItemFood
{    
    public ItemFood(String name, int heal, float saturation, boolean isMeat)
    {
        super(heal, saturation, isMeat, new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
}
