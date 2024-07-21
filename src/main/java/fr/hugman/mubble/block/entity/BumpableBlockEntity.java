package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.block.BumpableBlock;
import fr.hugman.mubble.block.BumpableDropMode;
import fr.hugman.mubble.nbt.MubbleNbtHelper;
import fr.hugman.mubble.registry.SuperMario;
import fr.hugman.mubble.screen.BumpableScreenHandler;
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
import net.minecraft.screen.PropertyDelegate;
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
	private static final String DROP_MODE_KEY = "DropMode";
	private static final String BUMP_TICKS_KEY = "BumpTicks";
	private static final String BUMP_AUTHOR_KEY = "BumpAuthor";
	private static final String BUMP_DIRECTION_KEY = "BumpDirection";

	private DefaultedList<ItemStack> inventory;
	private BumpableDropMode dropMode = BumpableDropMode.ALL;
	private boolean dropModeLocked = false;
	private @Nullable BlockState bumpedState;
	private int bumpTicks = -1;
	private @Nullable UUID bumpAuthorUuid;
	private @Nullable Entity bumpAuthor;
	private Direction bumpDirection = Direction.UP;

	private final PropertyDelegate propertyDelegate = new PropertyDelegate(){
		@Override
		public int get(int index) {
			return switch(index) {
				case 0 -> BumpableBlockEntity.this.dropMode.getIndex();
				case 1 -> BumpableBlockEntity.this.dropModeLocked ? 1 : 0;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch(index) {
				case 0 -> BumpableBlockEntity.this.dropMode = BumpableDropMode.get(value);
				case 1 -> BumpableBlockEntity.this.dropModeLocked = value == 1;
			}
		}

		@Override
		public int size() {
			return 2;
		}
	};

	private BumpableBlockEntity(BlockPos pos, BlockState state, DefaultedList<ItemStack> inventory) {
		super(SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, pos, state);
		this.inventory = inventory;
	}

	public BumpableBlockEntity(BlockPos pos, BlockState state) {
		this(pos, state, DefaultedList.ofSize(1, ItemStack.EMPTY));
	}

	public BumpableBlockEntity(BlockPos pos, BlockState state, @NotNull ItemStack stack, @Nullable BlockState bumpedState) {
		this(pos, state, DefaultedList.ofSize(1, stack));
		this.setBumpedState(bumpedState);
	}

	/*=======*/
	/*  NBT  */
	/*=======*/

	@Override
	protected void writeNbt(NbtCompound nbt) {
		if(!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
		nbt.putInt(DROP_MODE_KEY, this.dropMode.getIndex());
		if(this.bumpedState != null) {
			nbt.put(BUMPED_STATE_KEY, NbtHelper.fromBlockState(this.bumpedState));
		}
		if(this.bumpAuthorUuid != null) {
			nbt.putUuid(BUMP_AUTHOR_KEY, this.bumpAuthorUuid);
		}
		this.writeClientNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if(!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
		if(nbt.contains(BUMPED_STATE_KEY)) {
			if(this.world == null) {
				this.bumpedState = MubbleNbtHelper.toBlockStateNoWrapper(nbt.getCompound(BUMPED_STATE_KEY));
			}
			else {
				RegistryWrapper<Block> registry = this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK);
				this.bumpedState = NbtHelper.toBlockState(registry, nbt.getCompound(BUMPED_STATE_KEY));
			}
		}

		this.dropMode = BumpableDropMode.get(nbt.getInt(DROP_MODE_KEY));
		this.bumpTicks = Math.max(-1, nbt.getInt(BUMP_TICKS_KEY));
		if(nbt.containsUuid(BUMP_AUTHOR_KEY)) {
			this.bumpAuthorUuid = nbt.getUuid(BUMP_AUTHOR_KEY);
		}
		this.bumpDirection = Direction.byId(nbt.getInt(BUMP_DIRECTION_KEY));
		if(this.bumpDirection == null) {
			this.bumpDirection = Direction.UP;
		}
	}

	protected void writeClientNbt(NbtCompound nbt) {
		nbt.putInt(BUMP_TICKS_KEY, this.bumpTicks);
		nbt.putInt(BUMP_DIRECTION_KEY, this.bumpDirection.getId());
	}

	/*=====================*/
	/*  GETTERS & SETTERS  */
	/*=====================*/

	public BumpableDropMode getDropMode() {
		return dropMode;
	}

	public void setDropMode(@NotNull BumpableDropMode dropMode, boolean locked) {
		this.dropMode = dropMode;
		this.dropModeLocked = locked;
		this.markDirty();
	}

	public void setDropMode(@NotNull BumpableDropMode dropMode) {
		this.dropMode = dropMode;
		this.markDirty();
	}

	public void setDropModeLocked(boolean dropModeLocked) {
		this.dropModeLocked = dropModeLocked;
		this.markDirty();
	}

	@Nullable
	public BlockState getBumpedState() {
		return bumpedState;
	}

	public void setBumpedState(@Nullable BlockState bumpedState) {
		this.bumpedState = bumpedState;
		if(this.shouldBreak()) {
			this.dropMode = BumpableDropMode.ALL;
			this.dropModeLocked = true;
		}
		this.markDirty();
	}

	public boolean shouldBreak() {
		return bumpedState != null && bumpedState.isAir();
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
		return new BumpableScreenHandler(syncId, playerInv, this, propertyDelegate);
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new BumpableScreenHandler(syncId, playerInventory, this, propertyDelegate);
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
