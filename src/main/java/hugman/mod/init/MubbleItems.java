package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.item.ItemAnnoyingDog;
import hugman.mod.objects.item.ItemBandage;
import hugman.mod.objects.item.ItemCapeFeather;
import hugman.mod.objects.item.ItemFood;
import hugman.mod.objects.item.ItemMusicDisc;
import hugman.mod.objects.item.ItemSeedFood;
import hugman.mod.objects.item.ItemSimple;
import hugman.mod.objects.item.ItemSmashBall;
import hugman.mod.objects.item.ItemSpawnEgg;
import hugman.mod.objects.item.ItemSuperStar;
import hugman.mod.objects.item.ItemTotemOfAscending;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

public class MubbleItems
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item
	BANDAGE = new ItemBandage(),
	CARAMEL_CUBE = new ItemFood("caramel_cube", 4, 2.8f, false),
	CREPE = new ItemFood("crepe", 3, 0.5f, false),
	CHOCOLATE_CREPE = new ItemFood("chocolate_crepe", 8, 2f, false),
	CARAMEL_CREPE = new ItemFood("caramel_crepe", 9, 3.4f, false),
	CANDY_CANE = new ItemFood("candy_cane", 4, 1.8f, false),
	BURGER = new ItemFood("burger", 7, 2f, false),
	TOMATO = new ItemSeedFood("tomato", 3, 0.6f),
	SALAD = new ItemSeedFood("salad", 1, 0.2F),
	CHEESE = new ItemFood("cheese", 2, 0.4F, false),
	WHEAT_FLOUR = new ItemSimple("wheat_flour"),
	VANADIUM = new ItemSimple("vanadium"),
	TOTEM_OF_ASCENDING = new ItemTotemOfAscending(),
	
	SUPER_MUSHROOM = new ItemFood("super_mushroom", 3, 1f, false).setPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 550, 1), 1F),
	PEACH = new ItemFood("peach", 4, 2.4f, false),
	CAPE_FEATHER = new ItemCapeFeather("cape_feather"),
	SUPER_CAPE_FEATHER = new ItemCapeFeather("super_cape_feather", EnumRarity.EPIC),
	SUPER_STAR = new ItemSuperStar(),
	YELLOW_COIN = new ItemSimple("yellow_coin"),
	RED_COIN = new ItemSimple("red_coin"),
	BLUE_COIN = new ItemSimple("blue_coin"),
	
	SMASH_BALL = new ItemSmashBall(),

	ANNOYING_DOG = new ItemAnnoyingDog(),
	
	BLANK_MUSIC_DISK = new ItemSimple("blank_music_disc", 1, EnumRarity.RARE),
	MUSIC_DISC_BATTLEFIELD = new ItemMusicDisc("music_disc_battlefield", MubbleSounds.MUSIC_DISC_BATTLEFIELD, 1),
	MUSIC_DISC_CHAMPIONS_ROAD = new ItemMusicDisc("music_disc_champions_road", MubbleSounds.MUSIC_DISC_CHAMPIONS_ROAD, 1),
	MUSIC_DISC_CONFRONTING_MYSELF = new ItemMusicDisc("music_disc_confronting_myself", MubbleSounds.MUSIC_DISC_CONFRONTING_MYSELF, 1),
	MUSIC_DISC_ELECTROPLANKTON_MELDEY = new ItemMusicDisc("music_disc_electroplankton_medley", MubbleSounds.MUSIC_DISC_ELECTROPLANKTON_MELDEY, 1),
	MUSIC_DISC_FIELDS_OF_HOPES_AND_DREAMS = new ItemMusicDisc("music_disc_fields_of_hopes_and_dreams", MubbleSounds.MUSIC_DISC_FIELDS_OF_HOPES_AND_DREAMS, 1),
	MUSIC_DISC_KASS_THEME = new ItemMusicDisc("music_disc_kass_theme", MubbleSounds.MUSIC_DISC_KASS_THEME, 1),
	MUSIC_DISC_LOST_PAINTING = new ItemMusicDisc("music_disc_lost_painting", MubbleSounds.MUSIC_DISC_LOST_PAINTING, 1),
	MUSIC_DISC_MAD_MEW_MEW = new ItemMusicDisc("music_disc_mad_mew_mew", MubbleSounds.MUSIC_DISC_MAD_MEW_MEW, 1),
	MUSIC_DISC_MEGALOVANIA = new ItemMusicDisc("music_disc_megalovania", MubbleSounds.MUSIC_DISC_MEGALOVANIA, 1),
	MUSIC_DISC_POPPLE_BATTLE = new ItemMusicDisc("music_disc_popple_battle", MubbleSounds.MUSIC_DISC_POPPLE_BATTLE, 1),
	MUSIC_DISC_REFLECTION = new ItemMusicDisc("music_disc_reflection", MubbleSounds.MUSIC_DISC_REFLECTION, 1),
	MUSIC_DISC_RUDE_BUSTER = new ItemMusicDisc("music_disc_rude_buster", MubbleSounds.MUSIC_DISC_RUDE_BUSTER, 1),
	MUSIC_DISC_SHARK_BYTES = new ItemMusicDisc("music_disc_shark_bytes", MubbleSounds.MUSIC_DISC_SHARK_BYTES, 1),
	MUSIC_DISC_SOUND_STAGE = new ItemMusicDisc("music_disc_sound_stage", MubbleSounds.MUSIC_DISC_SOUND_STAGE, 1),
	MUSIC_DISC_STUDIOPOLIS_ACT1 = new ItemMusicDisc("music_disc_studiopolis_act1", MubbleSounds.MUSIC_DISC_STUDIOPOLIS_ACT1, 1),
	MUSIC_DISC_THE_GRAND_FINALE = new ItemMusicDisc("music_disc_the_grand_finale", MubbleSounds.MUSIC_DISC_THE_GRAND_FINALE, 1),
	MUSIC_DISC_TYPE_A = new ItemMusicDisc("music_disc_type_a", MubbleSounds.MUSIC_DISC_TYPE_A, 1),
	MUSIC_DISC_VAMPIRE_KILLER = new ItemMusicDisc("music_disc_vampire_killer", MubbleSounds.MUSIC_DISC_VAMPIRE_KILLER, 1),
	MUSIC_DISC_VS_CHAMPION_RED_AND_BLUE = new ItemMusicDisc("music_disc_vs_champion_red_and_blue", MubbleSounds.MUSIC_DISC_VS_CHAMPION_RED_AND_BLUE, 1),
	REMIX_DISC_NB_BEWARE_THE_FOREST_MUSHROOMS = new ItemMusicDisc("remix_disc_nb_beware_the_forest_mushrooms", MubbleSounds.REMIX_DISC_NB_BEWARE_THE_FOREST_MUSHROOMS, 2),
	REMIX_DISC_NB_BUOY_BASE_GALAXY = new ItemMusicDisc("remix_disc_nb_buoy_base_galaxy", MubbleSounds.REMIX_DISC_NB_BUOY_BASE_GALAXY, 2),
	REMIX_DISC_NB_FLY_OCTO_FLY = new ItemMusicDisc("remix_disc_nb_fly_octo_fly", MubbleSounds.REMIX_DISC_NB_FLY_OCTO_FLY, 2),
	REMIX_DISC_NB_HARVEST_HAZARDS = new ItemMusicDisc("remix_disc_nb_harvest_hazards", MubbleSounds.REMIX_DISC_NB_HARVEST_HAZARDS, 2),
	REMIX_DISC_NB_SWEDEN = new ItemMusicDisc("remix_disc_nb_sweden", MubbleSounds.REMIX_DISC_NB_SWEDEN, 2),
	REMIX_DISC_NB_WALUIGI_PINBALL = new ItemMusicDisc("remix_disc_nb_waluigi_pinball", MubbleSounds.REMIX_DISC_NB_WALUIGI_PINBALL, 2),
	
	TOAD_SPAWN_EGG = new ItemSpawnEgg("toad", MubbleEntities.TOAD, 14671839, 16722728),
	CHINCHO_SPAWN_EGG = new ItemSpawnEgg("chincho", MubbleEntities.CHINCHO, 7527671, 4903);
	
	public static void register(Item item)
	{
		ITEMS.add(item);
	}
}