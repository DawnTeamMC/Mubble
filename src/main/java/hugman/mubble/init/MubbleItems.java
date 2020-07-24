package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.init.data.MubbleFoods;
import hugman.mubble.init.data.MubbleItemTiers;
import hugman.mubble.object.item.AxeItem;
import hugman.mubble.object.item.HoeItem;
import hugman.mubble.object.item.PickaxeItem;
import hugman.mubble.object.item.*;
import hugman.mubble.util.MoreWordUtils;
import hugman.mubble.util.entry.ItemEntry;
import hugman.mubble.util.entry.ItemEntry.Builder;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;

public class MubbleItems {
	/* MUBBLE */
	public static final Item WHEAT_FLOUR = new Builder("wheat_flour", new Item(new Item.Settings().group(ItemGroup.FOOD))).build();
	public static final Item TOMATO = new Builder("tomato", new BlockItem(MubbleBlocks.TOMATOES, new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.TOMATO))).build();
	public static final Item SALAD = new Builder("salad", new BlockItem(MubbleBlocks.SALAD, new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SALAD))).build();
	public static final Item CHEESE = new Builder("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CHEESE))).build();
	public static final Item BANANA = new Builder("banana", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BANANA))).build();
	public static final Item APRICOT = new Builder("apricot", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.APRICOT))).build();
	public static final Item MANGO = new Builder("mango", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.MANGO))).build();
	public static final Item BLUEBERRIES = new Builder("blueberries", new BlockItem(MubbleBlocks.BLUEBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BLUEBERRIES))).build();
	public static final Item DUCK = new Builder("duck", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.DUCK))).build();
	public static final Item COOKED_DUCK = new Builder("cooked_duck", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.COOKED_DUCK))).build();
	public static final Item CARAMEL_CUBE = new Builder("caramel_cube", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CARAMEL_CUBE))).build();
	public static final Item BAGUETTE = new Builder("baguette", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BAGUETTE))).build();
	public static final Item BURGER = new Builder("burger", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BURGER))).build();
	public static final Item CREPE = new Builder("crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CREPE))).build();
	public static final Item SUGAR_CREPE = new Builder("sugar_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SUGAR_CREPE))).build();
	public static final Item CHOCOLATE_CREPE = new Builder("chocolate_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CHOCOLATE_CREPE))).build();
	public static final Item CARAMEL_CREPE = new Builder("caramel_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CARAMEL_CREPE))).build();
	public static final Item HONEY_CREPE = new Builder("honey_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.HONEY_CREPE))).build();
	public static final Item SWEET_BERRY_CREPE = new Builder("sweet_berry_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SWEET_BERRY_CREPE))).build();
	public static final Item BLUEBERRY_CREPE = new Builder("blueberry_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.BLUEBERRY_CREPE))).build();
	public static final Item CANDY_CANE = new Builder("candy_cane", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.CANDY_CANE))).build();
	public static final Item SMALL_BULB = new Builder("small_bulb", new SmallBulbItem(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item JINGLE_BELLS = new Builder("jingle_bells", new ShakeInstrumentItem(new Item.Settings().group(MubbleTabs.INSTRUMENTS).maxCount(1), MubbleSounds.ITEM_JINGLE_BELLS_USE)).build();
	public static final Item VANADIUM = new Builder("vanadium", new Item(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item BISMUTH_DUST = new Builder("bismuth_dust", new Item(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item CRYSTALLIZED_BISMUTH = new Builder("crystallized_bismuth", new Item(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item BISMUTH_SWORD = new Builder("bismuth_sword", new SwordItem(MubbleItemTiers.BISMUTH, 4, -2.4F, new Item.Settings().group(ItemGroup.COMBAT))).build();
	public static final Item BISMUTH_SHOVEL = new Builder("bismuth_shovel", new ShovelItem(MubbleItemTiers.BISMUTH, 1.5F, -3.0F, new Item.Settings().group(ItemGroup.TOOLS))).build();
	public static final Item BISMUTH_PICKAXE = new Builder("bismuth_pickaxe", new PickaxeItem(MubbleItemTiers.BISMUTH, 1, -2.4F, new Item.Settings().group(ItemGroup.TOOLS))).build();
	public static final Item BISMUTH_AXE = new Builder("bismuth_axe", new AxeItem(MubbleItemTiers.BISMUTH, 4.5F, -2.5F, new Item.Settings().group(ItemGroup.TOOLS))).build();
	public static final Item BISMUTH_HOE = new Builder("bismuth_hoe", new HoeItem(MubbleItemTiers.BISMUTH, -1, 0.0F, new Item.Settings().group(ItemGroup.TOOLS))).build();
	public static final Item PERMAFROST_BRICK = new Builder("permafrost_brick", new Item(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item BANDAGE = new Builder("bandage", new BandageItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16))).build();
	public static final Item TOTEM_OF_ASCENDING = new Builder("totem_of_ascending", new TotemOfAscendingItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON))).build();

	/* SUPER MARIO */
	public static final Item SUPER_MUSHROOM = new Builder("super_mushroom", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.SUPER_MUSHROOM))).build();
	public static final Item PEACH = new Builder("peach", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFoods.PEACH))).build();
	public static final Item FIREBALL = new Builder("fireball", new FireballItem(new Item.Settings().group(ItemGroup.COMBAT))).build();
	public static final Item ICEBALL = new Builder("iceball", new IceballItem(new Item.Settings().group(ItemGroup.COMBAT))).build();
	public static final Item CAPE_FEATHER = new Builder("cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION))).build();
	public static final Item SUPER_CAPE_FEATHER = new Builder("super_cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION).maxCount(1).rarity(Rarity.EPIC))).build();
	public static final Item SUPER_STAR = new Builder("super_star", new SuperStarItem(new Item.Settings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE).food(MubbleFoods.SUPER_STAR))).build();
	public static final Item YELLOW_COIN = new Builder("yellow_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item RED_COIN = new Builder("red_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item BLUE_COIN = new Builder("blue_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))).build();
	public static final Item SMB_KEY = new Builder("smb_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMB_KEY_DOOR)).build();
	public static final Item SMB3_KEY = new Builder("smb3_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMB3_KEY_DOOR)).build();
	public static final Item SMW_KEY = new Builder("smw_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMW_KEY_DOOR)).build();
	public static final Item NSMBU_KEY = new Builder("nsmbu_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.NSMBU_KEY_DOOR)).build();

	/* KIRBY */
	public static final Item KIRBY_BALL = new Builder("kirby_ball", new KirbyBallItem(new Item.Settings().group(ItemGroup.COMBAT))).build();

	/* FIRE EMBLEM */
	//public static final Item AYMR = new Builder("aymr", new AymrItem(new Item.Settings().group(ItemGroup.COMBAT))).create();

	/* SUPER SMASH BROS. */
	public static final Item SMASH_BALL = new Builder("smash_ball", new SmashBallItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.RARE))).build();

	/* STAR WARS */
	public static final Item WHITE_LIGHTSABER = new Builder("white_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item LIGHT_GRAY_LIGHTSABER = new Builder("light_gray_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item GRAY_LIGHTSABER = new Builder("gray_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item BLACK_LIGHTSABER = new Builder("black_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item BROWN_LIGHTSABER = new Builder("brown_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item RED_LIGHTSABER = new Builder("red_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item ORANGE_LIGHTSABER = new Builder("orange_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item YELLOW_LIGHTSABER = new Builder("yellow_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item LIME_LIGHTSABER = new Builder("lime_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item GREEN_LIGHTSABER = new Builder("green_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item CYAN_LIGHTSABER = new Builder("cyan_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item LIGHT_BLUE_LIGHTSABER = new Builder("light_blue_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item BLUE_LIGHTSABER = new Builder("blue_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item PURPLE_LIGHTSABER = new Builder("purple_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item MAGENTA_LIGHTSABER = new Builder("magenta_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();
	public static final Item PINK_LIGHTSABER = new Builder("pink_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))).build();

	/* SPECIAL */
	public static final Item DUCK_SPAWN_EGG = new Builder("duck_spawn_egg", new SpawnEggItem(MubbleEntities.DUCK, 10592673, 15904341, new Item.Settings().group(ItemGroup.MISC))).build();
	public static final Item ZOMBIE_COWMAN_SPAWN_EGG = new Builder("zombie_cowman_spawn_egg", new SpawnEggItem(MubbleEntities.ZOMBIE_COWMAN, 2957585, 5009705, new Item.Settings().group(ItemGroup.MISC))).build();

	public static final Item TOAD_SPAWN_EGG = new Builder("toad_spawn_egg", new SpawnEggItem(MubbleEntities.TOAD, 14671839, 16722728, new Item.Settings().group(ItemGroup.MISC))).build();
	public static final Item CHINCHO_SPAWN_EGG = new Builder("chincho_spawn_egg", new SpawnEggItem(MubbleEntities.CHINCHO, 7527671, 4903, new Item.Settings().group(ItemGroup.MISC))).build();
	public static final Item GOOMBA_SPAWN_EGG = new Builder("goomba_spawn_egg", new SpawnEggItem(MubbleEntities.GOOMBA, 10839375, 12097909, new Item.Settings().group(ItemGroup.MISC))).build();
}