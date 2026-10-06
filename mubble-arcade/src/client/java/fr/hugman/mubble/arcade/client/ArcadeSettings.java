package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.MubbleArcade;
import fr.hugman.mubble.client.config.MubbleSettings;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/** The settings of the arcade movement layer in the settings of Mubble: what {@link ArcadeClientConfig} holds. */
@Environment(EnvType.CLIENT)
public final class ArcadeSettings implements MubbleSettings.Section {
    private static final String PREFIX = "arcade." + Mubble.MOD_ID + ".settings.";

    @Nullable
    private Fields fields;

    /** The options last made, kept to read their values back when the screen closes. */
    private record Fields(
            OptionInstance<Boolean> orbitCamera,
            OptionInstance<Boolean> autoCameraKeyboard,
            OptionInstance<Boolean> autoCameraController,
            OptionInstance<Integer> autoCameraSpeed,
            OptionInstance<Integer> cameraDistance,
            OptionInstance<Integer> followLag,
            OptionInstance<Integer> orbitSensitivity,
            OptionInstance<Integer> fovKick,
            OptionInstance<Boolean> respectProfileHints,
            OptionInstance<Boolean> debugHud,
            OptionInstance<Boolean> silhouette
    ) {
    }

    @Override
    public String modId() {
        return MubbleArcade.MOD_ID;
    }

    @Override
    public Component title() {
        return Component.translatable("key.category." + Mubble.MOD_ID + ".arcade");
    }

    @Override
    public List<OptionInstance<?>> options() {
        var c = ArcadeClientConfig.get();
        this.fields = new Fields(
                bool("orbit_camera", c.orbitCamera()),
                bool("auto_camera_keyboard", c.autoCameraKeyboard()),
                bool("auto_camera_controller", c.autoCameraController()),
                number("auto_camera_speed", "degrees_per_second", 30, 360, (int) Math.round(c.autoCameraSpeed())),
                number("camera_distance", "blocks", 1, 16, (int) Math.round(c.cameraDistance())),
                number("follow_lag", "milliseconds", 0, 300, (int) Math.round(c.followLag() * 1000.0D)),
                number("orbit_sensitivity", "percent", 10, 300, (int) Math.round(c.orbitSensitivity() * 100.0D)),
                number("fov_kick", "percent", 0, 30, (int) Math.round(c.fovKick() * 100.0D)),
                bool("respect_profile_hints", c.respectProfileHints()),
                bool("debug_hud", c.debugHud()),
                bool("silhouette", c.silhouette()));
        var o = this.fields;
        return List.of(o.orbitCamera, o.cameraDistance, o.autoCameraKeyboard, o.autoCameraController, o.autoCameraSpeed,
                o.followLag, o.orbitSensitivity, o.fovKick, o.silhouette, o.respectProfileHints, o.debugHud);
    }

    @Override
    public void save() {
        var o = this.fields;
        if (o == null) {
            return;
        }
        var c = ArcadeClientConfig.get();
        ArcadeClientConfig.set(new ArcadeClientConfig(
                o.orbitCamera.get(), o.autoCameraKeyboard.get(), o.autoCameraController.get(), o.autoCameraSpeed.get(),
                o.cameraDistance.get(), c.cameraHeight(), o.followLag.get() / 1000.0D, c.recenterSpeed(),
                o.orbitSensitivity.get() / 100.0D, c.minPitch(), c.maxPitch(), o.fovKick.get() / 100.0D,
                o.respectProfileHints.get(), o.debugHud.get(), o.silhouette.get()));
    }

    private static OptionInstance<Boolean> bool(String name, boolean value) {
        return OptionInstance.createBoolean(PREFIX + name, OptionInstance.cachedConstantTooltip(Component.translatable(PREFIX + name + ".tooltip")), value);
    }

    private static OptionInstance<Integer> number(String name, String unit, int min, int max, int value) {
        return new OptionInstance<>(PREFIX + name, OptionInstance.cachedConstantTooltip(Component.translatable(PREFIX + name + ".tooltip")),
                (caption, v) -> Options.genericValueLabel(caption, Component.translatable(PREFIX + "unit." + unit, v)),
                new OptionInstance.IntRange(min, max), Math.clamp(value, min, max), v -> {
                });
    }
}
