package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

/**
 * @author MaxBrick
 * @since v4.0.0
 */

public class WarpBlockEntity extends BlockEntity {
    private BlockPos destinationPos = pos;

    //Copied code from BumpableBlockEntity. Sorry, Hugman X)
    public WarpBlockEntity(BlockPos pos, BlockState state) {
        super(SuperMario.WARP_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("DestinationPos", NbtHelper.fromBlockPos(destinationPos));

        super.writeNbt(nbt);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        destinationPos = (BlockPos) nbt.get("DestinationPos");
    }

    /*=====================*/
    /*  GETTERS & SETTERS  */
    /*=====================*/

    public BlockPos getDestinationPos() {
        return destinationPos;
    }
    public void setDestinationPos(BlockPos pos) {
        destinationPos = pos;
    }


}
