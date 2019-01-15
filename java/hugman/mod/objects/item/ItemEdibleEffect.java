package hugman.mod.objects.item;

import hugman.mod.Main;
import hugman.mod.init.MubbleItems;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/** 
 * Open class - can be initialized for multiple items with variables.
 */
public class ItemEdibleEffect extends ItemFood implements IHasModel
{
	private PotionEffect[] effects;
	
	public ItemEdibleEffect(String name, CreativeTabs tab, int amount, float saturation, boolean isWolfFood, PotionEffect...potionEffect)
	{
		super(amount, saturation, isWolfFood);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(tab);
		this.effects = potionEffect;
		MubbleItems.ITEMS.add(this);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		for(PotionEffect effect : effects)
		{
			playerIn.addPotionEffect(new PotionEffect(effect));
		}
	}
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
