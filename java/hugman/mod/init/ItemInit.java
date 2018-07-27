package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Main;
import hugman.mod.objects.items.ItemBase;
import hugman.mod.objects.items.ItemEdible;
import hugman.mod.objects.items.ItemEdibleEffect;
import hugman.mod.objects.items.ItemHelmet;
import hugman.mod.objects.items.ItemMusicDisc;
import hugman.mod.objects.items.ItemSuperStar;
import hugman.mod.util.handlers.SoundHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemInit
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	// Vanilla
	public static final Item VANADIUM = new ItemBase("vanadium", CreativeTabs.MISC);
	public static final Item WHEAT_FLOUR = new ItemBase("wheat_flour", CreativeTabs.MISC);

	// Extra
	public static final Item CARAMEL_CUBE = new ItemEdible("caramel_cube", CreativeTabs.FOOD, 4, 2.8f, false);
	public static final Item CREPE = new ItemEdible("crepe", CreativeTabs.FOOD, 3, 0.5f, false);
	public static final Item CHOCOLATE_CREPE = new ItemEdible("chocolate_crepe", CreativeTabs.FOOD, 8, 2f, false);
	public static final Item CARAMEL_CREPE = new ItemEdible("caramel_crepe", CreativeTabs.FOOD, 9, 3.4f, false);
	
	// Mario
	public static final Item SUPER_MUSHROOM = new ItemEdibleEffect("super_mushroom", Main.NINTENDO_ITEMS, 3, 1f, false, new PotionEffect(Potion.getPotionById(8), 550, 1));
	public static final Item PEACH = new ItemEdible("peach", Main.NINTENDO_ITEMS, 4, 2.4f, true);
	public static final Item SUPER_STAR = new ItemSuperStar();
	public static final Item YELLOW_COIN = new ItemBase("yellow_coin", Main.NINTENDO_ITEMS);
	public static final Item RED_COIN = new ItemBase("red_coin", Main.NINTENDO_ITEMS);
	public static final Item BLUE_COIN = new ItemBase("blue_coin", Main.NINTENDO_ITEMS);
	public static final Item RECORD_CHAMPIONS_ROAD = new ItemMusicDisc("champions_road", SoundHandler.RECORD_CHAMPIONS_ROAD, Main.NINTENDO_ITEMS);
	
	// Splatoon
	// public static final Item RECORD_EBB_FLOW_DEMO = new ItemMusicDisc("ebb_flow_demo", SoundHandler.RECORD_EBB_FLOW_DEMO, null);
}
