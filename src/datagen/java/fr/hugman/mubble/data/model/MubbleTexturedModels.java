package fr.hugman.mubble.data.model;

import fr.hugman.mubble.Mubble;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.TexturedModel;

public class MubbleTexturedModels {
    public static TexturedModel.Factory brickBlock(String color) {
        return TexturedModel.makeFactory(block -> TextureMap.all(Mubble.id("block/brick_block_" + color)), Models.CUBE_ALL);
    }

    public static TexturedModel.Factory exclamationBlock(String color) {
        return TexturedModel.makeFactory(block -> new TextureMap()
                        .put(TextureKey.END, Mubble.id("block/dotted_block/normal_1_" + color))
                        .put(MubbleTextureKeys.SIDE_1, Mubble.id("block/dotted_block/exclamation_1_" + color))
                        .put(MubbleTextureKeys.SIDE_2, Mubble.id("block/dotted_block/exclamation_2_" + color)),
                MubbleModels.CUBE_COLUMN_ALTERNATING);
    }

    public static TexturedModel.Factory marimbaBlock(String color) {
        return TexturedModel.makeFactory(block -> TextureMap.sideEnd(
                Mubble.id("block/smooth_block/marimba_" + color),
                Mubble.id("block/smooth_block/wavy_" + color)
        ), Models.CUBE_COLUMN);
    }

    public static TexturedModel.Factory snakeBlock(String type) {
        return TexturedModel.makeFactory(block -> TextureMap.sideEnd(
                Mubble.id("block/snake_block/" + type + "/side"),
                Mubble.id("block/snake_block/" + type + "/end")
        ), Models.CUBE_COLUMN);
    }

    public static TexturedModel.Factory eggBlock(String color) {
        return TexturedModel.makeFactory(block -> TextureMap.sideEnd(
                Mubble.id("block/egg_block/side_" + color),
                Mubble.id("block/egg_block/end_" + color)
        ), MubbleModels.EGG_BLOCK);
    }

    public static TexturedModel.Factory beepBlock(String color) {
        return TexturedModel.makeFactory(block -> TextureMap.all(Mubble.id("block/beep_block/" + color)), Models.CUBE_ALL);

    }
}
