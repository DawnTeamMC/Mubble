package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class BoomerangRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public float spin;
    public float headingYaw;
    public float headingPitch;
    /** Positive stretches along the heading, negative squashes. */
    public float stretch;
    /** Negative squashes along the rebound axis. */
    public float rebound;
    public Direction.Axis reboundAxis = Direction.Axis.Y;
    /** Negative squashes vertically. */
    public float pulse;
    public float scale = 1.0F;
}
