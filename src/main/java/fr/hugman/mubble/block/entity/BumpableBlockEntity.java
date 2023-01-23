package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.block.BumpableBlock;
import fr.hugman.mubble.block.bump.BumpConfig;
import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
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
public class BumpableBlockEntity extends BlockEntity {
	public static final int ANIMATION_TICKS = SharedConstants.TICKS_PER_SECOND / 4;
	public static final int PEAK_TICK = ANIMATION_TICKS / 2;

	private static final String BUMP_CONFIG_KEY = "BumpConfig";
	private static final String BUMP_TICKS_KEY = "BumpTicks";
	private static final String BUMP_AUTHOR_KEY = "BumpAuthor";
	private static final String BUMP_DIRECTION_KEY = "BumpDirection";

	private @NotNull BumpConfig bumpConfig;
	private int bumpTicks = -1;
	private UUID bumpAuthorUuid;
	private Entity bumpAuthor;
	private Direction bumpDirection = Direction.UP;

	public BumpableBlockEntity(BlockPos pos, BlockState state) {
		this(pos, state, BumpConfig.NOTHING);
	}

	public BumpableBlockEntity(BlockPos pos, BlockState state, @NotNull BumpConfig bumpConfig) {
		super(SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, pos, state);
		this.bumpConfig = bumpConfig;
	}

	/*=====================*/
	/*  GETTERS & SETTERS  */
	/*=====================*/

	@NotNull
	public BumpConfig getBumpConfig() {
		return this.bumpConfig;
	}

	public void setBumpConfig(BumpConfig bumpConfig) {
		this.bumpConfig = bumpConfig;
		this.markDirty();
	}

	public int getBumpTicks() {
		return this.bumpTicks;
	}

	public void setBumpAuthor(@Nullable Entity entity) {
		if(entity != null) {
			this.bumpAuthorUuid = entity.getUuid();
			this.bumpAuthor = entity;
		}
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

	public Direction getBumpDirection() {
		return this.bumpDirection;
	}

	public boolean isBumping() {
		return this.bumpTicks >= 0 && this.bumpTicks <= BumpableBlockEntity.ANIMATION_TICKS;
	}

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

	/*============*/
	/*  BEHAVIOR  */
	/*============*/

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

	/*=======*/
	/*  NBT  */
	/*=======*/

	protected void writeClientNbt(NbtCompound nbt) {
		nbt.putInt(BUMP_TICKS_KEY, this.bumpTicks);
		nbt.putInt(BUMP_DIRECTION_KEY, this.bumpDirection.getId());
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		if(this.bumpConfig != null) {
			nbt.put(BUMP_CONFIG_KEY, this.bumpConfig.toNbt());
		}
		if(this.bumpAuthorUuid != null) {
			nbt.putUuid(BUMP_AUTHOR_KEY, this.bumpAuthorUuid);
		}
		this.writeClientNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.bumpTicks = Math.max(-1, nbt.getInt(BUMP_TICKS_KEY));
		if(nbt.contains(BUMP_CONFIG_KEY)) {
			this.bumpConfig = BumpConfig.fromNbt(nbt.getCompound(BUMP_CONFIG_KEY), this.world);
		}
		if(nbt.containsUuid(BUMP_AUTHOR_KEY)) {
			this.bumpAuthorUuid = nbt.getUuid(BUMP_AUTHOR_KEY);
		}
		this.bumpDirection = Direction.byId(nbt.getInt(BUMP_DIRECTION_KEY));
		if(this.bumpDirection == null) {
			this.bumpDirection = Direction.UP;
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
