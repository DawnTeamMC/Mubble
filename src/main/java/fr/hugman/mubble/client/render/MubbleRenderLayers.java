package fr.hugman.mubble.client.render;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.render.model.GoombaEntityModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class MubbleRenderLayers {
    public static final EntityModelLayer GOOMBA = createModelLayer("goomba");
    
    public static void registerLayers() {
        EntityModelLayerRegistry.registerModelLayer(GOOMBA, GoombaEntityModel::getTexturedModelData);
    }

    private static EntityModelLayer createModelLayer(String name) {
        return new EntityModelLayer(Mubble.id(name), "main");
    }

    private static EntityModelLayer createModelLayer(String name, String layer) {
        return new EntityModelLayer(Mubble.id(name), layer);
    }
}
