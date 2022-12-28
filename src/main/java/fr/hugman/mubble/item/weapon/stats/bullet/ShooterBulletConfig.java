package fr.hugman.mubble.item.weapon.stats.bullet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import fr.hugman.mubble.util.SplatConversions;

/**
 * This class is used to store the configuration of a shooter's bullet.
 *
 * <p>Here's how the object's fields are used:
 * <ul>
 *   <li> Between the moment the bullet is shot and the start "reduce tick", the bullet will deal maximum damage.
 *   <li> Between the start and end "reduce ticks", the bullet will deal damage that is linearly reduced from maximum to minimum.
 *   <li> After the end "reduce tick", the bullet will deal minimum damage.
 *   <li> The bullet will travel at an initial speed when shot, and will travel in a straight until the brake tick.
 *   <li> After the brake tick, the bullet will travel at a speed which is maximized to a certain value.
 * </ul>
 *
 * @author Hugman
 * @since v4.0.0
 */
public class ShooterBulletConfig {
	public static final Codec<ShooterBulletConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("max_damage").forGetter(config -> config.maxDamage),
			MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("min_damage").forGetter(config -> config.minDamage),
			MubbleCodecs.NONNEGATIVE_LONG.fieldOf("start_reduce_tick").forGetter(config -> config.startReduceTick),
			MubbleCodecs.NONNEGATIVE_LONG.fieldOf("end_reduce_tick").forGetter(config -> config.endReduceTick),
			MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("initial_speed").forGetter(config -> config.initialSpeed),
			MubbleCodecs.NONNEGATIVE_LONG.fieldOf("brake_tick").forGetter(config -> config.brakeTick),
			MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("brake_max_speed").forGetter(config -> config.brakeMaxSpeed)
	).apply(instance, ShooterBulletConfig::of));

	private final float maxDamage;
	private final float minDamage;
	private final long startReduceTick;
	private final long endReduceTick;
	private final float initialSpeed;
	private final long brakeTick;
	private final float brakeMaxSpeed;

	private ShooterBulletConfig(float maxDamage, float minDamage, long startReduceTick, long endReduceTick, float initialSpeed, long brakeTick, float brakeMaxSpeed) {
		this.maxDamage = maxDamage;
		this.minDamage = minDamage;
		this.startReduceTick = startReduceTick;
		this.endReduceTick = endReduceTick;
		this.initialSpeed = initialSpeed;
		this.brakeTick = brakeTick;
		this.brakeMaxSpeed = brakeMaxSpeed;
	}

	/**
	 * Creates new bullet stats.
	 *
	 * @param maxDamage       the maximum damage of the bullet
	 * @param minDamage       the minimum damage of the bullet
	 * @param startReduceTick the tick at which the bullet starts to reduce its damage
	 * @param endReduceTick   the tick at which the bullet stops to reduce its damage
	 * @param initialSpeed    the initial speed of the bullet, in blocks per second
	 * @param brakeTick       the tick at which the bullet brakes
	 * @param brakeMaxSpeed   the maximum speed of the bullet when braking, in blocks per second
	 */
	public static ShooterBulletConfig of(float maxDamage, float minDamage, long startReduceTick, long endReduceTick, float initialSpeed, long brakeTick, float brakeMaxSpeed) {
		if(maxDamage < 0) {
			throw new IllegalArgumentException("Weapon maximum damage must be non-negative (>=0).");
		}
		if(minDamage < 0) {
			throw new IllegalArgumentException("Weapon minimum damage must be non-negative (>=0).");
		}
		if(maxDamage < minDamage) {
			throw new IllegalArgumentException("The max damage must be greater than the min damage.");
		}
		if(startReduceTick < 0) {
			throw new IllegalArgumentException("The start reduce tick must be non-negative (>=0).");
		}
		if(endReduceTick < 0) {
			throw new IllegalArgumentException("The end reduce tick must be non-negative (>=0).");
		}
		if(endReduceTick < startReduceTick) {
			throw new IllegalArgumentException("The end \"reduce tick\" must be greater than the start \"reduce tick\".");
		}
		if(initialSpeed < 0) {
			throw new IllegalArgumentException("The initial speed must be non-negative (>=0).");
		}
		if(brakeTick < 0) {
			throw new IllegalArgumentException("The brake tick must be non-negative (>=0).");
		}
		if(brakeMaxSpeed < 0) {
			throw new IllegalArgumentException("The brake max speed must be non-negative (>=0).");
		}
		return new ShooterBulletConfig(maxDamage, minDamage, startReduceTick, endReduceTick, initialSpeed, brakeTick, brakeMaxSpeed);
	}

	/**
	 * Creates new bullet stats. Unlike {@link #of}, this method will take the values as they are in Splatoon 3, specifically from <a href="https://leanny.github.io/splat3/parameters.html">this database</a>.
	 * Furthermore, this method also takes parameters in the same order as the database, with their original names.
	 *
	 * @param reduceEndFrame              the frame at which the bullet stops to reduce its damage
	 * @param reduceStartFrame            the tick at which the bullet starts to reduce its damage
	 * @param valueMax                    the maximum damage of the bullet
	 * @param valueMin                    the minimum damage of the bullet
	 * @param goStraightStateEndMaxSpeed  the maximum speed of the bullet when braking, in blocks per second
	 * @param goStraightToBrakeStateFrame the tick at which the bullet brakes
	 * @param spawnSpeed                  the initial speed of the bullet, in blocks per second
	 */
	public static ShooterBulletConfig ofSplat(int reduceEndFrame, int reduceStartFrame, int valueMax, int valueMin, float goStraightStateEndMaxSpeed, int goStraightToBrakeStateFrame, float spawnSpeed) {
		return of(
				SplatConversions.damage(valueMax),
				SplatConversions.damage(valueMin),
				SplatConversions.tps(reduceStartFrame),
				SplatConversions.tps(reduceEndFrame),
				SplatConversions.unitsToBlocks(spawnSpeed),
				SplatConversions.tps(goStraightToBrakeStateFrame),
				SplatConversions.unitsToBlocks(goStraightStateEndMaxSpeed)
		);
	}

	public float maxDamage() {
		return maxDamage;
	}

	public float minDamage() {
		return minDamage;
	}

	public float startReduceTick() {
		return startReduceTick;
	}

	public float endReduceTick() {
		return endReduceTick;
	}

	public float initialSpeed() {
		return initialSpeed;
	}

	public float brakeTick() {
		return brakeTick;
	}

	public float brakeMaxSpeed() {
		return brakeMaxSpeed;
	}
}
