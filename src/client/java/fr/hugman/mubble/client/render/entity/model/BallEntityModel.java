package fr.hugman.mubble.client.render.entity.model;

import fr.hugman.mubble.client.render.entity.state.BallRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class BallEntityModel extends EntityModel<BallRenderState> {
    public static final String MAIN = "main";
    public static final String TRAIL = "trail";

    public BallEntityModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(MAIN,
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        modelPartData.addOrReplaceChild(TRAIL,
                CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -2.0F, 2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        return LayerDefinition.create(modelData, 16, 16);
    }
}