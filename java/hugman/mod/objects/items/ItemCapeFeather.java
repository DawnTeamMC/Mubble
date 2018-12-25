package hugman.mod.objects.items;

import java.util.Random;

import hugman.mod.init.ItemInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemCapeFeather extends ItemBase implements IHasModel
{
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public ItemCapeFeather(String name)
	{
		super(name);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Random rand = new Random();
        for (int i = 0; i < rand.nextInt(6) + 1; i++)
        {
        	worldIn.spawnParticle(EnumParticleTypes.CLOUD, playerIn.posX + (rand.nextInt(11) - 5) / 10F, playerIn.posY, playerIn.posZ + (rand.nextInt(11) - 5) / 10F, 0.0D, (rand.nextInt(3) + 1) / 10F, 0);
        }
        playerIn.motionY = 0.7D;
        playerIn.fallDistance = 0f;
        if (!playerIn.capabilities.isCreativeMode && this != ItemInit.SUPER_CAPE_FEATHER) stack.shrink(1);
        playerIn.addStat(StatList.getObjectUseStats(this));
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.ITEM_CAPE_FEATHER_USE, SoundCategory.PLAYERS, 0.5F, 1F);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
