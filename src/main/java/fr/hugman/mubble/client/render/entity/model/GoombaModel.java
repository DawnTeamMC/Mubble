package fr.hugman.mubble.client.render.entity.model;

import fr.hugman.mubble.client.render.entity.animation.GoombaAnimations;
import fr.hugman.mubble.client.render.entity.state.GoombaEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class GoombaModel extends EntityModel<GoombaEntityRenderState> {
    public static final String LEFT_EYEBROW = "left_eyebrow";
    public static final String RIGHT_EYEBROW = "right_eyebrow";

    public GoombaModel(ModelPart part) {
        super(part.getChild(EntityModelPartNames.ROOT));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData root = modelPartData.addChild(EntityModelPartNames.ROOT, ModelPartBuilder.create().uv(0, 22).cuboid(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.5F, 1.0F));
        root.addChild(EntityModelPartNames.RIGHT_FOOT, ModelPartBuilder.create().uv(0, 30).cuboid(-2.5F, -1.0F, -4.5F, 3.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 1.5F, 1.0F));
        root.addChild(EntityModelPartNames.LEFT_FOOT, ModelPartBuilder.create().uv(16, 30).cuboid(-0.5F, -1.0F, -4.5F, 3.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 1.5F, 1.0F));
        ModelPartData head = root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 7).cuboid(-5.0F, -6.0F, -4.0F, 10.0F, 7.0F, 8.0F, new Dilation(0.0F)).uv(0, 0).cuboid(-4.0F, -7.0F, -3.0F, 8.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));
        head.addChild("teeth_r1", ModelPartBuilder.create().uv(0, 37).cuboid(-3.0F, -2.0F, 0.0F, 6.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -4.0F, 0.3927F, 0.0F, 0.0F));

        ModelPartData leftEyebrow = head.addChild(LEFT_EYEBROW, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.4962F, -4.2372F));
        leftEyebrow.addChild("left_eyebrow_r1", ModelPartBuilder.create().uv(20, 22).cuboid(0.0F, -3.4943F, 0.1307F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.088F, 0.1304F, 0.0115F));

        ModelPartData rightEyebrow = head.addChild(RIGHT_EYEBROW, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.4962F, -4.2372F));
        rightEyebrow.addChild("right_eyebrow_r1", ModelPartBuilder.create().uv(20, 26).cuboid(-6.0F, -3.4981F, 0.0436F, 6.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.0038F, 0.0872F, 0.0876F, -0.0869F, -0.0076F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(GoombaEntityRenderState renderState) {
        super.setAngles(renderState);
        this.animateWalking(GoombaAnimations.WALKING, renderState.limbFrequency, renderState.limbAmplitudeMultiplier, 4.0F, 2.5F);
        this.animate(renderState.surprisedAnimationState, GoombaAnimations.SURPRISE, renderState.age);
        this.animate(renderState.crushAnimationState, GoombaAnimations.CRUSH, renderState.age);
    }
}