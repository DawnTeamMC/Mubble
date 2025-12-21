package fr.hugman.mubble.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.item.MubbleItems;
import fr.hugman.mubble.world.level.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.sounds.MubbleSounds;
import fr.hugman.mubble.world.level.block.entity.MubbleBlockEntityTypes;
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MubbleBlockEntityTypes.BUMPABLE_BLOCK, (w, p, s, e) -> e.tick(w, p, s));
    }

	@Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getItemInHand(hand).is(MubbleItems.MAKER_GLOVE)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof BumpableBlockEntity bumpableEntity) {
            player.openMenu(bumpableEntity);
            // TODO: add stat for inspecting bumpable blocks
            //player.incrementStat(MubbleStats.INSPECT_BUMPABLE);
        }
        return InteractionResult.CONSUME;
    }

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moved) {
		var newState = level.getBlockState(pos);
		if (state.is(newState.getBlock())) {
			return;
		}
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof BumpableBlockEntity bumpable) {
			Containers.dropContents(level, pos, bumpable);
			level.updateNeighbourForOutputSignal(pos, this);
		}
		super.affectNeighborsAfterRemoval(state, level, pos, moved);
	}

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
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
    public boolean canBump(Level level, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity, Entity entity, BlockHitResult hit) {
        // TODO: check if the block is locked (vanilla locks to players only)
        return !blockEntity.isBumping();
    }

    /**
     * Called when the block is getting bumped.
     */
    public void onBumpStart(Level level, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        var bumpAuthor = blockEntity.getBumpAuthor();
        //TODO: change the game event to something more appropriate
		level.gameEvent(bumpAuthor, GameEvent.BLOCK_ACTIVATE, pos);
        if (bumpAuthor instanceof Player player) {
            //TODO: create a new "Bumped Blocks" stat
            //player.incrementStat(MubbleStats.BUMPED_BLOCKS);
        }
    }

    /**
     * Called when a block is at the middle of being bumped.
     */
    public void onBumpMiddle(Level level, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        if (level != null && !level.isClientSide()) {
            if (blockEntity.shouldBreak()) {
                Vec3 center = blockEntity.getBlockPos().getCenter();

                this.loot(level, pos, blockEntity, true);
                level.destroyBlock(blockEntity.getBlockPos(), false);
                level.playSound(null, center.x(), center.y(), center.z(), MubbleSounds.BUMPABLE_BLOCK_DESTROY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Called when a block finishes being bumped.
     */
    public void onBumpEnd(Level level, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        if (level != null && !level.isClientSide()) {
            if (blockEntity.shouldBreak()) {
                // this should never happen since it already happened in onBumpMiddle
                this.loot(level, pos, blockEntity, true);
                level.destroyBlock(blockEntity.getBlockPos(), false);
                return;
            }
            var newState = blockEntity.getBumpedState();
            this.loot(level, pos, blockEntity, false);
            if (newState != null && blockEntity.isEmpty()) {
                level.setBlockAndUpdate(pos, newState);
            }
        }
    }


    @Override
    public void onHit(Level level, BlockState state, Entity entity, BlockHitResult hit) {
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        level.getBlockEntity(pos, MubbleBlockEntityTypes.BUMPABLE_BLOCK).ifPresent(blockEntity -> {
            if (this.canBump(level, pos, state, blockEntity, entity, hit)) {
                blockEntity.bump(pos, entity, hit.getDirection().getOpposite());
				onBumpStart(level, pos, state, blockEntity);
            }
        });
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        this.onHit(level, state, projectile, hit);
    }

    public void loot(Level level, BlockPos pos, BumpableBlockEntity blockEntity, boolean atCenter) {
        if (blockEntity.isEmpty()) {
            return;
        }
        var center = pos.getCenter();
        BumpableDropMode dropMode = blockEntity.getDropMode();
        if (atCenter) {
            switch (dropMode) {
                case ALL -> spawnItems(level, center, null, blockEntity);
                case ONE -> spawnItem(level, center, null, blockEntity.getItem(0).split(1));
            }
        } else {
            var direction = blockEntity.getBumpDirection();
            switch (dropMode) {
                case ALL -> spawnItems(level, center, direction, blockEntity);
                case ONE -> spawnItem(level, center, direction, blockEntity.getItem(0).split(1));
            }
        }
        level.playSound(null, center.x(), center.y(), center.z(), MubbleSounds.BUMPABLE_BLOCK_LOOT, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (blockEntity.getContainerSize() <= 0) {
            blockEntity.clearContent();
        }
    }

    private static void spawnItems(Level level, Vec3 pos, @Nullable Direction direction, Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            spawnItem(level, pos, direction, inventory.getItem(i));
        }
    }

    private static void spawnItem(Level level, Vec3 pos, @Nullable Direction direction, ItemStack stack) {
        pos = pos.relative(direction, 0.75D);

        double entityWidth = EntityType.ITEM.getWidth();
        double e = 1.0 - entityWidth;
        double f = entityWidth / 2.0;

        double x = Math.floor(pos.x()) + level.getRandom().nextDouble() * e + f;
        double y = Math.floor(pos.y()) + level.getRandom().nextDouble() * (1.0 - EntityType.ITEM.getHeight());
        double z = Math.floor(pos.z()) + level.getRandom().nextDouble() * e + f;

        while (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack.split(1));
            float i = 0.2f;
            float j = 0.11485000171139836f;
            itemEntity.setDeltaMovement(
                    (i * (direction == null ? 0 : direction.getStepX())) + level.getRandom().triangle(0.0, j),
                    (i * (direction == null ? 0 : direction.getStepY())) + level.getRandom().triangle(0.0, j),
                    (i * (direction == null ? 0 : direction.getStepZ())) + level.getRandom().triangle(0.0, j)
            );
            level.addFreshEntity(itemEntity);
        }
    }
}
