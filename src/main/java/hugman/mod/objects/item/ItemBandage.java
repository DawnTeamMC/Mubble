package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleItems;
import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBandage extends Item
{    
    public ItemBandage()
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Mubble.MOD_ID, "bandage");
		MubbleItems.register(this);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(playerIn.isPotionActive(MobEffects.MINING_FATIGUE)
        || playerIn.isPotionActive(MobEffects.NAUSEA)
        || playerIn.isPotionActive(MobEffects.POISON)
        || playerIn.isPotionActive(MobEffects.WITHER))
        {
            playerIn.removePotionEffect(MobEffects.MINING_FATIGUE);
            playerIn.removePotionEffect(MobEffects.NAUSEA);
            playerIn.removePotionEffect(MobEffects.POISON);
            playerIn.removePotionEffect(MobEffects.WITHER);
            if (!playerIn.abilities.isCreativeMode) stack.shrink(1);
            playerIn.addStat(StatList.ITEM_USED.get(this));
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 0.5F, 1F);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }
}
