package hugman.mod.objects.items;

import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemAnnoyingDog extends ItemBase implements IHasModel
{
	private PotionEffect[] effects;
	public ItemAnnoyingDog()
	{
		super("annoying_dog");
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack itemIn, EntityPlayer playerIn) {
		World worldIn = playerIn.getEntityWorld();
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.NEUTRAL, 0.5F, 1F);
		return super.onDroppedByPlayer(itemIn, playerIn);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!playerIn.capabilities.isCreativeMode && stack.getCount() <= 63)
        {
        	stack.grow(1);
            playerIn.addStat(StatList.getObjectUseStats(this));
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.PLAYERS, 0.5F, 1F);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }
}
