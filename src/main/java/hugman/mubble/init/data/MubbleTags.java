package hugman.mubble.init.data;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MubbleTags
{
    public static class Blocks
    {
        public static final Tag<Block> FREEZABLE_TO_PACKED_ICE = tag("freezable/packed_ice");
        public static final Tag<Block> MELTABLE_TO_AIR = tag("meltable/air");
        public static final Tag<Block> MELTABLE_TO_ICE = tag("meltable/ice");
        public static final Tag<Block> MELTABLE_TO_WATER = tag("meltable/water");
        public static final Tag<Block> CLOUD_BLOCKS = tag("cloud_blocks");
        public static final Tag<Block> PALM_SAPLING_VALID_GROUND = tag("valid_ground/palm_sapling");

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
        public static final Tag<Item> TOAD_FEAR = tag("fear/toad");
        public static final Tag<Item> TOAD_FEEDING = tag("feeding/toad");
        public static final Tag<Item> DUCK_FEEDING = tag("feeding/duck");

        public static final Tag<Item> GEMS_BISMUTH = tag("gems/bismuth");
        public static final Tag<Item> GEMS_KYBER = tag("gems/kyber");
        public static final Tag<Item> GEMS_VANADIUM = tag("gems/vanadium");

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
    
    public static class EntityTypes
    {
        public static final Tag<EntityType<?>> CAN_WEAR_HELMET = tag("can_wear_helmet");

        private static Tag<EntityType<?>> tag(String name)
        {
            return new EntityTypeTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }
    }
    
    public static class Fluids
    {
        public static final Tag<Fluid> FREEZABLE_TO_ICE = tag("freezable/ice");

        private static Tag<Fluid> tag(String name)
        {
            return new FluidTags.Wrapper(new ResourceLocation(Mubble.MOD_ID, name));
        }
    }
}