package fr.hugman.mubble.entity;

import fr.hugman.mubble.MubbleClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BeanstalkEntityRenderer extends MobEntityRenderer<BeanstalkEntity, BeanstalkEntityModel> {

    public BeanstalkEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BeanstalkEntityModel(context.getPart(MubbleClient.MODEL_CUBE_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(BeanstalkEntity entity) {
        return new Identifier("mubble", "textures/block/beanstalk.png");
    }
}
