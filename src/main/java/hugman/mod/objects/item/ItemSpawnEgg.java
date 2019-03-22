package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleItems;
import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class ItemSpawnEgg extends net.minecraft.item.ItemSpawnEgg
{
    public ItemSpawnEgg(String entity_name, EntityType<?> entity, int primaryColor, int secondaryColor)
    {
        super(entity, primaryColor, secondaryColor, new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Mubble.MOD_ID, entity_name + "_spawn_egg");
		MubbleItems.register(this);
    }
}
