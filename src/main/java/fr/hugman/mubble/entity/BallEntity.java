package fr.hugman.mubble.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class BallEntity extends ThrownEntity {
	public static final String REBOUNDS_KEY = "rebounds";
    protected int rebounds = 3;

    protected BallEntity(EntityType<? extends BallEntity> type, World world) {
        super(type, world);
    }

    protected BallEntity(EntityType<? extends BallEntity> type, World world, LivingEntity owner) {
        super(type, world);
        this.setOwner(owner);
    }

    protected BallEntity(EntityType<? extends BallEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    public void tick() {
        super.tick();
        Vec3d vec3d = this.getVelocity();

        float f = (float)(MathHelper.atan2(-vec3d.x, -vec3d.z) * 180.0F / (float)Math.PI);
        float g = (float)(MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 180.0F / (float)Math.PI);
        this.setPitch(updateRotation(this.getPitch(), g));
        this.setYaw(updateRotation(this.getYaw(), f));
    }

    protected abstract SoundEvent getDeathSound();

    protected abstract ParticleEffect getDeathParticle();

	@Override
	protected double getGravity() {
		return 0.08;
	}

    @Override
    protected void onCollision(HitResult result) {
        this.rebounds--;
        super.onCollision(result);
        if (this.isAlive() && this.rebounds < 0) {
            this.finalHit();
        }
    }

    /**
     * Triggers after the ball has hit and can no longer rebound.
     */
    protected void finalHit() {
        if (!this.getEntityWorld().isClient()) {
            this.getEntityWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.remove(RemovalReason.DISCARDED);
        }
        this.getEntityWorld().playSound(null, getX(), getY(), getZ(), this.getDeathSound(), SoundCategory.NEUTRAL, 0.5F, 1.0F);
    }

	@Override
	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putInt(REBOUNDS_KEY, rebounds);
	}

	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		this.rebounds = view.getInt(REBOUNDS_KEY, 3);
	}

    @Environment(EnvType.CLIENT)
    @Override
    public void handleStatus(byte state) {
        if (state == 3) {
            this.spawnDeathParticles();
        }
    }

    protected void spawnDeathParticles() {
        for (int i = 0; i < 8; ++i) {
            float s1 = random.nextFloat() * 0.2F - 0.1F;
            float s2 = random.nextFloat() * 0.2F - 0.1F;
            float s3 = random.nextFloat() * 0.2F - 0.1F;
            this.getEntityWorld().addParticleClient(this.getDeathParticle(), this.getX(), this.getY(), this.getZ(), s1, s2, s3);
        }
    }

    public double getSpeed() {
        return this.getVelocity().length();
    }

    abstract public AssetInfo.TextureAssetInfo getTexture();
}
