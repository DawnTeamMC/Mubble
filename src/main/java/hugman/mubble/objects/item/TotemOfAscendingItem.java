package hugman.mubble.objects.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class TotemOfAscendingItem extends Item
{
	public TotemOfAscendingItem(Item.Settings builder)
	{
		super(builder);
	}

	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getStackInHand(handIn);
		BlockPos desPos = new BlockPos(playerIn.getX(), worldIn.getChunk(playerIn.getBlockPos()).sampleHeightmap(Heightmap.Type.WORLD_SURFACE, (int) playerIn.getX(), (int) playerIn.getZ()), playerIn.getZ());
		worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 1f);
		if (desPos.getY() <= playerIn.getY())
		{
			playerIn.updatePosition(playerIn.getX(), playerIn.getY() + 20D, playerIn.getZ());
		}
		else
		{
			playerIn.updatePosition(playerIn.getX(), desPos.getY() + 2D, playerIn.getZ());
		}
		playerIn.fallDistance = 0f;
		if (!playerIn.abilities.creativeMode)
		{
			stack.decrement(1);
		}
		playerIn.getItemCooldownManager().set(this, 25);
		playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
		return TypedActionResult.success(stack);
	}
}
