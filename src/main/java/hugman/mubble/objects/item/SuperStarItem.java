package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
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
    	if (!worldIn.isRemote)
    	{
        	worldIn.playMovingSound((PlayerEntity)null, entityLiving, MubbleSounds.ITEM_SUPER_STAR_THEME, SoundCategory.PLAYERS, 1.0F, 1.0F);
    	}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
