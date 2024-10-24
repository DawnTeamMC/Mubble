package fr.hugman.mubble.item.consume;

import fr.hugman.mubble.Mubble;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleConsumeEffectTypeKeys {
    public static final RegistryKey<ConsumeEffect.Type<?>> CHANGE_POWER_UP = of("change_power_up");

    private static RegistryKey<ConsumeEffect.Type<?>> of(String path) {
        return RegistryKey.of(RegistryKeys.CONSUME_EFFECT_TYPE, Mubble.id(path));
    }
}
