package hugman.mubble.init.data;

import hugman.mubble.Mubble;
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
        public static final Tag<Block> VALID_GROUND_PALM_SAPLING = tag("valid_ground/palm_sapling");

        private static Tag<Block> tag(String name)
        {
            return new BlockTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }
    }
    
    public static class Items
    {
        public static final Tag<Item> COINS = tag("coins");
        public static final Tag<Item> CROWNS = tag("crowns");
        public static final Tag<Item> FLUID_BUCKETS = tag("fluid_buckets");
        public static final Tag<Item> WEIGHT_HEAVY = tag("weight/heavy");
        public static final Tag<Item> WEIGHT_LIGHT = tag("weight/light");
        public static final Tag<Item> SCARY_TO_TOAD = tag("scary_to/toad");
        public static final Tag<Item> TEMPTING_TO_TOAD = tag("tempting_to/toad");

        private static Tag<Item> tag(String name)
        {
            return new ItemTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }
    }
}