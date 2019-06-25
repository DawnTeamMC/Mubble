package hugman.mod.objects.costume;

import java.util.List;

import hugman.mod.init.MubbleCostumes;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class Costume extends Item
{
	protected final EquipmentSlotType armorType;
	protected final SoundEvent sound;
	protected final EffectInstance[] effects;
    
    public Costume(Item.Properties builder, SoundEvent sound, EquipmentSlotType armorType, EffectInstance... potionEffects)
    {
        super(builder);
		this.sound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
	    DispenserBlock.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
    	if(!world.isRemote && effects != null) for(EffectInstance effect : effects)
    	{
    		player.addPotionEffect(new EffectInstance(effect));
    	}
    }
	
	public static final IDispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior()
	{
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			ItemStack itemstack = ArmorItem.dispenseArmor(source, stack);
			return itemstack.isEmpty() ? super.dispenseStack(source, stack) : itemstack;
		}
	};
	
	public static ItemStack dispenseArmor(IBlockSource blockSource, ItemStack stack)
	{
		BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().get(DispenserBlock.FACING));
		List<LivingEntity> list = blockSource.getWorld().getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(blockpos), EntityPredicates.NOT_SPECTATING.and(new EntityPredicates.ArmoredMob(stack)));
		if (list.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		else
		{
			LivingEntity entitylivingbase = list.get(0);
			EquipmentSlotType entityequipmentslot = MobEntity.getSlotForItemStack(stack);
			ItemStack itemstack = stack.split(1);
			entitylivingbase.setItemStackToSlot(entityequipmentslot, itemstack);
			if (entitylivingbase instanceof MobEntity)
			{
				((MobEntity)entitylivingbase).setDropChance(entityequipmentslot, 2.0F);
				((MobEntity)entitylivingbase).enablePersistence();
			}
			return stack;
		}
	}
    
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack)
    {
    	return this.armorType;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(armorType);
        if (itemstack1.isEmpty())
        {
        	playerIn.setItemStackToSlot(armorType, itemstack.copy());
        	itemstack.shrink(1);
        	worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, this.sound, SoundCategory.PLAYERS, 1f, 1f);
        	if(this == MubbleCostumes.SNORLAX_HAT && "SnorlaxLover".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.snorlax_hat.secret_status", new Object[0]), true);
        	if(this == MubbleCostumes.MAYRO_CAP && "MayroSMM".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.mayro_cap.secret_status", new Object[0]), true);
        	if(this == MubbleCostumes.NOTEBLOCK_HEAD && "NoteBlockRemix".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.noteblock_head.secret_status", new Object[0]), true);
        	if(this == MubbleCostumes.BANDANA && "Pixelcraftian".equals(playerIn.getGameProfile().getName())) playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.bandana.secret_status", new Object[0]), true);
        	return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
        else
        {
        	return new ActionResult<>(ActionResultType.FAIL, itemstack);
        }
	}
    
    public static boolean isUsable(ItemStack stack)
    {
    	return stack.getDamage() < stack.getMaxDamage() - 1;
	}
}