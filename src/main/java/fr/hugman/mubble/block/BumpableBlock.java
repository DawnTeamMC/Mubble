package fr.hugman.mubble.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.item.MubbleItems;
import fr.hugman.mubble.sound.MubbleSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
public class BumpableBlock extends BlockWithEntity implements HittableBlock {
    public static final MapCodec<BumpableBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            BlockState.CODEC.fieldOf("default_bumped_state").forGetter((block) -> block.defaultBumpedState),
            createSettingsCodec()
    ).apply(instance, BumpableBlock::new));
    public static final BooleanProperty BUMPING = BooleanProperty.of("bumping");

    protected final @Nullable BlockState defaultBumpedState;

    public BumpableBlock(@Nullable BlockState defaultBumpedState, Settings settings) {
        super(settings);
        this.defaultBumpedState = defaultBumpedState;
        this.setDefaultState(this.stateManager.getDefaultState().with(BUMPING, false));
    }

    @Override
    protected MapCodec<? extends BumpableBlock> getCodec() {
        return CODEC;
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

    @Nullable
    public BlockState getDefaultBumpedState() {
        return defaultBumpedState;
    }

    /*================*/
    /*  BLOCK ENTITY  */
    /*================*/

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BumpableBlockEntity(pos, state, this.getDefaultBumpedState());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, MubbleBlockEntityTypes.BUMPABLE_BLOCK, (w, p, s, e) -> e.tick(w, p, s));
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getStackInHand(hand).isOf(MubbleItems.MAKER_GLOVE)) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (world.isClient) {
            return ItemActionResult.SUCCESS;
        }
        if (world.getBlockEntity(pos) instanceof BumpableBlockEntity bumpableEntity) {
            player.openHandledScreen(bumpableEntity);
            // TODO: add stat for inspecting bumpable blocks
            //player.incrementStat(MubbleStats.INSPECT_BUMPABLE);
        }
        return ItemActionResult.CONSUME;
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
        if (MinecraftClient.isFancyGraphicsOrBetter()) return BlockRenderType.ENTITYBLOCK_ANIMATED;
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
        if (bumpAuthor instanceof PlayerEntity player) {
            //TODO: create a new "Bumped Blocks" stat
            //player.incrementStat(MubbleStats.BUMPED_BLOCKS);
        }
    }

    /**
     * Called when a block is at the middle of being bumped.
     */
    public void onBumpMiddle(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        if (world != null && !world.isClient()) {
            if (blockEntity.shouldBreak()) {
                Vec3d center = blockEntity.getPos().toCenterPos();

                this.loot(world, pos, blockEntity, true);
                world.breakBlock(blockEntity.getPos(), false);
                world.playSound(null, center.getX(), center.getY(), center.getZ(), MubbleSounds.BUMPABLE_BLOCK_DESTROY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Called when a block finishes being bumped.
     */
    public void onBumpEnd(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        if (world != null && !world.isClient()) {
            if (blockEntity.shouldBreak()) {
                // this should never happen since it already happened in onBumpMiddle
                this.loot(world, pos, blockEntity, true);
                world.breakBlock(blockEntity.getPos(), false);
                return;
            }
            var newState = blockEntity.getBumpedState();
            this.loot(world, pos, blockEntity, false);
            if (newState != null && blockEntity.isEmpty()) {
                world.setBlockState(pos, newState);
            } else {
                world.setBlockState(pos, state.with(BUMPING, false));
            }
        }
    }


    @Override
    public void onHit(World world, BlockState state, Entity entity, BlockHitResult hit) {
        if (world.isClient()) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        world.getBlockEntity(pos, MubbleBlockEntityTypes.BUMPABLE_BLOCK).ifPresent(blockEntity -> {
            if (this.canBump(world, pos, state, blockEntity, entity, hit)) {
                blockEntity.bump(world, pos, state, entity, hit.getSide().getOpposite());
            }
        });
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        this.onHit(world, state, projectile, hit);
    }

    public void loot(World world, BlockPos pos, BumpableBlockEntity blockEntity, boolean atCenter) {
        if (blockEntity.isEmpty()) {
            return;
        }
        var center = pos.toCenterPos();
        BumpableDropMode dropMode = blockEntity.getDropMode();
        if (atCenter) {
            switch (dropMode) {
                case ALL -> spawnItems(world, center, null, blockEntity);
                case ONE -> spawnItem(world, center, null, blockEntity.getStack(0).split(1));
            }
        } else {
            var direction = blockEntity.getBumpDirection();
            switch (dropMode) {
                case ALL -> spawnItems(world, center, direction, blockEntity);
                case ONE -> spawnItem(world, center, direction, blockEntity.getStack(0).split(1));
            }
        }
        world.playSound(null, center.getX(), center.getY(), center.getZ(), MubbleSounds.BUMPABLE_BLOCK_LOOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (blockEntity.size() <= 0) {
            blockEntity.clear();
        }
    }

    private static void spawnItems(World world, Vec3d pos, @Nullable Direction direction, Inventory inventory) {
        for (int i = 0; i < inventory.size(); ++i) {
            spawnItem(world, pos, direction, inventory.getStack(i));
        }
    }

    private static void spawnItem(World world, Vec3d pos, @Nullable Direction direction, ItemStack stack) {
        pos = pos.offset(direction, 0.75D);

        double entityWidth = EntityType.ITEM.getWidth();
        double e = 1.0 - entityWidth;
        double f = entityWidth / 2.0;

        double x = Math.floor(pos.getX()) + world.random.nextDouble() * e + f;
        double y = Math.floor(pos.getY()) + world.random.nextDouble() * (1.0 - EntityType.ITEM.getHeight());
        double z = Math.floor(pos.getZ()) + world.random.nextDouble() * e + f;

        while (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack.split(1));
            float i = 0.2f;
            float j = 0.11485000171139836f;
            itemEntity.setVelocity(
                    (i * (direction == null ? 0 : direction.getOffsetX())) + world.random.nextTriangular(0.0, j),
                    (i * (direction == null ? 0 : direction.getOffsetY())) + world.random.nextTriangular(0.0, j),
                    (i * (direction == null ? 0 : direction.getOffsetZ())) + world.random.nextTriangular(0.0, j)
            );
            world.spawnEntity(itemEntity);
        }
    }
}
