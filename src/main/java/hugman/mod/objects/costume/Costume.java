package hugman.mod.objects.costume;

import java.util.List;

import hugman.mod.init.MubbleCostumes;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class Costume extends Item
{
	protected final EntityEquipmentSlot armorType;
	protected final SoundEvent sound;
	protected final PotionEffect[] effects;
    
    public Costume(Item.Properties builder, SoundEvent sound, EntityEquipmentSlot armorType, PotionEffect... potionEffects)
    {
        super(builder);
		this.sound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
	    BlockDispenser.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	if(!world.isRemote && effects != null) for(PotionEffect effect : effects)
    	{
    		player.addPotionEffect(new PotionEffect(effect));
    	}
    }
	
	public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem()
	{
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			ItemStack itemstack = ItemArmor.dispenseArmor(source, stack);
			return itemstack.isEmpty() ? super.dispenseStack(source, stack) : itemstack;
		}
	};
	
	public static ItemStack dispenseArmor(IBlockSource blockSource, ItemStack stack)
	{
		BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().get(BlockDispenser.FACING));
		List<EntityLivingBase> list = blockSource.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(blockpos), EntitySelectors.NOT_SPECTATING.and(new EntitySelectors.ArmoredMob(stack)));
		if (list.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		else
		{
			EntityLivingBase entitylivingbase = list.get(0);
			EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(stack);
			ItemStack itemstack = stack.split(1);
			entitylivingbase.setItemStackToSlot(entityequipmentslot, itemstack);
			if (entitylivingbase instanceof EntityLiving)
			{
				((EntityLiving)entitylivingbase).setDropChance(entityequipmentslot, 2.0F);
				((EntityLiving)entitylivingbase).enablePersistence();
			}
			return stack;
		}
	}
    
    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack)
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
        	itemstack.shrink(1);
        	worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, this.sound, SoundCategory.PLAYERS, 1f, 1f);
        	if(this == MubbleCostumes.SNORLAX_HAT && "SnorlaxLover".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.mubble.snorlax_hat.secret_status", new Object[0]), true);
        	if(this == MubbleCostumes.MAYRO_CAP && "MayroSMM".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.mubble.mayro_cap.secret_status", new Object[0]), true);
        	if(this == MubbleCostumes.NOTEBLOCK_HEAD && "NoteBlockRemix".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.mubble.noteblock_head.secret_status", new Object[0]), true);
        	if(this == MubbleCostumes.BANDANA && "Pixelcraftian".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.mubble.bandana.secret_status", new Object[0]), true);
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