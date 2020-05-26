package hugman.mubble.objects.container;

import java.util.List;

import com.google.common.collect.Lists;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.container.Property;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;

public class TimeswapTableContainer extends Container
{
	private final BlockContext worldPosCallable;
	private final Property selectedOutputItem = Property.create();
	private List<Item> outputItems = Lists.newArrayList();
	private ItemStack itemStackInput = ItemStack.EMPTY;
	private long lastOnTake;
	final Slot inputInventorySlot;
	final Slot outputInventorySlot;
	
	private Runnable inventoryUpdateListener = () -> {};
	public final Inventory inputInventory = new BasicInventory(1)
	{
		public void markDirty()
		{
			super.markDirty();
			TimeswapTableContainer.this.onContentChanged(this);
			TimeswapTableContainer.this.inventoryUpdateListener.run();
		}
	};
	
	private final CraftingResultInventory inventory = new CraftingResultInventory();
	
	public TimeswapTableContainer(int windowIdIn, PlayerInventory playerInventoryIn)
	{
		this(windowIdIn, playerInventoryIn, BlockContext.EMPTY);
	}

	public TimeswapTableContainer(int windowIdIn, PlayerInventory playerInventoryIn, final BlockContext worldPosCallableIn)
	{
		super(null, windowIdIn);
	    this.worldPosCallable = worldPosCallableIn;
	    this.inputInventorySlot = this.addSlot(new Slot(this.inputInventory, 0, 20, 33));
	    
	    this.outputInventorySlot = this.addSlot(new Slot(this.inventory, 1, 143, 33)
	    {
	    	@Override
	    	public boolean canInsert(ItemStack stack)
	    	{
	    		return false;
	    	}
	    	
	    	@Override
	    	public ItemStack onTakeItem(PlayerEntity thePlayer, ItemStack stack)
	    	{
	    		ItemStack itemstack = TimeswapTableContainer.this.inputInventorySlot.takeStack(1);
	    		if (!itemstack.isEmpty())
	    		{
	    			TimeswapTableContainer.this.updateOutputResultSlot();
	    		}
	    		stack.getItem().onCraft(stack, thePlayer.world, thePlayer);
	    		worldPosCallableIn.run((worldIn, blockPos) -> 
	    		{
	    			long l = worldIn.getTime();
	    			if (TimeswapTableContainer.this.lastOnTake != l)
	    			{
	    				worldIn.playSound((PlayerEntity) null, blockPos, MubbleSounds.UI_TIMESWAP_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
	    				TimeswapTableContainer.this.lastOnTake = l;
	    			}
	    		});
	    		return super.onTakeItem(thePlayer, stack);
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
	    
	    this.addProperty(this.selectedOutputItem);
	}
	
	@Environment(EnvType.CLIENT)
	public int getSelectedOutputItem()
	{
		return selectedOutputItem.get();
	}
	
	@Environment(EnvType.CLIENT)
	public List<Item> getOutputItemsList()
	{
		return this.outputItems;
	}
	
	@Environment(EnvType.CLIENT)
	public int getOutputItemsListSize()
	{
		return this.outputItems.size();
	}
	
	@Environment(EnvType.CLIENT)
	public boolean hasItemsinInputSlot()
	{
		return this.inputInventorySlot.hasStack() && !this.outputItems.isEmpty();
	}

	@Override
	public boolean canUse(PlayerEntity playerIn)
	{
		return canUse(this.worldPosCallable, playerIn, MubbleBlocks.TIMESWAP_TABLE);
	}
	
	@Override
	public boolean onButtonClick(PlayerEntity playerIn, int id)
	{
		if (id >= 0 && id < this.outputItems.size())
		{
			this.selectedOutputItem.set(id);
			this.updateOutputResultSlot();
		}
		return true;
	}
	
	@Override
	public void onContentChanged(Inventory inventoryIn)
	{
		ItemStack itemstack = this.inputInventorySlot.getStack();
		if (itemstack.getItem() != this.itemStackInput.getItem())
		{
			this.itemStackInput = itemstack.copy();
			this.updateAvailableOutputs(inventoryIn, itemstack);
		}
	}
	
	private void updateAvailableOutputs(Inventory inventoryIn, ItemStack stack)
	{
		this.outputItems.clear();
		this.selectedOutputItem.set(-1);
		this.outputInventorySlot.setStack(ItemStack.EMPTY);
		if(!stack.isEmpty())
		{
			Tag<Item> tag = correspondingTag(stack.getItem());
			if(tag != null)
			{
				this.outputItems = Lists.newArrayList();
				for(Item item : Registry.ITEM)
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
			this.outputInventorySlot.setStack(new ItemStack(item));
		}
		else
		{
			this.outputInventorySlot.setStack(ItemStack.EMPTY);
		}
		
		this.sendContentUpdates();
	}
	
	@Environment(EnvType.CLIENT)
	public void setInventoryUpdateListener(Runnable listenerIn)
	{
		this.inventoryUpdateListener = listenerIn;
	}
	
	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slotIn)
	{
		return false;
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if(slot != null && slot.hasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			Item item = itemstack1.getItem();
			itemstack = itemstack1.copy();
			Tag<Item> tag = correspondingTag(item);
			if(index == 1)
			{
				item.onCraft(itemstack1, playerIn.world, playerIn);
				if (!this.insertItem(itemstack1, 2, 38, true))
				{
					return ItemStack.EMPTY;
				}
				slot.onStackChanged(itemstack1, itemstack);
			}
			else if (index == 0)
			{
				if (!this.insertItem(itemstack1, 2, 38, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(tag != null)
			{
				if (!this.insertItem(itemstack1, 0, 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (index >= 2 && index < 29)
			{
				if (!this.insertItem(itemstack1, 29, 38, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (index >= 29 && index < 38 && !this.insertItem(itemstack1, 2, 29, false))
			{
				return ItemStack.EMPTY;
			}
			
			if(itemstack1.isEmpty())
			{
				slot.setStack(ItemStack.EMPTY);
			}
			
			slot.markDirty();
			if(itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTakeItem(playerIn, itemstack1);
			this.sendContentUpdates();
		}
		
		return itemstack;
	}
	
	@Override
	public void close(PlayerEntity playerIn)
	{
		super.close(playerIn);
		this.inventory.removeInvStack(1);
		this.worldPosCallable.run((p_217079_2_, p_217079_3_) ->
		{
			this.dropInventory(playerIn, playerIn.world, this.inputInventory);
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