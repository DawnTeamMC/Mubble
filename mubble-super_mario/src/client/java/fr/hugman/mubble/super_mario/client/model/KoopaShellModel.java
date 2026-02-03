package fr.hugman.mubble.super_mario.client.model;

import fr.hugman.mubble.super_mario.client.renderer.entity.state.KoopaShellRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class KoopaShellModel extends EntityModel<KoopaShellRenderState> {
    public KoopaShellModel(ModelPart part) {
        super(part.getChild(PartNames.CUBE));
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(PartNames.CUBE,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -3.25F, -5.0F, 10.0F, 7.0F, 10.0F)
                        .texOffs(0, 17)
                        .addBox(-6.0F, -1.25F, -6.0F, 12.0F, 2.0F, 12.0F),
                PartPose.offset(0.0F, 20.25F, 0.0F)
        );
        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void setupAnim(KoopaShellRenderState koopaShellRenderState) {
        super.setupAnim(koopaShellRenderState);
        this.root.yRot = koopaShellRenderState.horizontalRotation;
    }
}