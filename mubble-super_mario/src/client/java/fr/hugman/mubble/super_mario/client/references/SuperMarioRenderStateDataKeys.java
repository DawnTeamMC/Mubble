package fr.hugman.mubble.super_mario.client.references;

import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Unique;

public class SuperMarioRenderStateDataKeys {
    @Unique
    public static final RenderStateDataKey<Holder<PowerUp>> POWER_UP = RenderStateDataKey.create(() -> "Power-up");
}
