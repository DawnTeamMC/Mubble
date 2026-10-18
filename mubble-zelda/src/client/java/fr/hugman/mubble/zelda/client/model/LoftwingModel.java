package fr.hugman.mubble.zelda.client.model;

import fr.hugman.mubble.zelda.client.renderer.entity.state.LoftwingRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * TODO: placeholder geometry. The body is one box the size of a standing loftwing, so that the
 * entity loads, renders and can be positioned in the world. Replace the mesh with the real bird
 * (body, head, beak, two wings, two legs, tail) and animate it from the states below.
 */
@Environment(EnvType.CLIENT)
public class LoftwingModel extends EntityModel<LoftwingRenderState> {
	public LoftwingModel(ModelPart part) {
		super(part.getChild(PartNames.ROOT));
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition parts = mesh.getRoot();

		// TODO: a single 24x40x24 box standing on the ground, waiting for the real model.
		parts.addOrReplaceChild(
				PartNames.ROOT,
				CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -40.0F, -12.0F, 24.0F, 40.0F, 24.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F)
		);

		return LayerDefinition.create(mesh, 128, 128);
	}
}
