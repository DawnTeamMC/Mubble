package fr.hugman.mubble.block;

import fr.hugman.mubble.block.bump.BumpConfig;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.registry.MubbleSounds;
import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
public abstract class BumpableBlock extends BlockWithEntity implements HittableBlock {
	public static final BooleanProperty BUMPING = BooleanProperty.of("bumping");

	private final BumpConfig defaultBumpConfig;

	public BumpableBlock(BumpConfig defaultBumpConfig, Settings settings) {
		super(settings);
		this.defaultBumpConfig = defaultBumpConfig;
		this.setDefaultState(this.stateManager.getDefaultState().with(BUMPING, false));
	}

	/*==========*/
	/*  STATES  */
	/*==========*/

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BUMPING);
	}

	/*===========*/
	/*  GETTERS  */
	/*===========*/

	public BumpConfig getDefaultBumpConfigInstance() {
		return defaultBumpConfig.copy();
	}

	/*================*/
	/*  BLOCK ENTITY  */
	/*================*/

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BumpableBlockEntity(pos, state, this.getDefaultBumpConfigInstance());
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, (w, p, s, e) -> e.tick(w, p, s));
	}

	/*=============*/
	/*  RENDERING  */
	/*=============*/

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return state.get(BUMPING) ? BlockRenderType.ENTITYBLOCK_ANIMATED : BlockRenderType.MODEL;
	}

	/*============*/
	/*  BEHAVIOR  */
	/*============*/

	/**
	 * This method is called before the block entity receives the new data.
	 *
	 * @return true if the block should be bumped, false otherwise
	 */
	public boolean canBump(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity, Entity entity, BlockHitResult hit) {
		return !state.get(BUMPING);
	}

	/**
	 * Called when the block is getting bumped.
	 */
	public void onBump(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		var bumpAuthor = blockEntity.getBumpAuthor();
		//TODO: change the game event to something more appropriate
		world.emitGameEvent(bumpAuthor, GameEvent.BLOCK_OPEN, pos);
		if(bumpAuthor instanceof PlayerEntity player) {
			//TODO: create a new "Bumped Blocks" stat
			//player.incrementStat(MubbleStats.BUMPED_BLOCKS);
		}
	}

	/**
	 * Called when a block is at the middle of being bumped.
	 */
	public void onBumpMiddle(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		if(world != null && !world.isClient()) {
			if(blockEntity.getBumpConfig().shouldDestroy()) {
				Vec3d center = blockEntity.getPos().toCenterPos();

				this.loot(world, pos, state, blockEntity);
				world.breakBlock(blockEntity.getPos(), false);
				world.playSound(null, center.getX(), center.getY(), center.getZ(), MubbleSounds.BUMPABLE_BLOCK_DESTROY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Called when a block finishes being bumped.
	 */
	public void onBumpEnd(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		if(world != null && !world.isClient()) {
			this.loot(world, pos, state, blockEntity);
			if(blockEntity.getBumpConfig().shouldDestroy()) {
				// this should never happen since it already happened in onBumpMiddle
				world.breakBlock(blockEntity.getPos(), false);
				return;
			}
			var newState = blockEntity.getBumpConfig().state();
			if(newState != null) {
				world.setBlockState(pos, newState);
			}
			else {
				world.setBlockState(pos, state.with(BUMPING, false));
			}
		}
	}


	@Override
	public void onHit(World world, BlockPos pos, BlockState state, Entity entity, BlockHitResult hit) {
		if(world.isClient()) {
			return;
		}

		Optional<BumpableBlockEntity> opt = world.getBlockEntity(pos, SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE);
		opt.ifPresent(blockEntity -> {
			if(this.canBump(world, pos, state, blockEntity, entity, hit)) {
				blockEntity.bump(world, pos, state, entity, hit.getSide().getOpposite());
			}
		});
	}

	public void loot(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		var config = blockEntity.getBumpConfig();
		var stack = config.stack();
		if(stack == null) {
			return;
		}

		var actualState = world.getBlockState(pos);

		// TODO
		// if there is no block at the position, drop in the center of the block like a regular drop
		// else, drop at the center of the block but apply the direction offset

		var center = pos.toCenterPos();
		if(actualState.isAir()) {
			ItemScatterer.spawn(world, center.getX(), center.getY(), center.getZ(), stack);
		}
		else {
			var direction = blockEntity.getBumpDirection();
			var x = center.getX() + direction.getOffsetX() * 0.75D;
			var y = center.getY() + direction.getOffsetY() * 0.75D;
			var z = center.getZ() + direction.getOffsetZ() * 0.75D;
			ItemScatterer.spawn(world, x, y, z, stack);
		}
		blockEntity.setBumpConfig(config.withStack(null));
		world.playSound(null, center.getX(), center.getY(), center.getZ(), MubbleSounds.BUMPABLE_BLOCK_LOOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
