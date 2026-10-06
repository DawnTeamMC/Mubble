package fr.hugman.mubble.client.renderer;

import fr.hugman.mubble.client.model.MubbleModelLayers;
import fr.hugman.mubble.client.renderer.entity.CollectibleEntityRenderer;
import fr.hugman.mubble.client.renderer.entity.layers.PowerUpHumanoidLayer;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

public class MubbleRenderers {
    public static void registerEntities() {
        EntityRenderers.register(MubbleEntityTypes.COLLECTIBLE, CollectibleEntityRenderer::new);
    }

    public static void registerLayers() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((_, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof AvatarRenderer<?> avatarEntityRenderer) {
                registrationHelper.register(new PowerUpHumanoidLayer<>(avatarEntityRenderer, ArmorModelSet.bake(MubbleModelLayers.PLAYER_POWERUP, context.getModelSet(), part -> new PlayerModel(part, false))));
            }
        });
    }
}
