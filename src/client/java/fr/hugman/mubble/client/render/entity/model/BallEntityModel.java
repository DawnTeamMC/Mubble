package fr.hugman.mubble.client.render.entity.model;

import fr.hugman.mubble.client.render.entity.state.BallRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;

@Environment(EnvType.CLIENT)
public class BallEntityModel extends EntityModel<BallRenderState> {
    public static final String MAIN = "main";
    public static final String TRAIL = "trail";

    public BallEntityModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(MAIN,
                ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
                ModelTransform.NONE
        );
        modelPartData.addChild(TRAIL,
                ModelPartBuilder.create().uv(0, 8).cuboid(-2.0F, -2.0F, 2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
                ModelTransform.NONE
        );
        return TexturedModelData.of(modelData, 16, 16);
    }
}