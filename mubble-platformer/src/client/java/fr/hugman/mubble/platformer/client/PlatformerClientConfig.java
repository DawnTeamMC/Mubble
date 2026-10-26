package fr.hugman.mubble.platformer.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The client side settings of the platformer movement layer, kept in {@code config/mubble-platformer-client.json}
 * and shown in the settings of Mubble, see {@link PlatformerSettings}. Missing fields keep their default;
 * the file is written back with every field on load.
 *
 * @param orbitCamera         whether the third person (back) camera orbits freely around the player,
 *                            the stick moving the player relative to it
 * @param autoCameraKeyboard  whether the orbit camera swings round behind the player as they move,
 *                            playing on the keyboard and mouse
 * @param autoCameraController the same, playing on a controller
 * @param autoCameraSpeed     how fast it swings round at its quickest, rolling sideways past the run speed,
 *                            in degrees per second, see {@link fr.hugman.mubble.platformer.PlatformerCameraFollow}
 * @param cameraDistanceScale distance between the orbit camera and the player, as a scale of the one of
 *                            the vanilla third person camera ({@code minecraft:camera_distance}, 4 blocks
 *                            unless something changes it)
 * @param cameraHeight        height of the point the camera looks at, above the eyes, in blocks
 * @param cameraMargin        how much room the camera leaves around the player before following them,
 *                            as a scale of the margins of {@link fr.hugman.mubble.platformer.PlatformerFraming}: 0
 *                            keeps the player in the middle
 * @param recenterSpeed       how fast the camera swings back behind the player, in degrees per second
 * @param orbitSensitivity    scale of the mouse and right stick while orbiting
 * @param minPitch            how far up the camera may look, in degrees
 * @param maxPitch            how far down the camera may look, in degrees
 * @param fovKick             extra field of view at full run speed, as a share of the base one
 * @param respectProfileHints whether the camera hints of the active profile apply
 * @param debugHud            whether the debug HUD starts shown
 * @param silhouette          whether the player shows through what hides them from the orbit camera
 */
@Environment(EnvType.CLIENT)
public record PlatformerClientConfig(
        boolean orbitCamera,
        boolean autoCameraKeyboard,
        boolean autoCameraController,
        double autoCameraSpeed,
        double cameraDistanceScale,
        double cameraHeight,
        double cameraMargin,
        double recenterSpeed,
        double orbitSensitivity,
        double minPitch,
        double maxPitch,
        double fovKick,
        boolean respectProfileHints,
        boolean debugHud,
        boolean silhouette
) {
    private static final Logger LOGGER = LogManager.getLogger(Mubble.MOD_ID);
    public static final PlatformerClientConfig DEFAULT = new PlatformerClientConfig(true, false, true, 90.0D, 1.25D, 0.3D, 1.0D, 540.0D, 1.0D, -80.0D, 80.0D, 0.08D, true, false, true);

    public static final Codec<PlatformerClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("orbit_camera", DEFAULT.orbitCamera).forGetter(PlatformerClientConfig::orbitCamera),
            Codec.BOOL.optionalFieldOf("auto_camera_keyboard", DEFAULT.autoCameraKeyboard).forGetter(PlatformerClientConfig::autoCameraKeyboard),
            Codec.BOOL.optionalFieldOf("auto_camera_controller", DEFAULT.autoCameraController).forGetter(PlatformerClientConfig::autoCameraController),
            Codec.doubleRange(0.0D, 1000.0D).optionalFieldOf("auto_camera_speed", DEFAULT.autoCameraSpeed).forGetter(PlatformerClientConfig::autoCameraSpeed),
            Codec.doubleRange(0.25D, 8.0D).optionalFieldOf("camera_distance_scale", DEFAULT.cameraDistanceScale).forGetter(PlatformerClientConfig::cameraDistanceScale),
            Codec.doubleRange(-2.0D, 4.0D).optionalFieldOf("camera_height", DEFAULT.cameraHeight).forGetter(PlatformerClientConfig::cameraHeight),
            Codec.doubleRange(0.0D, 2.0D).optionalFieldOf("camera_margin", DEFAULT.cameraMargin).forGetter(PlatformerClientConfig::cameraMargin),
            Codec.doubleRange(0.0D, 10000.0D).optionalFieldOf("recenter_speed", DEFAULT.recenterSpeed).forGetter(PlatformerClientConfig::recenterSpeed),
            Codec.doubleRange(0.0D, 10.0D).optionalFieldOf("orbit_sensitivity", DEFAULT.orbitSensitivity).forGetter(PlatformerClientConfig::orbitSensitivity),
            Codec.doubleRange(-90.0D, 90.0D).optionalFieldOf("min_pitch", DEFAULT.minPitch).forGetter(PlatformerClientConfig::minPitch),
            Codec.doubleRange(-90.0D, 90.0D).optionalFieldOf("max_pitch", DEFAULT.maxPitch).forGetter(PlatformerClientConfig::maxPitch),
            Codec.doubleRange(0.0D, 1.0D).optionalFieldOf("fov_kick", DEFAULT.fovKick).forGetter(PlatformerClientConfig::fovKick),
            Codec.BOOL.optionalFieldOf("respect_profile_hints", DEFAULT.respectProfileHints).forGetter(PlatformerClientConfig::respectProfileHints),
            Codec.BOOL.optionalFieldOf("debug_hud", DEFAULT.debugHud).forGetter(PlatformerClientConfig::debugHud),
            Codec.BOOL.optionalFieldOf("silhouette", DEFAULT.silhouette).forGetter(PlatformerClientConfig::silhouette)
    ).apply(instance, PlatformerClientConfig::new));

    private static PlatformerClientConfig current = DEFAULT;

    public static PlatformerClientConfig get() {
        return current;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("mubble-platformer-client.json");
    }

    public static void load() {
        var path = path();
        try {
            if (Files.exists(path)) {
                var json = JsonParser.parseString(Files.readString(path));
                migrateCameraDistance(json);
                current = CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(error -> LOGGER.warn("Invalid platformer client config, using defaults where needed: {}", error)).orElse(DEFAULT);
            }
            write(path);
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Could not read or write the platformer client config", exception);
        }
    }

    /**
     * Turns the camera distance of a file written before it was a scale, in blocks, into the scale of
     * the vanilla distance that gives the same distance.
     */
    private static void migrateCameraDistance(JsonElement json) {
        if (json instanceof JsonObject object && !object.has("camera_distance_scale")
                && object.get("camera_distance") instanceof JsonPrimitive blocks && blocks.isNumber()) {
            object.addProperty("camera_distance_scale", blocks.getAsDouble() / Attributes.CAMERA_DISTANCE.value().getDefaultValue());
        }
    }

    /** The same settings, the camera following the player round on a controller or not. */
    public PlatformerClientConfig withAutoCameraController(boolean on) {
        return new PlatformerClientConfig(this.orbitCamera, this.autoCameraKeyboard, on, this.autoCameraSpeed, this.cameraDistanceScale, this.cameraHeight,
                this.cameraMargin, this.recenterSpeed, this.orbitSensitivity, this.minPitch, this.maxPitch, this.fovKick, this.respectProfileHints, this.debugHud, this.silhouette);
    }

    /** Sets the settings, as the settings screen does, and writes them down. */
    public static void set(PlatformerClientConfig config) {
        current = config;
        try {
            write(path());
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Could not write the platformer client config", exception);
        }
    }

    private static void write(Path path) throws IOException {
        var encoded = CODEC.encodeStart(JsonOps.INSTANCE, current).getOrThrow();
        // every field is written, defaults included, so that the file documents itself
        var object = encoded.getAsJsonObject();
        writeEveryField(object);
        Files.createDirectories(path.getParent());
        Files.writeString(path, new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(object));
    }

    private static void writeEveryField(com.google.gson.JsonObject object) {
        var c = current;
        object.addProperty("orbit_camera", c.orbitCamera);
        object.addProperty("auto_camera_keyboard", c.autoCameraKeyboard);
        object.addProperty("auto_camera_controller", c.autoCameraController);
        object.addProperty("auto_camera_speed", c.autoCameraSpeed);
        object.addProperty("camera_distance_scale", c.cameraDistanceScale);
        object.addProperty("camera_height", c.cameraHeight);
        object.addProperty("camera_margin", c.cameraMargin);
        object.addProperty("recenter_speed", c.recenterSpeed);
        object.addProperty("orbit_sensitivity", c.orbitSensitivity);
        object.addProperty("min_pitch", c.minPitch);
        object.addProperty("max_pitch", c.maxPitch);
        object.addProperty("fov_kick", c.fovKick);
        object.addProperty("respect_profile_hints", c.respectProfileHints);
        object.addProperty("debug_hud", c.debugHud);
        object.addProperty("silhouette", c.silhouette);
    }
}
