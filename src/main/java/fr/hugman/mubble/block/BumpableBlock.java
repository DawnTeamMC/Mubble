package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.entity.BeanstalkEntity;
import fr.hugman.mubble.registry.MubbleSounds;
import fr.hugman.mubble.registry.SuperMario;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * @author haykam
 * @author Hugman
 * @author MaxBrick
 * @since v4.0.0
 */
public class BumpableBlock extends BlockWithEntity implements HittableBlock {
	public static final BooleanProperty BUMPING = BooleanProperty.of("bumping");

	private final ItemStack defaultStack;
	private final @Nullable BlockState defaultBumpedState;

	public BumpableBlock(ItemStack defaultStack, @Nullable BlockState defaultBumpedState, Settings settings) {
		super(settings);
		this.defaultStack = defaultStack;
		this.defaultBumpedState = defaultBumpedState;
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

	public ItemStack getDefaultStack() {
		return defaultStack;
	}

	@Nullable
	public BlockState getDefaultBumpedState() {
		return defaultBumpedState;
	}

	/*================*/
	/*  BLOCK ENTITY  */
	/*================*/

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BumpableBlockEntity(pos, state, this.getDefaultStack().copy(), this.getDefaultBumpedState());
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, (w, p, s, e) -> e.tick(w, p, s));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(!player.getStackInHand(hand).isOf(SuperMario.MAKER_GLOVE)) {
			return ActionResult.PASS;
		}
		if(world.isClient) {
			return ActionResult.SUCCESS;
		}
		if(world.getBlockEntity(pos) instanceof BumpableBlockEntity bumpableEntity) {
			player.openHandledScreen(bumpableEntity);
			// TODO: add stat for inspecting bumpable blocks
			//player.incrementStat(Stats.INSPECT_HOPPER);
		}
		return ActionResult.CONSUME;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(stack.hasCustomName() && world.getBlockEntity(pos) instanceof BumpableBlockEntity bumpable) {
			bumpable.setCustomName(stack.getName());
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.isOf(newState.getBlock())) {
			return;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BumpableBlockEntity bumpable) {
			ItemScatterer.spawn(world, pos, bumpable);
			world.updateComparators(pos, this);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	/*=============*/
	/*  RENDERING  */
	/*=============*/

	@Override
	@Environment(EnvType.CLIENT)
	public BlockRenderType getRenderType(BlockState state) {
		if(MinecraftClient.isFancyGraphicsOrBetter()) return BlockRenderType.ENTITYBLOCK_ANIMATED;
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
		// TODO: check if the block is locked (vanilla locks to players only)
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
			if(blockEntity.getBumpedState() != null && blockEntity.getBumpedState().isAir()) {
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
			if(blockEntity.getBumpedState() != null && blockEntity.getBumpedState().isAir()) {
				// this should never happen since it already happened in onBumpMiddle
				world.breakBlock(blockEntity.getPos(), false);
				return;
			}
			var newState = blockEntity.getBumpedState();

            this.loot(world, pos, state, blockEntity);

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
		if(blockEntity.isEmpty()) {
			return;
		}
		var actualState = world.getBlockState(pos);
		var center = pos.toCenterPos();



		if(actualState.isAir()) {
			ItemScatterer.spawn(world, pos, blockEntity);
		}
		else {

			var direction = blockEntity.getBumpDirection();
			var x = center.getX() + direction.getOffsetX() * 0.75D;
			var y = center.getY() + direction.getOffsetY() * 0.75D;
			var z = center.getZ() + direction.getOffsetZ() * 0.75D;
			for(int i = 0; i < blockEntity.size(); ++i) {
				/*
				  This should check if the container holds beanstalks.
				  If it does, and blockEntity.getBumpedState() is not air,
				  it will spawn a beanstalk, setting its growth value
				  to the amount of beanstalks in the container,
				  and finally empties the blockEntity.
				 */
				if(
				    blockEntity.getStack(i).isOf(SuperMario.BEANSTALK.asItem()) && (blockEntity.getBumpedState() == null || !blockEntity.getBumpedState().isAir())
				) {
					Objects.requireNonNull(SuperMario.BEANSTALK_ENTITY.spawn(world.getServer().getWorld(world.getRegistryKey()), pos, SpawnReason.TRIGGERED)).growth = blockEntity.count(SuperMario.BEANSTALK.asItem());
					//TODO: make custom sound for beanstalk
					world.playSound(null, center.getX(), center.getY(), center.getZ(), MubbleSounds.BUMPABLE_BLOCK_LOOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
					blockEntity.clear();
					return;
				}

				ItemScatterer.spawn(world, x, y, z, blockEntity.getStack(i));
			}
		}
		world.playSound(null, center.getX(), center.getY(), center.getZ(), MubbleSounds.BUMPABLE_BLOCK_LOOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
		blockEntity.clear();
	}
}
