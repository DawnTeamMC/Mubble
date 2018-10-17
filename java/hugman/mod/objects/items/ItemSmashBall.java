package hugman.mod.objects.items;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.ItemInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSmashBall extends ItemBase implements IHasModel
{
	public ItemSmashBall()
	{
		super("smash_ball",1);
		ItemInit.ITEMS.add(this);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Random rand = new Random();
        for (int i = 0; i < rand.nextInt(21) + 10; i++) {
        	worldIn.spawnParticle(EnumParticleTypes.FLAME, playerIn.posX + (rand.nextInt(11) - 5) / 10F, playerIn.posY + rand.nextInt(21) / 10F, playerIn.posZ + (rand.nextInt(11) - 5) / 10F, (rand.nextInt(21) - 10) / 120F, (rand.nextInt(2) + 0.1) / 11F, (rand.nextInt(21) - 10) / 120F);
        }
        playerIn.motionY = playerIn.motionY + 0.25D;
        playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(5), 900, 3));
        playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(21), 900, 4));
        playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 25, 0));
        playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(24), 25, 0));
        if (!playerIn.capabilities.isCreativeMode) stack.shrink(1);
        playerIn.getCooldownTracker().setCooldown(this, 25);
        playerIn.addStat(StatList.getObjectUseStats(this));
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.ITEM_SMASH_BALL_USE, SoundCategory.PLAYERS, 1f, 1f);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
