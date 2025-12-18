package fr.hugman.mubble.item.consume;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class MubbleConsumeEffectTypeKeys {
    public static final ResourceKey<ConsumeEffect.Type<?>> CHANGE_POWER_UP = of("change_power_up");

    private static ResourceKey<ConsumeEffect.Type<?>> of(String path) {
        return ResourceKey.create(Registries.CONSUME_EFFECT_TYPE, Mubble.id(path));
    }
}
