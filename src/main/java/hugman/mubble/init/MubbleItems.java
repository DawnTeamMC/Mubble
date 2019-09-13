package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.item.BandageItem;
import hugman.mubble.objects.item.CapeFeatherItem;
import hugman.mubble.objects.item.KeyItem;
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

    /* MINECRAFT */
    public static final Item WHEAT_FLOUR = item("wheat_flour", new Item(new Item.Properties().group(ItemGroup.FOOD)));
    public static final Item TOMATO = item("tomato", new BlockNamedItem(MubbleBlocks.TOMATOES, new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.TOMATO)));
    public static final Item SALAD = item("salad", new BlockNamedItem(MubbleBlocks.SALAD, new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.SALAD)));
    public static final Item CHEESE = item("cheese", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.CHEESE)));
    public static final Item BANANA = item("banana", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.BANANA)));
    public static final Item APRICOT = item("apricot", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.APRICOT)));
    public static final Item MANGO = item("mango", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.MANGO)));
    public static final Item BLUEBERRIES = item("blueberries", new BlockNamedItem(MubbleBlocks.BLUEBERRY_BUSH, new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.BLUEBERRIES)));
    public static final Item CARAMEL_CUBE = item("caramel_cube", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.CARAMEL_CUBE)));
    public static final Item BAGUETTE = item("baguette", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.BAGUETTE)));
    public static final Item BURGER = item("burger", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.BURGER)));
    public static final Item CREPE = item("crepe", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.CREPE)));
    public static final Item CHOCOLATE_CREPE = item("chocolate_crepe", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.CHOCOLATE_CREPE)));
    public static final Item CARAMEL_CREPE = item("caramel_crepe", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.CARAMEL_CREPE)));
    public static final Item CANDY_CANE = item("candy_cane", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.CANDY_CANE)));
    public static final Item VANADIUM = item("vanadium", new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final Item BISMUTH_DUST = item("bismuth_dust", new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final Item CRYSTALLIZED_BISMUTH = item("crystallized_bismuth", new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final Item PERMAFROST_BRICK = item("permafrost_brick", new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final Item BANDAGE = item("bandage", new BandageItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(16)));
    public static final Item TOTEM_OF_ASCENDING = item("totem_of_ascending", new TotemOfAscendingItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1).rarity(Rarity.UNCOMMON)));

    /* SUPER MARIO */
    public static final Item SUPER_MUSHROOM = item("super_mushroom", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.SUPER_MUSHROOM)));
    public static final Item PEACH = item("peach", new Item(new Item.Properties().group(ItemGroup.FOOD).food(MubbleFoods.PEACH)));
    public static final Item CAPE_FEATHER = item("cape_feather", new CapeFeatherItem(new Item.Properties().group(ItemGroup.TRANSPORTATION)));
    public static final Item SUPER_CAPE_FEATHER = item("super_cape_feather", new CapeFeatherItem(new Item.Properties().group(ItemGroup.TRANSPORTATION).maxStackSize(1).rarity(Rarity.EPIC)));
    public static final Item SUPER_STAR = item("super_star", new SuperStarItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).rarity(Rarity.RARE).food(MubbleFoods.SUPER_STAR)));
    public static final Item YELLOW_COIN = item("yellow_coin", new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final Item RED_COIN = item("red_coin", new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final Item BLUE_COIN = item("blue_coin", new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final Item SMB_KEY = item("smb_key", new KeyItem(new Item.Properties().group(ItemGroup.MATERIALS), MubbleBlocks.SMB_KEY_DOOR));
    public static final Item SMB3_KEY = item("smb3_key", new KeyItem(new Item.Properties().group(ItemGroup.MATERIALS), MubbleBlocks.SMB3_KEY_DOOR));
    public static final Item SMW_KEY = item("smw_key", new KeyItem(new Item.Properties().group(ItemGroup.MATERIALS), MubbleBlocks.SMW_KEY_DOOR));
    public static final Item NSMBU_KEY = item("nsmbu_key", new KeyItem(new Item.Properties().group(ItemGroup.MATERIALS), MubbleBlocks.NSMBU_KEY_DOOR));
    
    /* SUPER SMASH BROS. */
    public static final Item SMASH_BALL = item("smash_ball", new SmashBallItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1).rarity(Rarity.RARE)));

    /* SPECIAL */
    public static final Item CHINCHO_SPAWN_EGG = item("chincho_spawn_egg", new SpawnEggItem(MubbleEntities.CHINCHO, 7527671, 4903, new Item.Properties().group(ItemGroup.MISC)));
    public static final Item GOOMBA_SPAWN_EGG = item("goomba_spawn_egg", new SpawnEggItem(MubbleEntities.GOOMBA, 10839375, 12097909, new Item.Properties().group(ItemGroup.MISC)));
    public static final Item TOAD_SPAWN_EGG = item("toad_spawn_egg", new SpawnEggItem(MubbleEntities.TOAD, 14671839, 16722728, new Item.Properties().group(ItemGroup.MISC)));
    public static final Item ZOMBIE_COWMAN_SPAWN_EGG = item("zombie_cowman_spawn_egg", new SpawnEggItem(MubbleEntities.ZOMBIE_COWMAN, 2957585, 5009705, new Item.Properties().group(ItemGroup.MISC)));
    
    public static Item item(String name, Item item)
    {
    	Item fItem = item.setRegistryName(Mubble.MOD_ID, name);
        ITEMS.add(fItem);
		return fItem;
    }
}