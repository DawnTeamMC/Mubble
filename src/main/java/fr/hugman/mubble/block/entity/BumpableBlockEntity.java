package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.block.BumpableBlock;
import fr.hugman.mubble.block.BumpableDropMode;
import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.screen.BumpableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author Hugman
 * @author haykam
 * @since v4.0.0
 */
public class BumpableBlockEntity extends LootableContainerBlockEntity {
    public static final float BUMP_SPEED = 1/5f;

    private static final String BUMPED_STATE_KEY = "bumped_state";
    private static final String DROP_MODE_KEY = "drop_mode";
    private static final String BUMP_PROGRESS_KEY = "bump_progress";
    private static final String BUMP_AUTHOR_KEY = "bump_author";
    private static final String BUMP_DIRECTION_KEY = "bump_direction";

    private DefaultedList<ItemStack> inventory;
    private BumpableDropMode dropMode = BumpableDropMode.ALL;
    private boolean dropModeLocked = false;
    private @Nullable BlockState bumpedState;
	private float bumpProgress = Integer.MIN_VALUE;
	private float lastBumpProgress = 0.0F;
	@Nullable
	private LazyEntityReference<Entity> bumpAuthor;
	@Nullable
    private Direction bumpDirection;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
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
        public int size() {
            return 2;
        }
    };

    private BumpableBlockEntity(BlockPos pos, BlockState state, DefaultedList<ItemStack> inventory) {
        super(MubbleBlockEntityTypes.BUMPABLE_BLOCK, pos, state);
        this.inventory = inventory;
    }

    public BumpableBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, DefaultedList.ofSize(1, ItemStack.EMPTY));
    }

    public BumpableBlockEntity(BlockPos pos, BlockState state, @Nullable BlockState bumpedState) {
        this(pos, state, DefaultedList.ofSize(1, ItemStack.EMPTY));
        this.setBumpedState(bumpedState);
    }

    /*========*/
    /*  DATA  */
    /*========*/

	@Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (!this.writeLootTable(view)) {
            Inventories.writeData(view, this.inventory);
        }
		view.put(DROP_MODE_KEY, BumpableDropMode.CODEC, this.dropMode);
        if (this.bumpedState != null) {
			view.put(BUMPED_STATE_KEY, BlockState.CODEC, this.bumpedState);
        }
		LazyEntityReference.writeData(this.bumpAuthor, view, BUMP_AUTHOR_KEY);
		view.putFloat(BUMP_PROGRESS_KEY, this.lastBumpProgress);
		view.putNullable(BUMP_DIRECTION_KEY, Direction.INDEX_CODEC, this.bumpDirection);
    }

	@Override
	protected void readData(ReadView view) {
        super.readData(view);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(view)) {
            Inventories.readData(view, this.inventory);
        }
		view.read(BUMPED_STATE_KEY, BlockState.CODEC).ifPresent(this::setBumpedState);

        this.dropMode = view.read(DROP_MODE_KEY, BumpableDropMode.CODEC).orElse(BumpableDropMode.ALL);
		this.bumpProgress = view.getFloat(BUMP_PROGRESS_KEY, 0.0F);
		view.read(BUMP_DIRECTION_KEY, Direction.INDEX_CODEC).ifPresent(this::setBumpDirection);
		this.bumpAuthor = LazyEntityReference.fromData(view, BUMP_AUTHOR_KEY);
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
        if (this.shouldBreak()) {
            this.dropMode = BumpableDropMode.ALL;
            this.dropModeLocked = true;
        }
        this.markDirty();
    }

    public boolean shouldBreak() {
        return bumpedState != null && bumpedState.isAir();
    }

	public float getBumpProgress(float tickProgress) {
		if (tickProgress > 1.0F) {
			tickProgress = 1.0F;
		}

		return MathHelper.lerp(tickProgress, this.lastBumpProgress, this.bumpProgress);
	}

    @Nullable
    public Entity getBumpAuthor() {
		return LazyEntityReference.getEntity(this.bumpAuthor, this.getWorld());
    }

    public void setBumpAuthor(@Nullable Entity entity) {
        if (entity != null) {
            this.bumpAuthor = LazyEntityReference.of(entity);
        }
    }

	public void setBumpDirection(@Nullable Direction bumpDirection) {
		this.bumpDirection = bumpDirection;
	}

	public @Nullable Direction getBumpDirection() {
        return this.bumpDirection;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
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
     */
    public void bump(World world, BlockPos pos, BlockState state, Entity entity, Direction direction) {
        this.bumpProgress = 0.0F;
		this.lastBumpProgress = 0.0F;
        this.bumpDirection = direction;
        this.setBumpAuthor(entity);
        this.markDirty();
        if (state.getBlock() instanceof BumpableBlock bumpable) {
            bumpable.onBump(world, pos, state, this);
            world.setBlockState(pos, state.with(BumpableBlock.BUMPING, true));
        }
    }

    public void tick(World world, BlockPos pos, BlockState state) {
		this.lastBumpProgress = this.bumpProgress;

		if(!state.get(BumpableBlock.BUMPING)) {
			return;
		}

		if (this.lastBumpProgress == 1.0F) {
			if (state.getBlock() instanceof BumpableBlock bumpable) {
				this.setBumpAuthor(null);
				this.markDirty();
				bumpable.onBumpEnd(world, pos, state, this);
				this.bumpProgress = Integer.MIN_VALUE;
			}
		}
		else {
			this.bumpProgress += BUMP_SPEED;
			if (this.bumpProgress >= 1.0F) {
				this.bumpProgress = 1.0F;
			}

			if(Math.abs(this.bumpProgress - 0.5f) < BUMP_SPEED) {
				if (state.getBlock() instanceof BumpableBlock bumpable) {
					bumpable.onBumpMiddle(world, pos, state, this);
				}
			}
		}
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = new NbtCompound();
		nbt.putFloat(BUMP_PROGRESS_KEY, this.bumpProgress);
		nbt.putNullable(BUMP_DIRECTION_KEY, Direction.INDEX_CODEC, this.bumpDirection);
        return nbt;
    }
}
