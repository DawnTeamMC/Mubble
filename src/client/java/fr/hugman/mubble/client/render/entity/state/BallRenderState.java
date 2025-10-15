package fr.hugman.mubble.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BallRenderState extends EntityRenderState {
    public float pitch;
    public float yaw;
    public double speed;
    public AssetInfo.TextureAssetInfo texture;
}