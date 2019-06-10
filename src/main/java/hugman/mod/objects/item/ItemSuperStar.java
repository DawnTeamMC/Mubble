package hugman.mod.objects.item;

import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemSuperStar extends net.minecraft.item.ItemFood
{    
    public ItemSuperStar(int healAmountIn, float saturation, boolean meat, Item.Properties builder)
    {
        super(healAmountIn, saturation, meat, builder);
    }
    
    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        if (!worldIn.isRemote)
        {
        	player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 600, 1));
        	player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, 0));
        	player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 1));
        	player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 1));
        	player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 600, 0));
        	player.playSound(MubbleSounds.ITEM_SUPER_STAR_THEME, 60000000, 1);
        }
	}
}
