package hugman.mod.objects.costume;

import hugman.mod.init.MubbleCostumes;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CostumeBlock extends ItemBlock
{
	protected final EntityEquipmentSlot armorType;
	protected final SoundEvent sound;
	
    public CostumeBlock(Block base_block, SoundEvent sound, EntityEquipmentSlot armorType, ItemGroup group)
    {
        super(base_block, new Item.Properties().group(group));
        setRegistryName(base_block.getRegistryName());
		MubbleCostumes.register(this);
		this.sound = sound;
	    this.armorType = armorType;
    }
    
    @Override
    public boolean canEquip(ItemStack stack, EntityEquipmentSlot armorType, Entity entity)
    {
    	return this.getEquipmentSlot() == EntityEquipmentSlot.HEAD;
    }
    
    public EntityEquipmentSlot getEquipmentSlot()
    {
        return EntityEquipmentSlot.HEAD;
	}
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (itemstack1.isEmpty())
        {
           playerIn.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this));
           itemstack.shrink(1);
           worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_SNOW_HIT, SoundCategory.PLAYERS, 1f, 1f);
           return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
           return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
	}
    
    public static boolean isUsable(ItemStack stack)
    {
    	return stack.getDamage() < stack.getMaxDamage() - 1;
	}
}
