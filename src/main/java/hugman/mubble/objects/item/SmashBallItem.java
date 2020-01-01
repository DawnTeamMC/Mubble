package hugman.mubble.objects.item;

import java.util.Random;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SmashBallItem extends Item
{    
    public SmashBallItem(Item.Settings builder)
    {
        super(builder);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
    	ItemStack stack = playerIn.getStackInHand(handIn);
    	Random rand = new Random();
    	for (int i = 0; i < rand.nextInt(21) + 10; i++)
    	{
    		worldIn.addParticle(ParticleTypes.FLAME, playerIn.getX() + (rand.nextInt(11) - 5) / 10F, playerIn.getY() + rand.nextInt(21) / 10F, playerIn.getZ() + (rand.nextInt(11) - 5) / 10F, (rand.nextInt(21) - 10) / 120F, (rand.nextInt(2) + 0.1) / 11F, (rand.nextInt(21) - 10) / 120F);
    	}
    	playerIn.getVelocity().add(0.0D, 0.25, 0.0D);
    	playerIn.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 25, 0));
    	playerIn.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 0));
    	playerIn.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 900, 3));
    	playerIn.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 900, 4));
    	if (!playerIn.abilities.creativeMode) stack.decrement(1);
    	playerIn.getItemCooldownManager().set(this, 25);
    	playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
    	worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MubbleSounds.ITEM_SMASH_BALL_USE, SoundCategory.PLAYERS, 1f, 1f);
    	return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, stack);
    }
}
