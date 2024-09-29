package fr.hugman.mubble.client.render.entity.state;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GoombaEntityRenderState extends LivingEntityRenderState {
    private static final Identifier DEFAULT_TEXTURE = Mubble.id("textures/entity/goomba/normal/normal.png");

    public AnimationState surprisedAnimationState = new AnimationState();
    public AnimationState crushAnimationState = new AnimationState();
    public Identifier texture = DEFAULT_TEXTURE;
    public boolean surprised = false;
    public boolean stomped = false;
}
