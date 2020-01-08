package hugman.mubble.objects.tile_entity;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTileEntityTypes;
import hugman.mubble.objects.block.PresentBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;

public class PresentTileEntity extends LockableContainerBlockEntity
{
	private DefaultedList<ItemStack> content = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
	private int numPlayersUsing;
	
	public PresentTileEntity()
	{
		super(MubbleTileEntityTypes.PRESENT);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag compound)
	{
		super.toTag(compound);
		if(!this.isInvEmpty())
		{
			Inventories.toTag(compound, this.content);
		}
		return compound;
	}
	
	@Override
	public void fromTag(CompoundTag compound)
	{
		super.fromTag(compound);
		this.content = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
		if(!this.isInvEmpty())
		{
			Inventories.fromTag(compound, this.content);
		}
	}
	
	@Override
	public int getInvSize()
	{
		return 9 * 2;
	}
	
	@Override
	public boolean isInvEmpty()
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
	public ItemStack getInvStack(int index)
	{
		return this.content.get(index);
	}
	
	@Override
	public ItemStack takeInvStack(int index, int count)
	{
		this.scheduleTick();
		return Inventories.splitStack(this.content, index, count);
	}
	
	@Override
	public ItemStack removeInvStack(int index)
	{
		this.scheduleTick();
		return Inventories.removeStack(this.content, index);
	}
	
	@Override
	public void setInvStack(int index, ItemStack stack)
	{
		this.content.set(index, stack);
		if(stack.getCount() > this.getInvMaxStackAmount())
		{
			stack.setCount(this.getInvMaxStackAmount());
		}
		this.scheduleTick();
	}
	
	@Override
	public void clear()
	{
		this.content.clear();
	}
	
	@Override
	protected Text getContainerName()
	{
		return new TranslatableText("container." + Mubble.MOD_ID + ".present");
	}
	
	@Override
	protected Container createContainer(int id, PlayerInventory player)
	{
		return new GenericContainer(ContainerType.GENERIC_9X2, id, player, this, 2);
	}
	
	@Override
	public void onInvOpen(PlayerEntity player)
	{
		if(!player.isSpectator())
		{
			if(this.numPlayersUsing < 0)
			{
				this.numPlayersUsing = 0;
			}
			++this.numPlayersUsing;
			
			BlockState blockstate = this.getCachedState();
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
		this.world.getBlockTickScheduler().schedule(this.getPos(), this.getCachedState().getBlock(), 5);
	}
	
	public void presentTick()
	{
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		
		this.numPlayersUsing = ChestBlockEntity.countViewers(this.world, this, i, j, k);
		
		BlockState blockstate = this.getCachedState();
		boolean flag1 = blockstate.get(PresentBlock.OPEN);
		boolean flag2 = this.isInvEmpty();

		this.setEmptyProperty(blockstate, flag2);
		if(this.numPlayersUsing > 0)
		{
			this.scheduleTick();
		}
		else
		{
			if(!(blockstate.getBlock() instanceof PresentBlock))
			{
				this.markRemoved();
				return;
			}
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
	public void onInvClose(PlayerEntity player)
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
	
	private void setEmptyProperty(BlockState state, boolean empty)
	{
		this.world.setBlockState(this.getPos(), state.with(PresentBlock.EMPTY, Boolean.valueOf(empty)), 3);
	}
	
	private void playSound(BlockState state, SoundEvent sound)
	{
		double d0 = (double) this.pos.getX() + 0.5D;
		double d1 = (double) this.pos.getY() + 0.5D;
		double d2 = (double) this.pos.getZ() + 0.5D;
		this.world.playSound((PlayerEntity) null, d0, d1, d2, sound, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity player) {
		return true;
	}
}