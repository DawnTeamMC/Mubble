package hugman.mubble.objects.item;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BandageItem extends Item
{    
    public BandageItem(Item.Settings builder)
    {
        super(builder);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getStackInHand(handIn);
        if(playerIn.hasStatusEffect(StatusEffects.MINING_FATIGUE)
        || playerIn.hasStatusEffect(StatusEffects.NAUSEA)
        || playerIn.hasStatusEffect(StatusEffects.POISON)
        || playerIn.hasStatusEffect(StatusEffects.WITHER))
        {
            playerIn.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            playerIn.removeStatusEffect(StatusEffects.NAUSEA);
            playerIn.removeStatusEffect(StatusEffects.POISON);
            playerIn.removeStatusEffect(StatusEffects.WITHER);
            if (!playerIn.abilities.creativeMode) stack.decrement(1);
            playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
            worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 0.5F, 1F);
            return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, stack);
        }
        return new TypedActionResult<ItemStack>(ActionResult.FAIL, stack);
    }
}
