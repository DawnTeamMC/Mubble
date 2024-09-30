package fr.hugman.mubble.client.render.entity.model;

import fr.hugman.mubble.client.render.entity.state.KoopaShellEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class KoopaShellModel extends EntityModel<KoopaShellEntityRenderState> {
    public KoopaShellModel(ModelPart part) {
        super(part.getChild(EntityModelPartNames.CUBE));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F), ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}