package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Main;
import hugman.mod.objects.blocks.BlockBase;
import hugman.mod.objects.blocks.BlockBrick;
import hugman.mod.objects.blocks.BlockCloud;
import hugman.mod.objects.blocks.BlockCrops;
import hugman.mod.objects.blocks.BlockDirectional;
import hugman.mod.objects.blocks.BlockDirt;
import hugman.mod.objects.blocks.BlockEmpty;
import hugman.mod.objects.blocks.BlockFallingGlass;
import hugman.mod.objects.blocks.BlockFlower;
import hugman.mod.objects.blocks.BlockGrass;
import hugman.mod.objects.blocks.BlockGrassPlant;
import hugman.mod.objects.blocks.BlockHugeMushroom;
import hugman.mod.objects.blocks.BlockMushroom;
import hugman.mod.objects.blocks.BlockNote;
import hugman.mod.objects.blocks.BlockOre;
import hugman.mod.objects.blocks.BlockPillar;
import hugman.mod.objects.blocks.BlockPortal;
import hugman.mod.objects.blocks.BlockQuestion;
import hugman.mod.objects.blocks.BlockRotating;
import hugman.mod.objects.blocks.BlockStairs;
import hugman.mod.objects.blocks.BlockTetris;
import hugman.mod.objects.blocks.BlockUnstable;
import hugman.mod.objects.blocks.BlockWall;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/** 
 * Init class - used to initialize blocks.
 */
public class BlockInit
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	// VANILLA
	public static final Block VANADIUM_BLOCK = new BlockBase("vanadium_block", Material.IRON, 5f, 30f, SoundType.METAL);
	public static final Block VANADIUM_ORE = new BlockOre("vanadium_ore");
	public static final Block RED_BRICKS = new BlockBase("red_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block RED_BRICK_STAIRS = new BlockStairs("red_brick", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block WHITE_BRICKS = new BlockBase("white_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block WHITE_BRICK_STAIRS = new BlockStairs("white_brick", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block BLUNITE = new BlockBase("blunite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block BLUNITE_STAIRS = new BlockStairs("blunite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block BLUNITE_WALL = new BlockWall("blunite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block POLISHED_BLUNITE = new BlockBase("polished_blunite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block POLISHED_BLUNITE_STAIRS = new BlockStairs("polished_blunite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block CARBONITE = new BlockBase("carbonite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block CARBONITE_STAIRS = new BlockStairs("carbonite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block CARBONITE_WALL = new BlockWall("carbonite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block POLISHED_CARBONITE = new BlockBase("polished_carbonite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block POLISHED_CARBONITE_STAIRS = new BlockStairs("polished_carbonite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block FOOTBLOCK = new BlockBase("footblock", Material.CLOTH, 0.8f, 4f, SoundType.CLOTH);
	public static final Block WHITE_CLOUD_BLOCK = new BlockCloud("white");
	public static final Block LIGHT_GRAY_CLOUD_BLOCK = new BlockCloud("light_gray");
	public static final Block GRAY_CLOUD_BLOCK = new BlockCloud("gray");
	public static final Block BLACK_CLOUD_BLOCK = new BlockCloud("black");
	public static final Block TOMATO = new BlockCrops("tomato", ItemInit.TOMATO);
	public static final Block SALAD = new BlockCrops("salad", ItemInit.SALAD);
	public static final Block CHEESE_BLOCK = new BlockBase("cheese_block", Material.CLOTH, 0.5f, 2f, SoundType.SNOW);
	public static final Block UNSTABLE_STONE = new BlockUnstable("unstable_stone", Material.ROCK, 0.1f, 1f, SoundType.STONE);
	public static final Block WOL_PORTAL = new BlockPortal("wol_portal");
	
	// SUPER MARIO
	public static final Block QUESTION_BLOCK = new BlockQuestion();
	public static final Block EMPTY_BLOCK = new BlockEmpty();
	public static final Block ROTATING_BLOCK = new BlockRotating();
	public static final Block LIGHT_BLOCK = new BlockBase("light_block", Material.ROCK, 1.5f, 30f, SoundType.STONE, 15);
	public static final Block BRICK_BLOCK = new BlockBrick("brick_block");
	public static final Block GOLDEN_BRICK_BLOCK = new BlockBrick("golden_brick_block");
	public static final Block NOTE_BLOCK = new BlockNote("note_block");
	public static final Block SUPER_NOTE_BLOCK = new BlockNote("super_note_block");
	public static final Block FIRE_FLOWER = new BlockFlower("fire_flower", 0);
	public static final Block ICE_FLOWER = new BlockFlower("ice_flower", 0);
	public static final Block BOOMERANG_FLOWER = new BlockFlower("boomerang_flower", 0);
	public static final Block CLOUD_FLOWER = new BlockFlower("cloud_flower", 0);
	public static final Block GOLD_FLOWER = new BlockFlower("gold_flower", 5);
	public static final Block BLUE_MUSHROOM = new BlockMushroom("blue", 0);
	public static final Block LIGHT_BLUE_MUSHROOM = new BlockMushroom("light_blue", 0);
	public static final Block CYAN_MUSHROOM = new BlockMushroom("cyan", 0);
	public static final Block GREEN_MUSHROOM = new BlockMushroom("green", 0);
	public static final Block LIME_MUSHROOM = new BlockMushroom("lime", 0);
	public static final Block YELLOW_MUSHROOM = new BlockMushroom("yellow", 0);
	public static final Block ORANGE_MUSHROOM = new BlockMushroom("orange", 0);
	public static final Block PINK_MUSHROOM = new BlockMushroom("pink", 0);
	public static final Block MAGENTA_MUSHROOM = new BlockMushroom("magenta", 0);
	public static final Block PURPLE_MUSHROOM = new BlockMushroom("purple", 0);
	public static final Block WHITE_MUSHROOM = new BlockMushroom("white", 0);
	public static final Block LIGHT_GRAY_MUSHROOM = new BlockMushroom("light_gray", 0);
	public static final Block GRAY_MUSHROOM = new BlockMushroom("gray", 0);
	public static final Block BLACK_MUSHROOM = new BlockMushroom("black", 0);
	public static final Block BLUE_MUSHROOM_BLOCK = new BlockHugeMushroom("blue", 0);
	public static final Block GREEN_MUSHROOM_BLOCK = new BlockHugeMushroom("green", 0);
	public static final Block YELLOW_MUSHROOM_BLOCK = new BlockHugeMushroom("yellow", 0);
	public static final Block PURPLE_MUSHROOM_BLOCK = new BlockHugeMushroom("purple", 0);
	
	// TETRIS
	public static final Block BLUE_TETRIS_BLOCK = new BlockTetris("blue", "block", Material.ROCK);
	public static final Block LIGHT_BLUE_TETRIS_BLOCK = new BlockTetris("light_blue", "block" ,Material.ROCK);
	public static final Block CYAN_TETRIS_BLOCK = new BlockTetris("cyan", "block", Material.ROCK);
	public static final Block GREEN_TETRIS_BLOCK = new BlockTetris("green", "block", Material.ROCK);
	public static final Block LIME_TETRIS_BLOCK = new BlockTetris("lime", "block", Material.ROCK);
	public static final Block YELLOW_TETRIS_BLOCK = new BlockTetris("yellow", "block", Material.ROCK);
	public static final Block ORANGE_TETRIS_BLOCK = new BlockTetris("orange", "block", Material.ROCK);
	public static final Block RED_TETRIS_BLOCK = new BlockTetris("red", "block", Material.ROCK);
	public static final Block PINK_TETRIS_BLOCK = new BlockTetris("pink", "block", Material.ROCK);
	public static final Block MAGENTA_TETRIS_BLOCK = new BlockTetris("magenta", "block", Material.ROCK);
	public static final Block PURPLE_TETRIS_BLOCK = new BlockTetris("purple", "block", Material.ROCK);
	public static final Block BROWN_TETRIS_BLOCK = new BlockTetris("brown", "block", Material.ROCK);
	public static final Block WHITE_TETRIS_BLOCK = new BlockTetris("white", "block", Material.ROCK);
	public static final Block LIGHT_GRAY_TETRIS_BLOCK = new BlockTetris("light_gray", "block", Material.ROCK);
	public static final Block GRAY_TETRIS_BLOCK = new BlockTetris("gray", "block", Material.ROCK);
	public static final Block BLACK_TETRIS_BLOCK = new BlockTetris("black", "block", Material.ROCK);
	public static final Block TETRIS_GLASS = new BlockFallingGlass("tetris_glass", Main.MUBBLE_BLOCKS);
	public static final Block JAPANESE_TETRIS_CUSHION = new BlockTetris("japanese", "cushion", Material.CLOTH);
	public static final Block RAINBOW_TETRIS_SCAFFOLDING = new BlockTetris("rainbow", "scaffolding", Material.IRON);
	public static final Block MONOCHROME_TETRIS_SCAFFOLDING = new BlockTetris("monochrome", "scaffolding", Material.IRON);
	
	// CASTLEVANIA
	public static final Block VAMPIRE_STONE = new BlockBase("vampire_stone", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block MEDUSA_STONE = new BlockBase("medusa_stone", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block MEDUSA_BRICKS = new BlockBase("medusa_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block MEDUSA_BRICKS_STAIRS = new BlockStairs("medusa_brick", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block BLUE_CANDY_CANE_PILLAR = new BlockPillar("blue_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block LIGHT_BLUE_CANDY_CANE_PILLAR = new BlockPillar("light_blue_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block CYAN_CANDY_CANE_PILLAR = new BlockPillar("cyan_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block GREEN_CANDY_CANE_PILLAR = new BlockPillar("green_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block LIME_CANDY_CANE_PILLAR = new BlockPillar("lime_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block YELLOW_CANDY_CANE_PILLAR = new BlockPillar("yellow_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block ORANGE_CANDY_CANE_PILLAR = new BlockPillar("orange_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block RED_CANDY_CANE_PILLAR = new BlockPillar("red_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block PINK_CANDY_CANE_PILLAR = new BlockPillar("pink_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block MAGENTA_CANDY_CANE_PILLAR = new BlockPillar("magenta_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block PURPLE_CANDY_CANE_PILLAR = new BlockPillar("purple_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block BROWN_CANDY_CANE_PILLAR = new BlockPillar("brown_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block WHITE_CANDY_CANE_PILLAR = new BlockPillar("white_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block LIGHT_GRAY_CANDY_CANE_PILLAR = new BlockPillar("light_gray_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block GRAY_CANE_PILLAR = new BlockPillar("gray_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block BLACK_CANDY_CANE_PILLAR = new BlockPillar("black_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	
	// SONIC
	public static final Block GREEN_HILL_GRASS_BLOCK = new BlockGrass("green_hill_grass_block", Material.GROUND, 0.6f, 3f, SoundType.PLANT);
	public static final Block GREEN_HILL_DIRT = new BlockDirt("green_hill_dirt", Material.GROUND, 0.5f, 2.5f, SoundType.GROUND);
	public static final Block MARBLE_ZONE_STONE = new BlockBase("marble_zone_stone", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block YELLOW_STUDIOPOLIS_CLAPPER = new BlockDirectional("yellow_studiopolis_clapper", Material.IRON, 5f, 30f, SoundType.METAL);
	public static final Block BLUE_STUDIOPOLIS_CLAPPER = new BlockDirectional("blue_studiopolis_clapper", Material.IRON, 5f, 30f, SoundType.METAL);
	
	// DELTARUNE
	public static final Block SCARLET_GRASS_BLOCK = new BlockGrass("scarlet_grass_block", Material.GROUND, 0.6f, 3f, SoundType.PLANT);
	public static final Block SCARLET_GRASS = new BlockGrassPlant("scarlet_grass");
	public static final Block SCARLET_MUSHROOM = new BlockMushroom("scarlet", 7);
	public static final Block SCARLET_ORCHID = new BlockFlower("scarlet_orchid", 7);

	// CELESTE
	public static final Block IRON_SCAFFOLDING = new BlockBase("iron_scaffolding", Material.IRON, 5f, 30f, SoundType.METAL);
	public static final Block PURPLE_BRICKS = new BlockBase("purple_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block PURPLE_BRICKS_STAIRS = new BlockStairs("purple_brick", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block CYAN_BRICKS = new BlockBase("cyan_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block CYAN_BRICKS_STAIRS = new BlockStairs("cyan_brick", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block ELDER_PEBBLES = new BlockBase("elder_pebbles", Material.ROCK, 2f, 30f, SoundType.STONE, 5);
	public static final Block SPACE_MATTER = new BlockBase("space_matter", Material.SAND, 0.4f, 30f, SoundTypeInit.SPACE_MATTER, 5);
	
	// YOUTUBE
	public static final Block REWIND_BLOCK = new BlockUnstable("rewind_block", Material.IRON, 5f, 30f, SoundType.METAL);
	public static final Block KORETATO_BLOCK = new BlockDirectional("koretato_block", Material.GROUND, 0.4f, 10f, SoundType.SNOW);
}
