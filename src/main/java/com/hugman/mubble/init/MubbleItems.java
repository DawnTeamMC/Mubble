package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.ItemCreator.Builder;
import com.hugman.dawn.api.object.item.AxeItem;
import com.hugman.mubble.init.data.MubbleFoods;
import com.hugman.mubble.init.data.MubbleItemTiers;
import com.hugman.mubble.object.item.HoeItem;
import com.hugman.mubble.object.item.PickaxeItem;
import com.hugman.mubble.object.item.*;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

public class MubbleItems extends MubblePack {
	/* MUBBLE */
	public static final Item WHEAT_FLOUR = register(new Builder("wheat_flour", new Item(new Item.Settings().group(ItemGroup.FOOD))));
	public static final Item TOMATO = register(new Builder("tomato", new BlockItem(MubbleBlocks.TOMATOES, new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.TOMATO))));
	public static final Item SALAD = register(new Builder("salad", new BlockItem(MubbleBlocks.SALAD, new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SALAD))));
	public static final Item CHEESE = register(new Builder("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CHEESE))));
	public static final Item BANANA = register(new Builder("banana", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BANANA))));
	public static final Item APRICOT = register(new Builder("apricot", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.APRICOT))));
	public static final Item MANGO = register(new Builder("mango", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.MANGO))));
	public static final Item BLUEBERRIES = register(new Builder("blueberries", new BlockItem(MubbleBlocks.BLUEBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BLUEBERRIES))));
	public static final Item DUCK = register(new Builder("duck", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.DUCK))));
	public static final Item COOKED_DUCK = register(new Builder("cooked_duck", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.COOKED_DUCK))));
	public static final Item CARAMEL_CUBE = register(new Builder("caramel_cube", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CARAMEL_CUBE))));
	public static final Item BAGUETTE = register(new Builder("baguette", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BAGUETTE))));
	public static final Item BURGER = register(new Builder("burger", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BURGER))));
	public static final Item CREPE = register(new Builder("crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CREPE))));
	public static final Item SUGAR_CREPE = register(new Builder("sugar_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SUGAR_CREPE))));
	public static final Item CHOCOLATE_CREPE = register(new Builder("chocolate_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CHOCOLATE_CREPE))));
	public static final Item CARAMEL_CREPE = register(new Builder("caramel_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CARAMEL_CREPE))));
	public static final Item HONEY_CREPE = register(new Builder("honey_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.HONEY_CREPE))));
	public static final Item SWEET_BERRY_CREPE = register(new Builder("sweet_berry_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SWEET_BERRY_CREPE))));
	public static final Item BLUEBERRY_CREPE = register(new Builder("blueberry_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BLUEBERRY_CREPE))));
	public static final Item CANDY_CANE = register(new Builder("candy_cane", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CANDY_CANE))));
	public static final Item SMALL_BULB = register(new Builder("small_bulb", new SmallBulbItem(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item JINGLE_BELLS = register(new Builder("jingle_bells", new ShakeInstrumentItem(new Item.Settings().group(MubbleTabs.INSTRUMENTS).maxCount(1), MubbleSounds.ITEM_JINGLE_BELLS_USE)));
	public static final Item VANADIUM = register(new Builder("vanadium", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item BISMUTH_DUST = register(new Builder("bismuth_dust", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item CRYSTALLIZED_BISMUTH = register(new Builder("crystallized_bismuth", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item BISMUTH_SWORD = register(new Builder("bismuth_sword", new SwordItem(MubbleItemTiers.BISMUTH, 4, -2.4F, new Item.Settings().group(ItemGroup.COMBAT))));
	public static final Item BISMUTH_SHOVEL = register(new Builder("bismuth_shovel", new ShovelItem(MubbleItemTiers.BISMUTH, 1.5F, -3.0F, new Item.Settings().group(ItemGroup.TOOLS))));
	public static final Item BISMUTH_PICKAXE = register(new Builder("bismuth_pickaxe", new PickaxeItem(MubbleItemTiers.BISMUTH, 1, -2.4F, new Item.Settings().group(ItemGroup.TOOLS))));
	public static final Item BISMUTH_AXE = register(new Builder("bismuth_axe", new AxeItem(MubbleItemTiers.BISMUTH, 4.5F, -2.5F, new Item.Settings().group(ItemGroup.TOOLS))));
	public static final Item BISMUTH_HOE = register(new Builder("bismuth_hoe", new HoeItem(MubbleItemTiers.BISMUTH, -1, 0.0F, new Item.Settings().group(ItemGroup.TOOLS))));
	public static final Item PERMAFROST_BRICK = register(new Builder("permafrost_brick", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item BANDAGE = register(new Builder("bandage", new BandageItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16))));
	public static final Item TOTEM_OF_ASCENDING = register(new Builder("totem_of_ascending", new TotemOfAscendingItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON))));

	/* SUPER MARIO */
	public static final Item SUPER_MUSHROOM = register(new Builder("super_mushroom", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SUPER_MUSHROOM))));
	public static final Item PEACH = register(new Builder("peach", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.PEACH))));
	public static final Item FIREBALL = register(new Builder("fireball", new FireballItem(new Item.Settings().group(ItemGroup.COMBAT))));
	public static final Item ICEBALL = register(new Builder("iceball", new IceballItem(new Item.Settings().group(ItemGroup.COMBAT))));
	public static final Item CAPE_FEATHER = register(new Builder("cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION))));
	public static final Item SUPER_CAPE_FEATHER = register(new Builder("super_cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION).maxCount(1).rarity(Rarity.EPIC))));
	public static final Item SUPER_STAR = register(new Builder("super_star", new SuperStarItem(new Item.Settings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE).food(MubbleFoods.SUPER_STAR))));
	public static final Item YELLOW_COIN = register(new Builder("yellow_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item RED_COIN = register(new Builder("red_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item BLUE_COIN = register(new Builder("blue_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item SMB_KEY = register(new Builder("smb_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMB_KEY_DOOR)));
	public static final Item SMB3_KEY = register(new Builder("smb3_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMB3_KEY_DOOR)));
	public static final Item SMW_KEY = register(new Builder("smw_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMW_KEY_DOOR)));
	public static final Item NSMBU_KEY = register(new Builder("nsmbu_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.NSMBU_KEY_DOOR)));

	/* KIRBY */
	public static final Item KIRBY_BALL = register(new Builder("kirby_ball", new KirbyBallItem(new Item.Settings().group(ItemGroup.COMBAT))));

	/* FIRE EMBLEM */
	//public static final Item AYMR = register(new Builder("aymr", new AymrItem(new Item.Settings().group(ItemGroup.COMBAT))));

	/* SUPER SMASH BROS. */
	public static final Item SMASH_BALL = register(new Builder("smash_ball", new SmashBallItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.RARE))));

	/* STAR WARS */
	public static final Item WHITE_LIGHTSABER = register(new Builder("white_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item LIGHT_GRAY_LIGHTSABER = register(new Builder("light_gray_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item GRAY_LIGHTSABER = register(new Builder("gray_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item BLACK_LIGHTSABER = register(new Builder("black_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item BROWN_LIGHTSABER = register(new Builder("brown_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item RED_LIGHTSABER = register(new Builder("red_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item ORANGE_LIGHTSABER = register(new Builder("orange_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item YELLOW_LIGHTSABER = register(new Builder("yellow_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item LIME_LIGHTSABER = register(new Builder("lime_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item GREEN_LIGHTSABER = register(new Builder("green_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item CYAN_LIGHTSABER = register(new Builder("cyan_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item LIGHT_BLUE_LIGHTSABER = register(new Builder("light_blue_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item BLUE_LIGHTSABER = register(new Builder("blue_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item PURPLE_LIGHTSABER = register(new Builder("purple_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item MAGENTA_LIGHTSABER = register(new Builder("magenta_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item PINK_LIGHTSABER = register(new Builder("pink_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));

	/* SPECIAL */
	public static final Item DUCK_SPAWN_EGG = register(new Builder("duck_spawn_egg", new SpawnEggItem(MubbleEntities.DUCK, 10592673, 15904341, new Item.Settings().group(ItemGroup.MISC))));
	public static final Item ZOMBIE_COWMAN_SPAWN_EGG = register(new Builder("zombie_cowman_spawn_egg", new SpawnEggItem(MubbleEntities.ZOMBIE_COWMAN, 2957585, 5009705, new Item.Settings().group(ItemGroup.MISC))));

	public static final Item TOAD_SPAWN_EGG = register(new Builder("toad_spawn_egg", new SpawnEggItem(MubbleEntities.TOAD, 14671839, 16722728, new Item.Settings().group(ItemGroup.MISC))));
	public static final Item CHINCHO_SPAWN_EGG = register(new Builder("chincho_spawn_egg", new SpawnEggItem(MubbleEntities.CHINCHO, 7527671, 4903, new Item.Settings().group(ItemGroup.MISC))));
	public static final Item GOOMBA_SPAWN_EGG = register(new Builder("goomba_spawn_egg", new SpawnEggItem(MubbleEntities.GOOMBA, 10839375, 12097909, new Item.Settings().group(ItemGroup.MISC))));
}