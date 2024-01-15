package fr.hugman.mubble.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @author MaxBrick
 * @since v4.0.0
 */

//I have no idea what I'm doing!
public class BeanstalkEntityModel extends EntityModel<BeanstalkEntity>{
    private final ModelPart base;

    public BeanstalkEntityModel(ModelPart modelPart) {
        this.base = modelPart.getChild(EntityModelPartNames.CUBE);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(
                        0, 0
                ).cuboid(
                        -8F, 8F, -8F, 16F, 16F, 16F
                ), ModelTransform.pivot(0F, 0F, 0F)
        );
        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void setAngles(BeanstalkEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.base).forEach((modelRenderer) -> {
            modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }
}
