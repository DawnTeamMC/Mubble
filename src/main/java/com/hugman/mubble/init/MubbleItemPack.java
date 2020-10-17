package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.ItemCreator.Builder;
import com.hugman.dawn.api.object.item.AxeItem;
import com.hugman.dawn.api.object.item.HoeItem;
import com.hugman.dawn.api.object.item.PickaxeItem;
import com.hugman.mubble.init.data.MubbleFood;
import com.hugman.mubble.init.data.MubbleItemTiers;
import com.hugman.mubble.object.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Rarity;

public class MubbleItemPack extends MubblePack {
	/* MUBBLE */
	public static final Item WHEAT_FLOUR = register(new Builder("wheat_flour", new Item(new Item.Settings().group(ItemGroup.FOOD))).compostingChance(0.3F));
	public static final Item CHEESE = register(new Builder("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CHEESE))).compostingChance(0.5F));
	public static final Item DUCK = register(new Builder("duck", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.DUCK))));
	public static final Item COOKED_DUCK = register(new Builder("cooked_duck", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.COOKED_DUCK))));
	public static final Item CARAMEL_CUBE = register(new Builder("caramel_cube", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CARAMEL_CUBE))));
	public static final Item BAGUETTE = register(new Builder("baguette", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.BAGUETTE))).compostingChance(0.85F));
	public static final Item CREPE = register(new Builder("crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CREPE))).compostingChance(0.65F));
	public static final Item SUGAR_CREPE = register(new Builder("sugar_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.SUGAR_CREPE))).compostingChance(0.85F));
	public static final Item CHOCOLATE_CREPE = register(new Builder("chocolate_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CHOCOLATE_CREPE))).compostingChance(0.85F));
	public static final Item CARAMEL_CREPE = register(new Builder("caramel_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CARAMEL_CREPE))).compostingChance(0.85F));
	public static final Item HONEY_CREPE = register(new Builder("honey_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.HONEY_CREPE))).compostingChance(0.85F));
	public static final Item SWEET_BERRY_CREPE = register(new Builder("sweet_berry_crepe", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.SWEET_BERRY_CREPE))).compostingChance(0.85F));
	public static final Item CANDY_CANE = register(new Builder("candy_cane", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.CANDY_CANE))).compostingChance(0.85F));
	public static final Item SMALL_BULB = register(new Builder("small_bulb", new SmallBulbItem(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item JINGLE_BELLS = register(new Builder("jingle_bells", new ShakeInstrumentItem(new Item.Settings().group(MubbleItemGroups.INSTRUMENTS_GROUP).maxCount(1), MubbleSoundPack.ITEM_JINGLE_BELLS_USE)));
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
	public static final Item SUPER_MUSHROOM = register(new Builder("super_mushroom", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.SUPER_MUSHROOM))));
	public static final Item PEACH = register(new Builder("peach", new Item(new Item.Settings().group(ItemGroup.FOOD).food(MubbleFood.PEACH))));
	public static final Item FIREBALL = register(new Builder("fireball", new FireballItem(new Item.Settings().group(ItemGroup.COMBAT))));
	public static final Item ICEBALL = register(new Builder("iceball", new IceballItem(new Item.Settings().group(ItemGroup.COMBAT))));
	public static final Item CAPE_FEATHER = register(new Builder("cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION))));
	public static final Item SUPER_CAPE_FEATHER = register(new Builder("super_cape_feather", new CapeFeatherItem(new Item.Settings().group(ItemGroup.TRANSPORTATION).maxCount(1).rarity(Rarity.EPIC))));
	public static final Item SUPER_STAR = register(new Builder("super_star", new SuperStarItem(new Item.Settings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE).food(MubbleFood.SUPER_STAR))));
	public static final Item YELLOW_COIN = register(new Builder("yellow_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item RED_COIN = register(new Builder("red_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item BLUE_COIN = register(new Builder("blue_coin", new Item(new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item SMB_KEY = register(new Builder("smb_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlockPack.SMB_KEY_DOOR)));
	public static final Item SMB3_KEY = register(new Builder("smb3_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlockPack.SMB3_KEY_DOOR)));
	public static final Item SMW_KEY = register(new Builder("smw_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlockPack.SMW_KEY_DOOR)));
	public static final Item NSMBU_KEY = register(new Builder("nsmbu_key", new KeyItem(new Item.Settings().group(ItemGroup.MATERIALS), MubbleBlockPack.NSMBU_KEY_DOOR)));

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

	public static class Settings {
		protected static final Item.Settings SPAWN_EGG = new Item.Settings().group(ItemGroup.MISC);
	}
}