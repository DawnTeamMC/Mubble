package fr.hugman.mubble.splatoon.world.entity.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletCollisionParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletDamageParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletMoveParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletPaintParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.SplashPaintParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.SplashSpawnParam;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

/**
 * Everything about the bullets of a shooter, grouped the way the parameter tables of Splatoon 3 group them, so that
 * values can be copied from <a href="https://leanny.github.io/splat3/parameters.html">Leanny's database</a> as they are.
 * Every length is in units (1 unit = 1 block), every duration in frames (60 per second).
 *
 * @author Hugman
 * @since v4.0.0
 */
public record ShooterInkBulletConfig(
        BulletMoveParam move,
        BulletDamageParam damage,
        BulletCollisionParam collision,
        BulletPaintParam paint,
        SplashSpawnParam splashSpawn,
        SplashPaintParam splashPaint
) {
    public static final Codec<ShooterInkBulletConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BulletMoveParam.CODEC.fieldOf("move_param").forGetter(ShooterInkBulletConfig::move),
            BulletDamageParam.CODEC.fieldOf("damage_param").forGetter(ShooterInkBulletConfig::damage),
            BulletCollisionParam.CODEC.optionalFieldOf("collision_param", BulletCollisionParam.DEFAULT).forGetter(ShooterInkBulletConfig::collision),
            BulletPaintParam.CODEC.fieldOf("paint_param").forGetter(ShooterInkBulletConfig::paint),
            SplashSpawnParam.CODEC.optionalFieldOf("splash_spawn_param", SplashSpawnParam.NONE).forGetter(ShooterInkBulletConfig::splashSpawn),
            SplashPaintParam.CODEC.fieldOf("splash_paint_param").forGetter(ShooterInkBulletConfig::splashPaint)
    ).apply(instance, ShooterInkBulletConfig::new));

    public static final StreamCodec<ByteBuf, ShooterInkBulletConfig> STREAM_CODEC = StreamCodec.composite(
            BulletMoveParam.STREAM_CODEC, ShooterInkBulletConfig::move,
            BulletDamageParam.STREAM_CODEC, ShooterInkBulletConfig::damage,
            BulletCollisionParam.STREAM_CODEC, ShooterInkBulletConfig::collision,
            BulletPaintParam.STREAM_CODEC, ShooterInkBulletConfig::paint,
            SplashSpawnParam.STREAM_CODEC, ShooterInkBulletConfig::splashSpawn,
            SplashPaintParam.STREAM_CODEC, ShooterInkBulletConfig::splashPaint,
            ShooterInkBulletConfig::new
    );

    /**
     * The Splattershot ({@code WeaponShooterNormal}), as of Splatoon 3 version 11.3.0.
     */
    public static final ShooterInkBulletConfig SPLATTERSHOT = new ShooterInkBulletConfig(
            BulletMoveParam.of(2.266F, 4, 1.493F, 0.016F),
            new BulletDamageParam(360, 180, 8, 40),
            BulletCollisionParam.of(0.2F, 0.285F),
            BulletPaintParam.of(1.93F, 1.93F, 1.71F, 1.1F, 1.31F, 2.24F, 1.12F, 2.24F),
            new SplashSpawnParam(9.2F, 1.2F, 1.5F, 8, List.of(4)),
            new SplashPaintParam(1.472F, 2.0608F, 3.0F, 10.0F)
    );

    /**
     * The .96 Gal ({@code WeaponShooterHeavy}), as of Splatoon 3 version 11.3.0.
     */
    public static final ShooterInkBulletConfig DOT_96_GAL = new ShooterInkBulletConfig(
            BulletMoveParam.of(2.45F, 5, 2.377F, 0.016F),
            new BulletDamageParam(620, 350, 9, 25),
            BulletCollisionParam.of(0.2F, 0.235F),
            BulletPaintParam.of(2.57F, 2.57F, 2.25F, 1.1F, 1.31F, 2.24F, 1.12F, 2.24F),
            new SplashSpawnParam(6.0F, 1.3F, 3.4F, 5, List.of()),
            new SplashPaintParam(1.5525F, 2.1735F, 3.0F, 10.0F)
    );
}
