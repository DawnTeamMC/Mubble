package hugman.mubble.init.data;

import java.util.ArrayList;
import java.util.List;

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
        public static final List<Tag<Item>> TIMESWAP_TAGS = new ArrayList<Tag<Item>>();
        
        public static final Tag<Item> COINS = tag("coins");
        public static final Tag<Item> CROWNS = tag("crowns");
        public static final Tag<Item> WEIGHT_HEAVY = tag("weight/heavy");
        public static final Tag<Item> WEIGHT_LIGHT = tag("weight/light");
        public static final Tag<Item> SCARY_TO_TOAD = tag("scary_to/toad");
        public static final Tag<Item> TEMPTING_TO_TOAD = tag("tempting_to/toad");

        public static final Tag<Item> TIMESWAP_QUESTION_BLOCKS = timeswapTag("timeswap/question_blocks");
        public static final Tag<Item> TIMESWAP_GROUND_BLOCKS = timeswapTag("timeswap/ground_blocks");
        public static final Tag<Item> TIMESWAP_EMPTY_BLOCKS = timeswapTag("timeswap/empty_blocks");
        public static final Tag<Item> TIMESWAP_ROTATING_BLOCKS = timeswapTag("timeswap/rotating_blocks");
        public static final Tag<Item> TIMESWAP_BRICK_BLOCKS = timeswapTag("timeswap/brick_blocks");
        public static final Tag<Item> TIMESWAP_GOLDEN_BRICK_BLOCKS = timeswapTag("timeswap/golden_brick_blocks");
        public static final Tag<Item> TIMESWAP_HARD_BLOCKS = timeswapTag("timeswap/hard_blocks");
        public static final Tag<Item> TIMESWAP_ICE_BLOCKS = timeswapTag("timeswap/ice_blocks");
        public static final Tag<Item> TIMESWAP_NOTE_BLOCKS = timeswapTag("timeswap/note_blocks");
        public static final Tag<Item> TIMESWAP_SUPER_NOTE_BLOCKS = timeswapTag("timeswap/super_note_blocks");
        public static final Tag<Item> TIMESWAP_DOORS = timeswapTag("timeswap/doors");
        public static final Tag<Item> TIMESWAP_KEY_DOORS = timeswapTag("timeswap/key_doors");
        public static final Tag<Item> TIMESWAP_KEYS = timeswapTag("timeswap/keys");

        private static Tag<Item> tag(String name)
        {
        	return new ItemTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }

        private static Tag<Item> timeswapTag(String name)
        {
        	Tag<Item> fTag = new ItemTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        	TIMESWAP_TAGS.add(fTag);
            return fTag;
        }
    }
}