package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class MubbleConsumeEffectTypeKeys {
    public static final ResourceKey<ConsumeEffect.Type<?>> CHANGE_POWER_UP = createKey("change_power_up");

    private static ResourceKey<ConsumeEffect.Type<?>> createKey(String path) {
        return ResourceKey.create(Registries.CONSUME_EFFECT_TYPE, Mubble.id(path));
    }
}
