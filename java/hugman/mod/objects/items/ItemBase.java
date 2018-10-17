package hugman.mod.objects.items;

import hugman.mod.Main;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel
{
	public ItemBase(String name)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_ITEMS);
	}
	
	public ItemBase(String name, int max)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_ITEMS);
		setMaxStackSize(max);
	}
	
	public ItemBase(String name, int max, CreativeTabs tab)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(tab);
		setMaxStackSize(max);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
