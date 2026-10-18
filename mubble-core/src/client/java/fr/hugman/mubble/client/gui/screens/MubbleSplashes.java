package fr.hugman.mubble.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lets modules append their own splash texts to the ones shown on the title screen.
 * <p>
 * Register splashes during client initialization; they are added to the vanilla ones every time the resources are (re)loaded.
 */
@Environment(EnvType.CLIENT)
public final class MubbleSplashes {
    private static final List<String> SPLASHES = new ArrayList<>();

    private MubbleSplashes() {}

    public static void add(String splash) {
        SPLASHES.add(splash);
    }

    public static void addAll(String... splashes) {
        Collections.addAll(SPLASHES, splashes);
    }

    public static List<String> all() {
        return Collections.unmodifiableList(SPLASHES);
    }
}
