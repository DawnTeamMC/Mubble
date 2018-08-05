package hugman.mod.objects.items;

import hugman.mod.Main;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemMusicDisc extends ItemRecord implements IHasModel
{	
	public ItemMusicDisc(String p_i46742_1_, SoundEvent soundIn, CreativeTabs tab)
	{
		super(p_i46742_1_, soundIn);
		setTranslationKey("record");
		setRegistryName("record_" + p_i46742_1_);
        setCreativeTab(tab);
		
		ItemInit.ITEMS.add(this);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
