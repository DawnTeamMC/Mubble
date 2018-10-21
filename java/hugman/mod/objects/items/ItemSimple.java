package hugman.mod.objects.items;

import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;

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
