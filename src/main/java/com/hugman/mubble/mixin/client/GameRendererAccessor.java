package com.hugman.mubble.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
	@Invoker
	void invokeLoadShader(Identifier identifier);
}
