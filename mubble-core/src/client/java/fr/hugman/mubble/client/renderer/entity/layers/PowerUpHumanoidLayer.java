package fr.hugman.mubble.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.client.references.MubbleRenderStateDataKeys;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class PowerUpHumanoidLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends RenderLayer<S, M> {
    private final ArmorModelSet<A> modelSet;
    private final ArmorModelSet<A> babyModelSet;

    public PowerUpHumanoidLayer(final RenderLayerParent<S, M> renderer, final ArmorModelSet<A> modelSet) {
        this(renderer, modelSet, modelSet);
    }

    public PowerUpHumanoidLayer(final RenderLayerParent<S, M> renderer, final ArmorModelSet<A> modelSet, final ArmorModelSet<A> babyModelSet) {
        super(renderer);
        this.modelSet = modelSet;
        this.babyModelSet = babyModelSet;
    }

    public ArmorModelSet<A> getModelSet() {
        return modelSet;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        var powerUp = state.getData(MubbleRenderStateDataKeys.POWER_UP);
        if (powerUp == null) {
            return;
        }
        var texture = powerUp.value().cosmectics().humanoidOverlayAssetId().map(id -> id.withPath(s -> "textures/" + s + ".png"));
        if(texture.isEmpty()) {
            return;
        }
        this.renderPowerUp(poseStack, submitNodeCollector, EquipmentSlot.HEAD, lightCoords, state, texture.get());
        this.renderPowerUp(poseStack, submitNodeCollector, EquipmentSlot.CHEST, lightCoords, state, texture.get());
        this.renderPowerUp(poseStack, submitNodeCollector, EquipmentSlot.LEGS, lightCoords, state, texture.get());
        this.renderPowerUp(poseStack, submitNodeCollector, EquipmentSlot.FEET, lightCoords, state, texture.get());
    }

    private A getPowerUpModel(final S state, final EquipmentSlot slot) {
        return (state.isBaby ? this.babyModelSet : this.modelSet).get(slot);
    }

    private void renderPowerUp(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EquipmentSlot slot, int lightCoords, S state, Identifier texture) {
        A model = this.getPowerUpModel(state, slot);
        submitNodeCollector.submitModel(model, state, poseStack, RenderTypes.armorCutoutNoCull(texture), lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}
