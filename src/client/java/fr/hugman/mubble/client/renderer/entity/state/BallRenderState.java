package fr.hugman.mubble.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.ClientAsset;

@Environment(EnvType.CLIENT)
public class BallRenderState extends EntityRenderState {
    public float xRot;
    public float yRot;
    public double speed;
    public ClientAsset.ResourceTexture texture;
}