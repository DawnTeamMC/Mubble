package hugman.mod.objects.item;

import java.util.Random;

import hugman.mod.Reference;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import hugman.mod.init.MubbleTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.Particles;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSmashBall extends Item
{    
    public ItemSmashBall()
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS).rarity(EnumRarity.RARE));
        setRegistryName(Reference.MOD_ID, "smash_ball");
		MubbleItems.register(this);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	ItemStack stack = playerIn.getHeldItem(handIn);
    	Random rand = new Random();
    	for (int i = 0; i < rand.nextInt(21) + 10; i++)
    	{
    		worldIn.spawnParticle(Particles.FLAME, playerIn.posX + (rand.nextInt(11) - 5) / 10F, playerIn.posY + rand.nextInt(21) / 10F, playerIn.posZ + (rand.nextInt(11) - 5) / 10F, (rand.nextInt(21) - 10) / 120F, (rand.nextInt(2) + 0.1) / 11F, (rand.nextInt(21) - 10) / 120F);
    	}
    	playerIn.motionY = playerIn.motionY + 0.25D;
    	playerIn.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 25, 0));
    	playerIn.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 25, 0));
    	playerIn.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 900, 3));
    	playerIn.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 900, 4));
    	if (!playerIn.abilities.isCreativeMode) stack.shrink(1);
    	playerIn.getCooldownTracker().setCooldown(this, 25);
    	playerIn.addStat(StatList.ITEM_USED.get(this));
    	worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_SMASH_BALL_USE, SoundCategory.PLAYERS, 1f, 1f);
    	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
