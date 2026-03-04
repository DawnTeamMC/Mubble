package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.ClientAsset;

@Environment(EnvType.CLIENT)
public class BubbleRenderState extends EntityRenderState {
    public ClientAsset.ResourceTexture texture;
}
