package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.item.AnnoyingDogItem;
import hugman.mubble.objects.item.BandageItem;
import hugman.mubble.objects.item.CapeFeatherItem;
import hugman.mubble.objects.item.SmashBallItem;
import hugman.mubble.objects.item.SuperStarItem;
import hugman.mubble.objects.item.TotemOfAscendingItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.SpawnEggItem;

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
    public static final Item TOMATO = item("tomato", new BlockNamedItem(MubbleBlocks.TOMATOES, pFood.food(MubbleFoods.TOMATO)));
    public static final Item SALAD = item("salad", new BlockNamedItem(MubbleBlocks.SALAD, pFood.food(MubbleFoods.SALAD)));
    public static final Item CHEESE = item("cheese", new Item(pFood.food(MubbleFoods.CHEESE)));
    public static final Item BANANA = item("banana", new Item(pFood.food(MubbleFoods.BANANA)));
    public static final Item APRICOT = item("apricot", new Item(pFood.food(MubbleFoods.APRICOT)));
    public static final Item MANGO = item("mango", new Item(pFood.food(MubbleFoods.MANGO)));
    public static final Item BLUEBERRIES = item("blueberries", new BlockNamedItem(MubbleBlocks.BLUEBERRY_BUSH, pFood.food(MubbleFoods.BLUEBERRIES)));
    public static final Item CARAMEL_CUBE = item("caramel_cube", new Item(pFood.food(MubbleFoods.CARAMEL_CUBE)));
    public static final Item BAGUETTE = item("baguette", new Item(pFood.food(MubbleFoods.BAGUETTE)));
    public static final Item BURGER = item("burger", new Item(pFood.food(MubbleFoods.BURGER)));
    public static final Item CREPE = item("crepe", new Item(pFood.food(MubbleFoods.CREPE)));
    public static final Item CHOCOLATE_CREPE = item("chocolate_crepe", new Item(pFood.food(MubbleFoods.CHOCOLATE_CREPE)));
    public static final Item CARAMEL_CREPE = item("caramel_crepe", new Item(pFood.food(MubbleFoods.CARAMEL_CREPE)));
    public static final Item CANDY_CANE = item("candy_cane", new Item(pFood.food(MubbleFoods.CANDY_CANE)));
    public static final Item VANADIUM = item("vanadium", new Item(pMaterials));
    public static final Item BISMUTH_DUST = item("bismuth_dust", new Item(pMaterials));
    public static final Item CRYSTALLIZED_BISMUTH = item("crystallized_bismuth", new Item(pMaterials));
    public static final Item PERMAFROST_BRICK = item("permafrost_brick", new Item(pMaterials));
    public static final Item BANDAGE = item("bandage", new BandageItem(pMisc.maxStackSize(16)));
    public static final Item TOTEM_OF_ASCENDING = item("totem_of_ascending", new TotemOfAscendingItem(pCombat.maxStackSize(1).rarity(Rarity.UNCOMMON)));

    /* SUPER MARIO */
    public static final Item SUPER_MUSHROOM = item("super_mushroom", new Item(pFood.food(MubbleFoods.SUPER_MUSHROOM)));
    public static final Item PEACH = item("peach", new Item(pFood.food(MubbleFoods.PEACH)));
    public static final Item CAPE_FEATHER = item("cape_feather", new CapeFeatherItem(pTransportation));
    public static final Item SUPER_CAPE_FEATHER = item("super_cape_feather", new CapeFeatherItem(pTransportation.maxStackSize(1).rarity(Rarity.EPIC)));
    public static final Item SUPER_STAR = item("super_star", new SuperStarItem(pMisc.maxStackSize(1).rarity(Rarity.RARE).food(MubbleFoods.SUPER_STAR)));
    public static final Item YELLOW_COIN = item("yellow_coin", new Item(pMaterials));
    public static final Item RED_COIN = item("red_coin", new Item(pMaterials));
    public static final Item BLUE_COIN = item("blue_coin", new Item(pMaterials));
    //public static final Item MAKER_TOOL = item("maker_tool", new ItemMakerTool(pMisc.maxStackSize(1)));
    
    /* SUPER SMASH BROS. */
    public static final Item SMASH_BALL = item("smash_ball", new SmashBallItem(pCombat.maxStackSize(1).rarity(Rarity.RARE)));

    /* UNDERTALE / DELTARUNE */
    public static final Item ANNOYING_DOG = item("annoying_dog", new AnnoyingDogItem(pMisc.maxStackSize(1)));

    /* SPECIAL */
    public static final Item CHINCHO_SPAWN_EGG = item("chincho_spawn_egg", new SpawnEggItem(MubbleEntities.CHINCHO, 7527671, 4903, pMisc));
    public static final Item GOOMBA_SPAWN_EGG = item("goomba_spawn_egg", new SpawnEggItem(MubbleEntities.GOOMBA, 10839375, 12097909, pMisc));
    public static final Item TOAD_SPAWN_EGG = item("toad_spawn_egg", new SpawnEggItem(MubbleEntities.TOAD, 14671839, 16722728, pMisc));
    public static final Item ZOMBIE_COWMAN_SPAWN_EGG = item("zombie_cowman_spawn_egg", new SpawnEggItem(MubbleEntities.ZOMBIE_COWMAN, 2957585, 5009705, pMisc));
    
    public static Item item(String name, Item item)
    {
    	Item fItem = item.setRegistryName(Mubble.MOD_ID, name);
        ITEMS.add(fItem);
		return fItem;
    }
}