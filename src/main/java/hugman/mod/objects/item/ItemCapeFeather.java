package hugman.mod.objects.item;

import java.util.Random;

import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemCapeFeather extends Item
{
    public ItemCapeFeather(Item.Properties builder)
    {
        super(builder);
    }
    
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Random rand = new Random();
        Vec3d vec3d = playerIn.getMotion();
        for (int i = 0; i < rand.nextInt(6) + 1; i++)
        {
        	worldIn.addParticle(ParticleTypes.CLOUD, playerIn.posX + (rand.nextInt(11) - 5) / 10F, playerIn.posY, playerIn.posZ + (rand.nextInt(11) - 5) / 10F, 0.0D, (rand.nextInt(3) + 1) / 10F, 0);
        }
        playerIn.setMotion(vec3d.x, 0.7D, vec3d.z);
        playerIn.fallDistance = 0f;
        if (!playerIn.abilities.isCreativeMode && this != MubbleItems.SUPER_CAPE_FEATHER) stack.shrink(1);
        playerIn.addStat(Stats.ITEM_USED.get(this));
        worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_CAPE_FEATHER_USE, SoundCategory.PLAYERS, 0.5F, 1F);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
    }
}
