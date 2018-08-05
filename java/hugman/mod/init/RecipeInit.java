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
		GameRegistry.addSmelting(Item.getItemFromBlock(BlockInit.VANADIUM_ORE), new ItemStack(ItemInit.VANADIUM), 1F);
	}
}
