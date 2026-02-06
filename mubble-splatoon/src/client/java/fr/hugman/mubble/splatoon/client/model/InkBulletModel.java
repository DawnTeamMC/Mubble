package fr.hugman.mubble.splatoon.client.model;

import fr.hugman.mubble.splatoon.client.renderer.entity.state.ShooterInkBulletRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class InkBulletModel extends EntityModel<ShooterInkBulletRenderState>
{
    public static final String MAIN = "main";
    public static final float SIZE = 1;

    public InkBulletModel(ModelPart root) {
        super(root);
    }

    protected static MeshDefinition createBodyMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(MAIN, CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-SIZE / 2, -SIZE / 2, -SIZE / 2, SIZE, SIZE, SIZE), PartPose.ZERO);

        return mesh;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createBodyMesh(), 16, 16);
    }
}
