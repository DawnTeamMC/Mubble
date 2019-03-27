package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.SoundEvent;

public class ItemMusicDisc extends net.minecraft.item.ItemRecord
{    
    public ItemMusicDisc(String name, SoundEvent soundIn, int comparatorValue)
    {
        super(comparatorValue, soundIn, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).rarity(EnumRarity.RARE));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
}
