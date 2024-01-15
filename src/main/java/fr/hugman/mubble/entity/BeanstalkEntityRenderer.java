package fr.hugman.mubble.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class BeanstalkEntityRenderer extends EntityRenderer<BeanstalkEntity> {

    public BeanstalkEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BeanstalkEntity entity) {
        return new Identifier("mubble", "textures/block/beanstalk.png");
    }
}
