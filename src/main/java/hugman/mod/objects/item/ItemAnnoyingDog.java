package hugman.mod.objects.item;

import java.util.Random;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleItems;
import hugman.mod.init.elements.MubbleSounds;
import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemAnnoyingDog extends Item
{    
    public ItemAnnoyingDog()
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Mubble.MOD_ID, "annoying_dog");
		MubbleItems.register(this);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
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
    			worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_ANNOYING_DOG_DISAPPEAR, SoundCategory.PLAYERS, 0.5F, 1F);
    		}
    		else
    		{
    			if(!playerIn.abilities.isCreativeMode && stack.getCount() <= 63) stack.grow(1);
    			worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_ANNOYING_DOG_BARK, SoundCategory.PLAYERS, 0.5F, 1F);
    		}
    		playerIn.addStat(StatList.ITEM_USED.get(this));
    		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    	}
    	return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }
}
