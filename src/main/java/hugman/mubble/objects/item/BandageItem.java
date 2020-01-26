package hugman.mubble.objects.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BandageItem extends Item
{
	public static final List<Effect> CURABLE_EFFECTS = new ArrayList<>(Arrays.asList(Effects.MINING_FATIGUE, Effects.NAUSEA, Effects.POISON, Effects.WITHER));
	
    public BandageItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        boolean doesCure = false;
        
        for(Effect effect : CURABLE_EFFECTS)
        {
        	if(player.isPotionActive(effect))
        	{
        		doesCure = true;
        		player.removeActivePotionEffect(effect);
        	}
        }
        if(doesCure)
        {
            worldIn.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 0.5F, 1F);
            
            if(!player.abilities.isCreativeMode)
            {
            	stack.shrink(1);
            }
            player.addStat(Stats.ITEM_USED.get(this));
            return ActionResult.success(stack);
        }
        
        return ActionResult.fail(stack);
    }
}
