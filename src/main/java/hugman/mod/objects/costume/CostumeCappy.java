package hugman.mod.objects.costume;

import java.util.Random;

import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

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
			if(world.getDimension().isNether() && random <= 3)
			{
				world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, MubbleSounds.COSTUME_CAPPY_AMBIENT_NETHER, SoundCategory.VOICE, 1f, 1f);
				return;
			}
			//if(world.getBiome(player.getPosition()) == MubbleBiomes.MUSHROOM_KINGDOM && random <= 2) world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundHandler.COSTUME_CAPPY_HAPPY, SoundCategory.VOICE, 1f, 1f);
			else world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, MubbleSounds.COSTUME_CAPPY_AMBIENT, SoundCategory.VOICE, 1f, 1f);
		}
	}
}