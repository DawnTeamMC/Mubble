package fr.hugman.mubble.splatoon.world.entity.projectile;

import fr.hugman.mubble.splatoon.SplatoonConversions;
import fr.hugman.mubble.splatoon.network.syncher.SplatoonEntityDataSerializers;
import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import fr.hugman.mubble.splatoon.world.damagesource.SplatoonDamageTypes;
import fr.hugman.mubble.splatoon.world.entity.SplatoonEntityTypes;
import fr.hugman.mubble.splatoon.world.item.weapon.AutomaticShooterConfig;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletMoveParam;
import fr.hugman.mubble.splatoon.world.level.ink.InkPainter;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A bullet of a shooter. It flies through the {@linkplain BulletMoveParam straight, brake and free states} of
 * Splatoon 3 frame by frame, hurts less the longer it flies, lets droplets of ink fall on its way, and leaves a splat
 * stretched in its direction where it lands.
 *
 * <p>The whole configuration is synced to clients, which fly the bullet the same way between server updates.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class ShooterInkBullet extends InkProjectile {
    private static final EntityDataAccessor<ShooterInkBulletConfig> CONFIG = SynchedEntityData.defineId(ShooterInkBullet.class, SplatoonEntityDataSerializers.SHOOTER_INK_BULLET_CONFIG);

    /**
     * The index of this bullet among the shots of its weapon, which tells which droplets it lets fall.
     */
    private int shot;
    private final List<Droplet> droplets = new ArrayList<>();
    private boolean dropletsPlanned;

    public ShooterInkBullet(EntityType<? extends ShooterInkBullet> entityType, Level level) {
        super(entityType, level);
    }

    public ShooterInkBullet(Level level, LivingEntity shooter, AutomaticShooterConfig weapon, InkStyle ink, int shot) {
        this(SplatoonEntityTypes.SHOOTER_INK_BULLET, level);
        this.setOwner(shooter);
        this.setConfig(weapon.bullet());
        this.setInk(ink);
        this.shot = shot;
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1 - this.getBbHeight() / 2.0, shooter.getZ());

        float swerve = shooter.onGround() ? weapon.standDegSwerve() : weapon.jumpDegSwerve();
        float pitch = shooter.getXRot() + (this.random.nextFloat() - this.random.nextFloat()) * swerve;
        float yaw = shooter.getYRot() + (this.random.nextFloat() - this.random.nextFloat()) * swerve;
        var direction = Vec3.directionFromRotation(pitch, yaw);
        var velocity = direction.scale(weapon.bullet().move().spawnSpeed());

        // the shooter's own forward motion carries over to its shots
        var forward = new Vec3(direction.x, 0, direction.z);
        if (weapon.spawnAdditionZRate() > 0 && forward.lengthSqr() > 1.0E-6) {
            forward = forward.normalize();
            double forwardSpeed = shooter.getKnownMovement().dot(forward) / SplatoonConversions.FRAMES_PER_TICK;
            if (forwardSpeed > 0) {
                velocity = velocity.add(forward.scale(forwardSpeed * weapon.spawnAdditionZRate()));
            }
        }
        this.setDeltaMovement(velocity.scale(SplatoonConversions.FRAMES_PER_TICK));
        this.updateRotation();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(CONFIG, ShooterInkBulletConfig.SPLATTERSHOT);
    }

    public ShooterInkBulletConfig getConfig() {
        return this.entityData.get(CONFIG);
    }

    public void setConfig(ShooterInkBulletConfig config) {
        this.entityData.set(CONFIG, config);
    }

    public BulletMoveParam.BulletState getState() {
        return this.getConfig().move().state(this.frame);
    }

    @Override
    protected Vec3 nextVelocity(Vec3 velocity) {
        return this.getConfig().move().nextVelocity(velocity, this.frame);
    }

    @Override
    protected float entityMargin() {
        // the path is traced from the middle of the bullet, so the whole radius counts
        return this.getConfig().collision().radiusForPlayer(this.frame);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !(entity instanceof InkProjectile);
    }

    @Override
    protected void onFrameMoved(Vec3 from, Vec3 to, Vec3 velocity) {
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }
        if (!this.dropletsPlanned) {
            this.planDroplets();
        }
        double speed = velocity.length();
        while (!this.droplets.isEmpty() && this.droplets.getFirst().distance() <= this.traveled) {
            var droplet = this.droplets.removeFirst();
            var pos = speed <= 0 ? to : to.subtract(velocity.scale((this.traveled - droplet.distance()) / speed));
            level.addFreshEntity(new InkDrop(level, this.getOwner(), this.getConfig(), this.ink, pos, velocity, droplet.nearest()));
        }
    }

    /**
     * Picks where the droplets of this shot fall, following its {@link fr.hugman.mubble.splatoon.world.item.weapon.param.SplashSpawnParam}.
     */
    private void planDroplets() {
        this.dropletsPlanned = true;
        var spawn = this.getConfig().splashSpawn();
        if (spawn.dropsNearest(this.shot)) {
            this.droplets.add(new Droplet(spawn.spawnNearestLength(), true));
        }
        if (spawn.spawnBetweenLength() > 0) {
            int count = spawn.dropletCount(this.shot);
            for (int i = 0; i < count; i++) {
                double distance = spawn.spawnNearestLength() + (i + this.random.nextDouble()) * spawn.spawnBetweenLength();
                this.droplets.add(new Droplet(distance, false));
            }
        }
        this.droplets.sort(Comparator.comparingDouble(Droplet::distance));
    }

    @Override
    protected void onBlockImpact(BlockHitResult hit, Vec3 velocity) {
        if (this.level() instanceof ServerLevel level) {
            var paint = this.getConfig().paint();
            var normal = Vec3.atLowerCornerOf(hit.getDirection().getUnitVec3i());
            var direction = velocity.normalize();
            float widthHalf = paint.widthHalf(this.traveled);
            float depthScale = paint.depthScale(direction.dot(normal), this.getState() != BulletMoveParam.BulletState.STRAIGHT);
            InkPainter.splat(level, new InkPainter.Splat(hit.getLocation(), normal, direction,
                    SplatoonConversions.distance(widthHalf), SplatoonConversions.distance(widthHalf * depthScale), this.random.nextLong()), this.ink);
            this.playSound(SplatoonSounds.INK_SPLASH, 0.3F, 1.0F);
        }
        this.discard();
    }

    @Override
    protected void onEntityImpact(EntityHitResult hit, Vec3 velocity) {
        if (this.level() instanceof ServerLevel level) {
            var source = this.damageSources().source(SplatoonDamageTypes.INK, this, this.getOwner());
            hit.getEntity().hurtServer(level, source, this.getConfig().damage().damage(this.frame));
            this.playSound(SplatoonSounds.INK_SPLASH, 0.3F, 1.3F);
        }
        this.discard();
    }

    private record Droplet(double distance, boolean nearest) {
    }
}
