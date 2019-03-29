package hugman.mod.objects.costume;

import java.util.List;

import hugman.mod.init.MubbleCostumes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CostumeBlock extends ItemBlock
{
	protected final EntityEquipmentSlot armorType;
	protected final SoundEvent sound;
	
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
	
    public CostumeBlock(Block base_block, SoundEvent sound, EntityEquipmentSlot armorType, ItemGroup group)
    {
        super(base_block, new Item.Properties().group(group));
        setRegistryName(base_block.getRegistryName());
		MubbleCostumes.register(this);
		this.sound = sound;
	    this.armorType = armorType;
	    BlockDispenser.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
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
