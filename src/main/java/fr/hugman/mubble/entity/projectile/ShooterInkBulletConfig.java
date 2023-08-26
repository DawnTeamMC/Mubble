package fr.hugman.mubble.entity.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.codec.MubbleCodecs;
import fr.hugman.mubble.util.SplatConversions;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;

/**
 * This class is used to store the configuration of a shooter's ink bullet.
 *
 * <p>Here's how the config's fields are used:
 * <ul>
 *   <li> Between the moment the bullet is shot and the start "reduce tick", the bullet will deal maximum damage.
 *   <li> Between the start and end "reduce ticks", the bullet will deal damage that is linearly reduced from maximum to minimum.
 *   <li> After the end "reduce tick", the bullet will deal minimum damage.
 *   <li> The bullet will travel at an initial speed when shot, and will travel in a straight until the brake tick.
 *   <li> After the brake tick, the bullet's speed will be maximized to a certain value for 1 frame.
 *   <li> After that frame, the bullet's speed will gradually decreases until it reaches the free gravity threshold.
 * </ul>
 *
 * @author Hugman
 * @since v4.0.0
 */
public class ShooterInkBulletConfig {
    public static final Codec<ShooterInkBulletConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("max_damage").forGetter(config -> config.maxDamage),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("min_damage").forGetter(config -> config.minDamage),
            MubbleCodecs.NONNEGATIVE_LONG.fieldOf("start_reduce_tick").forGetter(config -> config.startReduceTick),
            MubbleCodecs.NONNEGATIVE_LONG.fieldOf("end_reduce_tick").forGetter(config -> config.endReduceTick),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("initial_speed").forGetter(config -> config.initialSpeed),
            MubbleCodecs.NONNEGATIVE_LONG.fieldOf("brake_tick").forGetter(config -> config.brakeTick),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("brake_max_speed").forGetter(config -> config.brakeMaxSpeed),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("free_gravity_threshold").forGetter(config -> config.freeGravityThreshold)
    ).apply(instance, ShooterInkBulletConfig::of));

    public static final ShooterInkBulletConfig DEFAULT = ShooterInkBulletConfig.ofSplat(40, 8, 360, 180, 1.4495F, 4, 2.2F, 0.016f);

    private final float maxDamage;
    private final float minDamage;
    private final long startReduceTick;
    private final long endReduceTick;
    private final float initialSpeed;
    private final long brakeTick;
    private final float brakeMaxSpeed;
    private final float freeGravityThreshold;

    private ShooterInkBulletConfig(float maxDamage, float minDamage, long startReduceTick, long endReduceTick, float initialSpeed, long brakeTick, float brakeMaxSpeed, float freeGravityThreshold) {
        this.maxDamage = maxDamage;
        this.minDamage = minDamage;
        this.startReduceTick = startReduceTick;
        this.endReduceTick = endReduceTick;
        this.initialSpeed = initialSpeed;
        this.brakeTick = brakeTick;
        this.brakeMaxSpeed = brakeMaxSpeed;
        this.freeGravityThreshold = freeGravityThreshold;
    }

    /**
     * Creates new bullet stats.
     *
     * @param maxDamage            the maximum damage of the bullet
     * @param minDamage            the minimum damage of the bullet
     * @param startReduceTick      the tick at which the bullet starts to reduce its damage
     * @param endReduceTick        the tick at which the bullet stops to reduce its damage
     * @param initialSpeed         the initial speed of the bullet, in blocks per second
     * @param brakeTick            the tick at which the bullet brakes
     * @param brakeMaxSpeed        the maximum speed of the bullet when braking, in blocks per second
     * @param freeGravityThreshold the speed threshold at which the bullet will start to free fall after it braked, in blocks per second
     */
    public static ShooterInkBulletConfig of(float maxDamage, float minDamage, long startReduceTick, long endReduceTick, float initialSpeed, long brakeTick, float brakeMaxSpeed, float freeGravityThreshold) {
        if (maxDamage < 0) {
            throw new IllegalArgumentException("Weapon maximum damage must be non-negative (>=0).");
        }
        if (minDamage < 0) {
            throw new IllegalArgumentException("Weapon minimum damage must be non-negative (>=0).");
        }
        if (maxDamage < minDamage) {
            throw new IllegalArgumentException("The max damage must be greater than the min damage.");
        }
        if (startReduceTick < 0) {
            throw new IllegalArgumentException("The start reduce tick must be non-negative (>=0).");
        }
        if (endReduceTick < 0) {
            throw new IllegalArgumentException("The end reduce tick must be non-negative (>=0).");
        }
        if (endReduceTick < startReduceTick) {
            throw new IllegalArgumentException("The end \"reduce tick\" must be greater than the start \"reduce tick\".");
        }
        if (initialSpeed < 0) {
            throw new IllegalArgumentException("The initial speed must be non-negative (>=0).");
        }
        if (brakeTick < 0) {
            throw new IllegalArgumentException("The brake tick must be non-negative (>=0).");
        }
        if (brakeMaxSpeed < 0) {
            throw new IllegalArgumentException("The brake max speed must be non-negative (>=0).");
        }
        if (freeGravityThreshold < 0) {
            throw new IllegalArgumentException("The free gravity threshold must be non-negative (>=0).");
        }
        return new ShooterInkBulletConfig(maxDamage, minDamage, startReduceTick, endReduceTick, initialSpeed, brakeTick, brakeMaxSpeed, freeGravityThreshold);
    }

    /**
     * Creates new bullet stats. Unlike {@link #of}, this method will take the values as they are in Splatoon 3, specifically from <a href="https://leanny.github.io/splat3/parameters.html">this database</a>.
     * Furthermore, this method also takes parameters in the same order as the database, with their original names.
     *
     * @param reduceEndFrame              the frame at which the bullet stops to reduce its damage
     * @param reduceStartFrame            the frame at which the bullet starts to reduce its damage
     * @param valueMax                    the maximum damage of the bullet
     * @param valueMin                    the minimum damage of the bullet
     * @param goStraightStateEndMaxSpeed  the maximum speed of the bullet when braking, in units per second
     * @param goStraightToBrakeStateFrame the tick at which the bullet brakes
     * @param spawnSpeed                  the initial speed of the bullet, in units per second
     * @param freeGravity                 the speed threshold at which the bullet will start to free fall after it braked, in units per second
     */
    public static ShooterInkBulletConfig ofSplat(int reduceEndFrame, int reduceStartFrame, int valueMax, int valueMin, float goStraightStateEndMaxSpeed, int goStraightToBrakeStateFrame, float spawnSpeed, float freeGravity) {
        //TODO: calculate the straight line distance correctly
        return of(
                SplatConversions.damage(valueMax),
                SplatConversions.damage(valueMin),
                SplatConversions.tps(reduceStartFrame),
                SplatConversions.tps(reduceEndFrame),
                SplatConversions.unitsToBlocks(spawnSpeed),
                SplatConversions.tps(goStraightToBrakeStateFrame),
                SplatConversions.unitsToBlocks(goStraightStateEndMaxSpeed),
                SplatConversions.unitsToBlocks(freeGravity)
        );
    }

    public float maxDamage() {
        return maxDamage;
    }

    public float minDamage() {
        return minDamage;
    }

    public long startReduceTick() {
        return startReduceTick;
    }

    public long endReduceTick() {
        return endReduceTick;
    }

    public float initialSpeed() {
        return initialSpeed;
    }

    public long brakeTick() {
        return brakeTick;
    }

    public float brakeMaxSpeed() {
        return brakeMaxSpeed;
    }

    public float freeGravityThreshold() {
        return freeGravityThreshold;
    }

    public ShooterInkBulletConfig copy() {
        return of(this.maxDamage, this.minDamage, this.startReduceTick, this.endReduceTick, this.initialSpeed, this.brakeTick, this.brakeMaxSpeed, this.freeGravityThreshold);
    }

    public static ShooterInkBulletConfig fromNbt(DynamicRegistryManager registryManager, NbtElement element) {
        return CODEC.parse(RegistryOps.of(NbtOps.INSTANCE, registryManager), element).getOrThrow(false, System.err::println);
    }

    public NbtElement toNbt(DynamicRegistryManager registryManager) {
        return CODEC.encodeStart(RegistryOps.of(NbtOps.INSTANCE, registryManager), this).resultOrPartial(Mubble.LOGGER::error).orElse(null);
    }
}
