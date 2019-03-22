package hugman.mod.init.technical;

import hugman.mod.Mubble;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MubbleTags
{
    public static class Items
    {
        public static final Tag<Item> COINS = tag("coins");

        private static Tag<Item> tag(String name)
        {
            return new ItemTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }
    }
}