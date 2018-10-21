package hugman.mod.objects.items;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BiomeInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemHelmet extends ItemBase implements IHasModel
{	
	String name;
	SoundEvent sound;
	private PotionEffect[] effects;
    public final EntityEquipmentSlot armorType = EntityEquipmentSlot.HEAD;
    
	public ItemHelmet(String name, SoundEvent sound, PotionEffect...potionEffect)
	{
		super(name, 1, Main.MUBBLE_COSTUMES);
		if(name == "wing_cap") this.setMaxDamage(600);
		
		ItemInit.ITEMS.add(this);
		this.effects = potionEffect;
		this.name = name;
		this.sound = sound;
	}
	
	public static boolean isUsable(ItemStack stack)
    {
        return stack.getItemDamage() < stack.getMaxDamage() - 1;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		if(this.name == "wing_cap") return repair.getItem() == Items.FEATHER;
		else return false;
    }
	
	@Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity)
    {
		return armorType == EntityEquipmentSlot.HEAD;
    }
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack armor)
	{
		if(this.name == "wing_cap" && this.isUsable(armor) && playerIn.isSprinting()) armor.damageItem(1, playerIn);
        if(!worldIn.isRemote)
        {
            Random rand = new Random();
    		for(PotionEffect effect : effects)
    		{
    			playerIn.addPotionEffect(new PotionEffect(effect));
    		}
    		if(this.name == "mayro_cap")
    		{
    			playerIn.inventory.clearMatchingItems(ItemInit.YELLOW_COIN, 0, 1, null);
    			playerIn.inventory.clearMatchingItems(ItemInit.RED_COIN, 0, 1, null);
    			playerIn.inventory.clearMatchingItems(ItemInit.BLUE_COIN, 0, 1, null);
    		}
    		if(this.name == "vanish_cap" && playerIn.isSneaking()) playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 2, 0));
    		if(this.name == "wing_cap" && this.isUsable(armor) && playerIn.isSprinting())
    		{
    			playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(25), 1, 2));
    			playerIn.fallDistance = 0f;
    		}
    		if(this.name == "gooigi_cap" && rand.nextInt(51) == 0)
    		{
    	        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_SLIME_HIT, SoundCategory.PLAYERS, 1f, 1f);
    		    playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(8), (rand.nextInt(3) + 1) * 20, rand.nextInt(3)));
    		}
    		if(this.name == "cappy" && rand.nextInt(301) == 0)
    		{
    			int random = rand.nextInt(5);
    			if(worldIn.provider.getDimension() == -1 && random <= 3)
    			{
    				worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_AMBIENT_NETHER, SoundCategory.VOICE, 1f, 1f);
    				return;
    			}
    			if(worldIn.getBiome(playerIn.getPosition()) == BiomeInit.MUSHROOM_KINGDOM && random <= 2) worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_HAPPY, SoundCategory.VOICE, 1f, 1f);
    			else worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_AMBIENT, SoundCategory.VOICE, 1f, 1f);
    		}
        }
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
        Random rand = new Random();
        World worldIn = entityItem.getEntityWorld();
		if(this.name == "cappy" && rand.nextInt(201) == 0)
		{
			if(worldIn.getBlockState(entityItem.getPosition()).getBlock() == Blocks.WATER
			|| worldIn.getBlockState(entityItem.getPosition()).getBlock() == Blocks.FLOWING_WATER) worldIn.playSound((EntityPlayer)null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundHandler.COSTUME_CAPPY_HELP_WATER, SoundCategory.VOICE, 1f, 1f);
			else worldIn.playSound((EntityPlayer)null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundHandler.COSTUME_CAPPY_HELP, SoundCategory.VOICE, 1f, 1f);
		}
		return super.onEntityItemUpdate(entityItem);
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
            playerIn.addStat(StatList.getObjectUseStats(this));
            if(this.name == "cappy")
            {
            	worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_EQUIP, SoundCategory.VOICE, 1f, 1f);
            	if("Hugman_76".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.cappy.secret_status", new Object[0]), true);
            }
            if(this.name == "mayro_cap" && "MayroSMM".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.mayro_cap.secret_status", new Object[0]), true);
            if(this.name == "noteblock_head" && "NoteBlockRemix".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.noteblock_head.secret_status", new Object[0]), true);
            if(this.name == "bandana" && "Pixelcraftian".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.bandana.secret_status", new Object[0]), true);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }
}
