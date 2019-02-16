package hugman.mod.objects.item;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTotemOfAscending extends ItemBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public ItemTotemOfAscending()
	{
		super("totem_of_ascending", 1);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        BlockPos desPos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(playerIn.posX, playerIn.posY, playerIn.posZ));
        playerIn.setPosition(desPos.getX() + 0.5D, desPos.getY() + 0.5D, desPos.getZ() + 0.5D);
        if (!playerIn.capabilities.isCreativeMode) stack.shrink(1);
        playerIn.getCooldownTracker().setCooldown(this, 25);
        playerIn.addStat(StatList.getObjectUseStats(this));
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 1f);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
