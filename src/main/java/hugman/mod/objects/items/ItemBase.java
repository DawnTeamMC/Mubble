package hugman.mod.objects.items;

import hugman.mod.Main;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/** 
 * Open class - can be initialized for multiple items with variables.<br>
 * Template class - is used to create other classes.
 */
public class ItemBase extends Item implements IHasModel
{
	public ItemBase(String name)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_ITEMS);
		ItemInit.ITEMS.add(this);
	}
	
	public ItemBase(String name, int max)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_ITEMS);
		setMaxStackSize(max);
		ItemInit.ITEMS.add(this);
	}
	
	public ItemBase(String name, int max, CreativeTabs tab)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(tab);
		setMaxStackSize(max);
		ItemInit.ITEMS.add(this);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
