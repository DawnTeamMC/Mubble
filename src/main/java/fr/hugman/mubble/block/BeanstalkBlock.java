package fr.hugman.mubble.block;

import fr.hugman.mubble.registry.SuperMario;
import fr.hugman.mubble.tag.MubbleBlockTags;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

/**
 * Most of the functions of this code were
 * essentially copied from BambooBlock.
 *
 * @author MaxBrick
 * @since v4.0.0
 */

public class BeanstalkBlock extends Block implements Fertilizable {
    public BeanstalkBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }



    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isIn(MubbleBlockTags.BEANSTALK_PLANTABLE_ON);
    }


    protected int countBeanstalksAbove(BlockView world, BlockPos pos) {
        int i;
        for(i = 0; i < 16 && world.getBlockState(pos.up(i + 1)).isOf(SuperMario.BEANSTALK); ++i) {
        }

        return i;
    }
    protected int countBeanstalksBelow(BlockView world, BlockPos pos) {
        int i;
        for(i = 0; i < 16 && world.getBlockState(pos.down(i + 1)).isOf(SuperMario.BEANSTALK); ++i) {
        }

        return i;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    //This function as well as the count functions are ripped from BambooBlock.class
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        int i = this.countBeanstalksAbove(world, pos);
        int j = this.countBeanstalksBelow(world, pos);
        return i + j + 1 < 16;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    //Here I took the logic I needed from BambooBlock.class
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int i = this.countBeanstalksAbove(world, pos);
        int j = this.countBeanstalksBelow(world, pos);
        int k = i + j + 1;
        int l = 1 + random.nextInt(2);


        for(int m = 0; m < l; ++m) {
            BlockPos blockPos = pos.up(i);
            if (k >= 16 || !world.isAir(blockPos.up())) {
                return;
            }

            world.setBlockState(blockPos.up(1), state);
            ++i;
            ++k;
        }

    }
}
