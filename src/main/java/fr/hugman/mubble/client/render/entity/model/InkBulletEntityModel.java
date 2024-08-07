package fr.hugman.mubble.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class InkBulletEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
    public static final String MAIN = "main";
    public static final float SIZE = 1;
    private final ModelPart root;

    public InkBulletEntityModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                MAIN,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-SIZE / 2, -SIZE / 2, -SIZE / 2, SIZE, SIZE, SIZE),
                ModelTransform.pivot(0F, 0F, 0F)
        );
        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
