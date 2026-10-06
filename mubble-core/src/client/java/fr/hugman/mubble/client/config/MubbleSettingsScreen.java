package fr.hugman.mubble.client.config;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

/** The settings of Mubble and of its modules, one section each, see {@link MubbleSettings}. */
@Environment(EnvType.CLIENT)
public class MubbleSettingsScreen extends OptionsSubScreen {
    public MubbleSettingsScreen(Screen lastScreen) {
        super(lastScreen, Minecraft.getInstance().options, Component.translatable(Mubble.MOD_ID + ".settings.title"));
    }

    @Override
    protected void addOptions() {
        if (MubbleSettings.sections().isEmpty()) {
            this.list.addHeader(Component.translatable(Mubble.MOD_ID + ".settings.empty"));
        }
        for (var section : MubbleSettings.sections()) {
            this.list.addHeader(section.title());
            this.list.addSmall(section.options().toArray(OptionInstance[]::new));
        }
    }

    @Override
    public void removed() {
        super.removed();
        MubbleSettings.sections().forEach(MubbleSettings.Section::save);
    }
}
