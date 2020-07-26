package com.hugman.mubble.init.data;

import com.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MubbleTags {
	public static class Blocks {
		public static final Tag<Block> FREEZABLE_TO_PACKED_ICE = register("freezable/packed_ice");
		public static final Tag<Block> MELTABLE_TO_AIR = register("meltable/air");
		public static final Tag<Block> MELTABLE_TO_ICE = register("meltable/ice");
		public static final Tag<Block> MELTABLE_TO_WATER = register("meltable/water");
		public static final Tag<Block> CLOUD_BLOCKS = register("cloud_blocks");
		public static final Tag<Block> PALM_SAPLING_VALID_GROUND = register("valid_ground/palm_sapling");

		private static Tag<Block> register(String name) {
			return TagRegistry.block(new Identifier(Mubble.MOD_ID, name));
		}
	}

	public static class Items {
		public static final List<Tag<Item>> TIMESWAP_TAGS = new ArrayList<Tag<Item>>();

		public static final Tag<Item> COINS = register("coins");
		public static final Tag<Item> CROWNS = register("crowns");
		public static final Tag<Item> WEIGHT_HEAVY = register("weight/heavy");
		public static final Tag<Item> WEIGHT_LIGHT = register("weight/light");
		public static final Tag<Item> TOAD_FEAR = register("fear/toad");

		public static final Tag<Item> GEMS_BISMUTH = register("gems/bismuth");
		public static final Tag<Item> GEMS_KYBER = register("gems/kyber");
		public static final Tag<Item> GEMS_VANADIUM = register("gems/vanadium");

		public static final Tag<Item> TIMESWAP_QUESTION_BLOCKS = registerTimeswap("timeswap/question_blocks");
		public static final Tag<Item> TIMESWAP_GROUND_BLOCKS = registerTimeswap("timeswap/ground_blocks");
		public static final Tag<Item> TIMESWAP_EMPTY_BLOCKS = registerTimeswap("timeswap/empty_blocks");
		public static final Tag<Item> TIMESWAP_ROTATING_BLOCKS = registerTimeswap("timeswap/rotating_blocks");
		public static final Tag<Item> TIMESWAP_BRICK_BLOCKS = registerTimeswap("timeswap/brick_blocks");
		public static final Tag<Item> TIMESWAP_GOLDEN_BRICK_BLOCKS = registerTimeswap("timeswap/golden_brick_blocks");
		public static final Tag<Item> TIMESWAP_HARD_BLOCKS = registerTimeswap("timeswap/hard_blocks");
		public static final Tag<Item> TIMESWAP_ICE_BLOCKS = registerTimeswap("timeswap/ice_blocks");
		public static final Tag<Item> TIMESWAP_NOTE_BLOCKS = registerTimeswap("timeswap/note_blocks");
		public static final Tag<Item> TIMESWAP_SUPER_NOTE_BLOCKS = registerTimeswap("timeswap/super_note_blocks");
		public static final Tag<Item> TIMESWAP_DOORS = registerTimeswap("timeswap/doors");
		public static final Tag<Item> TIMESWAP_KEY_DOORS = registerTimeswap("timeswap/key_doors");
		public static final Tag<Item> TIMESWAP_KEYS = registerTimeswap("timeswap/keys");

		private static Tag<Item> register(String name) {
			return TagRegistry.item(new Identifier(Mubble.MOD_ID, name));
		}

		private static Tag<Item> registerTimeswap(String name) {
			Tag<Item> fTag = TagRegistry.item(new Identifier(Mubble.MOD_ID, name));
			TIMESWAP_TAGS.add(fTag);
			return fTag;
		}
	}

	public static class EntityTypes {
		public static final Tag<EntityType<?>> CAN_WEAR_HELMET = register("can_wear_helmet");

		private static Tag<EntityType<?>> register(String name) {
			return TagRegistry.entityType(new Identifier(Mubble.MOD_ID, name));
		}
	}

	public static class Fluids {
		public static final Tag<Fluid> FREEZABLE_TO_ICE = register("freezable/ice");

		private static Tag<Fluid> register(String name) {
			return TagRegistry.fluid(new Identifier(Mubble.MOD_ID, name));
		}
	}
}