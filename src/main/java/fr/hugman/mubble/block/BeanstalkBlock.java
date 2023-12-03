package fr.hugman.mubble.block;

import fr.hugman.mubble.tag.MubbleBlockTags;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

/**
 * Most of the functions of this code were
 * essentially copied from the BambooBlock class.
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

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isIn(MubbleBlockTags.BEANSTALK_PLANTABLE_ON);
    }

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up(pos.getY() - world.getHeight())) == state;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos.up(1), state);
    }
}
