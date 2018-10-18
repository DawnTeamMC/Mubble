package hugman.mod.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeInit
{
	public static void addRecipes()
	{
		GameRegistry.addSmelting(Items.SUGAR, new ItemStack(ItemInit.CARAMEL_CUBE), 1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(BlockInit.VANADIUM_ORE), new ItemStack(ItemInit.VANADIUM), 1.5F);
		GameRegistry.addSmelting(ItemInit.CANDY_CANE, new ItemStack(Items.SUGAR, 2), 0.2F);
		GameRegistry.addSmelting(Item.getItemFromBlock(BlockInit.BLUE_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(BlockInit.GREEN_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(BlockInit.YELLOW_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
		GameRegistry.addSmelting(Item.getItemFromBlock(BlockInit.RED_CANDY_CANE_PILLAR), new ItemStack(Items.SUGAR, 4), 0.1F);
	}
}
