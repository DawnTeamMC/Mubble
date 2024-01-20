package fr.hugman.mubble.entity;

import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

/**
 * This class is made by copying and modifying
 * methods from FallingBlockEntity
 * Real creative, I know :)
 *
 * @author MaxBrick
 * @since v4.0.0
 */

public class BeanstalkEntity extends Entity {

    //This will determine how high the entity will grow. It should be set upon spawning
    public int growth = 0;

    //This should always be set to its y position when spawned
    private final int spawnHeight = (int) this.getY();

    private int travelDistance = 0;

    public BeanstalkEntity(EntityType<? extends BeanstalkEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {

    }

    protected Entity.MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    public void tick() {
        //I don't know why, but I have to use the move function to actually make it move
        this.setVelocity(0.0D, 0.3D, 0.0D);
        this.move(MovementType.SELF, this.getVelocity());

        if(!this.getWorld().getBlockState(this.getBlockPos().up()).isAir() || growth < 1) {
            this.discard();
        }

        //Checks if its current position is one block higher than where it previously was before placing block
        if(this.getY() > spawnHeight + travelDistance + 1 && this.getWorld().getBlockState(this.getBlockPos()).isAir()) {
            travelDistance++;

            this.getWorld().setBlockState(this.getBlockPos(), SuperMario.BEANSTALK.getDefaultState());

            growth--;
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Growth", this.growth);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.growth = nbt.getInt("Growth");
    }

    public boolean isAttackable() {
        return false;
    }
}