package fr.hugman.mubble.client.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

/**
 * The settings of Mubble and of its modules, shown together on one screen (see
 * {@link MubbleSettingsScreen}), which Mod Menu opens from the entry of Mubble or of any module adding
 * a section. Each module adds the section of its own settings once, when the client starts.
 */
@Environment(EnvType.CLIENT)
public final class MubbleSettings {
    private static final List<Section> SECTIONS = new ArrayList<>();

    private MubbleSettings() {
    }

    public static void register(Section section) {
        SECTIONS.add(section);
    }

    public static List<Section> sections() {
        return Collections.unmodifiableList(SECTIONS);
    }

    /** The settings of one module. */
    public interface Section {
        /** The id of the mod the settings belong to, which Mod Menu opens the screen from too. */
        String modId();

        Component title();

        /**
         * Options showing the settings as they stand, made anew every time the screen opens. Their
         * values are what {@link #save} writes down.
         */
        List<OptionInstance<?>> options();

        /** Writes down the values of the options last made, as the screen closes. */
        void save();
    }
}
