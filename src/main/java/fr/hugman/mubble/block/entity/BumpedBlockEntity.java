package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.block.BumpableBlock;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BumpedBlockEntity extends BlockEntity {
	public static final int ANIMATION_TICKS = SharedConstants.TICKS_PER_SECOND / 4;

	private static final String BLOCK_STATE_KEY = "BlockState";
	private static final String BUMP_TICKS_KEY = "BumpTicks";

	private BlockState blockState = Blocks.AIR.getDefaultState();
	private int bumpTicks = 0;

	public BumpedBlockEntity(BlockPos pos, BlockState state) {
		super(SuperMarioContent.BUMPED_BLOCK_ENTITY_TYPE, pos, state);
	}

	public BlockState getBlockState() {
		return this.blockState;
	}

	public void setBlockState(BlockState blockState) {
		this.blockState = blockState;
		this.markDirty();
	}

	public int getBumpTicks() {
		return this.bumpTicks;
	}

	private void writeClientNbt(NbtCompound nbt) {
		nbt.put(BLOCK_STATE_KEY, NbtHelper.fromBlockState(this.getBlockState()));
		nbt.putInt(BUMP_TICKS_KEY, this.bumpTicks);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		RegistryWrapper<Block> registry = this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK);
		this.setBlockState(NbtHelper.toBlockState(registry, nbt.getCompound(BLOCK_STATE_KEY)));

		this.bumpTicks = Math.max(0, nbt.getInt(BUMP_TICKS_KEY));
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		this.writeClientNbt(nbt);
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

	public static void bump(World world, BlockPos pos) {
		if (world.isClient()) {
			return;
		}

		BlockState state = world.getBlockState(pos);
		world.setBlockState(pos, SuperMarioContent.BUMPED_BLOCK.getDefaultState());

		world.getBlockEntity(pos, SuperMarioContent.BUMPED_BLOCK_ENTITY_TYPE).ifPresent(entity -> {
			entity.setBlockState(state);
		});
	}

	public static void tick(World world, BlockPos pos, BlockState state, BumpedBlockEntity entity) {
		entity.bumpTicks++;

		if (!world.isClient() && entity.bumpTicks > ANIMATION_TICKS) {
			BlockState newState = BumpableBlock.onBumpCompleted(entity.getBlockState(), entity);
			world.setBlockState(pos, newState);
		}
	}
}
