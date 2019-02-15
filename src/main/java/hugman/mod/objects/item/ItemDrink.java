package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDrink extends ItemFood implements IHasModel
{
	String type;
	
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public ItemDrink(String name, String type, int amount, float saturation, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
		setMaxStackSize(1);
		this.type = type;
		MubbleItems.ITEMS.add(this);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		stack.shrink(1);
		return stack;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
	
	@Override
	public void registerModels()
	{
		Mubble.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
