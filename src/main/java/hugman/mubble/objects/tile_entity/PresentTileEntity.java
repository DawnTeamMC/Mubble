package hugman.mubble.objects.tile_entity;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTileEntityTypes;
import hugman.mubble.objects.block.PresentBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PresentTileEntity extends LockableLootTileEntity
{
	private NonNullList<ItemStack> content = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private int numPlayersUsing;
	
	public PresentTileEntity()
	{
		super(MubbleTileEntityTypes.PRESENT);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		if(!this.checkLootAndWrite(compound))
		{
			ItemStackHelper.saveAllItems(compound, this.content);
		}
		return compound;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.content = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if(!this.checkLootAndRead(compound))
		{
			ItemStackHelper.loadAllItems(compound, this.content);
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return 9 * 2;
	}
	
	@Override
	public boolean isEmpty()
	{
		for(ItemStack itemstack : this.content)
		{
			if(!itemstack.isEmpty())
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.content.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.content, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.content, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.content.set(index, stack);
		if(stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}
	}
	
	@Override
	public void clear()
	{
		this.content.clear();
	}
	
	@Override
	protected NonNullList<ItemStack> getItems()
	{
		return this.content;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn)
	{
		this.content = itemsIn;
	}
	
	@Override
	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container." + Mubble.MOD_ID + ".present");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new ChestContainer(ContainerType.GENERIC_9X2, id, player, this, 2);
	}
	
	@Override
	public void openInventory(PlayerEntity player)
	{
		if(!player.isSpectator())
		{
			if(this.numPlayersUsing < 0)
			{
				this.numPlayersUsing = 0;
			}
			++this.numPlayersUsing;
			
			BlockState blockstate = this.getBlockState();
			boolean flag1 = blockstate.get(PresentBlock.OPEN);
			boolean flag2 = blockstate.get(PresentBlock.EMPTY);
			if(!flag1)
			{
				if(!flag2)
				{
					this.playSound(blockstate, MubbleSounds.BLOCK_PRESENT_OPEN);
				}
				this.setOpenProperty(blockstate, true);
			}
			this.scheduleTick();
		}
	}
	
	private void scheduleTick()
	{
		this.world.getPendingBlockTicks().scheduleTick(this.getPos(), this.getBlockState().getBlock(), 5);
	}
	
	public void presentTick()
	{
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		this.numPlayersUsing = ChestTileEntity.calculatePlayersUsing(this.world, this, i, j, k);
		if(this.numPlayersUsing > 0)
		{
			this.scheduleTick();
		}
		else
		{
			BlockState blockstate = this.getBlockState();
			if(!(blockstate.getBlock() instanceof PresentBlock))
			{
				this.remove();
				return;
			}
			
			boolean flag1 = blockstate.get(PresentBlock.OPEN);
			boolean flag2 = this.isEmpty();
			if(flag1)
			{
				if(!flag2)
				{
					this.playSound(blockstate, MubbleSounds.BLOCK_PRESENT_CLOSE);
				}
				this.setOpenProperty(blockstate.with(PresentBlock.EMPTY, flag2), false);
			}
		}
	} 
	
	@Override
	public void closeInventory(PlayerEntity player)
	{
		if(!player.isSpectator())
		{
			--this.numPlayersUsing;
		}
	}
	
	private void setOpenProperty(BlockState state, boolean open)
	{
		this.world.setBlockState(this.getPos(), state.with(PresentBlock.OPEN, Boolean.valueOf(open)), 3);
	}
	
	private void playSound(BlockState state, SoundEvent sound)
	{
		double d0 = (double) this.pos.getX() + 0.5D;
		double d1 = (double) this.pos.getY() + 0.5D;
		double d2 = (double) this.pos.getZ() + 0.5D;
		this.world.playSound((PlayerEntity)null, d0, d1, d2, sound, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
	}
}