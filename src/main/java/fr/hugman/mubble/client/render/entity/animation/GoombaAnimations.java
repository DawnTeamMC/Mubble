package fr.hugman.mubble.client.render.entity.animation;

import fr.hugman.mubble.client.render.entity.model.GoombaModel;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class GoombaAnimations {
    public static final Animation WALKING = Animation.Builder.create(1.0F).looping()
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 10.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 0.9F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createScalingVector(1.0F, 0.9F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.625F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.9167F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createScalingVector(1.0F, 0.9F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.22F, 3.2F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.0833F, AnimationHelper.createRotationalVector(-2.5F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(45.0F, 20.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.875F, AnimationHelper.createRotationalVector(-20.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createRotationalVector(-10.22F, 3.2F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.05F, -2.72F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.25F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, -1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, -0.05F, -2.72F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 0.8128F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.0833F, AnimationHelper.createScalingVector(1.0F, 0.7F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createScalingVector(1.0F, 0.7F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.3333F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.875F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createScalingVector(1.0F, 0.8128F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(45.0F, -20.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createRotationalVector(-20.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-2.5F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createRotationalVector(45.0F, -20.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, -1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.25F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5833F, AnimationHelper.createScalingVector(1.0F, 0.7F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 0.7F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.8333F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(-2.5F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(-2.5F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.0F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .build();

    public static final Animation SURPRISE = Animation.Builder.create(0.5F)
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.0833F, AnimationHelper.createTranslationalVector(0.0F, 4.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 4.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createScalingVector(1.0F, 1.2F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createScalingVector(1.0F, 0.8F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createRotationalVector(-35.0F, 20.0F, -15.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-35.0F, 20.0F, -15.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createRotationalVector(17.5F, 20.0F, -15.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createRotationalVector(-35.0F, -20.0F, 15.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-35.0F, -20.0F, 15.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createRotationalVector(17.5F, -20.0F, 15.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125F, AnimationHelper.createScalingVector(1.1F, 1.1F, 1.1F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2917F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(GoombaModel.RIGHT_EYEBROW, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 10.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 10.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(GoombaModel.RIGHT_EYEBROW, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createTranslationalVector(-0.5F, 1.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(GoombaModel.LEFT_EYEBROW, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .addBoneAnimation(GoombaModel.LEFT_EYEBROW, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.5F, 1.0F, 0.0F), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
            ))
            .build();

    public static final Animation CRUSH = Animation.Builder.create(1f)
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(0f, -2f, 0f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(EntityModelPartNames.ROOT, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.08343333f, AnimationHelper.createScalingVector(1f, 0.4f, 1f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1f, 0.3f, 1f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createTranslationalVector(0f, 0f, -5f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(0f, 0f, -4f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(EntityModelPartNames.RIGHT_FOOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createRotationalVector(-55.69f, 22.92f, -15.63f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(-32.16f, 52.32f, -33.72f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createTranslationalVector(0f, 0f, -5f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(0f, 0f, -4f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(EntityModelPartNames.LEFT_FOOT, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createRotationalVector(-55.69f, -22.92f, 15.63f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(-32.16f, -52.32f, 33.72f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.20834334f, AnimationHelper.createRotationalVector(7.5f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(15f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.375f, AnimationHelper.createRotationalVector(7.5f, 0f, 0f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(GoombaModel.LEFT_EYEBROW, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.041676664f, AnimationHelper.createTranslationalVector(2f, 6.75f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.08343333f, AnimationHelper.createTranslationalVector(2f, 12f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createTranslationalVector(1.86f, 9.93f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.20834334f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(GoombaModel.LEFT_EYEBROW, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(0f, 0f, -20f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 0f, -20f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, 0f, 12.5f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(GoombaModel.LEFT_EYEBROW, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.16766666f, AnimationHelper.createScalingVector(1f, 2.3f, 1f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1f, 1f, 1f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(GoombaModel.RIGHT_EYEBROW, new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.041676664f, AnimationHelper.createTranslationalVector(-2f, 6.75f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.08343333f, AnimationHelper.createTranslationalVector(-2f, 12f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createTranslationalVector(-1.86f, 9.93f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.20834334f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(GoombaModel.RIGHT_EYEBROW, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(0f, 0f, 20f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 0f, 20f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, 0f, -12.5f), Transformation.Interpolations.CUBIC)))
            .addBoneAnimation(GoombaModel.RIGHT_EYEBROW, new Transformation(Transformation.Targets.SCALE,
                    new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.16766666f, AnimationHelper.createScalingVector(1f, 2.3f, 1f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1f, 1f, 1f), Transformation.Interpolations.CUBIC))).build();
}
