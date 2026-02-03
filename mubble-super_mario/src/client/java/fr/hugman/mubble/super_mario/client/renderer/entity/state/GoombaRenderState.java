package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.super_mario.SuperMario;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class GoombaRenderState extends LivingEntityRenderState {
    private static final Identifier DEFAULT_TEXTURE = SuperMario.id("textures/entity/goomba/normal/normal.png");

    public final AnimationState surprisedAnimationState = new AnimationState();
    public final AnimationState crushAnimationState = new AnimationState();
    public Identifier texture = DEFAULT_TEXTURE;
    public boolean stomped = false;
}
