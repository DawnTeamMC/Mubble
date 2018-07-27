package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Main;
import hugman.mod.objects.blocks.BlockBase;
import hugman.mod.objects.blocks.BlockBrick;
import hugman.mod.objects.blocks.BlockNote;
import hugman.mod.objects.blocks.BlockOre;
import hugman.mod.objects.blocks.BlockQuestion;
import hugman.mod.objects.blocks.BlockRotating;
import hugman.mod.objects.blocks.BlockTetris;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockInit
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	// Vanilla
	public static final Block VANADIUM_BLOCK = new BlockBase("vanadium_block", CreativeTabs.BUILDING_BLOCKS, Material.IRON, 5f, 30f, SoundType.METAL, 0);
	public static final Block VANADIUM_ORE = new BlockOre("vanadium_ore");
	public static final Block RED_BRICKS = new BlockBase("red_bricks", CreativeTabs.BUILDING_BLOCKS, Material.ROCK, 2f, 30f, SoundType.STONE, 0);
	public static final Block WHITE_BRICKS = new BlockBase("white_bricks", CreativeTabs.BUILDING_BLOCKS, Material.ROCK, 2f, 30f, SoundType.STONE, 0);
	public static final Block BLUNITE = new BlockBase("blunite", CreativeTabs.BUILDING_BLOCKS, Material.ROCK, 1.5f, 30f, SoundType.STONE, 0);
	public static final Block POLISHED_BLUNITE = new BlockBase("polished_blunite", CreativeTabs.BUILDING_BLOCKS, Material.ROCK, 1.5f, 30f, SoundType.STONE, 0);
	public static final Block CARBONITE = new BlockBase("carbonite", CreativeTabs.BUILDING_BLOCKS, Material.ROCK, 1.5f, 30f, SoundType.STONE, 0);
	public static final Block POLISHED_CARBONITE = new BlockBase("polished_carbonite", CreativeTabs.BUILDING_BLOCKS, Material.ROCK, 1.5f, 30f, SoundType.STONE, 0);
	
	// Extra
	public static final Block FOOTBLOCK = new BlockBase("footblock", CreativeTabs.DECORATIONS, Material.CLOTH, 0.8f, 4f, SoundType.CLOTH, 0);
	
	// Mario
	public static final Block QUESTION_BLOCK = new BlockQuestion();
	public static final Block EMPTY_BLOCK = new BlockBase("empty_block", Main.NINTENDO_BLOCKS, Material.IRON, 1.5f, 30f, SoundType.METAL, 0);
	public static final Block ROTATING_BLOCK = new BlockRotating();
	public static final Block LIGHT_BLOCK = new BlockBase("light_block", Main.NINTENDO_BLOCKS, Material.ROCK, 1.5f, 30f, SoundType.STONE, 15);
	public static final Block BRICK_BLOCK = new BlockBrick("normal");
	public static final Block GOLDEN_BRICK_BLOCK = new BlockBrick("golden");
	public static final Block NOTE_BLOCK = new BlockNote("normal");
	public static final Block SUPER_NOTE_BLOCK = new BlockNote("super");
	
	// Tetris
	public static final Block BLUE_TETRIS_BLOCK = new BlockTetris("blue");
	public static final Block GREEN_TETRIS_BLOCK = new BlockTetris("green");
	public static final Block YELLOW_TETRIS_BLOCK = new BlockTetris("yellow");
	public static final Block ORANGE_TETRIS_BLOCK = new BlockTetris("orange");
	public static final Block RED_TETRIS_BLOCK = new BlockTetris("red");
	public static final Block PURPLE_TETRIS_BLOCK = new BlockTetris("purple");
	public static final Block GRAY_TETRIS_BLOCK = new BlockTetris("gray");
}
