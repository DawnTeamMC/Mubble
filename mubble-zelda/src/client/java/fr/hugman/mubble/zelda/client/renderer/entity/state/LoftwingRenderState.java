package fr.hugman.mubble.zelda.client.renderer.entity.state;

import fr.hugman.mubble.zelda.Zelda;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class LoftwingRenderState extends LivingEntityRenderState {
    private static final Identifier DEFAULT_TEXTURE = Zelda.id("textures/entity/loftwing/blue.png");

    public final AnimationState flapAnimationState = new AnimationState();
    public final AnimationState roostAnimationState = new AnimationState();
    public final AnimationState preenAnimationState = new AnimationState();
    public Identifier texture = DEFAULT_TEXTURE;
    public boolean flying = false;
    public boolean roosting = false;
    public boolean preening = false;
}
