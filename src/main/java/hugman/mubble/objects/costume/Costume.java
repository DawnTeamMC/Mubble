package hugman.mubble.objects.costume;

import java.util.List;
import java.util.UUID;

import com.mojang.blaze3d.platform.GLX;

import hugman.mubble.init.MubbleCostumes;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.shader.ShaderGroup;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class Costume extends Item
{
	protected final EquipmentSlotType armorType;
	protected final SoundEvent equipSound;
	protected final EffectInstance[] effects;
	protected final ResourceLocation shader;
    
    public Costume(Item.Properties builder, SoundEvent sound, EquipmentSlotType armorType, EffectInstance... potionEffects)
    {
        super(builder);
		this.equipSound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
        this.shader = null;
	    DispenserBlock.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    public Costume(Item.Properties builder, SoundEvent sound, EquipmentSlotType armorType, ResourceLocation shader, EffectInstance... potionEffects)
    {
        super(builder);
		this.equipSound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
        this.shader = shader;
	    DispenserBlock.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
		GameRenderer renderer = Minecraft.getInstance().gameRenderer;
		ShaderGroup shaderGroup = renderer.getShaderGroup();
		ResourceLocation shader = this.getShader();
		if(world.isRemote && GLX.usePostProcess)
		{
			if(shaderGroup != null)
			{
				if(shaderGroup.getShaderGroupName() != shader.toString())
				{
					shaderGroup.close();
				}
			}

			shaderGroup = null;
			if(shader != null)
			{
				renderer.loadShader(shader);
			}
		}
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
        	worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, this.equipSound, SoundCategory.PLAYERS, 1f, 1f);
        	if(this == MubbleCostumes.SNORLAX_HAT && playerIn.getGameProfile().getId() == UUID.fromString("202d33ae3f81418b8282926f9d689f1f"))
        	{
        		playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.snorlax_hat.secret_status", new Object[0]), true);
        	}
        	else if(this == MubbleCostumes.MAYRO_CAP && playerIn.getGameProfile().getId() == UUID.fromString("8cf615194ac24d609d65d0c7abcf4524"))
        	{
        		playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.mayro_cap.secret_status", new Object[0]), true);
        	}
        	else if(this == MubbleCostumes.NOTEBLOCK_HEAD && playerIn.getGameProfile().getId() == UUID.fromString("5a68af56e29344e9bbf821d58300b3f3"))
        	{
        		playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.noteblock_head.secret_status", new Object[0]), true);
        	}
        	else if(this == MubbleCostumes.BANDANA && playerIn.getGameProfile().getId() == UUID.fromString("1805e857329e463e8ca8122fcc686996"))
        	{
        		playerIn.sendStatusMessage(new TranslationTextComponent("item.mubble.bandana.secret_status", new Object[0]), true);
        	}
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
    
    public SoundEvent getEquipSound()
    {
		return equipSound;
	}
    
    public ResourceLocation getShader()
    {
    	return this.shader;
	}
}