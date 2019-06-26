package hugman.mod.objects.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BandageItem extends Item
{    
    public BandageItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(playerIn.isPotionActive(Effects.MINING_FATIGUE)
        || playerIn.isPotionActive(Effects.NAUSEA)
        || playerIn.isPotionActive(Effects.POISON)
        || playerIn.isPotionActive(Effects.WITHER))
        {
            playerIn.removePotionEffect(Effects.MINING_FATIGUE);
            playerIn.removePotionEffect(Effects.NAUSEA);
            playerIn.removePotionEffect(Effects.POISON);
            playerIn.removePotionEffect(Effects.WITHER);
            if (!playerIn.abilities.isCreativeMode) stack.shrink(1);
            playerIn.addStat(Stats.ITEM_USED.get(this));
            worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 0.5F, 1F);
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
    }
}
