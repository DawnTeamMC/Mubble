package fr.hugman.mubble.screen;

import fr.hugman.mubble.Mubble;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class MubbleScreenHandlerTypes {
    // SUPER MARIO
    public static final ScreenHandlerType<BumpableScreenHandler> BUMPABLE_BLOCK = of("bumpable_block", new ScreenHandlerType<>(BumpableScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    private static <T extends ScreenHandler> ScreenHandlerType<T> of(String path, ScreenHandlerType<T> screenHandlerType) {
        return Registry.register(Registries.SCREEN_HANDLER, Mubble.id(path), screenHandlerType);
    }
}
