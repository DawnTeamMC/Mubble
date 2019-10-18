package hugman.mubble.objects.costume;

import java.util.Random;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class CappyCostume extends HeadCostume
{    
    public CappyCostume(Properties builder, SoundEvent sound)
    {
        super(builder, sound);
    }
    
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
	{
		Random rand = new Random();
		if(!world.isRemote && rand.nextInt(301) == 0)
		{
			int random = rand.nextInt(5);
			if(player.dimension == DimensionType.THE_NETHER && random <= 3)
			{
				world.playSound((PlayerEntity)null, player.posX, player.posY, player.posZ, MubbleSounds.COSTUME_CAPPY_AMBIENT_NETHER, SoundCategory.VOICE, 1f, 1f);
				return;
			}
			else world.playSound((PlayerEntity)null, player.posX, player.posY, player.posZ, MubbleSounds.COSTUME_CAPPY_AMBIENT, SoundCategory.VOICE, 1f, 1f);
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity)
	{
		Random rand = new Random();
		World world = entity.world;
		if(rand.nextInt(201) == 0)
		{
			if(world.getFluidState(entity.getPosition()).isTagged(FluidTags.WATER)) world.playSound((PlayerEntity)null, entity.posX, entity.posY, entity.posZ, MubbleSounds.COSTUME_CAPPY_HELP_WATER, SoundCategory.VOICE, 1f, 1f);
			else world.playSound((PlayerEntity)null, entity.posX, entity.posY, entity.posZ, MubbleSounds.COSTUME_CAPPY_HELP, SoundCategory.VOICE, 1f, 1f);
		}
		return super.onEntityItemUpdate(stack, entity);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(armorType);
        if (itemstack1.isEmpty()) worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.PLAYERS, 1f, 1f);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}