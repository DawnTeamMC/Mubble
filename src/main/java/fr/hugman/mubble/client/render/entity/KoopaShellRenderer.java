package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.entity.KoopaShellEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class KoopaShellRenderer extends EntityRenderer<KoopaShellEntity> {
    public KoopaShellRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(KoopaShellEntity entity) {
        return null;
    }
}
