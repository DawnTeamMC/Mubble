package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.mubble.init.data.MubbleFood;
import com.hugman.mubble.object.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;

public class MubbleItems extends MubblePack {
	/* MUBBLE */
	public static final Item CARAMEL_CUBE = register(new ItemCreator.Builder("caramel_cube", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CARAMEL_CUBE))));
	public static final Item BAGUETTE = register(new ItemCreator.Builder("baguette", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.BAGUETTE))).compostingChance(0.85F));
	public static final Item CREPE = register(new ItemCreator.Builder("crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CREPE))).compostingChance(0.65F));
	public static final Item SUGAR_CREPE = register(new ItemCreator.Builder("sugar_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.SUGAR_CREPE))).compostingChance(0.85F));
	public static final Item CHOCOLATE_CREPE = register(new ItemCreator.Builder("chocolate_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CHOCOLATE_CREPE))).compostingChance(0.85F));
	public static final Item CARAMEL_CREPE = register(new ItemCreator.Builder("caramel_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CARAMEL_CREPE))).compostingChance(0.85F));
	public static final Item HONEY_CREPE = register(new ItemCreator.Builder("honey_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.HONEY_CREPE))).compostingChance(0.85F));
	public static final Item SWEET_BERRY_CREPE = register(new ItemCreator.Builder("sweet_berry_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.SWEET_BERRY_CREPE))).compostingChance(0.85F));
	public static final Item CANDY_CANE = register(new ItemCreator.Builder("candy_cane", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CANDY_CANE))).compostingChance(0.85F));
	public static final Item SMALL_BULB = register(new ItemCreator.Builder("small_bulb", new SmallBulbItem(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item JINGLE_BELLS = register(new ItemCreator.Builder("jingle_bells", new ShakeInstrumentItem(new Item.Settings().group(MubbleItemGroups.INSTRUMENTS_GROUP).maxCount(1), MubbleSounds.ITEM_JINGLE_BELLS_USE)));
	public static final Item BANDAGE = register(new ItemCreator.Builder("bandage", new BandageItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16))));
	/* SUPER MARIO */
	public static final Item SUPER_MUSHROOM = register(new ItemCreator.Builder("super_mushroom", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.SUPER_MUSHROOM))));
	public static final Item PEACH = register(new ItemCreator.Builder("peach", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.PEACH))));
	public static final Item FIREBALL = register(new ItemCreator.Builder("fireball", new FireballItem(new Item.Settings().group(ItemGroup.COMBAT))));
	public static final Item ICEBALL = register(new ItemCreator.Builder("iceball", new IceballItem(new Item.Settings().group(ItemGroup.COMBAT))));
	public static final Item CAPE_FEATHER = register(new ItemCreator.Builder("cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION))));
	public static final Item SUPER_CAPE_FEATHER = register(new ItemCreator.Builder("super_cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION).maxCount(1).rarity(Rarity.EPIC))));
	public static final Item SUPER_STAR = register(new ItemCreator.Builder("super_star", new SuperStarItem(new Item.Settings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE).food(MubbleFood.SUPER_STAR))));
	public static final Item YELLOW_COIN = register(new ItemCreator.Builder("yellow_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item RED_COIN = register(new ItemCreator.Builder("red_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item BLUE_COIN = register(new ItemCreator.Builder("blue_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item SMB_KEY = register(new ItemCreator.Builder("smb_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMB_KEY_DOOR)));
	public static final Item SMB3_KEY = register(new ItemCreator.Builder("smb3_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMB3_KEY_DOOR)));
	public static final Item SMW_KEY = register(new ItemCreator.Builder("smw_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.SMW_KEY_DOOR)));
	public static final Item NSMBU_KEY = register(new ItemCreator.Builder("nsmbu_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlocks.NSMBU_KEY_DOOR)));
	/* KIRBY */
	public static final Item KIRBY_BALL = register(new ItemCreator.Builder("kirby_ball", new KirbyBallItem(new Item.Settings().group(ItemGroup.COMBAT))));
	/* SUPER SMASH BROS. */
	public static final Item SMASH_BALL = register(new ItemCreator.Builder("smash_ball", new SmashBallItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.RARE))));

	/* FIRE EMBLEM */
	//public static final Item AYMR = register(new ItemCreator.Builder("aymr", new AymrItem(new Item.Settings().group(ItemGroup.COMBAT))));
	/* STAR WARS */
	public static final Item WHITE_LIGHTSABER = register(new ItemCreator.Builder("white_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item LIGHT_GRAY_LIGHTSABER = register(new ItemCreator.Builder("light_gray_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item GRAY_LIGHTSABER = register(new ItemCreator.Builder("gray_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item BLACK_LIGHTSABER = register(new ItemCreator.Builder("black_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item BROWN_LIGHTSABER = register(new ItemCreator.Builder("brown_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item RED_LIGHTSABER = register(new ItemCreator.Builder("red_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item ORANGE_LIGHTSABER = register(new ItemCreator.Builder("orange_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item YELLOW_LIGHTSABER = register(new ItemCreator.Builder("yellow_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item LIME_LIGHTSABER = register(new ItemCreator.Builder("lime_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item GREEN_LIGHTSABER = register(new ItemCreator.Builder("green_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item CYAN_LIGHTSABER = register(new ItemCreator.Builder("cyan_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item LIGHT_BLUE_LIGHTSABER = register(new ItemCreator.Builder("light_blue_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item BLUE_LIGHTSABER = register(new ItemCreator.Builder("blue_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item PURPLE_LIGHTSABER = register(new ItemCreator.Builder("purple_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item MAGENTA_LIGHTSABER = register(new ItemCreator.Builder("magenta_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));
	public static final Item PINK_LIGHTSABER = register(new ItemCreator.Builder("pink_lightsaber", new LightsaberItem(new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC))));

	public static void init() {

	}

	public static class Settings {
		protected static final Item.Settings SPAWN_EGG = new Item.Settings().group(ItemGroup.MISC);
	}
}