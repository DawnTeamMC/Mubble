package fr.hugman.mubble.data.model;

import fr.hugman.mubble.Mubble;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;

public class MubbleTexturedModels {
    public static TexturedModel.Provider brickBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.cube(Mubble.id("block/brick_block_" + color)), ModelTemplates.CUBE_ALL);
    }

    public static TexturedModel.Provider exclamationBlock(String color) {
        return TexturedModel.createDefault(block -> new TextureMapping()
                        .put(TextureSlot.END, Mubble.id("block/dotted_block/normal_1_" + color))
                        .put(MubbleTextureKeys.SIDE_1, Mubble.id("block/dotted_block/exclamation_1_" + color))
                        .put(MubbleTextureKeys.SIDE_2, Mubble.id("block/dotted_block/exclamation_2_" + color)),
                MubbleModels.CUBE_COLUMN_ALTERNATING);
    }

    public static TexturedModel.Provider marimbaBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                Mubble.id("block/smooth_block/marimba_" + color),
                Mubble.id("block/smooth_block/wavy_" + color)
        ), ModelTemplates.CUBE_COLUMN);
    }

    public static TexturedModel.Provider snakeBlock(String type) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                Mubble.id("block/snake_block/" + type + "/side"),
                Mubble.id("block/snake_block/" + type + "/end")
        ), ModelTemplates.CUBE_COLUMN);
    }

    public static TexturedModel.Provider eggBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.column(
                Mubble.id("block/egg_block/side_" + color),
                Mubble.id("block/egg_block/end_" + color)
        ), MubbleModels.EGG_BLOCK);
    }

    public static TexturedModel.Provider beepBlock(String color) {
        return TexturedModel.createDefault(block -> TextureMapping.cube(Mubble.id("block/beep_block/" + color)), ModelTemplates.CUBE_ALL);

    }
}
