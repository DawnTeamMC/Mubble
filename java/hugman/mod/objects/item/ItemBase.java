package hugman.mod.objects.item;

import hugman.mod.Main;
import hugman.mod.init.MubbleItems;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/** 
 * Open class - can be initialized for multiple items with variables.<br>
 * Template class - is used to create other classes.
 */
public class ItemBase extends Item implements IHasModel
{
	public ItemBase(String name, CreativeTabs tab)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(tab);
		MubbleItems.ITEMS.add(this);
	}
	
	public ItemBase(String name, CreativeTabs tab, int max)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(tab);
		setMaxStackSize(max);
		MubbleItems.ITEMS.add(this);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
