package hugman.mod.objects.costume;

import java.util.Random;

import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Fluids;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class CostumeCappy extends CostumeSimple
{    
    public CostumeCappy()
    {
        super("cappy", SoundEvents.BLOCK_SLIME_BLOCK_BREAK, EntityEquipmentSlot.HEAD);
    }
    
	@Override
	public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
	{
		Random rand = new Random();
		if(!world.isRemote && rand.nextInt(301) == 0)
		{
			int random = rand.nextInt(5);
			if(player.dimension == DimensionType.NETHER && random <= 3)
			{
				world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, MubbleSounds.COSTUME_CAPPY_AMBIENT_NETHER, SoundCategory.VOICE, 1f, 1f);
				return;
			}
			else world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, MubbleSounds.COSTUME_CAPPY_AMBIENT, SoundCategory.VOICE, 1f, 1f);
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, EntityItem entity)
	{
		Random rand = new Random();
		World world = entity.getEntityWorld();
		if(rand.nextInt(201) == 0)
		{
			if(world.getBlockState(entity.getPosition()).getFluidState() == Fluids.WATER
			|| world.getBlockState(entity.getPosition()).getFluidState() == Fluids.FLOWING_WATER) world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, MubbleSounds.COSTUME_CAPPY_HELP_WATER, SoundCategory.VOICE, 1f, 1f);
			else world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, MubbleSounds.COSTUME_CAPPY_HELP, SoundCategory.VOICE, 1f, 1f);
		}
		return super.onEntityItemUpdate(stack, entity);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.PLAYERS, 1f, 1f);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}