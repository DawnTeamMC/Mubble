package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Mubble;
import hugman.mod.objects.item.ItemAnnoyingDog;
import hugman.mod.objects.item.ItemBandage;
import hugman.mod.objects.item.ItemCapeFeather;
import hugman.mod.objects.item.ItemSeedFood;
import hugman.mod.objects.item.ItemSmashBall;
import hugman.mod.objects.item.ItemSuperStar;
import hugman.mod.objects.item.ItemTotemOfAscending;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemSpawnEgg;
import net.minecraft.potion.PotionEffect;

public class MubbleItems
{
	/* All Content Bag */
    public static final List<Item> ITEMS = new ArrayList<Item>();
    
    /* Templates */
    public static final Item.Properties pFood = new Item.Properties().group(ItemGroup.FOOD);
    public static final Item.Properties pMaterials = new Item.Properties().group(ItemGroup.MATERIALS);
    public static final Item.Properties pMisc = new Item.Properties().group(ItemGroup.MISC);
    public static final Item.Properties pCombat = new Item.Properties().group(ItemGroup.COMBAT);
    public static final Item.Properties pTransportation = new Item.Properties().group(ItemGroup.TRANSPORTATION);

    /* MINECRAFT */
    public static final Item WHEAT_FLOUR = item("wheat_flour", new Item(pFood));
    public static final Item TOMATO = item("tomato", new ItemSeedFood(3, 0.6f, pFood));
    public static final Item SALAD = item("salad", new ItemSeedFood(2, 0.7F, pFood));
    public static final Item CHEESE = item("cheese", new ItemFood(2, 0.4F, false, pFood));
    public static final Item BANANA = item("banana", new ItemFood(4, 0.3F, false, pFood));
    public static final Item APRICOT = item("apricot", new ItemFood(4, 0.3F, false, pFood));
    public static final Item MANGO = item("mango", new ItemFood(4, 0.3F, false, pFood));
    public static final Item CARAMEL_CUBE = item("caramel_cube", new ItemFood(4, 2.8f, false, pFood));
    public static final Item BAGUETTE = item("baguette", new ItemFood(7, 0.7F, false, pFood));
    public static final Item BURGER = item("burger", new ItemFood(9, 2.4f, false, pFood));
    public static final Item CREPE = item("crepe", new ItemFood(3, 0.5f, false, pFood));
    public static final Item CHOCOLATE_CREPE = item("chocolate_crepe", new ItemFood(8, 2f, false, pFood));
    public static final Item CARAMEL_CREPE = item("caramel_crepe", new ItemFood(9, 3.4f, false, pFood));
    public static final Item CANDY_CANE = item("candy_cane", new ItemFood(4, 1.8f, false, pFood));
    public static final Item VANADIUM = item("vanadium", new Item(pMaterials));
    public static final Item BISMUTH_DUST = item("bismuth_dust", new Item(pMaterials));
    public static final Item CRYSTALLIZED_BISMUTH = item("crystallized_bismuth", new Item(pMaterials));
    public static final Item PERMAFROST_BRICK = item("permafrost_brick", new Item(pMaterials));
    public static final Item BANDAGE = item("bandage", new ItemBandage(pMisc.maxStackSize(16)));
    public static final Item TOTEM_OF_ASCENDING = item("totem_of_ascending", new ItemTotemOfAscending(pCombat.maxStackSize(1).rarity(EnumRarity.UNCOMMON)));

    /* SUPER MARIO */
    public static final Item SUPER_MUSHROOM = item("super_mushroom", new ItemFood(3, 1f, false, pFood).setPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 550, 1), 1F));
    public static final Item PEACH = item("peach", new ItemFood(4, 2.4f, false, pFood));
    public static final Item CAPE_FEATHER = item("cape_feather", new ItemCapeFeather(pTransportation));
    public static final Item SUPER_CAPE_FEATHER = item("super_cape_feather", new ItemCapeFeather(pTransportation.maxStackSize(1).rarity(EnumRarity.EPIC)));
    public static final Item SUPER_STAR = item("super_star", new ItemSuperStar(3, 2.4F, false, pMisc.maxStackSize(1).rarity(EnumRarity.RARE)));
    public static final Item YELLOW_COIN = item("yellow_coin", new Item(pMaterials));
    public static final Item RED_COIN = item("red_coin", new Item(pMaterials));
    public static final Item BLUE_COIN = item("blue_coin", new Item(pMaterials));
    
    /* SUPER SMASH BROS. */
    public static final Item SMASH_BALL = item("smash_ball", new ItemSmashBall(pCombat.maxStackSize(1).rarity(EnumRarity.RARE)));

    /* UNDERTALE / DELTARUNE */
    public static final Item ANNOYING_DOG = item("annoying_dog", new ItemAnnoyingDog(pMisc.maxStackSize(1)));

    /* SPECIAL */
    public static final Item CHINCHO_SPAWN_EGG = item("chincho_spawn_egg", new ItemSpawnEgg(MubbleEntities.CHINCHO, 7527671, 4903, pMisc));
    public static final Item GOOMBA_SPAWN_EGG = item("goomba_spawn_egg", new ItemSpawnEgg(MubbleEntities.GOOMBA, 10839375, 12097909, pMisc));
    public static final Item TOAD_SPAWN_EGG = item("toad_spawn_egg", new ItemSpawnEgg(MubbleEntities.TOAD, 14671839, 16722728, pMisc));
    public static final Item ZOMBIE_COWMAN_SPAWN_EGG = item("zombie_cowman_spawn_egg", new ItemSpawnEgg(MubbleEntities.ZOMBIE_COWMAN, 2957585, 5009705, pMisc));
    
    public static Item item(String name, Item item)
    {
    	Item fItem = item.setRegistryName(Mubble.MOD_ID, name);
        ITEMS.add(fItem);
		return fItem;
    }
}