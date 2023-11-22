package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.WarpBlockEntity;
import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

/**
 * @author MaxBrick
 * @since v4.0.0
 */
public class WarpBlock extends Block implements BlockEntityProvider {
    public WarpBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WarpBlockEntity(pos, state);
    }

    //Copies coordinates to the Maker Glove if no coordinates are saved
    //If coordinates are saved then set destination to glove's coordinates and remove the saved coordinates from glove
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!player.getStackInHand(hand).isOf(SuperMario.MAKER_GLOVE)) {
            return ActionResult.PASS;
        }
        if(world.isClient) {
            return ActionResult.SUCCESS;
        }
        if(world.getBlockEntity(pos) instanceof WarpBlockEntity warpBlockEntity && player.getStackInHand(hand).isOf(SuperMario.MAKER_GLOVE)) {
            ItemStack itemStack = player.getStackInHand(hand);

            if(itemStack.getSubNbt("DestinationPos") == null) {
                itemStack.setSubNbt("DestinationPos", NbtHelper.fromBlockPos(pos));

                player.sendMessage(Text.of("Copied coordinates to your Maker Glove"), true);

            } else if (!NbtHelper.toBlockPos(Objects.requireNonNull(itemStack.getSubNbt("DestinationPos"))).equals(warpBlockEntity.getPos())) {
                warpBlockEntity.setDestinationPos(NbtHelper.toBlockPos(Objects.requireNonNull(itemStack.getSubNbt("DestinationPos"))));
                itemStack.removeSubNbt("DestinationPos");

                player.sendMessage(Text.of("Destination set from your Maker Glove"), true);
            }
        }
        return ActionResult.CONSUME;
    }

    //Players need to crouch to enter pipe, hence the separate event caller thing
    //I don't know how to properly center a location, so I added .5 to x and z
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (!world.isClient){

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof WarpBlockEntity warpBlockEntity){

                /*This long "if" statement effectively makes sure the destination block is the corresponding warp block
                  (in case the destination block is modified for example)
                  Also, it won't teleport you to the same block (which could soft-lock you)
                 */
                if(
                        entity.isPlayer()
                                && entity.isInSneakingPose()
                                && world.getBlockState(warpBlockEntity.getDestinationPos()).getBlock() == state.getBlock()
                                && blockEntity.getPos() != warpBlockEntity.getDestinationPos()
                ) {
                    entity.teleport(
                            warpBlockEntity.getDestinationPos().getX() + 0.5,
                            warpBlockEntity.getDestinationPos().getY() + 0.51,
                            warpBlockEntity.getDestinationPos().getZ() + 0.5
                    );
                }
            }
        }
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClient){
            super.onLandedUpon(world, state, pos, entity, fallDistance);
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof WarpBlockEntity warpBlockEntity){

                if(
                        !entity.isPlayer()
                                && world.getBlockState(warpBlockEntity.getDestinationPos()).getBlock() == state.getBlock()
                                && blockEntity.getPos() != warpBlockEntity.getDestinationPos()
                ) {
                    entity.teleport(
                            warpBlockEntity.getDestinationPos().getX() + 0.5,
                            warpBlockEntity.getDestinationPos().getY() + 0.51,
                            warpBlockEntity.getDestinationPos().getZ() + 0.5
                    );
                }
            }
        }
    }
}
