package fr.hugman.mubble.super_mario.client.references;

import fr.hugman.mubble.super_mario.client.renderer.entity.state.FreezeRenderData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

@Environment(EnvType.CLIENT)
public class SuperMarioRenderStateDataKeys {
    /** The block of ice around the entity, {@code null} whenever it is not frozen. */
    public static final RenderStateDataKey<FreezeRenderData> FREEZE = RenderStateDataKey.create(() -> "Freeze");
}
