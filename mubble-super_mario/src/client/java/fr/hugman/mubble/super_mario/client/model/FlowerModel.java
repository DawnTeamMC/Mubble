package fr.hugman.mubble.super_mario.client.model;

import fr.hugman.mubble.super_mario.client.renderer.entity.state.FlowerRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/**
 * The huge flower grown by the Super Flower Pot: a bloom sitting on a stem, with a leaf on either side.
 * <p>
 * Everything is authored right side up, since the renderer behind it draws entities straight rather than
 * flipping them the way the humanoid ones are. It is kept a little inside the 2×2 blocks the entity is
 * worth, so that the flower never pokes out of its own hitbox.
 */
@Environment(EnvType.CLIENT)
public class FlowerModel extends EntityModel<FlowerRenderState> {
    public static final String STEM = "stem";
    public static final String BLOOM = "bloom";
    public static final String LOWER_LEAF = "lower_leaf";
    public static final String UPPER_LEAF = "upper_leaf";

    public static final int TEXTURE_WIDTH = 256;
    public static final int TEXTURE_HEIGHT = 64;

    public FlowerModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        var modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(STEM,
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4, 24, 4, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        modelPartData.addOrReplaceChild(BLOOM,
                CubeListBuilder.create().texOffs(32, 0).addBox(-14.0F, 24.0F, -14.0F, 28, 6, 28, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        modelPartData.addOrReplaceChild(LOWER_LEAF,
                CubeListBuilder.create().texOffs(0, 32).addBox(-12.0F, 6.0F, -1.0F, 10, 2, 2, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        modelPartData.addOrReplaceChild(UPPER_LEAF,
                CubeListBuilder.create().texOffs(0, 40).addBox(2.0F, 13.0F, -1.0F, 10, 2, 2, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        return LayerDefinition.create(modelData, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
