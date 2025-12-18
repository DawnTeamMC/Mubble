package fr.hugman.mubble.registry;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.action.PowerUpAction;
import fr.hugman.mubble.power_up.action.PowerUpActionType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MubbleRegistryKeys {
    public static final ResourceKey<Registry<GoombaVariant>> GOOMBA_VARIANT = of("goomba_variant");
    public static final ResourceKey<Registry<PowerUp>> POWER_UP = of("power_up");
    public static final ResourceKey<Registry<PowerUpActionType<?>>> POWER_UP_ACTION_TYPE = of("power_up_action_type");
    public static final ResourceKey<Registry<PowerUpAction>> POWER_UP_ACTION = of("power_up_action");

    public static <T> ResourceKey<Registry<T>> of(String path) {
        return ResourceKey.createRegistryKey(Mubble.id(path));
    }
}
