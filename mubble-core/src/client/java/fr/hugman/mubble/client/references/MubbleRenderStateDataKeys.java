package fr.hugman.mubble.client.references;

import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Unique;

public class MubbleRenderStateDataKeys {
    @Unique
    public static final RenderStateDataKey<Holder<PowerUp>> POWER_UP = RenderStateDataKey.create(() -> "Power-up");
}
