package fr.hugman.mubble.client.render.entity.model;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class MubbleEntityModelLayers {
    public static final EntityModelLayer INK_BULLET = of("ink_bullet", InkBulletEntityModel.MAIN, InkBulletEntityModel::getTexturedModelData);

    private static EntityModelLayer of(String path, String name, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        var layer = new EntityModelLayer(Mubble.id(path), name);
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }
}
