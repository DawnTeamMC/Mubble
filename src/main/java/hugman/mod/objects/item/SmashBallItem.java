package hugman.mod.objects.item;

import java.util.Random;

import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class SmashBallItem extends Item
{    
    public SmashBallItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
    	ItemStack stack = playerIn.getHeldItem(handIn);
    	Random rand = new Random();
    	for (int i = 0; i < rand.nextInt(21) + 10; i++)
    	{
    		worldIn.addParticle(ParticleTypes.FLAME, playerIn.posX + (rand.nextInt(11) - 5) / 10F, playerIn.posY + rand.nextInt(21) / 10F, playerIn.posZ + (rand.nextInt(11) - 5) / 10F, (rand.nextInt(21) - 10) / 120F, (rand.nextInt(2) + 0.1) / 11F, (rand.nextInt(21) - 10) / 120F);
    	}
    	playerIn.getMotion().add(0.0D, 0.25, 0.0D);
    	playerIn.addPotionEffect(new EffectInstance(Effects.GLOWING, 25, 0));
    	playerIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 25, 0));
    	playerIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 900, 3));
    	playerIn.addPotionEffect(new EffectInstance(Effects.HEALTH_BOOST, 900, 4));
    	if (!playerIn.abilities.isCreativeMode) stack.shrink(1);
    	playerIn.getCooldownTracker().setCooldown(this, 25);
    	playerIn.addStat(Stats.ITEM_USED.get(this));
    	worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_SMASH_BALL_USE, SoundCategory.PLAYERS, 1f, 1f);
    	return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
    }
}
