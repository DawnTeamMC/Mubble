package fr.hugman.mubble.super_mario.client.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.client.renderer.RenderPipelines;

public class SuperMarioRenderPipelines {
    // Based on RenderPipelines.ENTITY_TRANSLUCENT
    public static final RenderPipeline GOLDEN_ENTITY_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
                    .withLocation(SuperMario.id("pipeline/golden_entity"))
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withFragmentShader(SuperMario.id("core/golden_entity"))
                    .withSampler("Sampler1")
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withCull(false)
                    .build()
    );
}