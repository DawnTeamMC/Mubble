package fr.hugman.mubble.client.model;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;

@Environment(EnvType.CLIENT)
public class MubbleModelLayers {
    private static final CubeDeformation OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);
    private static final CubeDeformation INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);

    public static final ArmorModelSet<ModelLayerLocation> PLAYER_POWERUP = registerArmorSet("player_powerup", () -> PlayerModel.createArmorMeshSet(INNER_ARMOR_DEFORMATION, OUTER_ARMOR_DEFORMATION)
            .map(mesh -> LayerDefinition.create(mesh, 64, 32)));

    public static final ModelLayerLocation BALL = register("ball", BallModel::getTexturedModelData);

    private static ModelLayerLocation register(String path, String layerName, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        var layer = layerLocation(path, layerName);
        ModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }
    private static ModelLayerLocation register(String path, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        return register(path, "main", provider);
    }

    private static ArmorModelSet<ModelLayerLocation> registerArmorSet(String path, ModelLayerRegistry.TexturedArmorModelSetProvider provider) {
        var armorModelSet = new ArmorModelSet<>(layerLocation(path, "helmet"), layerLocation(path, "chestplate"), layerLocation(path, "leggings"), layerLocation(path, "boots"));
        ModelLayerRegistry.registerArmorModelLayers(armorModelSet, provider);
        return armorModelSet;
    }

    private static ModelLayerLocation layerLocation(String path, String layerName) {
        return new ModelLayerLocation(Mubble.id(path), layerName);
    }

}