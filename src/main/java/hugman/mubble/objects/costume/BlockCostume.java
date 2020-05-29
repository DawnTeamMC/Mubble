package hugman.mubble.objects.costume;

import hugman.mubble.mixin.GameRendererAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
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
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		if (world.isClient && entity instanceof PlayerEntity)
		{
			GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;
			PlayerEntity player = (PlayerEntity) entity;
			if (player.inventory.getArmorStack(3).equals(stack))
			{
				ShaderEffect shaderGroup = renderer.getShader();
				Identifier shader = this.getShader();
				if (shader != null)
				{
					if (shaderGroup != null)
					{
						if (!shaderGroup.getName().equals(shader.toString()))
						{
							((GameRendererAccessor) renderer).invokeLoadShader(shader);
						}
					}
					else
					{
						((GameRendererAccessor) renderer).invokeLoadShader(shader);
					}
				}
			}
			else
			{
				renderer.disableShader();
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
