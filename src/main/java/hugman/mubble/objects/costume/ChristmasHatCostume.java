package hugman.mubble.objects.costume;

import java.util.Random;

import hugman.mubble.objects.CalendarEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ChristmasHatCostume extends HeadCostume
{    
    public ChristmasHatCostume(Item.Properties builder, SoundEvent sound)
    {
        super(builder, sound);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
    	Random rand = new Random();
    	if(world.isRemote && rand.nextInt(2) == 0 && CalendarEvents.isDecember)
    	{
    		world.addParticle(ParticleTypes.ITEM_SNOWBALL, player.posX + (rand.nextDouble() - 0.5D) * 0.3D, player.posY + player.getHeight() + rand.nextDouble() * 0.3D, player.posZ + (rand.nextDouble() - 0.5D) * 0.3D, (rand.nextDouble() - 0.5D) * 1.1D, (rand.nextDouble() - 0.5D) * 1.1D, (rand.nextDouble() - 0.5D) * 1.1D);
    	}
    	super.onArmorTick(stack, world, player);
    }
}