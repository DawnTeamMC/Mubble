package fr.hugman.mubble.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.hugman.mubble.client.config.MubbleSettings;
import fr.hugman.mubble.client.config.MubbleSettingsScreen;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/** The settings of Mubble in Mod Menu: one screen, opened from Mubble and from every module adding to it. */
@Environment(EnvType.CLIENT)
public final class MubbleModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return MubbleSettingsScreen::new;
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> factories = new HashMap<>();
        for (var section : MubbleSettings.sections()) {
            factories.put(section.modId(), MubbleSettingsScreen::new);
        }
        return factories;
    }
}
