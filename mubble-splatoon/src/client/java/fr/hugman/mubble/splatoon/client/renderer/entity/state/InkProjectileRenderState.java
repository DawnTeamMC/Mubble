package fr.hugman.mubble.splatoon.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class InkProjectileRenderState extends EntityRenderState {
    public float pitch;
    public float yaw;
    /**
     * In blocks per tick.
     */
    public double speed;
    public int color = -1;
}
