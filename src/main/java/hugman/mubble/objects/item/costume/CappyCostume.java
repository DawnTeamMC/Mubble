package hugman.mubble.objects.item.costume;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
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
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity living, Hand hand) {
		ItemEntity entity = (ItemEntity) EntityType.ITEM.spawnFromItemStack(player.world, stack, player, player.getBlockPos(), SpawnReason.DISPENSER, true, false);
		Random rand = new Random();
		World world = entity.world;
		if(rand.nextInt(201) == 0) {
			if(world.getFluidState(entity.getBlockPos()).matches(FluidTags.WATER)) {
				world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), MubbleSounds.COSTUME_CAPPY_HELP_WATER, SoundCategory.VOICE, 1f, 1f);
			}
			else {
				world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), MubbleSounds.COSTUME_CAPPY_HELP, SoundCategory.VOICE, 1f, 1f);
			}
		}
		return super.useOnEntity(stack, player, living, hand);
	}

	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack1 = playerIn.getEquippedStack(armorType);
		if(itemstack1.isEmpty()) {
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.PLAYERS, 1f, 1f);
		}
		return super.use(worldIn, playerIn, handIn);
	}
}