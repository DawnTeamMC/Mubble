package hugman.mubble.objects.item;

import java.util.Random;

import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CapeFeatherItem extends Item
{
    public CapeFeatherItem(Item.Settings builder)
    {
        super(builder);
    }
    
	@Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getStackInHand(handIn);
        Random rand = new Random();
        Vec3d vec3d = playerIn.getVelocity();
        for (int i = 0; i < rand.nextInt(6) + 1; i++)
        {
        	worldIn.addParticle(ParticleTypes.CLOUD, playerIn.getX() + (rand.nextInt(11) - 5) / 10F, playerIn.getY(), playerIn.getZ() + (rand.nextInt(11) - 5) / 10F, 0.0D, (rand.nextInt(3) + 1) / 10F, 0);
        }
        playerIn.setVelocity(vec3d.x, 0.7D, vec3d.z);
        playerIn.fallDistance = 0f;
        if (!playerIn.abilities.creativeMode && this != MubbleItems.SUPER_CAPE_FEATHER) stack.decrement(1);
        playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
        worldIn.playSound((PlayerEntity)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MubbleSounds.ITEM_CAPE_FEATHER_USE, SoundCategory.PLAYERS, 0.5F, 1F);
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, stack);
    }
}
