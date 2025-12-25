package fr.hugman.mubble.client.model;

import fr.hugman.mubble.client.animation.definitions.GoombaAnimation;
import fr.hugman.mubble.client.renderer.entity.state.GoombaRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class GoombaModel extends EntityModel<GoombaRenderState> {
    public static final String LEFT_EYEBROW = "left_eyebrow";
    public static final String RIGHT_EYEBROW = "right_eyebrow";

	private final KeyframeAnimation walkingAnimation;
	private final KeyframeAnimation surprisedAnimation;
	private final KeyframeAnimation crushAnimation;


    public GoombaModel(ModelPart part) {
        super(part.getChild(PartNames.ROOT));
		this.walkingAnimation = GoombaAnimation.WALKING.bake(this.root);
		this.surprisedAnimation = GoombaAnimation.SURPRISE.bake(this.root);
		this.crushAnimation = GoombaAnimation.CRUSH.bake(this.root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition root = modelPartData.addOrReplaceChild(PartNames.ROOT, CubeListBuilder.create().texOffs(0, 22).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, 1.0F));
        root.addOrReplaceChild(PartNames.RIGHT_FOOT, CubeListBuilder.create().texOffs(0, 30).addBox(-2.5F, -1.0F, -4.5F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.5F, 1.0F));
        root.addOrReplaceChild(PartNames.LEFT_FOOT, CubeListBuilder.create().texOffs(16, 30).addBox(-0.5F, -1.0F, -4.5F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.5F, 1.0F));
        PartDefinition head = root.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 7).addBox(-5.0F, -6.0F, -4.0F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.0F, -7.0F, -3.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
        head.addOrReplaceChild("teeth_r1", CubeListBuilder.create().texOffs(0, 37).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition leftEyebrow = head.addOrReplaceChild(LEFT_EYEBROW, CubeListBuilder.create(), PartPose.offset(0.0F, -4.4962F, -4.2372F));
        leftEyebrow.addOrReplaceChild("left_eyebrow_r1", CubeListBuilder.create().texOffs(20, 22).addBox(0.0F, -3.4943F, 0.1307F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.088F, 0.1304F, 0.0115F));

        PartDefinition rightEyebrow = head.addOrReplaceChild(RIGHT_EYEBROW, CubeListBuilder.create(), PartPose.offset(0.0F, -4.4962F, -4.2372F));
        rightEyebrow.addOrReplaceChild("right_eyebrow_r1", CubeListBuilder.create().texOffs(20, 26).addBox(-6.0F, -3.4981F, 0.0436F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0038F, 0.0872F, 0.0876F, -0.0869F, -0.0076F));

        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(GoombaRenderState goombaRenderState) {
        super.setupAnim(goombaRenderState);
        this.walkingAnimation.applyWalk(goombaRenderState.walkAnimationPos, goombaRenderState.walkAnimationSpeed, 4.0F, 2.5F);

        this.surprisedAnimation.apply(goombaRenderState.surprisedAnimationState, goombaRenderState.ageInTicks);
        this.crushAnimation.apply(goombaRenderState.crushAnimationState, goombaRenderState.ageInTicks);
    }
}