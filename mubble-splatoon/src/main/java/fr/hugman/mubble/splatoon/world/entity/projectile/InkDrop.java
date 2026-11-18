package fr.hugman.mubble.splatoon.world.entity.projectile;

import fr.hugman.mubble.splatoon.SplatoonConversions;
import fr.hugman.mubble.splatoon.network.syncher.SplatoonEntityDataSerializers;
import fr.hugman.mubble.splatoon.world.entity.SplatoonEntityTypes;
import fr.hugman.mubble.splatoon.world.level.ink.InkPainter;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A droplet of ink a bullet lets fall on its way, as the {@link fr.hugman.mubble.splatoon.world.item.weapon.param.SplashSpawnParam}
 * of its weapon says. It falls like the bullet does in its free state, keeping a little of the bullet's momentum,
 * flies through entities, and paints a splat stretched in the direction of the shot where it lands, rounder the
 * higher it fell from.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class InkDrop extends InkProjectile {
    private static final EntityDataAccessor<ShooterInkBulletConfig> CONFIG = SynchedEntityData.defineId(InkDrop.class, SplatoonEntityDataSerializers.SHOOTER_INK_BULLET_CONFIG);
    private static final EntityDataAccessor<Boolean> NEAREST = SynchedEntityData.defineId(InkDrop.class, EntityDataSerializers.BOOLEAN);

    /**
     * How much of the speed of its bullet a droplet keeps.
     */
    public static final double MOMENTUM = 0.05;

    private Vec3 direction = new Vec3(0, 0, 1);
    private double startY;

    public InkDrop(EntityType<? extends InkDrop> entityType, Level level) {
        super(entityType, level);
    }

    public InkDrop(Level level, @Nullable Entity owner, ShooterInkBulletConfig config, InkStyle ink, Vec3 pos, Vec3 bulletVelocity, boolean nearest) {
        this(SplatoonEntityTypes.INK_DROP, level);
        this.setOwner(owner);
        this.entityData.set(CONFIG, config);
        this.entityData.set(NEAREST, nearest);
        this.setInk(ink);
        this.setPos(pos.x, pos.y - this.getBbHeight() / 2.0, pos.z);
        this.startY = this.getY();

        var horizontal = new Vec3(bulletVelocity.x, 0, bulletVelocity.z);
        if (horizontal.lengthSqr() > 1.0E-6) {
            this.direction = horizontal.normalize();
        }
        this.setDeltaMovement(horizontal.scale(MOMENTUM * SplatoonConversions.FRAMES_PER_TICK));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(CONFIG, ShooterInkBulletConfig.SPLATTERSHOT);
        entityData.define(NEAREST, false);
    }

    /**
     * @return whether this is the bigger droplet falling right in front of the shooter
     */
    public boolean isNearest() {
        return this.entityData.get(NEAREST);
    }

    @Override
    protected Vec3 nextVelocity(Vec3 velocity) {
        var move = this.entityData.get(CONFIG).move();
        return velocity.scale(1.0 - move.freeAirResist()).subtract(0, move.freeGravity(), 0);
    }

    @Override
    protected void onBlockImpact(BlockHitResult hit, Vec3 velocity) {
        if (this.level() instanceof ServerLevel level) {
            var config = this.entityData.get(CONFIG);
            var splash = config.splashPaint();
            float widthHalf = this.isNearest() ? splash.widthHalfNearest() : splash.widthHalf();
            float depthScale = splash.depthScale(this.startY - this.getY(), config.paint());
            var normal = Vec3.atLowerCornerOf(hit.getDirection().getUnitVec3i());
            InkPainter.splat(level, new InkPainter.Splat(hit.getLocation(), normal, this.direction,
                    SplatoonConversions.distance(widthHalf), SplatoonConversions.distance(widthHalf * depthScale), this.random.nextLong()), this.ink);
        }
        this.discard();
    }
}
