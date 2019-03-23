package hugman.mod.init.technical;

import hugman.mod.Mubble;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MubbleTags
{
    public static class Blocks
    {
        public static final Tag<Block> CLOUD_BLOCKS = tag("cloud_blocks");

        private static Tag<Block> tag(String name)
        {
            return new BlockTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }
    }
    
    public static class Items
    {
        public static final Tag<Item> COINS = tag("coins");
        public static final Tag<Item> CROWNS = tag("crowns");
        public static final Tag<Item> LIGHTWEIGHT_ITEMS = tag("lightweight_items");

        private static Tag<Item> tag(String name)
        {
            return new ItemTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }
    }
}