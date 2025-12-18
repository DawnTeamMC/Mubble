package fr.hugman.mubble.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.block.entity.MubbleBlockEntityTypes;
import fr.hugman.mubble.item.MubbleItems;
import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
public class BumpableBlock extends BaseEntityBlock implements HittableBlock {
    public static final MapCodec<BumpableBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            BlockState.CODEC.fieldOf("default_bumped_state").forGetter((block) -> block.defaultBumpedState),
            propertiesCodec()
    ).apply(instance, BumpableBlock::new));

    protected final @Nullable BlockState defaultBumpedState;

    public BumpableBlock(@Nullable BlockState defaultBumpedState, Properties settings) {
        super(settings);
        this.defaultBumpedState = defaultBumpedState;
    }

    @Override
    protected MapCodec<? extends BumpableBlock> codec() {
        return CODEC;
    }

    /*===========*/
    /*  GETTERS  */
    /*===========*/

    @Nullable
    public BlockState getDefaultBumpedState() {
        return defaultBumpedState;
    }

    /*================*/
    /*  BLOCK ENTITY  */
    /*================*/

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BumpableBlockEntity(pos, state, this.getDefaultBumpedState());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MubbleBlockEntityTypes.BUMPABLE_BLOCK, (w, p, s, e) -> e.tick(w, p, s));
    }

	@Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getItemInHand(hand).is(MubbleItems.MAKER_GLOVE)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (world.getBlockEntity(pos) instanceof BumpableBlockEntity bumpableEntity) {
            player.openMenu(bumpableEntity);
            // TODO: add stat for inspecting bumpable blocks
            //player.incrementStat(MubbleStats.INSPECT_BUMPABLE);
        }
        return InteractionResult.CONSUME;
    }

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
		var newState = world.getBlockState(pos);
		if (state.is(newState.getBlock())) {
			return;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BumpableBlockEntity bumpable) {
			Containers.dropContents(world, pos, bumpable);
			world.updateNeighbourForOutputSignal(pos, this);
		}
		super.affectNeighborsAfterRemoval(state, world, pos, moved);
	}

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

    /*=============*/
    /*  RENDERING  */
    /*=============*/

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    /*============*/
    /*  BEHAVIOR  */
    /*============*/

    /**
     * This method is called before the block entity receives the new data.
     *
     * @return true if the block should be bumped, false otherwise
     */
    public boolean canBump(Level world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity, Entity entity, BlockHitResult hit) {
        // TODO: check if the block is locked (vanilla locks to players only)
        return !blockEntity.isBumping();
    }

    /**
     * Called when the block is getting bumped.
     */
    public void onBumpStart(Level world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        var bumpAuthor = blockEntity.getBumpAuthor();
        //TODO: change the game event to something more appropriate
		world.gameEvent(bumpAuthor, GameEvent.BLOCK_ACTIVATE, pos);
        if (bumpAuthor instanceof Player player) {
            //TODO: create a new "Bumped Blocks" stat
            //player.incrementStat(MubbleStats.BUMPED_BLOCKS);
        }
    }

    /**
     * Called when a block is at the middle of being bumped.
     */
    public void onBumpMiddle(Level world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        if (world != null && !world.isClientSide()) {
            if (blockEntity.shouldBreak()) {
                Vec3 center = blockEntity.getBlockPos().getCenter();

                this.loot(world, pos, blockEntity, true);
                world.destroyBlock(blockEntity.getBlockPos(), false);
                world.playSound(null, center.x(), center.y(), center.z(), MubbleSounds.BUMPABLE_BLOCK_DESTROY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Called when a block finishes being bumped.
     */
    public void onBumpEnd(Level world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        if (world != null && !world.isClientSide()) {
            if (blockEntity.shouldBreak()) {
                // this should never happen since it already happened in onBumpMiddle
                this.loot(world, pos, blockEntity, true);
                world.destroyBlock(blockEntity.getBlockPos(), false);
                return;
            }
            var newState = blockEntity.getBumpedState();
            this.loot(world, pos, blockEntity, false);
            if (newState != null && blockEntity.isEmpty()) {
                world.setBlockAndUpdate(pos, newState);
            }
        }
    }


    @Override
    public void onHit(Level world, BlockState state, Entity entity, BlockHitResult hit) {
        if (world.isClientSide()) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        world.getBlockEntity(pos, MubbleBlockEntityTypes.BUMPABLE_BLOCK).ifPresent(blockEntity -> {
            if (this.canBump(world, pos, state, blockEntity, entity, hit)) {
                blockEntity.bump(pos, entity, hit.getDirection().getOpposite());
				onBumpStart(world, pos, state, blockEntity);
            }
        });
    }

    @Override
    public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        this.onHit(world, state, projectile, hit);
    }

    public void loot(Level world, BlockPos pos, BumpableBlockEntity blockEntity, boolean atCenter) {
        if (blockEntity.isEmpty()) {
            return;
        }
        var center = pos.getCenter();
        BumpableDropMode dropMode = blockEntity.getDropMode();
        if (atCenter) {
            switch (dropMode) {
                case ALL -> spawnItems(world, center, null, blockEntity);
                case ONE -> spawnItem(world, center, null, blockEntity.getItem(0).split(1));
            }
        } else {
            var direction = blockEntity.getBumpDirection();
            switch (dropMode) {
                case ALL -> spawnItems(world, center, direction, blockEntity);
                case ONE -> spawnItem(world, center, direction, blockEntity.getItem(0).split(1));
            }
        }
        world.playSound(null, center.x(), center.y(), center.z(), MubbleSounds.BUMPABLE_BLOCK_LOOT, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (blockEntity.getContainerSize() <= 0) {
            blockEntity.clearContent();
        }
    }

    private static void spawnItems(Level world, Vec3 pos, @Nullable Direction direction, Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            spawnItem(world, pos, direction, inventory.getItem(i));
        }
    }

    private static void spawnItem(Level world, Vec3 pos, @Nullable Direction direction, ItemStack stack) {
        pos = pos.relative(direction, 0.75D);

        double entityWidth = EntityType.ITEM.getWidth();
        double e = 1.0 - entityWidth;
        double f = entityWidth / 2.0;

        double x = Math.floor(pos.x()) + world.getRandom().nextDouble() * e + f;
        double y = Math.floor(pos.y()) + world.getRandom().nextDouble() * (1.0 - EntityType.ITEM.getHeight());
        double z = Math.floor(pos.z()) + world.getRandom().nextDouble() * e + f;

        while (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack.split(1));
            float i = 0.2f;
            float j = 0.11485000171139836f;
            itemEntity.setDeltaMovement(
                    (i * (direction == null ? 0 : direction.getStepX())) + world.getRandom().triangle(0.0, j),
                    (i * (direction == null ? 0 : direction.getStepY())) + world.getRandom().triangle(0.0, j),
                    (i * (direction == null ? 0 : direction.getStepZ())) + world.getRandom().triangle(0.0, j)
            );
            world.addFreshEntity(itemEntity);
        }
    }
}
