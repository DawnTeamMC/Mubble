package fr.hugman.mubble.super_mario.data.models;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;

public class SuperMarioTexturedModels {
    public static TexturedModel.Provider brickBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.cube(SuperMario.id("block/brick_block_" + color)), net.minecraft.client.data.models.model.ModelTemplates.CUBE_ALL);
    }

    public static TexturedModel.Provider exclamationBlock(String color) {
        return TexturedModel.createDefault(block -> new TextureMapping()
                        .put(TextureSlot.END, SuperMario.id("block/dotted_block/normal_1_" + color))
                        .put(SuperMarioTextureKeys.SIDE_1, SuperMario.id("block/dotted_block/exclamation_1_" + color))
                        .put(SuperMarioTextureKeys.SIDE_2, SuperMario.id("block/dotted_block/exclamation_2_" + color)),
                SuperMarioModelTemplates.CUBE_COLUMN_ALTERNATING);
    }

    public static TexturedModel.Provider marimbaBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                SuperMario.id("block/smooth_block/marimba_" + color),
                SuperMario.id("block/smooth_block/wavy_" + color)
        ), net.minecraft.client.data.models.model.ModelTemplates.CUBE_COLUMN);
    }

    public static TexturedModel.Provider snakeBlock(String type) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                SuperMario.id("block/snake_block/" + type + "/side"),
                SuperMario.id("block/snake_block/" + type + "/end")
        ), net.minecraft.client.data.models.model.ModelTemplates.CUBE_COLUMN);
    }

    public static TexturedModel.Provider eggBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                SuperMario.id("block/egg_block/side_" + color),
                SuperMario.id("block/egg_block/end_" + color)
        ), SuperMarioModelTemplates.EGG_BLOCK);
    }

    public static TexturedModel.Provider beepBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.cube(SuperMario.id("block/beep_block/" + color)), net.minecraft.client.data.models.model.ModelTemplates.CUBE_ALL);

    }
}
