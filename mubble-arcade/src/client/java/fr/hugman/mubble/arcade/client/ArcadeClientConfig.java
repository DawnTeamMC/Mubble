package fr.hugman.mubble.arcade.client;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The client side settings of the arcade movement layer, kept in {@code config/mubble-arcade-client.json}.
 * Missing fields keep their default; the file is written back with every field on load.
 *
 * @param orbitCamera         whether the third person (back) camera orbits freely around the player,
 *                            the stick moving the player relative to it
 * @param cameraDistance      distance between the orbit camera and the player, in blocks, at scale 1
 * @param cameraHeight        height of the point the camera looks at, above the eyes, in blocks
 * @param followLag           how long the camera takes to catch up with the player, in seconds
 * @param recenterSpeed       how fast the camera swings back behind the player, in degrees per second
 * @param orbitSensitivity    scale of the mouse and right stick while orbiting
 * @param minPitch            how far up the camera may look, in degrees
 * @param maxPitch            how far down the camera may look, in degrees
 * @param fovKick             extra field of view at full run speed, as a share of the base one
 * @param respectProfileHints whether the camera hints of the active profile apply
 * @param debugHud            whether the debug HUD starts shown
 */
@Environment(EnvType.CLIENT)
public record ArcadeClientConfig(
        boolean orbitCamera,
        double cameraDistance,
        double cameraHeight,
        double followLag,
        double recenterSpeed,
        double orbitSensitivity,
        double minPitch,
        double maxPitch,
        double fovKick,
        boolean respectProfileHints,
        boolean debugHud
) {
    private static final Logger LOGGER = LogManager.getLogger(Mubble.MOD_ID);
    public static final ArcadeClientConfig DEFAULT = new ArcadeClientConfig(true, 5.0D, 0.3D, 0.08D, 540.0D, 1.0D, -80.0D, 80.0D, 0.08D, true, false);

    public static final Codec<ArcadeClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("orbit_camera", DEFAULT.orbitCamera).forGetter(ArcadeClientConfig::orbitCamera),
            Codec.doubleRange(1.0D, 32.0D).optionalFieldOf("camera_distance", DEFAULT.cameraDistance).forGetter(ArcadeClientConfig::cameraDistance),
            Codec.doubleRange(-2.0D, 4.0D).optionalFieldOf("camera_height", DEFAULT.cameraHeight).forGetter(ArcadeClientConfig::cameraHeight),
            Codec.doubleRange(0.0D, 2.0D).optionalFieldOf("follow_lag", DEFAULT.followLag).forGetter(ArcadeClientConfig::followLag),
            Codec.doubleRange(0.0D, 10000.0D).optionalFieldOf("recenter_speed", DEFAULT.recenterSpeed).forGetter(ArcadeClientConfig::recenterSpeed),
            Codec.doubleRange(0.0D, 10.0D).optionalFieldOf("orbit_sensitivity", DEFAULT.orbitSensitivity).forGetter(ArcadeClientConfig::orbitSensitivity),
            Codec.doubleRange(-90.0D, 90.0D).optionalFieldOf("min_pitch", DEFAULT.minPitch).forGetter(ArcadeClientConfig::minPitch),
            Codec.doubleRange(-90.0D, 90.0D).optionalFieldOf("max_pitch", DEFAULT.maxPitch).forGetter(ArcadeClientConfig::maxPitch),
            Codec.doubleRange(0.0D, 1.0D).optionalFieldOf("fov_kick", DEFAULT.fovKick).forGetter(ArcadeClientConfig::fovKick),
            Codec.BOOL.optionalFieldOf("respect_profile_hints", DEFAULT.respectProfileHints).forGetter(ArcadeClientConfig::respectProfileHints),
            Codec.BOOL.optionalFieldOf("debug_hud", DEFAULT.debugHud).forGetter(ArcadeClientConfig::debugHud)
    ).apply(instance, ArcadeClientConfig::new));

    private static ArcadeClientConfig current = DEFAULT;

    public static ArcadeClientConfig get() {
        return current;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("mubble-arcade-client.json");
    }

    public static void load() {
        var path = path();
        try {
            if (Files.exists(path)) {
                var json = JsonParser.parseString(Files.readString(path));
                current = CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(error -> LOGGER.warn("Invalid arcade client config, using defaults where needed: {}", error)).orElse(DEFAULT);
            }
            var encoded = CODEC.encodeStart(JsonOps.INSTANCE, current).getOrThrow();
            // every field is written, defaults included, so that the file documents itself
            var object = encoded.getAsJsonObject();
            writeEveryField(object);
            Files.createDirectories(path.getParent());
            Files.writeString(path, new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(object));
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Could not read or write the arcade client config", exception);
        }
    }

    private static void writeEveryField(com.google.gson.JsonObject object) {
        var c = current;
        object.addProperty("orbit_camera", c.orbitCamera);
        object.addProperty("camera_distance", c.cameraDistance);
        object.addProperty("camera_height", c.cameraHeight);
        object.addProperty("follow_lag", c.followLag);
        object.addProperty("recenter_speed", c.recenterSpeed);
        object.addProperty("orbit_sensitivity", c.orbitSensitivity);
        object.addProperty("min_pitch", c.minPitch);
        object.addProperty("max_pitch", c.maxPitch);
        object.addProperty("fov_kick", c.fovKick);
        object.addProperty("respect_profile_hints", c.respectProfileHints);
        object.addProperty("debug_hud", c.debugHud);
    }
}
