package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemSpawnEgg extends net.minecraft.item.ItemSpawnEgg
{
    public ItemSpawnEgg(String entity_name, EntityType<?> entity, int primaryColor, int secondaryColor)
    {
        super(entity, primaryColor, secondaryColor, new Item.Properties().group(ItemGroup.MISC));
        setRegistryName(Mubble.MOD_ID, entity_name + "_spawn_egg");
		MubbleItems.register(this);
    }
}
