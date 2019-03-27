package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemSimple extends Item
{    
    public ItemSimple(String name, ItemGroup group)
    {
        super(new Item.Properties().group(group));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
    
    public ItemSimple(String name, ItemGroup group, int max)
    {
        super(new Item.Properties().group(group).maxStackSize(max));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
    
    public ItemSimple(String name, ItemGroup group, int max, EnumRarity rarity)
    {
        super(new Item.Properties().group(group).maxStackSize(max).rarity(rarity));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
}