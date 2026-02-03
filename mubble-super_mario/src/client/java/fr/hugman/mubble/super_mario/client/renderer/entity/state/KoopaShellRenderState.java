package fr.hugman.mubble.super_mario.client.renderer.entity.state;

import fr.hugman.mubble.super_mario.SuperMario;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class KoopaShellRenderState extends EntityRenderState {
    private static final Identifier DEFAULT_TEXTURE = SuperMario.id("textures/entity/koopa_shell/green.png");

    public Identifier texture = DEFAULT_TEXTURE;
    public boolean invisibleToPlayer;
    public boolean hasOutline;
    public float horizontalRotation = 0.0f;
}
