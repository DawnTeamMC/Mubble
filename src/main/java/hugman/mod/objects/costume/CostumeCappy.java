package hugman.mod.objects.costume;

import java.util.Random;

import hugman.mod.init.MubbleBiomes;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class CostumeCappy extends CostumeBaseHead implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public CostumeCappy()
	{
		super("cappy", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack armor)
	{
        Random rand = new Random();
        if(!worldIn.isRemote && rand.nextInt(301) == 0)
		{
			int random = rand.nextInt(5);
			if(worldIn.provider.getDimension() == -1 && random <= 3)
			{
				worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_AMBIENT_NETHER, SoundCategory.VOICE, 1f, 1f);
				return;
			}
			if(worldIn.getBiome(playerIn.getPosition()) == MubbleBiomes.MUSHROOM_KINGDOM && random <= 2) worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_HAPPY, SoundCategory.VOICE, 1f, 1f);
			else worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_AMBIENT, SoundCategory.VOICE, 1f, 1f);
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
        Random rand = new Random();
        World worldIn = entityItem.getEntityWorld();
		if(rand.nextInt(201) == 0)
		{
			if(worldIn.getBlockState(entityItem.getPosition()).getBlock() == Blocks.WATER
			|| worldIn.getBlockState(entityItem.getPosition()).getBlock() == Blocks.FLOWING_WATER) worldIn.playSound((EntityPlayer)null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundHandler.COSTUME_CAPPY_HELP_WATER, SoundCategory.VOICE, 1f, 1f);
			else worldIn.playSound((EntityPlayer)null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundHandler.COSTUME_CAPPY_HELP, SoundCategory.VOICE, 1f, 1f);
		}
		return super.onEntityItemUpdate(entityItem);
	}
}
