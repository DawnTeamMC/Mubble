package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemSuperStar extends net.minecraft.item.ItemFood
{    
    public ItemSuperStar()
    {
        super(3, 2.4F, false, new Item.Properties().group(ItemGroup.MISC).rarity(EnumRarity.RARE));
        setRegistryName(Mubble.MOD_ID, "super_star");
		MubbleItems.register(this);
		this.setAlwaysEdible();
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
