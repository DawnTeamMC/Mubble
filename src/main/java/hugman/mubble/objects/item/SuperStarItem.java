package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SuperStarItem extends Item
{    
    public SuperStarItem(Item.Properties builder)
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
