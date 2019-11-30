package hugman.mubble.objects.container;

import java.util.List;

import com.google.common.collect.Lists;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleContainerTypes;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class TimeswapTableContainer extends Container
{
	private final IWorldPosCallable worldPosCallable;
	private final IntReferenceHolder selectedOutputItem = IntReferenceHolder.single();
	private List<Item> outputItems = Lists.newArrayList();
	private ItemStack itemStackInput = ItemStack.EMPTY;
	private long lastOnTake;
	final Slot inputInventorySlot;
	final Slot outputInventorySlot;
	
	private Runnable inventoryUpdateListener = () -> {};
	public final IInventory inputInventory = new Inventory(1)
	{
		public void markDirty()
		{
			super.markDirty();
			TimeswapTableContainer.this.onCraftMatrixChanged(this);
			TimeswapTableContainer.this.inventoryUpdateListener.run();
		}
	};
	
	private final CraftResultInventory inventory = new CraftResultInventory();
	
	public TimeswapTableContainer(int windowIdIn, PlayerInventory playerInventoryIn)
	{
		this(windowIdIn, playerInventoryIn, IWorldPosCallable.DUMMY);
	}

	public TimeswapTableContainer(int windowIdIn, PlayerInventory playerInventoryIn, final IWorldPosCallable worldPosCallableIn)
	{
		super(MubbleContainerTypes.TIMESWAP_TABLE, windowIdIn);
	    this.worldPosCallable = worldPosCallableIn;
	    this.inputInventorySlot = this.addSlot(new Slot(this.inputInventory, 0, 20, 33));
	    
	    this.outputInventorySlot = this.addSlot(new Slot(this.inventory, 1, 143, 33)
	    {
	    	@Override
	    	public boolean isItemValid(ItemStack stack)
	    	{
	    		return false;
	    	}
	    	
	    	@Override
	    	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
	    	{
	    		ItemStack itemstack = TimeswapTableContainer.this.inputInventorySlot.decrStackSize(1);
	    		if (!itemstack.isEmpty())
	    		{
	    			TimeswapTableContainer.this.updateOutputResultSlot();
	    		}
	    		stack.getItem().onCreated(stack, thePlayer.world, thePlayer);
	    		worldPosCallableIn.consume((worldIn, blockPos) -> 
	    		{
	    			long l = worldIn.getGameTime();
	    			if (TimeswapTableContainer.this.lastOnTake != l)
	    			{
	    				worldIn.playSound((PlayerEntity)null, blockPos, MubbleSounds.UI_TIMESWAP_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
	    				TimeswapTableContainer.this.lastOnTake = l;
	    			}
	    		});
	    		return super.onTake(thePlayer, stack);
	    	}
	    });
	    
	    for(int i = 0; i < 3; ++i)
	    {
	    	for(int j = 0; j < 9; ++j)
	    	{
	    		this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	    	}
	    }
	    
	    for(int k = 0; k < 9; ++k)
	    {
	    	this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
	    }
	    
	    this.trackInt(this.selectedOutputItem);
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getSelectedOutputItem()
	{
		return selectedOutputItem.get();
	}
	
	@OnlyIn(Dist.CLIENT)
	public List<Item> getOutputItemsList()
	{
		return this.outputItems;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getOutputItemsListSize()
	{
		return this.outputItems.size();
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean hasItemsinInputSlot()
	{
		return this.inputInventorySlot.getHasStack() && !this.outputItems.isEmpty();
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(this.worldPosCallable, playerIn, MubbleBlocks.TIMESWAP_TABLE);
	}
	
	@Override
	public boolean enchantItem(PlayerEntity playerIn, int id)
	{
		if (id >= 0 && id < this.outputItems.size())
		{
			this.selectedOutputItem.set(id);
			this.updateOutputResultSlot();
		}
		return true;
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		ItemStack itemstack = this.inputInventorySlot.getStack();
		if (itemstack.getItem() != this.itemStackInput.getItem())
		{
			this.itemStackInput = itemstack.copy();
			this.updateAvailableOutputs(inventoryIn, itemstack);
		}
	}
	
	private void updateAvailableOutputs(IInventory inventoryIn, ItemStack stack)
	{
		this.outputItems.clear();
		this.selectedOutputItem.set(-1);
		this.outputInventorySlot.putStack(ItemStack.EMPTY);
		if(!stack.isEmpty())
		{
			Tag<Item> tag = correspondingTag(stack.getItem());
			if(tag != null)
			{
				this.outputItems = Lists.newArrayList();
				for(Item item : ForgeRegistries.ITEMS)
				{
					if(item.isIn(tag)) this.outputItems.add(item);
				}
			}
		}
	}
	
	private void updateOutputResultSlot()
	{
		if(!this.outputItems.isEmpty())
		{
			Item item = this.outputItems.get(this.selectedOutputItem.get());
			this.outputInventorySlot.putStack(new ItemStack(item));
		}
		else
		{
			this.outputInventorySlot.putStack(ItemStack.EMPTY);
		}
		
		this.detectAndSendChanges();
	}
	
	@Override
	public ContainerType<?> getType()
	{
		return MubbleContainerTypes.TIMESWAP_TABLE;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void setInventoryUpdateListener(Runnable listenerIn)
	{
		this.inventoryUpdateListener = listenerIn;
	}
	
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn)
	{
		return false;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			Item item = itemstack1.getItem();
			itemstack = itemstack1.copy();
			Tag<Item> tag = correspondingTag(item);
			if(index == 1)
			{
				item.onCreated(itemstack1, playerIn.world, playerIn);
				if (!this.mergeItemStack(itemstack1, 2, 38, true))
				{
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index == 0)
			{
				if (!this.mergeItemStack(itemstack1, 2, 38, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(tag != null)
			{
				if (!this.mergeItemStack(itemstack1, 0, 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (index >= 2 && index < 29)
			{
				if (!this.mergeItemStack(itemstack1, 29, 38, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false))
			{
				return ItemStack.EMPTY;
			}
			
			if(itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			
			slot.onSlotChanged();
			if(itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, itemstack1);
			this.detectAndSendChanges();
		}
		
		return itemstack;
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn)
	{
		super.onContainerClosed(playerIn);
		this.inventory.removeStackFromSlot(1);
		this.worldPosCallable.consume((p_217079_2_, p_217079_3_) ->
		{
			this.clearContainer(playerIn, playerIn.world, this.inputInventory);
		});
	}
	
	private static Tag<Item> correspondingTag(Item item)
	{
		for(Tag<Item> tag : MubbleTags.Items.TIMESWAP_TAGS)
		{
			if(item.isIn(tag)) return tag;
			else continue;
		}
		return null;
	}
}