package hugman.mod.objects.item;

import hugman.mod.Main;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.item.Item;

/** 
 * Open class - can be initialized for multiple items with variables.<br>
 * Template class - is used to create other classes.
 */
public class ItemBase extends Item implements IHasModel
{
	public ItemBase(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
		MubbleItems.ITEMS.add(this);
	}
	
	public ItemBase(String name, int max)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
		setMaxStackSize(max);
		MubbleItems.ITEMS.add(this);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
