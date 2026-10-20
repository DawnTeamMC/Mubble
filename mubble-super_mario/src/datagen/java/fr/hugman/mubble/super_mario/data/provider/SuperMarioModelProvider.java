package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.data.models.SuperMarioTextureMaps;
import fr.hugman.mubble.super_mario.data.models.SuperMarioTexturedModels;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.super_mario.world.level.block.BeepBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlockState;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.client.data.models.BlockModelGenerators.createBooleanModelDispatch;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class SuperMarioModelProvider extends FabricModelProvider {
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public SuperMarioModelProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output);
        this.registriesFuture = registriesFuture;
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        gen.createTrivialBlock(SuperMarioBlocks.EMPTY_BLOCK, TexturedModel.createDefault(block -> SuperMarioTextureMaps.all(SuperMario.id("bolted_block/normal_brown")), ModelTemplates.CUBE_ALL));
        gen.createTrivialBlock(SuperMarioBlocks.QUESTION_BLOCK, TexturedModel.createDefault(block -> SuperMarioTextureMaps.sideEnd(SuperMario.id("bolted_block/question_yellow"), SuperMario.id("bolted_block/normal_yellow")), ModelTemplates.CUBE_COLUMN));
        gen.createTrivialBlock(SuperMarioBlocks.BRICK_BLOCK, SuperMarioTexturedModels.brickBlock("brown"));
        gen.createTrivialBlock(SuperMarioBlocks.CRYSTAL_BLOCK, SuperMarioTexturedModels.brickBlock("amethyst"));
        gen.createTrivialBlock(SuperMarioBlocks.GOLD_BLOCK, SuperMarioTexturedModels.brickBlock("gold"));
        gen.createTrivialBlock(SuperMarioBlocks.BLUE_EXCLAMATION_BLOCK, SuperMarioTexturedModels.exclamationBlock("blue"));
        gen.createTrivialBlock(SuperMarioBlocks.GREEN_EXCLAMATION_BLOCK, SuperMarioTexturedModels.exclamationBlock("green"));
        gen.createTrivialBlock(SuperMarioBlocks.YELLOW_EXCLAMATION_BLOCK, SuperMarioTexturedModels.exclamationBlock("yellow"));
        gen.createTrivialBlock(SuperMarioBlocks.RED_EXCLAMATION_BLOCK, SuperMarioTexturedModels.exclamationBlock("red"));
        gen.createTrivialBlock(SuperMarioBlocks.NOTE_BLOCK, TexturedModel.createDefault(block -> SuperMarioTextureMaps.sideEnd(SuperMario.id("smooth_block/note_white"), SuperMario.id("smooth_block/straight_white")), ModelTemplates.CUBE_COLUMN));
        gen.createTrivialBlock(SuperMarioBlocks.BLUE_MARIMBA_BLOCK, SuperMarioTexturedModels.marimbaBlock("blue"));
        gen.createTrivialBlock(SuperMarioBlocks.GREEN_MARIMBA_BLOCK, SuperMarioTexturedModels.marimbaBlock("green"));
        gen.createTrivialBlock(SuperMarioBlocks.YELLOW_MARIMBA_BLOCK, SuperMarioTexturedModels.marimbaBlock("yellow"));
        gen.createTrivialBlock(SuperMarioBlocks.RED_MARIMBA_BLOCK, SuperMarioTexturedModels.marimbaBlock("red"));
        registerSnakeBlock(gen, SuperMarioBlocks.SNAKE_BLOCK, "normal");
        registerSnakeBlock(gen, SuperMarioBlocks.FAST_SNAKE_BLOCK, "fast");
        registerSnakeBlock(gen, SuperMarioBlocks.SLOW_SNAKE_BLOCK, "slow");
        registerSnakePath(gen);
        // Nothing of the moving block is ever drawn from its own state: its renderer submits the
        // snake block it is carrying instead. All it needs is a particle to break into.
        gen.createAirLikeBlock(SuperMarioBlocks.SNAKE_MOVING_BLOCK, new Material(SuperMario.id("block/snake_block/normal/side")));
        registerBeepBlock(gen, SuperMarioBlocks.RED_BEEP_BLOCK, "red");
        registerBeepBlock(gen, SuperMarioBlocks.BLUE_BEEP_BLOCK, "blue");

        gen.createTrivialBlock(SuperMarioBlocks.BLUE_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("blue"));
        gen.createTrivialBlock(SuperMarioBlocks.CYAN_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("cyan"));
        gen.createTrivialBlock(SuperMarioBlocks.GREEN_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("green"));
        gen.createTrivialBlock(SuperMarioBlocks.YELLOW_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("yellow"));
        gen.createTrivialBlock(SuperMarioBlocks.ORANGE_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("orange"));
        gen.createTrivialBlock(SuperMarioBlocks.RED_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("red"));
        gen.createTrivialBlock(SuperMarioBlocks.PINK_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("magenta"));
        gen.createTrivialBlock(SuperMarioBlocks.BLACK_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("black"));
        gen.createTrivialBlock(SuperMarioBlocks.WHITE_EGG_BLOCK, SuperMarioTexturedModels.eggBlock("white"));
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(SuperMarioItems.COIN, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.RED_COIN, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.BLUE_COIN, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.FLOWER_COIN, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(SuperMarioItems.GREEN_KOOPA_SHELL, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.RED_KOOPA_SHELL, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(SuperMarioItems.SUPER_MUSHROOM, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.MINI_MUSHROOM, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.MEGA_MUSHROOM, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.FIRE_FLOWER, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.ICE_FLOWER, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.GOLD_FLOWER, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.CLOUD_FLOWER, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.BUBBLE_FLOWER, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.SUPERBALL_FLOWER, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(SuperMarioItems.CAPE_FEATHER, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.SUPER_CAPE_FEATHER, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(SuperMarioItems.MAKER_GLOVE, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(SuperMarioItems.GOOMBA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SuperMarioItems.MINI_GOOMBA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
    }

    /**
     * A snake block never turns. Its shape comes from the blocks it is laid next to, not from a
     * facing, so showing one would only tell the player about a link they cannot change and do not
     * need to see. The one thing the model does say is whether the block has been knocked out.
     */
    private static void registerSnakeBlock(BlockModelGenerators gen, Block block, String variant) {
        MultiVariant normal = plainVariant(SuperMarioTexturedModels.snakeBlock(variant).create(block, gen.modelOutput));
        MultiVariant deactivated = plainVariant(SuperMarioTexturedModels.snakeBlock(variant, "deactivated_")
                .createWithSuffix(block, "_deactivated", gen.modelOutput));
        gen.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(SnakeBlock.SNAKE_STATE)
                        .select(SnakeBlockState.IDLE, normal)
                        .select(SnakeBlockState.MOVING, normal)
                        .select(SnakeBlockState.DEACTIVATED, deactivated)));
    }

    private static void registerSnakePath(BlockModelGenerators gen) {
        MultiVariant model = plainVariant(SuperMarioTexturedModels.snakeBlock("path").create(SuperMarioBlocks.SNAKE_PATH, gen.modelOutput));
        gen.blockStateOutput.accept(MultiVariantGenerator.dispatch(SuperMarioBlocks.SNAKE_PATH, model)
                .with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
    }

    private static void registerBeepBlock(BlockModelGenerators gen, Block block, String color) {
        MultiVariant normal = plainVariant(SuperMarioTexturedModels.beepBlock(color).create(block, gen.modelOutput));
        MultiVariant frame = plainVariant(SuperMario.id("block/beep_block/frame"));
        gen.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(BeepBlock.FRAME, frame, normal)));
    }

}
