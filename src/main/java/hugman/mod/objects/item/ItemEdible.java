package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class ItemEdible extends ItemFood implements IHasModel
{
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public ItemEdible(String name, int amount, float saturation, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
		MubbleItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels()
	{
		Mubble.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
