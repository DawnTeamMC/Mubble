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
        modelPartData.addChild(EntityModelPartNames.CUBE,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-5.0F, -3.25F, -5.0F, 10.0F, 7.0F, 10.0F)
                        .uv(0, 17)
                        .cuboid(-6.0F, -1.25F, -6.0F, 12.0F, 2.0F, 12.0F),
                ModelTransform.pivot(0.0F, 20.25F, 0.0F)
        );
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void setAngles(KoopaShellEntityRenderState state) {
        super.setAngles(state);
        this.root.yaw = state.horizontalRotation;
    }
}