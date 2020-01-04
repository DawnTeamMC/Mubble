package hugman.mubble.objects.costume;

import java.util.List;
import java.util.Random;

import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.util.CalendarEvents;
import hugman.mubble.util.Shaders;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class Costume extends ArmorItem
{
	protected final EquipmentSlot armorType;
	protected final SoundEvent equipSound;
	protected final StatusEffectInstance[] effects;
	protected final Identifier shader;
    
    public Costume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, StatusEffectInstance... potionEffects)
    {
        super(null, armorType, builder);
		this.equipSound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
        this.shader = null;
	    DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    public Costume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, Identifier shader, StatusEffectInstance... potionEffects)
    {
        super(null, armorType, builder);
		this.equipSound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
    	if(Shaders.RETRO_SHADERS.contains(shader) && CalendarEvents.isAprilFools)
    	{
    		Random rand = new Random();
    		this.shader = Shaders.RETRO_SHADERS.get(rand.nextInt(Shaders.RETRO_SHADERS.size()));
    	}
    	else
    	{
            this.shader = shader;
    	}
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
    	if(!world.isClient && effects != null)
    	{
    		for(StatusEffectInstance effect : effects)
        	{
        		player.addStatusEffect(new StatusEffectInstance(effect));
        	}
    	}
    }
	
	public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior()
	{
		@Override
		public ItemStack dispenseSilently(BlockPointer source, ItemStack stack)
		{
			ItemStack itemstack = ArmorItem.DISPENSER_BEHAVIOR.dispense(source, stack);
			return itemstack.isEmpty() ? super.dispenseSilently(source, stack) : itemstack;
		}
	};
	
	public static boolean dispenseArmor(BlockPointer blockSource, ItemStack stack)
	{
		BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().get(DispenserBlock.FACING));
		List<LivingEntity> list = blockSource.getWorld().getEntities(LivingEntity.class, new Box(blockpos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.CanPickup(stack)));
		if (list.isEmpty())
		{
			return false;
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
			return true;
		}
	}
    
    @Override
    public EquipmentSlot getSlotType()
    {
    	return this.armorType;
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        ItemStack itemstack1 = playerIn.getEquippedStack(armorType);
        if (itemstack1.isEmpty())
        {
        	playerIn.equipStack(armorType, itemstack.copy());
        	itemstack.decrement(1);
        	worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), this.equipSound, SoundCategory.PLAYERS, 1f, 1f);
        	if(this == MubbleCostumes.MAYRO_CAP && playerIn.getGameProfile().getId().toString() == "8cf61519-4ac2-4d60-9d65-d0c7abcf4524")
        	{
        		playerIn.addChatMessage(new TranslatableText("item.mubble.mayro_cap.secret_status", new Object[0]), true);
        	}
        	else if(this == MubbleCostumes.NOTEBLOCK_HEAD && playerIn.getGameProfile().getId().toString() == "5a68af56-e293-44e9-bbf8-21d58300b3f3")
        	{
        		playerIn.addChatMessage(new TranslatableText("item.mubble.noteblock_head.secret_status", new Object[0]), true);
        	}
        	else if(this == MubbleCostumes.BANDANA && playerIn.getGameProfile().getId().toString() == "1805e857-329e-463e-8ca8-122fcc686996")
        	{
        		playerIn.addChatMessage(new TranslatableText("item.mubble.bandana.secret_status", new Object[0]), true);
        	}
        	return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
        }
        else
        {
        	return new TypedActionResult<>(ActionResult.FAIL, itemstack);
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
    
    public Identifier getShader()
    {
    	return this.shader;
	}
}