package fr.hugman.mubble.splatoon.world.item.weapon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import fr.hugman.mubble.splatoon.SplatoonConversions;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBulletConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;

/**
 * A shooter that keeps firing while used, as {@code spl__WeaponShooterParam} in Splatoon 3.
 *
 * @param bullet              the bullets it fires
 * @param repeatFrame         the frames between two shots
 * @param standDegSwerve      how far shots stray from the aim, in degrees, when standing on the ground
 * @param jumpDegSwerve       how far shots stray from the aim, in degrees, when in the air
 * @param moveSpeed           how fast the shooter walks while firing, in units per frame
 * @param spawnAdditionZRate  how much of the shooter's own forward speed is added to the bullets
 * @author Hugman
 * @since v4.0.0
 */
public record AutomaticShooterConfig(
        ShooterInkBulletConfig bullet,
        int repeatFrame,
        float standDegSwerve,
        float jumpDegSwerve,
        float moveSpeed,
        float spawnAdditionZRate
) implements SplatoonWeapon {
    public static final int DEFAULT_REPEAT_FRAME = 6;

    public static final MapCodec<AutomaticShooterConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ShooterInkBulletConfig.CODEC.fieldOf("bullet").forGetter(AutomaticShooterConfig::bullet),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("repeat_frame", DEFAULT_REPEAT_FRAME).forGetter(AutomaticShooterConfig::repeatFrame),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("stand_deg_swerve").forGetter(AutomaticShooterConfig::standDegSwerve),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("jump_deg_swerve").forGetter(AutomaticShooterConfig::jumpDegSwerve),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("move_speed").forGetter(AutomaticShooterConfig::moveSpeed),
            MubbleCodecs.NONNEGATIVE_FLOAT.optionalFieldOf("spawn_addition_z_rate", 0.0F).forGetter(AutomaticShooterConfig::spawnAdditionZRate)
    ).apply(instance, AutomaticShooterConfig::new));

    public static final StreamCodec<ByteBuf, AutomaticShooterConfig> STREAM_CODEC = StreamCodec.composite(
            ShooterInkBulletConfig.STREAM_CODEC, AutomaticShooterConfig::bullet,
            ByteBufCodecs.VAR_INT, AutomaticShooterConfig::repeatFrame,
            ByteBufCodecs.FLOAT, AutomaticShooterConfig::standDegSwerve,
            ByteBufCodecs.FLOAT, AutomaticShooterConfig::jumpDegSwerve,
            ByteBufCodecs.FLOAT, AutomaticShooterConfig::moveSpeed,
            ByteBufCodecs.FLOAT, AutomaticShooterConfig::spawnAdditionZRate,
            AutomaticShooterConfig::new
    );

    @Override
    public SplatoonWeaponType<?> getType() {
        return SplatoonWeaponTypes.AUTOMATIC_SHOOTER;
    }

    /**
     * @param ticksUsed for how many ticks the shooter has been firing, counting the first one as 0
     * @return how many shots go off during that tick
     */
    public int shotsAt(int ticksUsed) {
        return shotsBefore(ticksUsed + 1) - shotsBefore(ticksUsed);
    }

    /**
     * @return how many shots went off before the given tick of firing, the first one going off right away
     */
    public int shotsBefore(int ticksUsed) {
        if (ticksUsed <= 0) {
            return 0;
        }
        return Mth.positiveCeilDiv(ticksUsed * SplatoonConversions.FRAMES_PER_TICK, this.repeatFrame);
    }

    /**
     * @return how many ticks to wait after letting go at the given tick, so that tapping does not fire faster
     */
    public int cooldownAfter(int ticksUsed) {
        int frames = ticksUsed * SplatoonConversions.FRAMES_PER_TICK;
        int nextShotFrame = this.shotsBefore(ticksUsed) * this.repeatFrame;
        return Mth.positiveCeilDiv(Math.max(0, nextShotFrame - frames), SplatoonConversions.FRAMES_PER_TICK);
    }

    /**
     * @return how fast the shooter walks while firing, relative to its usual walking speed
     */
    public float walkSpeedMultiplier() {
        return Math.min(1.0F, this.moveSpeed * SplatoonConversions.SPLATOON_TPS / SplatoonConversions.MINECRAFT_WALK_SPEED);
    }
}
