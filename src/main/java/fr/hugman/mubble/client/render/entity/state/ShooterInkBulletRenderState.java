package fr.hugman.mubble.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class ShooterInkBulletRenderState extends EntityRenderState {
    public float pitch;
    public float yaw;
    public double speed;
}
