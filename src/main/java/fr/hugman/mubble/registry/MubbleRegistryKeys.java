package fr.hugman.mubble.registry;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.action.PowerUpAction;
import fr.hugman.mubble.power_up.action.PowerUpActionType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MubbleRegistryKeys {
    public static final RegistryKey<Registry<GoombaVariant>> GOOMBA_VARIANT = of("goomba_variant");
    public static final RegistryKey<Registry<PowerUp>> POWER_UP = of("power_up");
    public static final RegistryKey<Registry<PowerUpActionType<?>>> POWER_UP_ACTION_TYPE = of("power_up_action_type");
    public static final RegistryKey<Registry<PowerUpAction>> POWER_UP_ACTION = of("power_up_action");

    public static <T> RegistryKey<Registry<T>> of(String path) {
        return RegistryKey.ofRegistry(Mubble.id(path));
    }
}
