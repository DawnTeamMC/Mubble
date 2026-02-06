package fr.hugman.mubble.splatoon.world.entity.projectile;

import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import fr.hugman.mubble.splatoon.world.entity.SplatoonEntityTypes;
import fr.hugman.mubble.splatoon.world.level.block.SplatoonBlocks;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ShooterInkBullet extends Projectile {
    public static final String LIFE_KEY = "life";
    public static final String CONFIG_KEY = "config";

    private int life;
    private ShooterInkBulletConfig config;
    private static final EntityDataAccessor<Boolean> FREE_GRAVITY = SynchedEntityData.defineId(ShooterInkBullet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BRAKED = SynchedEntityData.defineId(ShooterInkBullet.class, EntityDataSerializers.BOOLEAN);

    public ShooterInkBullet(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public ShooterInkBullet(Level level, LivingEntity shooter, @Nullable ShooterInkBulletConfig config, float angleDeviation) {
        this(SplatoonEntityTypes.SHOOTER_INK_BULLET, level);

        // owner
        this.setOwner(shooter);

        // config
        if (config == null) config = ShooterInkBulletConfig.DEFAULT;
        this.config = config.copy();

        // position
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());

        // velocity stuff
        float pitch = shooter.getXRot() + (random.nextFloat() - random.nextFloat()) * angleDeviation;
        float yaw = shooter.getYRot() + (random.nextFloat() - random.nextFloat()) * angleDeviation;

        float speed = config.initialSpeed();
        float x = -Mth.sin(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));
        float y = -Mth.sin((pitch) * ((float) Math.PI / 180));
        float z = Mth.cos(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));

        this.shoot(x, y, z, speed, 0.0F);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            if (this.config == null) {
                return;
            }
            var brakeTick = this.config.brakeTick();
            this.life++;
            if (this.life == brakeTick) {
                this.entityData.set(BRAKED, true);
                var maxSpeed = this.config.brakeMaxSpeed();
                if (this.getSpeed() > maxSpeed) {
                    this.setSpeed(maxSpeed);
                }
            }
            if (!this.isFreeGravity()) {
                var currentVel = this.getDeltaMovement();
                boolean ignoreY = currentVel.y < 0;
                var speed = ignoreY ? this.getHorizontalSpeed() : this.getSpeed();
                if (speed <= this.config.freeGravityThreshold()) {
                    this.entityData.set(FREE_GRAVITY, true);
                }
            }
        }

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }

        this.applyEffectsFromBlocks();
        this.updateRotation();
        if (this.isInWater()) {
            this.discard();
        }

        // update the position
        Vec3 velocity = this.getDeltaMovement();
        double posX = this.getX() + velocity.x;
        double posY = this.getY() + velocity.y;
        double posZ = this.getZ() + velocity.z;
        this.setPos(posX, posY, posZ);

        if (this.hasBraked()) {
            if (!this.isNoGravity()) {
                velocity = this.getDeltaMovement();
                this.setDeltaMovement(velocity.x, velocity.y - this.getGravity(), velocity.z);
            }
            if (!this.isFreeGravity()) {
                var currentVel = this.getDeltaMovement();
                boolean ignoreY = currentVel.y < 0;
                var newVel = this.getDeltaMovement().scale(0.36); // it seems arbitrary, but it's the same with all shooter weapons I checked so IDK
                this.setDeltaMovement(newVel.x, ignoreY ? currentVel.y : newVel.y, newVel.z);
            }
        }
        super.tick();
    }

    @Override
    protected void updateRotation() {
        Vec3 vec3d = this.getDeltaMovement();
        double d = vec3d.horizontalDistance();
        this.setXRot((float) (Mth.atan2(vec3d.y(), d) * 180.0F / (float) Math.PI));
        this.setYRot((float) (Mth.atan2(vec3d.x(), vec3d.z()) * 180.0F / (float) Math.PI));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(FREE_GRAVITY, false);
        entityData.define(BRAKED, false);
    }

    public boolean hasBraked() {
        return this.entityData.get(BRAKED);
    }

    public boolean isFreeGravity() {
        return this.entityData.get(FREE_GRAVITY);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);

        if (this.config != null) {
            output.store(CONFIG_KEY, ShooterInkBulletConfig.CODEC, this.config);
        }
        output.putShort(LIFE_KEY, (short) this.life);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read(CONFIG_KEY, ShooterInkBulletConfig.CODEC).ifPresent(config -> this.config = config);
        this.life = input.getShortOr(LIFE_KEY, (short) 0);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        var level = this.level();
        var pos = hitResult.getBlockPos();
        var side = hitResult.getDirection();

        if(!level.isClientSide()) {
            var inkPos = pos.relative(side);
            var currentState = level.getBlockState(inkPos);
            var inkState = SplatoonBlocks.INK_BLOCK.getStateForPlacement(currentState, level, inkPos, side.getOpposite());

            if (inkState != null) {
                level.setBlockAndUpdate(inkPos, inkState);
            }
            this.playSound(SplatoonSounds.INK_SPLASH, 0.3F, 1.0f);
            this.discard();
        }
    }

    public double getSpeed() {
        return this.getDeltaMovement().length();
    }

    protected void setSpeed(double speed) {
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(speed));
    }

    protected double getHorizontalSpeed() {
        return Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
    }

    @Override
    protected double getDefaultGravity() {
        // the high jump splatoon challenge mode has a different gravity I think
        // so we can change this later
        return 0.15d;
    }
}
