package fr.hugman.mubble.super_mario.data.models;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.model.Material;

public class SuperMarioTexturedModels {
    public static TexturedModel.Provider brickBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.cube(new Material(SuperMario.id("block/brick_block_" + color))), ModelTemplates.CUBE_ALL);
    }

    public static TexturedModel.Provider exclamationBlock(String color) {
        return TexturedModel.createDefault(block -> new TextureMapping()
                        .put(TextureSlot.END, new Material(SuperMario.id("block/dotted_block/normal_1_" + color)))
                        .put(SuperMarioTextureKeys.SIDE_1, new Material(SuperMario.id("block/dotted_block/exclamation_1_" + color)))
                        .put(SuperMarioTextureKeys.SIDE_2, new Material(SuperMario.id("block/dotted_block/exclamation_2_" + color))),
                SuperMarioModelTemplates.CUBE_COLUMN_ALTERNATING);
    }

    public static TexturedModel.Provider marimbaBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                new Material(SuperMario.id("block/smooth_block/marimba_" + color)),
                new Material(SuperMario.id("block/smooth_block/wavy_" + color))
        ), ModelTemplates.CUBE_COLUMN);
    }

    public static TexturedModel.Provider snakeBlock(String type) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                new Material(SuperMario.id("block/snake_block/" + type + "/side")),
                new Material(SuperMario.id("block/snake_block/" + type + "/end"))
        ), ModelTemplates.CUBE_COLUMN);
    }

    public static TexturedModel.Provider eggBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                new Material(SuperMario.id("block/egg_block/side_" + color)),
                new Material(SuperMario.id("block/egg_block/end_" + color))
        ), SuperMarioModelTemplates.EGG_BLOCK);
    }

    public static TexturedModel.Provider beepBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.cube(new Material(SuperMario.id("block/beep_block/" + color))), ModelTemplates.CUBE_ALL);

    }
}
