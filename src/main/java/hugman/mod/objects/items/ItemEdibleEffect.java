package hugman.mod.objects.items;

import hugman.mod.Main;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
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
	
	public ItemEdibleEffect(String name, int amount, float saturation, boolean isWolfFood, PotionEffect...potionEffect)
	{
		super(amount, saturation, isWolfFood);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_ITEMS);
		this.effects = potionEffect;
		ItemInit.ITEMS.add(this);
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
