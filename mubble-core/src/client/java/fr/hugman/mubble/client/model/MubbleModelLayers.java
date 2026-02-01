package fr.hugman.mubble.client.model;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class MubbleModelLayers {
    public static final ModelLayerLocation BALL = register("ball", BallModel::getTexturedModelData);

    private static ModelLayerLocation register(String path, String layerName, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        var layer = new ModelLayerLocation(Mubble.id(path), layerName);
        ModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }

    private static ModelLayerLocation register(String path, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        return register(path, "main", provider);
    }
}