package hugman.mubble.objects.costume;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class BlockCostume extends BlockItem
{
	protected final EquipmentSlot armorType;
	protected final SoundEvent sound;
	protected final Identifier shader;
	
    public BlockCostume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, Block base_block)
    {
        super(base_block, builder);
		this.sound = sound;
	    this.armorType = armorType;
        this.shader = null;
	    DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }
	
    public BlockCostume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, Block base_block, Identifier shader)
    {
        super(base_block, builder);
		this.sound = sound;
	    this.armorType = armorType;
        this.shader = shader;
	    DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    @Override
    public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks)
    {
    	if(world.isClient)
    	{
    		GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;
    		ShaderEffect shaderGroup = renderer.getShader();
    		Identifier shader = this.getShader();
    		if(shader != null)
    		{
    			if(shaderGroup != null)
    			{
    				if(!shaderGroup.getName().equals(shader.toString()))
    				{
    					renderer.toggleShadersEnabled();
    				}
    			}
    			else
    			{
    				renderer.toggleShadersEnabled();
    			}
    		}
    	}
    }
	
	public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior()
	{
		@Override
		public ItemStack dispenseSilently(BlockPointer source, ItemStack stack)
		{
			return ArmorItem.dispenseArmor(source, stack) ? stack : super.dispenseSilently(source, stack);
		}
	};
	
    public EquipmentSlot getArmorType()
    {
    	return this.armorType;
    }
	
	public static ItemStack dispenseArmor(BlockPointer blockSource, ItemStack stack)
	{
		BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().get(DispenserBlock.FACING));
		List<LivingEntity> list = blockSource.getWorld().getEntities(LivingEntity.class, new Box(blockpos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.CanPickup(stack)));
		if (list.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		else
		{
			LivingEntity entitylivingbase = list.get(0);
			EquipmentSlot entityequipmentslot = MobEntity.getPreferredEquipmentSlot(stack);
			ItemStack itemstack = stack.split(1);
			entitylivingbase.equipStack(entityequipmentslot, itemstack);
			if (entitylivingbase instanceof MobEntity)
			{
				((MobEntity) entitylivingbase).setEquipmentDropChance(entityequipmentslot, 2.0F);
				((MobEntity) entitylivingbase).setPersistent();
			}
			return stack;
		}
	}
    
    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        ItemStack itemstack1 = playerIn.getEquippedStack(EquipmentSlot.HEAD);
        if (itemstack1.isEmpty())
        {
           playerIn.equipStack(EquipmentSlot.HEAD, new ItemStack(this));
           itemstack.decrement(1);
           worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), this.sound, SoundCategory.PLAYERS, 1f, 1f);
           return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
        }
        else
        {
           return new TypedActionResult<>(ActionResult.FAIL, itemstack);
        }
	}
    
    public Identifier getShader()
    {
    	return this.shader;
	}
    
    public static boolean isUsable(ItemStack stack)
    {
    	return stack.getDamage() < stack.getMaxDamage() - 1;
	}
}
