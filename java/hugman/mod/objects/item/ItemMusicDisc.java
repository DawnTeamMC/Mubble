package hugman.mod.objects.item;

import hugman.mod.Main;
import hugman.mod.init.MubbleItems;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemMusicDisc extends ItemRecord implements IHasModel
{
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public ItemMusicDisc(String name, CreativeTabs tab, SoundEvent soundIn)
	{
		super(name, soundIn);
		setTranslationKey("record");
		setRegistryName("record_" + name);
        setCreativeTab(tab);
		MubbleItems.ITEMS.add(this);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
