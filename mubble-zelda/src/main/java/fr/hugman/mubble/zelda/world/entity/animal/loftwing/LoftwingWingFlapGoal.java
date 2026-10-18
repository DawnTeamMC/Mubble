package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * How a loftwing answers being attacked: one great beat of its wings that blows everything in front
 * of it a couple of blocks backwards. It barely scratches whoever started it — the point is to be
 * left alone, not to win.
 */
public class LoftwingWingFlapGoal extends MeleeAttackGoal {
	private static final double SPEED = 1.0;
	/** How far in front of the bird the gust reaches. */
	private static final double GUST_RANGE = 4.0;
	/** How hard it shoves, sideways and upwards. */
	private static final double PUSH_STRENGTH = 0.9;
	private static final double LIFT = 0.25;

	private final Loftwing loftwing;

	public LoftwingWingFlapGoal(Loftwing loftwing) {
		super(loftwing, SPEED, true);
		this.loftwing = loftwing;
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity target) {
		if (!this.canPerformAttack(target)) {
			return;
		}
		this.resetAttackCooldown();
		this.loftwing.playSound(ZeldaSounds.LOFTWING_FLAP, 1.0F, 1.0F);
		if (this.loftwing.level() instanceof ServerLevel level) {
			this.loftwing.doHurtTarget(level, target);
		}
		this.blowAwayEverythingInFront();
	}

	/**
	 * Entities, dropped items and experience alike: the gust does not care what it is pushing, only
	 * that it is in the way.
	 */
	private void blowAwayEverythingInFront() {
		Vec3 facing = this.loftwing.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
		Vec3 center = this.loftwing.position().add(facing.scale(GUST_RANGE / 2.0));
		AABB gust = new AABB(center, center).inflate(GUST_RANGE / 2.0 + this.loftwing.getBbWidth() / 2.0);
		for (Entity entity : this.loftwing.level().getEntities(this.loftwing, gust, this::isInFront)) {
			entity.push(facing.x() * PUSH_STRENGTH, LIFT, facing.z() * PUSH_STRENGTH);
			entity.hurtMarked = true;
		}
	}

	private boolean isInFront(Entity entity) {
		if (entity == this.loftwing) {
			return false;
		}
		if (entity instanceof LivingEntity living && this.loftwing.isOwnedBy(living)) {
			return false;
		}
		Vec3 toEntity = entity.position().subtract(this.loftwing.position()).multiply(1.0, 0.0, 1.0);
		return toEntity.lengthSqr() > 1.0E-4 && toEntity.normalize().dot(this.loftwing.getLookAngle().multiply(1.0, 0.0, 1.0).normalize()) > 0.3;
	}
}
