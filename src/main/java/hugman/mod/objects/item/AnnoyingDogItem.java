package hugman.mod.objects.item;

import java.util.Random;

import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class AnnoyingDogItem extends Item
{    
    public AnnoyingDogItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
    	ItemStack stack = playerIn.getHeldItem(handIn);
    	Random rand = new Random();
    	if(!worldIn.isRemote)
    	{
    		int luck = rand.nextInt(20);
    		if(playerIn.abilities.isCreativeMode) luck = 1;
    		if(luck == 0)
    		{
    			stack.setCount(0);
    			worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_ANNOYING_DOG_DISAPPEAR, SoundCategory.PLAYERS, 0.5F, 1F);
    		}
    		else
    		{
    			if(!playerIn.abilities.isCreativeMode && stack.getCount() <= 63) stack.grow(1);
    			worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_ANNOYING_DOG_BARK, SoundCategory.PLAYERS, 0.5F, 1F);
    		}
    		playerIn.addStat(Stats.ITEM_USED.get(this));
    		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
    	}
    	return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
    }
}
