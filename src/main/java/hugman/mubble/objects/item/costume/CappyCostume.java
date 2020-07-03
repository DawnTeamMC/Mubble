package hugman.mubble.objects.item.costume;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class CappyCostume extends HeadCostume {
	public CappyCostume(Settings builder, SoundEvent sound) {
		super(builder, sound);
	}

	@Override
	public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks) {
		Random rand = new Random();
		if(!world.isClient && rand.nextInt(301) == 0) {
			int random = rand.nextInt(5);
			if(world.getDimension().isUltrawarm() && random <= 3) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.COSTUME_CAPPY_AMBIENT_NETHER, SoundCategory.VOICE, 1f, 1f);
			}
			else {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.COSTUME_CAPPY_AMBIENT, SoundCategory.VOICE, 1f, 1f);
			}
		}
		super.usageTick(world, player, stack, remainingUseTicks);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemstack1 = player.getEquippedStack(armorType);
		if(itemstack1.isEmpty()) {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.PLAYERS, 1f, 1f);
		}
		return super.use(world, player, hand);
	}
}