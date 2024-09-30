package fr.hugman.mubble.item.weapon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import fr.hugman.mubble.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.util.SplatoonConversions;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * This class is used to store the configuration of an {@link SplatoonWeaponItem}.
 *
 * <p>Here's how the config's fields are used:
 * <ul>
 *   <li> The weapon will shoot a bullet every x ticks.
 *   <li> The shots will deviate from the center of the screen by a certain amount when standing, and by a (supposedly greater) amount when moving.
 * </ul>
 *
 * @author Hugman
 * @since v4.0.0
 */
public class AutomaticShooterConfig extends SplatoonWeapon {
    public static final MapCodec<AutomaticShooterConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ShooterInkBulletConfig.CODEC.fieldOf("ink_bullet_config").forGetter(config -> config.bulletConfig),
            MubbleCodecs.NONNEGATIVE_LONG.fieldOf("cooldown").forGetter(config -> config.cooldown),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("angle_deviation").forGetter(config -> config.angleDeviation),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("jumping_angle_deviation").forGetter(config -> config.jumpingAngleDeviation)
    ).apply(instance, AutomaticShooterConfig::of));

    public static final PacketCodec<RegistryByteBuf, AutomaticShooterConfig> PACKET_CODEC = PacketCodec.tuple(
            ShooterInkBulletConfig.PACKET_CODEC, AutomaticShooterConfig::bulletConfig,
            PacketCodecs.VAR_LONG, AutomaticShooterConfig::cooldown,
            PacketCodecs.FLOAT, AutomaticShooterConfig::angleDeviation,
            PacketCodecs.FLOAT, AutomaticShooterConfig::jumpingAngleDeviation,
            AutomaticShooterConfig::new
    );

    private final ShooterInkBulletConfig bulletConfig;
    private final long cooldown;
    private final float angleDeviation;
    private final float jumpingAngleDeviation;

    private AutomaticShooterConfig(ShooterInkBulletConfig bulletConfig, long cooldown, float angleDeviation, float jumpingAngleDeviation) {
        this.bulletConfig = bulletConfig;
        this.cooldown = cooldown;
        this.angleDeviation = angleDeviation;
        this.jumpingAngleDeviation = jumpingAngleDeviation;
    }

    @Override
    protected SplatoonWeaponType<?> getType() {
        return SplatoonWeaponTypes.AUTOMATIC_SHOOTER;
    }

    /*===========*/
    /*  FACTORY  */
    /*===========*/

    /**
     * Creates new automatic shooter stats.
     *
     * @param bulletConfig          the configuration of the bullets shot by the weapon
     * @param cooldown              the cooldown of the weapon, in ticks
     * @param angleDeviation        the angle deviation of the weapon, in degrees
     * @param jumpingAngleDeviation the angle deviation of the weapon while jumping, in degrees
     */
    public static AutomaticShooterConfig of(ShooterInkBulletConfig bulletConfig, long cooldown, float angleDeviation, float jumpingAngleDeviation) {
        if (cooldown < 0) {
            throw new IllegalArgumentException("Weapon cooldown must be non-negative.");
        }
        if (angleDeviation < 0) {
            throw new IllegalArgumentException("Weapon angle deviation must be non-negative.");
        }
        if (jumpingAngleDeviation < 0) {
            throw new IllegalArgumentException("Weapon jumping angle deviation must be non-negative.");
        }
        return new AutomaticShooterConfig(bulletConfig, cooldown, angleDeviation, jumpingAngleDeviation);
    }

    /**
     * Creates new automatic shooter stats. Unlike {@link #of}, this method will take the values as they are in Splatoon 3, specifically from <a href="https://leanny.github.io/splat3/parameters.html">this database</a>.
     * Furthermore, this method also takes parameters in the same order as the database, with their original names.
     *
     * @param bulletConfig   the configuration of the bullets shot by the weapon
     * @param repeatFrame    the cooldown of the weapon, in frames
     * @param jumpDegSwerve  the angle deviation of the weapon while jumping, in degrees
     * @param standDegSwerve the angle deviation of the weapon while standing, in degrees
     */
    public static AutomaticShooterConfig ofSplat(ShooterInkBulletConfig bulletConfig, int repeatFrame, float jumpDegSwerve, float standDegSwerve) {
        //TODO: the cooldown (RepeatFrame) seems to be missing from some weapon data (splattershot). Wiki mentions it is 6
        return of(
                bulletConfig,
                SplatoonConversions.time(repeatFrame),
                standDegSwerve,
                jumpDegSwerve
        );
    }
    /*===========*/
    /*  GETTERS  */

    /*===========*/

    public ShooterInkBulletConfig bulletConfig() {
        return bulletConfig;
    }

    public long cooldown() {
        return cooldown;
    }

    public float angleDeviation() {
        return angleDeviation;
    }

    public float jumpingAngleDeviation() {
        return jumpingAngleDeviation;
    }
}
