package hugman.mod.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/** 
 * Init class - used to initialize furnace recipes.
 */
public class MubbleRecipes
{
	public static void addRecipes()
	{
		GameRegistry.addSmelting(Items.SUGAR, new ItemStack(MubbleItems.CARAMEL_CUBE), 1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(MubbleBlocks.VANADIUM_ORE), new ItemStack(MubbleItems.VANADIUM), 1.5F);
		GameRegistry.addSmelting(MubbleItems.CANDY_CANE, new ItemStack(Items.SUGAR, 2), 0.2F);
		GameRegistry.addSmelting(Item.getItemFromBlock(MubbleBlocks.BLUE_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(MubbleBlocks.GREEN_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(MubbleBlocks.YELLOW_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(MubbleBlocks.RED_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
	}
}
