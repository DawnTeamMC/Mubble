package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.block.BumpableBlock;
import fr.hugman.mubble.registry.SuperMario;
import fr.hugman.mubble.screen.BumpableBlockScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * @author Hugman
 * @author haykam
 * @since v4.0.0
 */
public class BumpableBlockEntity extends LootableContainerBlockEntity {
	public static final int ANIMATION_TICKS = SharedConstants.TICKS_PER_SECOND / 4;
	public static final int PEAK_TICK = ANIMATION_TICKS / 2;

	private static final String BUMPED_STATE_KEY = "BumpedState";
	private static final String BUMP_TICKS_KEY = "BumpTicks";
	private static final String BUMP_AUTHOR_KEY = "BumpAuthor";
	private static final String BUMP_DIRECTION_KEY = "BumpDirection";

	private DefaultedList<ItemStack> inventory;
	private @Nullable BlockState bumpedState;
	private int bumpTicks = -1;
	private @Nullable UUID bumpAuthorUuid;
	private @Nullable Entity bumpAuthor;
	private Direction bumpDirection = Direction.UP;


	private BumpableBlockEntity(BlockPos pos, BlockState state, DefaultedList<ItemStack> inventory) {
		super(SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, pos, state);
		this.inventory = inventory;
	}

	public BumpableBlockEntity(BlockPos pos, BlockState state) {
		this(pos, state, DefaultedList.ofSize(1, ItemStack.EMPTY));
	}

	public BumpableBlockEntity(BlockPos pos, BlockState state, @NotNull ItemStack stack, @Nullable BlockState bumpedState) {
		this(pos, state, DefaultedList.ofSize(1, stack));
		this.bumpedState = bumpedState;
	}

	/*=======*/
	/*  NBT  */
	/*=======*/

	@Override
	protected void writeNbt(NbtCompound nbt) {
		if(!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
		if(this.bumpedState != null) {
			nbt.put(BUMPED_STATE_KEY, NbtHelper.fromBlockState(this.bumpedState));
		}
		if(this.bumpAuthorUuid != null) {
			nbt.putUuid(BUMP_AUTHOR_KEY, this.bumpAuthorUuid);
		}
		this.writeClientNbt(nbt);
	}

	protected void writeClientNbt(NbtCompound nbt) {
		nbt.putInt(BUMP_TICKS_KEY, this.bumpTicks);
		nbt.putInt(BUMP_DIRECTION_KEY, this.bumpDirection.getId());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if(!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
		if(this.world != null && nbt.contains(BUMPED_STATE_KEY)) {
			RegistryWrapper<Block> registry = this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK);
			this.bumpedState = NbtHelper.toBlockState(registry, nbt.getCompound(BUMPED_STATE_KEY));
		}

		this.bumpTicks = Math.max(-1, nbt.getInt(BUMP_TICKS_KEY));
		if(nbt.containsUuid(BUMP_AUTHOR_KEY)) {
			this.bumpAuthorUuid = nbt.getUuid(BUMP_AUTHOR_KEY);
		}
		this.bumpDirection = Direction.byId(nbt.getInt(BUMP_DIRECTION_KEY));
		if(this.bumpDirection == null) {
			this.bumpDirection = Direction.UP;
		}
	}

	/*=====================*/
	/*  GETTERS & SETTERS  */
	/*=====================*/

	@Nullable
	public BlockState getBumpedState() {
		return bumpedState;
	}

	public void setBumpedState(@Nullable BlockState bumpedState) {
		this.bumpedState = bumpedState;
	}

	public int getBumpTicks() {
		return this.bumpTicks;
	}

	@Nullable
	public Entity getBumpAuthor() {
		if(this.bumpAuthor != null && !this.bumpAuthor.isRemoved()) {
			return this.bumpAuthor;
		}
		if(this.bumpAuthorUuid != null && this.world instanceof ServerWorld) {
			this.bumpAuthor = ((ServerWorld) this.world).getEntity(this.bumpAuthorUuid);
			return this.bumpAuthor;
		}
		return null;
	}

	public void setBumpAuthor(@Nullable Entity entity) {
		if(entity != null) {
			this.bumpAuthorUuid = entity.getUuid();
			this.bumpAuthor = entity;
		}
	}

	public Direction getBumpDirection() {
		return this.bumpDirection;
	}

	public boolean isBumping() {
		return this.bumpTicks >= 0 && this.bumpTicks <= BumpableBlockEntity.ANIMATION_TICKS;
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	/*========*/
	/*  MENU  */
	/*========*/

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInv, PlayerEntity player) {
		return new BumpableBlockScreenHandler(syncId, playerInv, this);
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new BumpableBlockScreenHandler(syncId, playerInventory, this);
	}


	/*============*/
	/*  BEHAVIOR  */
	/*============*/

	/**
	 * Bumps the block.
	 *
	 * @param direction the direction of the bump
	 * @param entity    the entity that bumped the block
	 *
	 * @return true if the block was bumped, false if it was already bumping
	 */
	public void bump(World world, BlockPos pos, BlockState state, Entity entity, Direction direction) {
		this.bumpTicks = 0;
		this.bumpDirection = direction;
		this.setBumpAuthor(entity);
		this.markDirty();
		if(state.getBlock() instanceof BumpableBlock bumpable) {
			bumpable.onBump(world, pos, state, this);
			world.setBlockState(pos, state.with(BumpableBlock.BUMPING, true));
		}
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if(isBumping()) this.bumpTicks++;

		if(this.getBumpTicks() == BumpableBlockEntity.PEAK_TICK) {
			if(state.getBlock() instanceof BumpableBlock bumpable) {
				bumpable.onBumpMiddle(world, pos, state, this);
			}
		}

		if(this.getBumpTicks() > BumpableBlockEntity.ANIMATION_TICKS) {
			if(state.getBlock() instanceof BumpableBlock bumpable) {
				this.bumpTicks = -1;
				this.setBumpAuthor(null);
				this.markDirty();
				bumpable.onBumpEnd(world, pos, state, this);
			}
		}
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = new NbtCompound();
		this.writeClientNbt(nbt);
		return nbt;
	}
}
