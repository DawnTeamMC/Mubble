package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.QuestionBlock;
import fr.hugman.mubble.block.SnakeBlock;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class SuperMarioContent {
	public static final QuestionBlock QUESTION_BLOCK = new QuestionBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW).item());
	public static final Block EMPTY_BLOCK = new Block(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN).item());
	public static final Block BRICK_BLOCK = new Block(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).item());
	public static final Block GOLD_BLOCK = new Block(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).item());
	public static final SnakeBlock SNAKE_BLOCK = new SnakeBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME).item());

	public static void init() {
		Registrar.add(Mubble.id("question_block"), QUESTION_BLOCK);
		Registrar.add(Mubble.id("empty_block"), EMPTY_BLOCK);
		Registrar.add(Mubble.id("brick_block"),BRICK_BLOCK);
		Registrar.add(Mubble.id("gold_block"),GOLD_BLOCK);
		Registrar.add(Mubble.id("snake_block"),SNAKE_BLOCK);
	}

	public static final ItemGroup GROUP = FabricItemGroup.builder(Mubble.id("super_mario"))
			.displayName(Text.translatable("item_group.mubble.super_mario"))
			.icon(() -> new ItemStack(SuperMarioContent.QUESTION_BLOCK))
			.entries((enabledFeatures, entries, operatorEnabled) -> {
				entries.add(SuperMarioContent.QUESTION_BLOCK);
				entries.add(SuperMarioContent.EMPTY_BLOCK);
				entries.add(SuperMarioContent.BRICK_BLOCK);
				entries.add(SuperMarioContent.GOLD_BLOCK);
				entries.add(SuperMarioContent.SNAKE_BLOCK);

			})
			.build();
}
