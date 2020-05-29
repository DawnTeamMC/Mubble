package hugman.mubble.objects.costume;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

public class GooigiCapCostume extends HeadCostume
{
	public GooigiCapCostume(Item.Settings builder, SoundEvent sound)
	{
		super(builder, sound);
	}

	@Override
	public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks)
	{
		Random rand = new Random();
		if (!world.isClient && rand.nextInt(51) == 0)
		{
			world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_HIT, SoundCategory.PLAYERS, 1f, 1f);
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, (rand.nextInt(3) + 1) * 20, rand.nextInt(3)));
		}
		super.usageTick(world, player, stack, remainingUseTicks);
	}
}