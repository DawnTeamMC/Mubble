package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleItems;
import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

public class ItemSimple extends Item
{    
    public ItemSimple(String name)
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
    
    public ItemSimple(String name, int max)
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS).maxStackSize(max));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
    
    public ItemSimple(String name, int max, EnumRarity rarity)
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS).maxStackSize(max).rarity(rarity));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
}