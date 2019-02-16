package hugman.mod.objects.item;

import hugman.mod.Main;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
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
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
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
