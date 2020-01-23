package hugman.mubble.objects.costume;

import java.util.Random;

import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.init.client.MubbleShaders;
import hugman.mubble.util.CalendarEvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.world.World;

public class Costume extends Item
{
	protected final EquipmentSlot armorType;
	protected final SoundEvent equipSound;
	protected final StatusEffectInstance[] effects;
	protected final Identifier shader;
    
    public Costume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, StatusEffectInstance... potionEffects)
    {
        super(builder);
		this.equipSound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
        this.shader = null;
	    DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    public Costume(Item.Settings builder, SoundEvent sound, EquipmentSlot armorType, Identifier shader, StatusEffectInstance... potionEffects)
    {
        super(builder);
		this.equipSound = sound;
	    this.armorType = armorType;
	    this.effects = potionEffects;
    	if(MubbleShaders.RETRO_SHADERS.contains(shader) && CalendarEvents.isAprilFools)
    	{
    		Random rand = new Random();
    		this.shader = MubbleShaders.RETRO_SHADERS.get(rand.nextInt(MubbleShaders.RETRO_SHADERS.size()));
    	}
    	else
    	{
            this.shader = shader;
    	}
	    DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }
    
    public EquipmentSlot getArmorType()
    {
    	return this.armorType;
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
    					renderer.getShader().render(remainingUseTicks);
    				}
    			}
    			else
    			{
    				renderer.getShader().render(remainingUseTicks);
    			}
    		}
    	}
    	if(!world.isClient && effects != null)
    	{
    		long i = world.getTime();
    		if(i % 40L == 0L)
    		{
    			for(StatusEffectInstance effect : effects)
            	{
            		player.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), 260, effect.getAmplifier(), false, true));
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
    
    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        ItemStack itemstack1 = playerIn.getEquippedStack(this.armorType);
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