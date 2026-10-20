package fr.hugman.mubble.super_mario.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.model.GoombaModel;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.entity.ClingingMiniGoombas;
import fr.hugman.mubble.super_mario.client.renderer.entity.ClingingMiniGoombas.Side;
import fr.hugman.mubble.super_mario.client.renderer.entity.ClingingMiniGoombas.Spot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.PlayerModelType;

import java.util.List;

/**
 * Draws the mini goombas hanging off a player, gnawing at them.
 * <p>
 * Each one is given a spot from a fixed list, the first ones spread over the head, the torso, the arms
 * and the legs so that even a couple of them do not bunch up. Every spot is held by a body part, which
 * keeps them stuck to that part through the walk swing, crouching and looking around.
 */
@Environment(EnvType.CLIENT)
public class ClingingMiniGoombaLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    /**
     * Where they hold on, in the order they take the spots. Coordinates are in the body part's own model
     * pixels, on the surface of the part, for the wide arm model; the arm spots move in for slim arms.
     */
    private static final List<LimbSpot> SPOTS = List.of(
            new LimbSpot(Limb.BODY, Spot.side(1.5F, 4.0F, -2.0F, 0.0F, Side.FRONT)),
            new LimbSpot(Limb.HEAD, Spot.top(0.5F, -8.0F, 0.0F)),
            new LimbSpot(Limb.LEFT_ARM, Spot.side(3.0F, 4.0F, 0.0F, -1.0F, Side.LEFT)),
            new LimbSpot(Limb.RIGHT_LEG, Spot.side(0.0F, 5.0F, -2.0F, 0.0F, Side.FRONT)),
            new LimbSpot(Limb.BODY, Spot.side(-1.5F, 6.0F, 2.0F, 0.0F, Side.BACK)),
            new LimbSpot(Limb.RIGHT_ARM, Spot.side(-3.0F, 6.0F, 0.0F, 1.0F, Side.RIGHT)),
            new LimbSpot(Limb.LEFT_LEG, Spot.side(2.0F, 9.5F, 0.0F, 0.0F, Side.LEFT)),
            new LimbSpot(Limb.HEAD, Spot.side(-4.0F, -3.5F, 0.5F, 0.0F, Side.RIGHT)),
            new LimbSpot(Limb.RIGHT_LEG, Spot.side(0.0F, 10.0F, 2.0F, 0.0F, Side.BACK)),
            new LimbSpot(Limb.HEAD, Spot.side(1.5F, -2.5F, -4.0F, 0.0F, Side.FRONT)),
            new LimbSpot(Limb.LEFT_ARM, Spot.side(1.0F, 7.0F, -2.0F, -0.5F, Side.FRONT)),
            new LimbSpot(Limb.LEFT_LEG, Spot.side(0.0F, 10.5F, -2.0F, 0.0F, Side.FRONT))
    );

    private final GoombaModel model;

    public ClingingMiniGoombaLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new GoombaModel(modelSet.bakeLayer(SuperMarioModelLayers.GOOMBA));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        Integer count = state.getData(SuperMarioRenderStateDataKeys.CLINGING_MINI_GOOMBAS);
        if (count == null || count <= 0) {
            return;
        }
        PlayerModel parent = this.getParentModel();
        boolean slim = state.skin != null && state.skin.model() == PlayerModelType.SLIM;
        for (int i = 0; i < count; i++) {
            LimbSpot spot = SPOTS.get(i % SPOTS.size());
            poseStack.pushPose();
            parent.root().translateAndRotate(poseStack);
            spot.limb().of(parent).translateAndRotate(poseStack);
            ClingingMiniGoombas.submitOnSurface(this.model, poseStack, submitNodeCollector, lightCoords, state.outlineColor, state.ageInTicks, spot.spot(), slim, i);
            poseStack.popPose();
        }
    }

    private enum Limb {
        HEAD, BODY, RIGHT_ARM, LEFT_ARM, RIGHT_LEG, LEFT_LEG;

        ModelPart of(PlayerModel model) {
            return switch (this) {
                case HEAD -> model.head;
                case BODY -> model.body;
                case RIGHT_ARM -> model.rightArm;
                case LEFT_ARM -> model.leftArm;
                case RIGHT_LEG -> model.rightLeg;
                case LEFT_LEG -> model.leftLeg;
            };
        }
    }

    private record LimbSpot(Limb limb, Spot spot) {
    }
}
