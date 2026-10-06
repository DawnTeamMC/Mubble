package fr.hugman.mubble.super_mario.client.model;

import fr.hugman.mubble.super_mario.client.renderer.entity.state.CloudPlatformRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class CloudPlatformModel extends EntityModel<CloudPlatformRenderState> {
    public static final String MAIN = "main";

    public CloudPlatformModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(MAIN,
                CubeListBuilder.create().texOffs(0, 0).addBox(-32, 0, -32, 64, 16, 64, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        return LayerDefinition.create(modelData, 16, 16);
    }

    @Override
    public void setupAnim(CloudPlatformRenderState state) {
        super.setupAnim(state);
    }
}