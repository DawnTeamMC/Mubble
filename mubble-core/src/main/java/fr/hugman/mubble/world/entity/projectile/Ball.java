package fr.hugman.mubble.world.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class Ball extends ThrowableProjectile {
	public static final String REBOUNDS_KEY = "rebounds";

    /** Trail particles left behind per block travelled. */
    private static final double TRAIL_PARTICLES_PER_BLOCK = 6.0D;
    /** Trail particles a single tick may spawn at most, so that a very fast ball cannot flood the screen. */
    private static final int MAX_TRAIL_PARTICLES_PER_TICK = 8;
    /** Distance travelled in a tick under which a ball counts as standing still and trails nothing. */
    private static final double MIN_TRAIL_DISTANCE = 0.01D;
    /** How far off its path a trail particle may drift, in blocks. */
    private static final double TRAIL_SPREAD = 0.1D;

    protected int rebounds = 3;
    private boolean rotateClockwards = false;

    protected Ball(EntityType<? extends Ball> type, Level level) {
        super(type, level);
        initialize();
    }

    protected Ball(EntityType<? extends Ball> type, Level level, LivingEntity owner) {
        super(type, level);
        initialize();
        this.setOwner(owner);
    }

    protected Ball(EntityType<? extends Ball> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
        initialize();
    }

    public void initialize() {
        if(this.level().isClientSide()) {
            this.rotateClockwards = this.level().getRandom().nextBoolean();
        }
    }

    public boolean rotatesClockwards() {
        return rotateClockwards;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.spawnTrailParticles();
        }
    }

    @Nullable
    protected abstract SoundEvent getDeathSound();

    protected abstract ParticleOptions getDeathParticle();

    /**
     * @return the particle the ball trails behind it while it moves, or {@code null} for a ball that
     * leaves no trail at all
     */
    @Nullable
    protected ParticleOptions getTrailParticle() {
        return null;
    }

	@Override
	protected double getDefaultGravity() {
		return 0.08;
	}

    @Override
    protected void onHit(HitResult result) {
        this.rebounds--;
        super.onHit(result);
        if (this.isAlive() && this.rebounds < 0) {
            this.finalHit();
        }
    }

	public void reboundUp() {
		Vec3 movement = this.getDeltaMovement().multiply(1.0D, -0.65D, 1.0D);
		double minY = 0.5D;
		if (movement.y < minY) {
			movement = movement.with(Direction.Axis.Y, minY);
		}
		this.setDeltaMovement(movement);
	}

	/**
	 * Triggers after the ball has hit and can no longer rebound.
	 */
	protected final void finalHit() {
		this.finalHit(this.getDeathSound());
	}

    /**
     * Triggers after the ball has hit and can no longer rebound.
     */
    protected void finalHit(@Nullable SoundEvent deathSound) {
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.remove(RemovalReason.DISCARDED);
        }
        if(deathSound != null) {
            this.level().playSound(null, getX(), getY(), getZ(), deathSound, SoundSource.NEUTRAL, 0.5F, 1.0F);
        }
    }

	@Override
	protected void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		view.putInt(REBOUNDS_KEY, rebounds);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		this.rebounds = view.getIntOr(REBOUNDS_KEY, 3);
	}

    @Environment(EnvType.CLIENT)
    @Override
    public void handleEntityEvent(byte state) {
        if (state == 3) {
            this.spawnDeathParticles();
        }
    }

    /**
     * Spawns the trail a moving ball leaves behind, spread over the path it travelled during this tick.
     * <p>
     * Particles are placed along that path rather than all at the current position, since a ball covers
     * enough ground in a tick for a single burst per tick to show up as a dotted line. A ball only trails
     * while it actually moves: one that has come to a halt emits nothing.
     */
    protected void spawnTrailParticles() {
        ParticleOptions particle = this.getTrailParticle();
        if (particle == null || this.isRemoved()) {
            return;
        }
        Vec3 from = new Vec3(this.xo, this.yo, this.zo);
        Vec3 to = this.position();
        int count = trailParticleCount(from.distanceTo(to));
        double middle = this.getBbHeight() / 2.0D;

        for (int i = 0; i < count; i++) {
            // Each particle sits at a random point of its own slice of the path: spacing them evenly
            // would line the trails of successive ticks up into a visible grid.
            Vec3 pos = from.lerp(to, (i + this.random.nextDouble()) / count);
            this.level().addParticle(particle,
                    pos.x + this.trailOffset(),
                    pos.y + middle + this.trailOffset(),
                    pos.z + this.trailOffset(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * @param distance how far the ball travelled during the tick, in blocks
     * @return how many particles that distance is worth, none at all for a ball that barely moved
     */
    public static int trailParticleCount(double distance) {
        if (distance < MIN_TRAIL_DISTANCE) {
            return 0;
        }
        return Mth.clamp((int) Math.ceil(distance * TRAIL_PARTICLES_PER_BLOCK), 1, MAX_TRAIL_PARTICLES_PER_TICK);
    }

    private double trailOffset() {
        return (this.random.nextDouble() - 0.5D) * TRAIL_SPREAD;
    }

    protected void spawnDeathParticles() {
        for (int i = 0; i < 8; ++i) {
            float s1 = random.nextFloat() * 0.2F - 0.1F;
            float s2 = random.nextFloat() * 0.2F - 0.1F;
            float s3 = random.nextFloat() * 0.2F - 0.1F;
            this.level().addParticle(this.getDeathParticle(), this.getX(), this.getY(), this.getZ(), s1, s2, s3);
        }
    }

    public double getSpeed() {
        return this.getDeltaMovement().length();
    }

    abstract public ClientAsset.ResourceTexture getTexture();
}
