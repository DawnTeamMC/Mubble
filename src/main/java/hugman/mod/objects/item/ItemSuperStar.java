package hugman.mod.objects.item;

import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSuperStar extends Item
{    
    public ItemSuperStar(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
    	entityLiving.playSound(MubbleSounds.ITEM_SUPER_STAR_THEME, 60000000, 1);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
