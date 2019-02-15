package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
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
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
		MubbleItems.ITEMS.add(this);
	}
	
	public ItemBase(String name, int max)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
		setMaxStackSize(max);
		MubbleItems.ITEMS.add(this);
	}

	@Override
	public void registerModels()
	{
		Mubble.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
