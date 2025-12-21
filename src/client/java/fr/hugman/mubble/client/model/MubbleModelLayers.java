package fr.hugman.mubble.client.model;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class MubbleModelLayers {
    public static final ModelLayerLocation GOOMBA = of("goomba", GoombaModel::getTexturedModelData);
    public static final ModelLayerLocation BALL = of("ball", BallModel::getTexturedModelData);
    public static final ModelLayerLocation KOOPA_SHELL = of("koopa_shell", KoopaShellModel::getTexturedModelData);

    private static ModelLayerLocation of(String path, String layerName, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        var layer = new ModelLayerLocation(Mubble.id(path), layerName);
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }

    private static ModelLayerLocation of(String path, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        return of(path, "main", provider);
    }
}