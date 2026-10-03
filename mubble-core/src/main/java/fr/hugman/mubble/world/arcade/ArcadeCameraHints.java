package fr.hugman.mubble.world.arcade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * What a profile suggests to the camera. The camera itself is configured per client: these only
 * scale its values, and a player can tell the client to ignore them.
 *
 * @param distanceScale scale of the distance between the camera and the player
 * @param fovKickScale  scale of the speed-based field of view kick
 */
public record ArcadeCameraHints(double distanceScale, double fovKickScale) {
    public static final ArcadeCameraHints NEUTRAL = new ArcadeCameraHints(1.0D, 1.0D);
    public static final Codec<ArcadeCameraHints> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.doubleRange(0.0D, 4.0D).fieldOf("distance_scale").forGetter(ArcadeCameraHints::distanceScale),
            Codec.doubleRange(0.0D, 4.0D).fieldOf("fov_kick_scale").forGetter(ArcadeCameraHints::fovKickScale)
    ).apply(instance, ArcadeCameraHints::new));
}
