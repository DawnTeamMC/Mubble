package fr.hugman.mubble.client.render.entity.model;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class MubbleModelLayers {
    public static final EntityModelLayer GOOMBA = of("goomba", GoombaModel::getTexturedModelData);
    public static final EntityModelLayer INK_BULLET = of("ink_bullet", InkBulletEntityModel::getTexturedModelData);

    private static EntityModelLayer of(String path, String layerName, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        var layer = new EntityModelLayer(Mubble.id(path), layerName);
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }

    private static EntityModelLayer of(String path, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        return of(path, "main", provider);
    }
}
