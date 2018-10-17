package hugman.mod.objects.items;

import hugman.mod.Main;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSimple extends ItemBase implements IHasModel
{
	public ItemSimple(String name)
	{
		super(name);
		ItemInit.ITEMS.add(this);
	}
	
	public ItemSimple(String name, int max)
	{
		super(name, max);
		ItemInit.ITEMS.add(this);
	}
}
