package fr.hugman.mubble.splatoon.world.entity.projectile;

import fr.hugman.mubble.splatoon.SplatoonConversions;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Something made of ink that flies: a bullet, a droplet...
 *
 * <p>Ink moves the way it does in Splatoon 3, one frame at a time, {@value SplatoonConversions#FRAMES_PER_TICK}
 * frames per tick, so that the values of the game can be used as they are. Each frame, {@link #nextVelocity} gives the
 * velocity of the frame, in units (blocks) per frame, and the ink moves by that much, stopping at the first block,
 * liquid or entity in the way. The velocity is kept in {@link #getDeltaMovement()} in blocks per tick, like any
 * other entity, so that clients see it move between updates.
 *
 * <p>Ink dies in liquids, without painting anything.
 *
 * @author Hugman
 * @since v4.0.0
 */
public abstract class InkProjectile extends Projectile {
    private static final EntityDataAccessor<Integer> INK_COLOR = SynchedEntityData.defineId(InkProjectile.class, EntityDataSerializers.INT);

    protected InkStyle ink = InkStyle.DEFAULT;
    /**
     * How many frames the ink has flown for.
     */
    protected int frame;
    /**
     * How far the ink has flown, in units.
     */
    protected double traveled;

    protected InkProjectile(EntityType<? extends InkProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(INK_COLOR, InkStyle.DEFAULT.color());
    }

    public InkStyle getInk() {
        return this.ink;
    }

    public void setInk(InkStyle ink) {
        this.ink = ink;
        this.entityData.set(INK_COLOR, ink.color());
    }

    /**
     * @return the color of the ink, which is all clients know of it
     */
    public int getInkColor() {
        return this.entityData.get(INK_COLOR);
    }

    public int getFrame() {
        return this.frame;
    }

    /**
     * @return the velocity of the coming frame, in units per frame
     */
    protected abstract Vec3 nextVelocity(Vec3 velocity);

    /**
     * Called each frame the ink flew freely, from {@code from} to {@code to}.
     */
    protected void onFrameMoved(Vec3 from, Vec3 to, Vec3 velocity) {
    }

    protected void onBlockImpact(BlockHitResult hit, Vec3 velocity) {
        this.discard();
    }

    protected void onEntityImpact(EntityHitResult hit, Vec3 velocity) {
        this.discard();
    }

    protected void onLiquidImpact(BlockHitResult hit, Vec3 velocity) {
        this.discard();
    }

    /**
     * @return how far around entities this ink hits them, or a negative value to fly through them
     */
    protected float entityMargin() {
        return -1.0F;
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 velocity = this.getDeltaMovement().scale(1.0 / SplatoonConversions.FRAMES_PER_TICK);
        for (int i = 0; i < SplatoonConversions.FRAMES_PER_TICK && !this.isRemoved(); i++) {
            velocity = this.nextVelocity(velocity);
            this.setDeltaMovement(velocity.scale(SplatoonConversions.FRAMES_PER_TICK));

            // ink is tracked by its middle, not by its feet like other entities
            var toFeet = new Vec3(0, -this.getBbHeight() / 2.0, 0);
            var from = this.position().subtract(toFeet);
            var to = from.add(velocity);
            var hit = this.findHit(from, to, velocity);
            if (hit != null) {
                this.setPos(hit.getLocation().add(toFeet));
                this.traveled += from.distanceTo(hit.getLocation());
                this.onImpact(hit, velocity);
            } else {
                this.setPos(to.add(toFeet));
                this.traveled += velocity.length();
                this.onFrameMoved(from, to, velocity);
            }
            this.frame++;
        }
        this.updateRotation();
    }

    @Nullable
    private HitResult findHit(Vec3 from, Vec3 to, Vec3 velocity) {
        // liquids stop ink as much as blocks do
        BlockHitResult blockHit = this.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, this));
        var end = blockHit.getType() == HitResult.Type.MISS ? to : blockHit.getLocation();

        float margin = this.entityMargin();
        if (margin >= 0.0F) {
            var searchArea = this.getBoundingBox().expandTowards(velocity).inflate(1.0);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, from, end, searchArea, this::canHitEntity, margin);
            if (entityHit != null) {
                return entityHit;
            }
        }
        return blockHit.getType() == HitResult.Type.MISS ? null : blockHit;
    }

    private void onImpact(HitResult hit, Vec3 velocity) {
        if (hit instanceof EntityHitResult entityHit) {
            this.onEntityImpact(entityHit, velocity);
        } else if (hit instanceof BlockHitResult blockHit) {
            if (!this.level().getFluidState(blockHit.getBlockPos()).isEmpty()) {
                this.onLiquidImpact(blockHit, velocity);
            } else {
                this.onBlockImpact(blockHit, velocity);
            }
        }
    }

    @Override
    protected void updateRotation() {
        var velocity = this.getDeltaMovement();
        if (velocity.lengthSqr() < 1.0E-7) {
            return;
        }
        this.setXRot((float) (Mth.atan2(velocity.y, velocity.horizontalDistance()) * Mth.RAD_TO_DEG));
        this.setYRot((float) (Mth.atan2(velocity.x, velocity.z) * Mth.RAD_TO_DEG));
    }

    @Override
    protected double getDefaultGravity() {
        // gravity depends on the state of the ink, see nextVelocity
        return 0.0;
    }
}
