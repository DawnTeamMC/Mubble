package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Main;
import hugman.mod.objects.blocks.BlockBase;
import hugman.mod.objects.blocks.BlockBrick;
import hugman.mod.objects.blocks.BlockCloud;
import hugman.mod.objects.blocks.BlockDirectional;
import hugman.mod.objects.blocks.BlockFallingGlass;
import hugman.mod.objects.blocks.BlockFlower;
import hugman.mod.objects.blocks.BlockHugeMushroom;
import hugman.mod.objects.blocks.BlockMushroom;
import hugman.mod.objects.blocks.BlockNote;
import hugman.mod.objects.blocks.BlockOre;
import hugman.mod.objects.blocks.BlockPillar;
import hugman.mod.objects.blocks.BlockQuestion;
import hugman.mod.objects.blocks.BlockRotating;
import hugman.mod.objects.blocks.BlockTetris;
import hugman.mod.util.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
	public static final Block WHITE_BRICKS = new BlockBase("white_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block BLUNITE = new BlockBase("blunite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block POLISHED_BLUNITE = new BlockBase("polished_blunite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block CARBONITE = new BlockBase("carbonite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block POLISHED_CARBONITE = new BlockBase("polished_carbonite", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block FOOTBLOCK = new BlockBase("footblock", Material.CLOTH, 0.8f, 4f, SoundType.CLOTH);
	
	// SUPER MARIO
	public static final Block QUESTION_BLOCK = new BlockQuestion();
	public static final Block EMPTY_BLOCK = new BlockBase("empty_block", Material.IRON, 1.5f, 30f, SoundTypeInit.EMPTY_BLOCK);
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
	public static final Block CLOUD_BLOCK = new BlockCloud();
	public static final Block BLUE_MUSHROOM = new BlockMushroom("blue", 0);
	public static final Block GREEN_MUSHROOM = new BlockMushroom("green", 0);
	public static final Block YELLOW_MUSHROOM = new BlockMushroom("yellow", 0);
	public static final Block PURPLE_MUSHROOM = new BlockMushroom("purple", 0);
	public static final Block BLUE_MUSHROOM_BLOCK = new BlockHugeMushroom("blue", 0);
	public static final Block GREEN_MUSHROOM_BLOCK = new BlockHugeMushroom("green", 0);
	public static final Block YELLOW_MUSHROOM_BLOCK = new BlockHugeMushroom("yellow", 0);
	public static final Block PURPLE_MUSHROOM_BLOCK = new BlockHugeMushroom("purple", 0);
	
	// TETRIS
	public static final Block BLUE_TETRIS_BLOCK = new BlockTetris("blue", "block", Material.ROCK);
	public static final Block LIGHT_BLUE_TETRIS_BLOCK = new BlockTetris("light_blue", "block" ,Material.ROCK);
	public static final Block GREEN_TETRIS_BLOCK = new BlockTetris("green", "block", Material.ROCK);
	public static final Block YELLOW_TETRIS_BLOCK = new BlockTetris("yellow", "block", Material.ROCK);
	public static final Block ORANGE_TETRIS_BLOCK = new BlockTetris("orange", "block", Material.ROCK);
	public static final Block RED_TETRIS_BLOCK = new BlockTetris("red", "block", Material.ROCK);
	public static final Block PINK_TETRIS_BLOCK = new BlockTetris("pink", "block", Material.ROCK);
	public static final Block GRAY_TETRIS_BLOCK = new BlockTetris("gray", "block", Material.ROCK);
	public static final Block TETRIS_GLASS = new BlockFallingGlass("tetris_glass", Main.MUBBLE_BLOCKS);
	public static final Block JAPANESE_TETRIS_CUSHION = new BlockTetris("japanese", "cushion", Material.CLOTH);
	public static final Block RAINBOW_TETRIS_SCAFFOLDING = new BlockTetris("rainbow", "scaffolding", Material.IRON);
	public static final Block MONOCHROME_TETRIS_SCAFFOLDING = new BlockTetris("monochrome", "scaffolding", Material.IRON);
	
	// CASTLEVANIA
	public static final Block VAMPIRE_STONE = new BlockBase("vampire_stone", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block MEDUSA_STONE = new BlockBase("medusa_stone", Material.ROCK, 1.5f, 30f, SoundType.STONE);
	public static final Block MEDUSA_BRICKS = new BlockBase("medusa_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block BLUE_CANDY_CANE_PILLAR = new BlockPillar("blue_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block GREEN_CANDY_CANE_PILLAR = new BlockPillar("green_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block YELLOW_CANDY_CANE_PILLAR = new BlockPillar("yellow_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block RED_CANDY_CANE_PILLAR = new BlockPillar("red_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);
	public static final Block PURPLE_CANDY_CANE_PILLAR = new BlockPillar("purple_candy_cane_pillar", Material.ROCK, 1.2f, 15f, SoundType.SNOW);

	// CELESTE
	public static final Block IRON_SCAFFOLDING = new BlockBase("iron_scaffolding", Material.IRON, 5f, 30f, SoundType.METAL);
	public static final Block PURPLE_BRICKS = new BlockBase("purple_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block CYAN_BRICKS = new BlockBase("cyan_bricks", Material.ROCK, 2f, 30f, SoundType.STONE);
	public static final Block ELDER_PEBBLES = new BlockBase("elder_pebbles", Material.ROCK, 2f, 30f, SoundType.STONE, 5);
	public static final Block SPACE_MATTER = new BlockBase("space_matter", Material.SAND, 0.4f, 30f, SoundTypeInit.SPACE_MATTER, 5);
	
	// YOUTUBE
	public static final Block KORETATO_BLOCK = new BlockDirectional("koretato_block", Material.GROUND, 0.4f, 10f, SoundType.SNOW);
}
