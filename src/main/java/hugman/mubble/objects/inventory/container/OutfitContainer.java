package hugman.mubble.objects.inventory.container;

import com.mojang.datafixers.util.Pair;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OutfitContainer extends Container
{
	private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_BOOTS_SLOT_TEXTURE, PlayerContainer.EMPTY_LEGGINGS_SLOT_TEXTURE, PlayerContainer.EMPTY_CHESTPLATE_SLOT_TEXTURE, PlayerContainer.EMPTY_HELMET_SLOT_TEXTURE};
	private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	
	public OutfitContainer(PlayerEntity playerIn)
	{
		super((ContainerType<?>)null, 0);
		PlayerInventory inventory = playerIn.inventory;
		
		for(int k = 0; k < 4; ++k)
		{
			final EquipmentSlotType equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
			this.addSlot(new Slot(inventory, 39 - k, 8, 8 + k * 18)
			{
				public int getSlotStackLimit()
				{
					return 1;
				}
				
				public boolean isItemValid(ItemStack stack)
				{
					return stack.canEquip(equipmentslottype, playerIn);
				}
				
				public boolean canTakeStack(PlayerEntity player)
				{
					ItemStack itemstack = this.getStack();
					return !itemstack.isEmpty() && !player.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(player);
				}

				@OnlyIn(Dist.CLIENT)
				public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite()
				{
					return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, OutfitContainer.ARMOR_SLOT_TEXTURES[equipmentslottype.getIndex()]);
				}
			});
		}
		
		for(int l = 0; l < 3; ++l)
		{
			for (int j1 = 0; j1 < 9; ++j1)
			{
				this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
			}
		}

		for(int i1 = 0; i1 < 9; ++i1)
		{
			this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
		}

		this.addSlot(new Slot(inventory, 40, 77, 62)
		{
			@OnlyIn(Dist.CLIENT)
			public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite()
			{
				return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, PlayerContainer.EMPTY_OFFHAND_ARMOR_SLOT);
			}
		});
	}

	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemstack);
			if(slotIndex == 0)
			{
				if(!this.mergeItemStack(itemstack1, 4, 40, false))
				{
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(slotIndex >= 1 && slotIndex < 9)
			{
				if(!this.mergeItemStack(itemstack1, 4, 40, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(equipmentslottype.getSlotType() == EquipmentSlotType.Group.ARMOR && !this.inventorySlots.get(3 - equipmentslottype.getIndex()).getHasStack())
			{
				int i = 3 - equipmentslottype.getIndex();
				if(!this.mergeItemStack(itemstack1, i, i + 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(equipmentslottype == EquipmentSlotType.OFFHAND && !this.inventorySlots.get(40).getHasStack())
			{
				if(!this.mergeItemStack(itemstack1, 40, 41, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(slotIndex >= 9 && slotIndex < 36)
			{
				if(!this.mergeItemStack(itemstack1, 31, 40, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(slotIndex >= 36 && slotIndex < 45)
			{
				if(!this.mergeItemStack(itemstack1, 4, 31, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 4, 40, false))
			{
				return ItemStack.EMPTY;
			}

			if(itemstack1.isEmpty()) 
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if(itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(player, itemstack1);
			if(slotIndex == 0)
			{
				player.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}
}