package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.item.ItemCapeFeather;
import hugman.mod.objects.item.ItemFood;
import hugman.mod.objects.item.ItemSeedFood;
import hugman.mod.objects.item.ItemSimple;
import hugman.mod.objects.item.ItemSmashBall;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

public class MubbleItems
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item CARAMEL_CUBE = new ItemFood("caramel_cube", 4, 2.8f, false);
	public static final Item CREPE = new ItemFood("crepe", 3, 0.5f, false);
	public static final Item CHOCOLATE_CREPE = new ItemFood("chocolate_crepe", 8, 2f, false);
	public static final Item CARAMEL_CREPE = new ItemFood("caramel_crepe", 9, 3.4f, false);
	public static final Item CANDY_CANE = new ItemFood("candy_cane", 4, 1.8f, false);
	public static final Item BURGER = new ItemFood("burger", 7, 2f, false);
	public static final Item TOMATO = new ItemSeedFood("tomato", 3, 0.6f);
	public static final Item SALAD = new ItemSeedFood("salad", 1, 0.2F);
	
	public static final Item SUPER_MUSHROOM = new ItemFood("super_mushroom", 3, 1f, false).setPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 550, 1), 1F);
	public static final Item PEACH = new ItemFood("peach", 4, 2.4f, false);
	public static final Item CAPE_FEATHER = new ItemCapeFeather("cape_feather");
	public static final Item SUPER_CAPE_FEATHER = new ItemCapeFeather("super_cape_feather");
	public static final Item YELLOW_COIN = new ItemSimple("yellow_coin");
	public static final Item RED_COIN = new ItemSimple("red_coin");
	public static final Item BLUE_COIN = new ItemSimple("blue_coin");
	public static final Item SMASH_BALL = new ItemSmashBall();
	
	public static final Item SUPER_CROWN = new ItemSimple("super_crown");
	
    public static void register(Item item)
    {
    	ITEMS.add(item);
    }
}
