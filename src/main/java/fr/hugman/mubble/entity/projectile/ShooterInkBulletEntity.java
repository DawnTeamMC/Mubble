package fr.hugman.mubble.entity.projectile;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Util;
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

    private int life;
    private ShooterInkBulletConfig config;
    private static final TrackedData<Boolean> FREE_GRAVITY = DataTracker.registerData(ShooterInkBulletEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> BRAKED = DataTracker.registerData(ShooterInkBulletEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public ShooterInkBulletEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ShooterInkBulletEntity(World world, LivingEntity shooter, @Nullable ShooterInkBulletConfig config, float angleDeviation) {
        this(MubbleEntityTypes.SHOOTER_INK_BULLET, world);

        // owner
        this.setOwner(shooter);

        // config
        if (config == null) config = ShooterInkBulletConfig.DEFAULT;
        this.config = config.copy();

        // position
        this.setPosition(shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());

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
        if (!this.getWorld().isClient()) {
            if (this.config == null) {
                return;
            }
            var brakeTick = this.config.brakeTick();
            this.life++;
            if (this.life == brakeTick) {
                this.dataTracker.set(BRAKED, true);
                var maxSpeed = this.config.brakeMaxSpeed();
                if (this.getSpeed() > maxSpeed) {
                    this.setSpeed(maxSpeed);
                }
            }
            if (!this.isFreeGravity()) {
                var currentVel = this.getVelocity();
                boolean ignoreY = currentVel.y < 0;
                var speed = ignoreY ? this.getHorizontalSpeed() : this.getSpeed();
                if (speed <= this.config.freeGravityThreshold()) {
                    this.dataTracker.set(FREE_GRAVITY, true);
                }
            }
        }

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        this.tickBlockCollision();
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

        if (this.hasBraked()) {
            if (!this.hasNoGravity()) {
                velocity = this.getVelocity();
                this.setVelocity(velocity.x, velocity.y - this.getGravity(), velocity.z);
            }
            if (!this.isFreeGravity()) {
                var currentVel = this.getVelocity();
                boolean ignoreY = currentVel.y < 0;
                var newVel = this.getVelocity().multiply(0.36); // it seems arbitrary, but it's the same with all shooter weapons I checked so IDK
                this.setVelocity(newVel.x, ignoreY ? currentVel.y : newVel.y, newVel.z);
            }
        }
        super.tick();
    }

    @Override
    protected void updateRotation() {
        Vec3d vec3d = this.getVelocity();
        double d = vec3d.horizontalLength();
        this.setPitch((float) (MathHelper.atan2(vec3d.getY(), d) * 180.0F / (float) Math.PI));
        this.setYaw((float) (MathHelper.atan2(vec3d.getX(), vec3d.getZ()) * 180.0F / (float) Math.PI));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(FREE_GRAVITY, false);
        builder.add(BRAKED, false);
    }

    public boolean hasBraked() {
        return this.dataTracker.get(BRAKED);
    }

    public boolean isFreeGravity() {
        return this.dataTracker.get(FREE_GRAVITY);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.config != null) {
            ShooterInkBulletConfig.CODEC.encodeStart(NbtOps.INSTANCE, this.config)
                    .ifSuccess(nbtElement -> nbt.put(CONFIG_KEY, nbtElement));
        }
        nbt.putShort(LIFE_KEY, (short) this.life);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(CONFIG_KEY)) {
            ShooterInkBulletConfig.CODEC
                    .decode(NbtOps.INSTANCE, nbt.get(CONFIG_KEY))
                    .resultOrPartial(Util.addPrefix("Shooter ink bullet", Mubble.LOGGER::error))
                    .ifPresent(pair -> this.config = pair.getFirst());
        }
        this.life = nbt.getShort(LIFE_KEY);
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        var world = this.getWorld();
        var pos = result.getBlockPos();
        var side = result.getSide();

        if (!this.getWorld().isClient) {
            var inkPos = pos.offset(side);
            var currentState = world.getBlockState(inkPos);
            var inkState = MubbleBlocks.INK_BLOCK.withDirection(currentState, side.getOpposite());

            // TODO: check more here
            if (inkState.canPlaceAt(world, inkPos)) {
                world.setBlockState(inkPos, inkState);
            }
            this.playSound(MubbleSounds.INK_SPLASH, 0.3F, 1.0f);
            this.discard();
        }
    }

    public double getSpeed() {
        return this.getVelocity().length();
    }

    protected void setSpeed(double speed) {
        this.setVelocity(this.getVelocity().normalize().multiply(speed));
    }

    protected double getHorizontalSpeed() {
        return Math.sqrt(this.getVelocity().x * this.getVelocity().x + this.getVelocity().z * this.getVelocity().z);
    }

    public double getGravity() {
        // the high jump splatoon challenge mode has a different gravity I think
        // so we can change this later
        return 0.15d;
    }
}
