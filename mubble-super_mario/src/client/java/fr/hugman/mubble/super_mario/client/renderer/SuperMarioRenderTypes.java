package fr.hugman.mubble.super_mario.client.renderer;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;

public class SuperMarioRenderTypes {
    // Based on RenderTypes#ENTITY_TRANSLUCENT
    public static final BiFunction<Identifier, Boolean, RenderType> GOLDEN_ENTITY = Util.memoize(
            (texture, affectsOutline) -> {
        RenderSetup state = RenderSetup.builder(SuperMarioRenderPipelines.GOLDEN_ENTITY_PIPELINE)
                .withTexture("Sampler0", texture)
                .useLightmap()
                .useOverlay()
                .affectsCrumbling()
                .sortOnUpload()
                .setOutline(affectsOutline ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE)
                .createRenderSetup();
        return RenderType.create("super_mario_golden_entity", state);
    });

    public static RenderType getGoldenEntity(Identifier texture) {
        return GOLDEN_ENTITY.apply(texture, true);
    }
}