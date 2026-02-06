package fr.hugman.mubble.splatoon.client.model;

import fr.hugman.mubble.splatoon.Splatoon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class SplatoonModelLayers {
    public static final ModelLayerLocation INK_BULLET = register("ink_bullet", InkBulletModel::createBodyLayer);

    private static ModelLayerLocation register(String path, String layerName, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        var layer = new ModelLayerLocation(Splatoon.id(path), layerName);
        ModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }

    private static ModelLayerLocation register(String path, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        return register(path, "main", provider);
    }
}