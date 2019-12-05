package hugman.mubble.objects.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ShakeInstrumentItem extends Item
{
	protected final SoundEvent sound;
	
    public ShakeInstrumentItem(Item.Properties builder, SoundEvent soundIn)
    {
        super(builder);
        this.sound = soundIn;
    }
    
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
    	World worldIn = entity.getEntityWorld();
    	worldIn.playMovingSound((PlayerEntity)null, entity, this.sound, SoundCategory.PLAYERS, 0.5F, 1F);
    	return super.onEntitySwing(stack, entity);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
    	playerIn.swingArm(handIn);
		return new ActionResult<ItemStack>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }
}
