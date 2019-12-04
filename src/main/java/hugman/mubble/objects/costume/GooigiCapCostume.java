package hugman.mubble.objects.costume;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class GooigiCapCostume extends HeadCostume
{    
    public GooigiCapCostume(Item.Properties builder, SoundEvent sound)
    {
        super(builder, sound);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
    	Random rand = new Random();
    	if(!world.isRemote && rand.nextInt(51) == 0)
    	{
    		world.playSound((PlayerEntity)null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_SLIME_BLOCK_HIT, SoundCategory.PLAYERS, 1f, 1f);
    		player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, (rand.nextInt(3) + 1) * 20, rand.nextInt(3)));
    	}
    	super.onArmorTick(stack, world, player);
    }
}