package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class BubbleRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public ClientAsset.ResourceTexture texture;
    /** How flattened the bubble is against a block, from 0 (round) to 1 (fully squished). */
    public float squish;
    public Direction.Axis squishAxis = Direction.Axis.Y;
}
