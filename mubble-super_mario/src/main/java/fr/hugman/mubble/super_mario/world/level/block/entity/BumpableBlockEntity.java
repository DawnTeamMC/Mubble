package fr.hugman.mubble.super_mario.world.level.block.entity;

import fr.hugman.mubble.super_mario.world.inventory.BumpableScreenHandler;
import fr.hugman.mubble.super_mario.world.level.block.BumpableBlock;
import fr.hugman.mubble.super_mario.world.level.block.BumpableDropMode;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Hugman
 * @author haykam
 * @since v4.0.0
 */
public class BumpableBlockEntity extends RandomizableContainerBlockEntity {
	public static final int BUMP_LENGTH = SharedConstants.TICKS_PER_SECOND / 4;
	public static final int BUMP_MIDDLE_TICK = BUMP_LENGTH / 2;

    private static final String BUMPED_STATE_KEY = "bumped_state";
    private static final String DROP_MODE_KEY = "drop_mode";
    private static final String BUMP_TICKS_KEY = "bump_ticks";
    private static final String BUMP_AUTHOR_KEY = "bump_author";
    private static final String BUMP_DIRECTION_KEY = "bump_direction";

    private NonNullList<ItemStack> inventory;
    private BumpableDropMode dropMode = BumpableDropMode.ALL;
    private boolean dropModeLocked = false;
    private @Nullable BlockState bumpedState;

	private int bumpTicks = 0;
	private boolean bumping = false;
	@Nullable
	private Direction bumpDirection;
	@Nullable
	private EntityReference<Entity> bumpAuthor;

    private final ContainerData propertyDelegate = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BumpableBlockEntity.this.dropMode.getIndex();
                case 1 -> BumpableBlockEntity.this.dropModeLocked ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> BumpableBlockEntity.this.dropMode = BumpableDropMode.get(value);
                case 1 -> BumpableBlockEntity.this.dropModeLocked = value == 1;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private BumpableBlockEntity(BlockPos pos, BlockState state, NonNullList<ItemStack> inventory) {
        super(SuperMarioBlockEntityTypes.BUMPABLE_BLOCK, pos, state);
        this.inventory = inventory;
    }

    public BumpableBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, NonNullList.withSize(1, ItemStack.EMPTY));
    }

    public BumpableBlockEntity(BlockPos pos, BlockState state, @Nullable BlockState bumpedState) {
        this(pos, state, NonNullList.withSize(1, ItemStack.EMPTY));
        this.setBumpedState(bumpedState);
    }

    /*========*/
    /*  DATA  */
    /*========*/

	@Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        if (!this.trySaveLootTable(view)) {
            ContainerHelper.saveAllItems(view, this.inventory);
        }
		view.store(DROP_MODE_KEY, BumpableDropMode.CODEC, this.dropMode);
        if (this.bumpedState != null) {
			view.store(BUMPED_STATE_KEY, BlockState.CODEC, this.bumpedState);
        }
		EntityReference.store(this.bumpAuthor, view, BUMP_AUTHOR_KEY);
		view.putInt(BUMP_TICKS_KEY, this.bumpTicks);
		view.storeNullable(BUMP_DIRECTION_KEY, Direction.LEGACY_ID_CODEC, this.bumpDirection);
    }

	@Override
	protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(view)) {
            ContainerHelper.loadAllItems(view, this.inventory);
        }
		view.read(BUMPED_STATE_KEY, BlockState.CODEC).ifPresent(this::setBumpedState);

        this.dropMode = view.read(DROP_MODE_KEY, BumpableDropMode.CODEC).orElse(BumpableDropMode.ALL);
		this.bumpTicks = view.getIntOr(BUMP_TICKS_KEY, 0);
		view.read(BUMP_DIRECTION_KEY, Direction.LEGACY_ID_CODEC).ifPresent(this::setBumpDirection);
		this.bumpAuthor = EntityReference.read(view, BUMP_AUTHOR_KEY);
    }

	/*=====================*/
    /*  GETTERS & SETTERS  */
    /*=====================*/

	public boolean isBumping() {
		return bumping;
	}

	public int getBumpTicks() {
		return bumpTicks;
	}

	public BumpableDropMode getDropMode() {
        return dropMode;
    }

    public void setDropMode(@NotNull BumpableDropMode dropMode, boolean locked) {
        this.dropMode = dropMode;
        this.dropModeLocked = locked;
        this.setChanged();
    }

    public void setDropMode(@NotNull BumpableDropMode dropMode) {
        this.dropMode = dropMode;
        this.setChanged();
    }

    public void setDropModeLocked(boolean dropModeLocked) {
        this.dropModeLocked = dropModeLocked;
        this.setChanged();
    }

    @Nullable
    public BlockState getBumpedState() {
        return bumpedState;
    }

    public void setBumpedState(@Nullable BlockState bumpedState) {
        this.bumpedState = bumpedState;
        if (this.shouldBreak()) {
            this.dropMode = BumpableDropMode.ALL;
            this.dropModeLocked = true;
        }
        this.setChanged();
    }

    public boolean shouldBreak() {
        return bumpedState != null && bumpedState.isAir();
    }

    @Nullable
    public Entity getBumpAuthor() {
		return EntityReference.getEntity(this.bumpAuthor, this.getLevel());
    }

	/**
	 * Sets the entity a bump is credited to, {@code null} clearing it once the bump is over.
	 * <p>
	 * Clearing it matters: an {@link EntityReference} built from an entity holds that entity itself, and only
	 * ever gives it up for its UUID when it is read back after the entity has been removed. A block keeping
	 * its last bumper forever therefore keeps whatever that entity holds on to -- a whole player, connection
	 * and inventory included, long after they disconnected.
	 */
    public void setBumpAuthor(@Nullable Entity entity) {
        this.bumpAuthor = EntityReference.of(entity);
    }

	public void setBumpDirection(@Nullable Direction bumpDirection) {
		this.bumpDirection = bumpDirection;
	}

	public @Nullable Direction getBumpDirection() {
        return this.bumpDirection;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    /*========*/
    /*  MENU  */
    /*========*/

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInv, Player player) {
        return new BumpableScreenHandler(syncId, playerInv, this, propertyDelegate);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new BumpableScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }


    /*============*/
    /*  BEHAVIOR  */
    /*============*/

	public boolean triggerEvent(int type, int data) {
		if (type == 1) {
			this.bumping = true;
			this.bumpTicks = 0;
			this.bumpDirection = Direction.from3DDataValue(data);
			return true;
		} else {
			return super.triggerEvent(type, data);
		}
	}

    /**
     * Bumps the block.
     *
     * @param direction the direction of the bump
     * @param entity    the entity that bumped the block
     */
    public void bump(BlockPos pos, Entity entity, Direction direction) {
		if(this.level == null || this.level.isClientSide()) return;
		this.level.blockEvent(pos, this.getBlockState().getBlock(), 1, direction.get3DDataValue());
		this.setBumpAuthor(entity);
		this.setChanged();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
		if(this.bumping && this.bumpTicks < BUMP_LENGTH) {
			this.bumpTicks++;
		}

		if (this.bumpTicks == BUMP_MIDDLE_TICK) {
			if (state.getBlock() instanceof BumpableBlock bumpable) {
				bumpable.onBumpMiddle(level, pos, state, this);
			}
		}

		if (this.bumpTicks >= BUMP_LENGTH) {
			if (state.getBlock() instanceof BumpableBlock bumpable) {
				this.bumping = false;
				this.bumpTicks = 0;
				this.setBumpAuthor(null);
				this.setChanged();
				bumpable.onBumpEnd(level, pos, state, this);
			}
		}
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
