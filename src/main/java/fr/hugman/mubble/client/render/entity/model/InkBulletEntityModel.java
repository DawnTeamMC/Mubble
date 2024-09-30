package fr.hugman.mubble.client.render.entity.model;

import fr.hugman.mubble.client.render.entity.state.ShooterInkBulletRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;

@Environment(EnvType.CLIENT)
public class InkBulletEntityModel extends EntityModel<ShooterInkBulletRenderState> {
    public static final String MAIN = "main";
    public static final float SIZE = 1;

    public InkBulletEntityModel(ModelPart root) {
        super(root);
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
}
