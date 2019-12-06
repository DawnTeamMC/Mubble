package hugman.mubble.objects.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ShakeInstrumentItem extends InstrumentItem
{
    public ShakeInstrumentItem(Item.Properties builder, SoundEvent soundIn)
    {
        super(builder, soundIn);
    }
    
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
    	World worldIn = entity.getEntityWorld();
    	worldIn.playMovingSound((PlayerEntity)null, entity, getInstrumentSound(), SoundCategory.PLAYERS, 0.5F, 1F);
    	if(entity instanceof PlayerEntity)
    	{
    		((PlayerEntity)entity).addStat(Stats.ITEM_USED.get(this));
    	}
    	return super.onEntitySwing(stack, entity);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
    	playerIn.swingArm(handIn);
		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
