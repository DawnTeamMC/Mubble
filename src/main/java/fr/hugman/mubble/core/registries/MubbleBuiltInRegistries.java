package fr.hugman.mubble.core.registries;

import fr.hugman.mubble.world.entity.monster.goomba.GoombaVariant;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.action.PowerUpAction;
import fr.hugman.mubble.power_up.action.PowerUpActionType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MubbleBuiltInRegistries {
    public static final Registry<PowerUpActionType<?>> POWER_UP_ACTION_TYPE = register(MubbleRegistries.POWER_UP_ACTION_TYPE);

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.createSimple(key).buildAndRegister();
    }

    public static void register() {
        DynamicRegistries.registerSynced(MubbleRegistries.GOOMBA_VARIANT, GoombaVariant.CODEC);
        DynamicRegistries.registerSynced(MubbleRegistries.POWER_UP, PowerUp.CODEC);
        DynamicRegistries.registerSynced(MubbleRegistries.POWER_UP_ACTION, PowerUpAction.TYPE_CODEC);
    }
}
