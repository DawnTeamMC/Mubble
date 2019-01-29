package hugman.mod.objects.costume;

import hugman.mod.Main;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CostumeBaseHead extends Item implements IHasModel
{	
	String name;
	SoundEvent sound;
    public final EntityEquipmentSlot armorType = EntityEquipmentSlot.HEAD;
    
    /** 
     * Open class - can be initialized for multiple items with variables.<br>
     * Template class - is used to create other classes.
     */
	public CostumeBaseHead(String name, SoundEvent sound)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_COSTUMES);
		setMaxStackSize(1);
		
		MubbleCostumes.COSTUMES.add(this);
		this.name = name;
		this.sound = sound;
	}
	
	public static boolean isUsable(ItemStack stack)
    {
        return stack.getItemDamage() < stack.getMaxDamage() - 1;
    }
	
	@Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity)
    {
		return armorType == EntityEquipmentSlot.HEAD;
    }
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityEquipmentSlot.HEAD;
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);

        if (itemstack1.isEmpty())
        {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            if (!playerIn.capabilities.isCreativeMode) itemstack.setCount(0);
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, sound, SoundCategory.PLAYERS, 1f, 1f);
            if(this == MubbleCostumes.CAPPY) worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_EQUIP, SoundCategory.VOICE, 1f, 1f);
            if(this == MubbleCostumes.CAPPY && "Hugman_76".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.cappy.secret_status", new Object[0]), true);
            if(this == MubbleCostumes.NOTEBLOCK_HEAD && "NoteBlockRemix".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.noteblock_head.secret_status", new Object[0]), true);
            if(this == MubbleCostumes.BANDANA && "Pixelcraftian".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.bandana.secret_status", new Object[0]), true);
            playerIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
