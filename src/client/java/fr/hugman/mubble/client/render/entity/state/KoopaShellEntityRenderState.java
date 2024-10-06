package fr.hugman.mubble.client.render.entity.state;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class KoopaShellEntityRenderState extends EntityRenderState {
    private static final Identifier DEFAULT_TEXTURE = Mubble.id("textures/entity/koopa_shell/green.png");

    public Identifier texture = DEFAULT_TEXTURE;
    public boolean invisibleToPlayer;
    public boolean hasOutline;
    public float horizontalRotation;
}
