package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class FlowerRenderState extends EntityRenderState {
    /** How far the flower has travelled, in blocks, which is what it spins to. */
    public float travelled;
    /** How squashed the flower is by a ceiling it just hit, from 0 (upright) to 1 (fully squished). */
    public float squish;
}
