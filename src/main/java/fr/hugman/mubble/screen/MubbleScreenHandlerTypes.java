package fr.hugman.mubble.screen;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MubbleScreenHandlerTypes {
    // SUPER MARIO
    public static final MenuType<BumpableScreenHandler> BUMPABLE_BLOCK = of("bumpable_block", new MenuType<>(BumpableScreenHandler::new, FeatureFlags.VANILLA_SET));

    private static <T extends AbstractContainerMenu> MenuType<T> of(String path, MenuType<T> screenHandlerType) {
        return Registry.register(BuiltInRegistries.MENU, Mubble.id(path), screenHandlerType);
    }
}
