package fr.hugman.mubble.entity.projectile;

import fr.hugman.mubble.registry.Splatoon;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Ink bullet entity, shot by the Splatoon shooter weapons.
 * <p>
 * Upon hitting a surface, the ink bullet will spray ink blocks around it.
 * Upon hitting an entity, the ink bullet will add a certain amount of ink to the entity's ink submergence.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class ShooterInkBulletEntity extends ProjectileEntity {
    public static final String LIFE_KEY = "life";
    public static final String CONFIG_KEY = "config";
    public static final String FREE_GRAVITY_KEY = "free_gravity";
    public static final float GRAVITY = 0.03f;

    private int life;
    private boolean freeGravity;
    private ShooterInkBulletConfig config;

    public ShooterInkBulletEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ShooterInkBulletEntity(World world, LivingEntity shooter, @Nullable ShooterInkBulletConfig config, float angleDeviation) {
        this(Splatoon.SHOOTER_INK_BULLET, world);

        var random = shooter.getEntityWorld().getRandom();

        // owner
        this.setOwner(shooter);

        // config
        if (config == null) config = ShooterInkBulletConfig.DEFAULT;
        this.config = config.copy();

        // position
        this.setPosition(shooter.getX(), shooter.getEyeY() - (double) 0.1f, shooter.getZ());

        // velocity stuff
        float pitch = shooter.getPitch() + (random.nextFloat() - random.nextFloat()) * angleDeviation;
        float yaw = shooter.getYaw() + (random.nextFloat() - random.nextFloat()) * angleDeviation;

        float speed = config.initialSpeed();
        float x = -MathHelper.sin(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
        float y = -MathHelper.sin((pitch) * ((float) Math.PI / 180));
        float z = MathHelper.cos(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));

        this.setVelocity(x, y, z, speed, 0.0F);
    }

    @Override
    public void tick() {
        this.life++;
        if (this.config == null) {
            this.config = ShooterInkBulletConfig.DEFAULT.copy();
        }

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        this.checkBlockCollision();
        this.updateRotation();
        if (this.isTouchingWater()) {
            this.discard();
        }

        // update the position
        Vec3d velocity = this.getVelocity();
        double posX = this.getX() + velocity.x;
        double posY = this.getY() + velocity.y;
        double posZ = this.getZ() + velocity.z;
        this.setPosition(posX, posY, posZ);

        var brakeTick = this.config.brakeTick();
        if(this.life >= brakeTick) {
            if(this.life == brakeTick) {
                var maxSpeed = this.config.brakeMaxSpeed();
                if(this.getSpeed() > maxSpeed) {
                    this.setSpeed(maxSpeed);
                }
            }
            if (!this.hasNoGravity()) {
                velocity = this.getVelocity();
                this.setVelocity(velocity.x, velocity.y - this.getGravity(), velocity.z);
            }
            if(!this.freeGravity) {
                if(this.getSpeed() > this.config.freeGravityThreshold()) {
                    var currentVel = this.getVelocity();
                    boolean affectY = currentVel.y > 0;
                    var newVel = this.getVelocity().multiply(0.36); // it seems arbitrary, but it's the same with all shooter weapons I checked so IDK
                    this.setVelocity(newVel.x, affectY ? newVel.y : currentVel.y, newVel.z);
                }
                else {
                    this.freeGravity = true;
                }
            }
        }


        super.tick();
    }

    @Override
    protected void initDataTracker() {
        //TODO: idk?
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.config != null) {
            nbt.put(CONFIG_KEY, this.config.toNbt(this.getWorld().getRegistryManager()));
        }
        nbt.putShort(LIFE_KEY, (short)this.life);
        nbt.putBoolean(FREE_GRAVITY_KEY, this.freeGravity);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(CONFIG_KEY)) {
            this.config = ShooterInkBulletConfig.fromNbt(this.getWorld().getRegistryManager(), nbt.get(CONFIG_KEY));
        }
        this.life = nbt.getShort(LIFE_KEY);
        this.freeGravity = nbt.getBoolean(FREE_GRAVITY_KEY);
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        var world = this.getWorld();
        var pos = result.getBlockPos();
        var side = result.getSide();

        if (!this.getWorld().isClient) {
            var inkPos = pos.offset(side);
            var currentState = world.getBlockState(inkPos);
            var inkState = Splatoon.INK_BLOCK.withDirection(currentState, side.getOpposite());

            // TODO: check more here
            if (inkState.canPlaceAt(world, inkPos)) {
                world.setBlockState(inkPos, inkState);
            }

            this.discard();
        }
    }

    protected double getSpeed() {
        return this.getVelocity().length();
    }

    protected void setSpeed(double speed) {
        this.setVelocity(this.getVelocity().normalize().multiply(speed));
    }

    public float getGravity() {
        // the high jump splatoon challenge mode has a different gravity I think
        // so we can change this later
        return GRAVITY;
    }
}
