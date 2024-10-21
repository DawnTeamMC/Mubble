package fr.hugman.mubble.registry;

import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.action.PowerUpAction;
import fr.hugman.mubble.power_up.action.PowerUpActionType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MubbleRegistries {
    public static final Registry<PowerUpActionType<?>> POWER_UP_TRIGGER_EFFECT_TYPE = of(MubbleRegistryKeys.POWER_UP_ACTION_TYPE);

    private static <T> Registry<T> of(RegistryKey<Registry<T>> key) {
        return FabricRegistryBuilder.createSimple(key).buildAndRegister();
    }

    public static void register() {
        DynamicRegistries.registerSynced(MubbleRegistryKeys.GOOMBA_VARIANT, GoombaVariant.CODEC);
        DynamicRegistries.registerSynced(MubbleRegistryKeys.POWER_UP, PowerUp.CODEC);
        DynamicRegistries.registerSynced(MubbleRegistryKeys.POWER_UP_ACTION, PowerUpAction.TYPE_CODEC);
    }
}
