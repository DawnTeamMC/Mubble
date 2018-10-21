package hugman.mod.objects.items;

import hugman.mod.Main;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemMusicDisc extends ItemRecord implements IHasModel
{	
	public ItemMusicDisc(String name, SoundEvent soundIn)
	{
		super(name, soundIn);
		setTranslationKey("record");
		setRegistryName("record_" + name);
        setCreativeTab(Main.MUBBLE_ITEMS);
		
		ItemInit.ITEMS.add(this);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
