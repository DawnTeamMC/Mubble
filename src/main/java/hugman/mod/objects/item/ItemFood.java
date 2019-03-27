package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemFood extends net.minecraft.item.ItemFood
{    
    public ItemFood(String name, int heal, float saturation, boolean isMeat)
    {
        super(heal, saturation, isMeat, new Item.Properties().group(ItemGroup.FOOD));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
}
