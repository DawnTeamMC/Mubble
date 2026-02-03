package fr.hugman.mubble.super_mario.world.inventory;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class SuperMarioMenuTypes {
    public static final MenuType<BumpableScreenHandler> BUMPABLE_BLOCK = of("bumpable_block", new MenuType<>(BumpableScreenHandler::new, FeatureFlags.VANILLA_SET));

    private static <T extends AbstractContainerMenu> MenuType<T> of(String path, MenuType<T> screenHandlerType) {
        return Registry.register(BuiltInRegistries.MENU, SuperMario.id(path), screenHandlerType);
    }
}
