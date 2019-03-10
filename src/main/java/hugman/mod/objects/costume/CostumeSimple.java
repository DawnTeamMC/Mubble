package hugman.mod.objects.costume;

import hugman.mod.Reference;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CostumeSimple extends Item
{
	protected final EntityEquipmentSlot armorType;
	protected final SoundEvent sound;
	
    public CostumeSimple(String name, SoundEvent sound, EntityEquipmentSlot armorType)
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_COSTUMES).maxStackSize(1));
        setRegistryName(Reference.MOD_ID, name);
		MubbleCostumes.register(this);
		this.sound = sound;
	    this.armorType = armorType;
    }
    
    public CostumeSimple(String name, SoundEvent sound, EntityEquipmentSlot armorType, Properties properties)
    {
        super(properties);
        setRegistryName(Reference.MOD_ID, name);
		MubbleCostumes.register(this);
		this.sound = sound;
	    this.armorType = armorType;
    }
    
    @Override
    public boolean canEquip(ItemStack stack, EntityEquipmentSlot armorType, Entity entity)
    {
    	return this.getEquipmentSlot() == armorType;
    }
    
    public EntityEquipmentSlot getEquipmentSlot()
    {
        return this.armorType;
	}
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(armorType);
        if (itemstack1.isEmpty())
        {
           playerIn.setItemStackToSlot(armorType, itemstack.copy());
           itemstack.setCount(0);
           worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, sound, SoundCategory.PLAYERS, 1f, 1f);
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