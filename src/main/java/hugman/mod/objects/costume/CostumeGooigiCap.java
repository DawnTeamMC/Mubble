package hugman.mod.objects.costume;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class CostumeGooigiCap extends CostumeSimple
{    
    public CostumeGooigiCap()
    {
        super("gooigi_cap", SoundEvents.BLOCK_SLIME_BLOCK_BREAK, EntityEquipmentSlot.HEAD);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	Random rand = new Random();
    	if(!world.isRemote && rand.nextInt(51) == 0)
    	{
    		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_SLIME_BLOCK_HIT, SoundCategory.PLAYERS, 1f, 1f);
    		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, (rand.nextInt(3) + 1) * 20, rand.nextInt(3)));
    	}
    }
}