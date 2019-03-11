package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.item.ItemAnnoyingDog;
import hugman.mod.objects.item.ItemCapeFeather;
import hugman.mod.objects.item.ItemFood;
import hugman.mod.objects.item.ItemMusicDisc;
import hugman.mod.objects.item.ItemSeedFood;
import hugman.mod.objects.item.ItemSimple;
import hugman.mod.objects.item.ItemSmashBall;
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
	CARAMEL_CUBE = new ItemFood("caramel_cube", 4, 2.8f, false),
	CREPE = new ItemFood("crepe", 3, 0.5f, false),
	CHOCOLATE_CREPE = new ItemFood("chocolate_crepe", 8, 2f, false),
	CARAMEL_CREPE = new ItemFood("caramel_crepe", 9, 3.4f, false),
	CANDY_CANE = new ItemFood("candy_cane", 4, 1.8f, false),
	BURGER = new ItemFood("burger", 7, 2f, false),
	TOMATO = new ItemSeedFood("tomato", 3, 0.6f),
	SALAD = new ItemSeedFood("salad", 1, 0.2F),
	CHEESE = new ItemFood("cheese", 2, 0.4F, false),
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
	
	RECORD_BLANK = new ItemSimple("record_blank", 1),
	RECORD_CHAMPIONS_ROAD = new ItemMusicDisc("champions_road", MubbleSounds.RECORD_CHAMPIONS_ROAD, 1),
	RECORD_VAMPIRE_KILLER = new ItemMusicDisc("vampire_killer", MubbleSounds.RECORD_VAMPIRE_KILLER, 1),
	RECORD_FLY_OCTO_FLY = new ItemMusicDisc("fly_octo_fly", MubbleSounds.RECORD_FLY_OCTO_FLY, 1),
	RECORD_NB_SWEDEN = new ItemMusicDisc("nb_sweden", MubbleSounds.RECORD_NB_SWEDEN, 2),
	RECORD_NB_BUOY_BASE_GALAXY = new ItemMusicDisc("nb_buoy_base_galaxy", MubbleSounds.RECORD_NB_BUOY_BASE_GALAXY, 2),
	RECORD_NB_WALUIGI_PINBALL = new ItemMusicDisc("nb_waluigi_pinball", MubbleSounds.RECORD_NB_WALUIGI_PINBALL, 2),
	RECORD_NB_HARVEST_HAZARDS = new ItemMusicDisc("nb_harvest_hazards", MubbleSounds.RECORD_NB_HARVEST_HAZARDS, 2);
	
    public static void register(Item item)
    {
    	ITEMS.add(item);
    }
}
