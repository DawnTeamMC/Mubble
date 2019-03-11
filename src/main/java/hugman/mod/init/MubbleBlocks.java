package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.block.BlockBrick;
import hugman.mod.objects.block.BlockCandyCanePillar;
import hugman.mod.objects.block.BlockCloud;
import hugman.mod.objects.block.BlockCrops;
import hugman.mod.objects.block.BlockDirectional;
import hugman.mod.objects.block.BlockEmpty;
import hugman.mod.objects.block.BlockEmptyDrops;
import hugman.mod.objects.block.BlockFalling;
import hugman.mod.objects.block.BlockFlower;
import hugman.mod.objects.block.BlockGrass;
import hugman.mod.objects.block.BlockHugeMushroom;
import hugman.mod.objects.block.BlockMushroom;
import hugman.mod.objects.block.BlockNote;
import hugman.mod.objects.block.BlockPuyo;
import hugman.mod.objects.block.BlockQuestion;
import hugman.mod.objects.block.BlockRotating;
import hugman.mod.objects.block.BlockSimple;
import hugman.mod.objects.block.BlockSlab;
import hugman.mod.objects.block.BlockSlabVertical;
import hugman.mod.objects.block.BlockSpring;
import hugman.mod.objects.block.BlockStairs;
import hugman.mod.objects.block.BlockTetris;
import hugman.mod.objects.block.BlockTetrisGlass;
import hugman.mod.objects.block.BlockWall;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class MubbleBlocks
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();

	public static final Block
	VANADIUM_BLOCK = new BlockSimple("vanadium_block", Properties.from(Blocks.DIAMOND_BLOCK)),
    VANADIUM_ORE = new BlockSimple("vanadium_ore", Properties.from(Blocks.DIAMOND_ORE)),
    RED_BRICKS = new BlockSimple("red_bricks", Properties.from(Blocks.BRICKS)),
    RED_BRICK_STAIRS = new BlockStairs("red_brick", MubbleBlocks.RED_BRICKS),
    RED_BRICK_SLAB = new BlockSlab("red_brick", MubbleBlocks.RED_BRICKS),
    RED_BRICK_VERTICAL_SLAB = new BlockSlabVertical("red_brick", MubbleBlocks.RED_BRICKS),
    RED_BRICK_WALL = new BlockWall("red_brick", MubbleBlocks.RED_BRICKS),
    WHITE_BRICKS = new BlockSimple("white_bricks", Properties.from(Blocks.BRICKS)),
    WHITE_BRICK_STAIRS = new BlockStairs("white_brick", MubbleBlocks.WHITE_BRICKS),
    WHITE_BRICK_SLAB = new BlockSlab("white_brick", MubbleBlocks.WHITE_BRICKS),
    WHITE_BRICK_VERTICAL_SLAB = new BlockSlabVertical("white_brick", MubbleBlocks.WHITE_BRICKS),
    WHITE_BRICK_WALL = new BlockWall("white_brick", MubbleBlocks.WHITE_BRICKS),
    BLUNITE = new BlockSimple("blunite", Properties.from(Blocks.STONE)),
    BLUNITE_STAIRS = new BlockStairs(MubbleBlocks.BLUNITE),
    BLUNITE_SLAB = new BlockSlab(MubbleBlocks.BLUNITE),
    BLUNITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.BLUNITE),
    BLUNITE_WALL = new BlockWall(MubbleBlocks.BLUNITE),
    POLISHED_BLUNITE = new BlockSimple("polished_blunite", Properties.from(MubbleBlocks.BLUNITE)),
    POLISHED_BLUNITE_STAIRS = new BlockStairs(MubbleBlocks.POLISHED_BLUNITE),
    POLISHED_BLUNITE_SLAB = new BlockSlab(MubbleBlocks.POLISHED_BLUNITE),
    POLISHED_BLUNITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.POLISHED_BLUNITE),
    CARBONITE = new BlockSimple("carbonite", Properties.from(Blocks.STONE)),
    CARBONITE_STAIRS = new BlockStairs(MubbleBlocks.CARBONITE),
    CARBONITE_SLAB = new BlockSlab(MubbleBlocks.CARBONITE),
    CARBONITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.CARBONITE),
    CARBONITE_WALL = new BlockWall(MubbleBlocks.CARBONITE),
    POLISHED_CARBONITE = new BlockSimple("polished_carbonite", Properties.from(MubbleBlocks.CARBONITE)),
    POLISHED_CARBONITE_STAIRS = new BlockStairs(MubbleBlocks.POLISHED_CARBONITE),
    POLISHED_CARBONITE_SLAB = new BlockSlab(MubbleBlocks.POLISHED_CARBONITE),
    POLISHED_CARBONITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.POLISHED_CARBONITE),
    FOOTBLOCK = new BlockSimple("footblock", Properties.from(Blocks.WHITE_WOOL)),
    WHITE_CLOUD_BLOCK = new BlockCloud("white"),
    LIGHT_GRAY_CLOUD_BLOCK = new BlockCloud("light_gray"),
    GRAY_CLOUD_BLOCK = new BlockCloud("gray"),
    BLACK_CLOUD_BLOCK = new BlockCloud("black"),
    TOMATO = new BlockCrops("tomato"),
    SALAD = new BlockCrops("salad"),
    CHEESE_BLOCK = new BlockDirectional("cheese_block", Properties.create(Material.GROUND, MaterialColor.YELLOW).hardnessAndResistance(0.5f).sound(SoundType.SNOW)),
    
    QUESTION_BLOCK = new BlockQuestion(),
    EMPTY_BLOCK = new BlockEmpty(),
    ROTATING_BLOCK = new BlockRotating(),
    LIGHT_BLOCK = new BlockSimple("light_block", Properties.from(Blocks.STONE).lightValue(15)),
    BRICK_BLOCK = new BlockBrick("brick_block"),
    GOLDEN_BRICK_BLOCK = new BlockBrick("golden_brick_block"),
    NOTE_BLOCK = new BlockNote("note_block"),
    SUPER_NOTE_BLOCK = new BlockNote("super_note_block"),
    FIRE_FLOWER = new BlockFlower("fire_flower"),
    ICE_FLOWER = new BlockFlower("ice_flower"),
    BOOMERANG_FLOWER = new BlockFlower("boomerang_flower"),
    CLOUD_FLOWER = new BlockFlower("cloud_flower"),
    GOLD_FLOWER = new BlockFlower("gold_flower", 5),
    BLUE_MUSHROOM = new BlockMushroom(EnumDyeColor.BLUE),
    LIGHT_BLUE_MUSHROOM = new BlockMushroom(EnumDyeColor.LIGHT_BLUE),
    CYAN_MUSHROOM = new BlockMushroom(EnumDyeColor.CYAN),
    GREEN_MUSHROOM = new BlockMushroom(EnumDyeColor.GREEN),
    YELLOW_MUSHROOM = new BlockMushroom(EnumDyeColor.YELLOW),
    ORANGE_MUSHROOM = new BlockMushroom(EnumDyeColor.ORANGE),
    PINK_MUSHROOM = new BlockMushroom(EnumDyeColor.PINK),
    MAGENTA_MUSHROOM = new BlockMushroom(EnumDyeColor.MAGENTA),
    PURPLE_MUSHROOM = new BlockMushroom(EnumDyeColor.PURPLE),
    WHITE_MUSHROOM = new BlockMushroom(EnumDyeColor.WHITE),
    LIGHT_GRAY_MUSHROOM = new BlockMushroom(EnumDyeColor.LIGHT_GRAY),
    GRAY_MUSHROOM = new BlockMushroom(EnumDyeColor.GRAY),
    BLACK_MUSHROOM = new BlockMushroom(EnumDyeColor.BLACK),
    BLUE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.BLUE_MUSHROOM, EnumDyeColor.BLUE),
    LIGHT_BLUE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.LIGHT_BLUE_MUSHROOM, EnumDyeColor.LIGHT_BLUE),
    CYAN_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.CYAN_MUSHROOM, EnumDyeColor.CYAN),
    GREEN_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.GREEN_MUSHROOM, EnumDyeColor.GREEN),
    YELLOW_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.YELLOW_MUSHROOM, EnumDyeColor.YELLOW),
    ORANGE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.ORANGE_MUSHROOM, EnumDyeColor.ORANGE),
    PINK_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.PINK_MUSHROOM, EnumDyeColor.PINK),
    MAGENTA_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.MAGENTA_MUSHROOM, EnumDyeColor.MAGENTA),
    PURPLE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.PURPLE_MUSHROOM, EnumDyeColor.PURPLE),
    WHITE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.WHITE_MUSHROOM, EnumDyeColor.WHITE),
    LIGHT_GRAY_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.LIGHT_GRAY_MUSHROOM, EnumDyeColor.LIGHT_GRAY),
    GRAY_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.GRAY_MUSHROOM, EnumDyeColor.GRAY),
    BLACK_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.BLACK_MUSHROOM, EnumDyeColor.BLACK),

    BLUE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.BLUE),
    LIGHT_BLUE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.LIGHT_BLUE),
    CYAN_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.CYAN),
    GREEN_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.GREEN),
    LIME_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.LIME),
    YELLOW_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.YELLOW),
    ORANGE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.ORANGE),
    RED_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.RED),
    PINK_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.PINK),
    MAGENTA_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.MAGENTA),
    PURPLE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.PURPLE),
    BROWN_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.BROWN),
    WHITE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.WHITE),
    LIGHT_GRAY_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.LIGHT_GRAY),
    GRAY_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.GRAY),
    BLACK_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.BLACK),
    TETRIS_GLASS = new BlockTetrisGlass(),
    JAPANESE_TETRIS_CUSHION = new BlockFalling("japanese_tetris_cushion", Properties.from(Blocks.RED_WOOL)),
    RAINBOW_TETRIS_SCAFFOLDING = new BlockFalling("rainbow_tetris_scaffolding", Properties.from(Blocks.IRON_BLOCK)),
    MONOCHROME_TETRIS_SCAFFOLDING = new BlockFalling("monochrome_tetris_scaffolding", Properties.from(Blocks.IRON_BLOCK)),

    VAMPIRE_STONE = new BlockSimple("vampire_stone", Properties.from(Blocks.STONE)),
    MEDUSA_STONE = new BlockSimple("medusa_stone", Properties.from(Blocks.STONE)),
    MEDUSA_BRICKS = new BlockSimple("medusa_bricks", Properties.from(Blocks.STONE_BRICKS)),
    MEDUSA_BRICK_STAIRS = new BlockStairs("medusa_brick", MubbleBlocks.MEDUSA_BRICKS),
    MEDUSA_BRICK_SLAB = new BlockSlab("medusa_brick", MubbleBlocks.MEDUSA_BRICKS),
    MEDUSA_BRICK_VERTICAL_SLAB = new BlockSlabVertical("medusa_brick", MubbleBlocks.MEDUSA_BRICKS),
    MEDUSA_BRICK_WALL = new BlockWall("medusa_brick", MubbleBlocks.MEDUSA_BRICKS),
    BLUE_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.BLUE),
    LIGHT_BLUE_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.LIGHT_BLUE),
    CYAN_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.CYAN),
    GREEN_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.GREEN),
    LIME_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.LIME),
    YELLOW_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.YELLOW),
    ORANGE_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.ORANGE),
    RED_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.RED),
    PINK_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.PINK),
    MAGENTA_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.MAGENTA),
    PURPLE_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.PURPLE),
    BROWN_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.BROWN),
    WHITE_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.WHITE),
    LIGHT_GRAY_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.LIGHT_GRAY),
    GRAY_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.GRAY),
    BLACK_CANDY_CANE_PILLAR = new BlockCandyCanePillar(EnumDyeColor.BLACK),
    
    GREEN_HILL_GRASS_BLOCK  = new BlockGrass("green_hill_grass_block", MubbleBlocks.GREEN_HILL_DIRT, Properties.from(Blocks.GRASS_BLOCK)),
    GREEN_HILL_DIRT = new BlockSimple("green_hill_dirt", Properties.from(Blocks.DIRT)),
    MARBLE_ZONE_STONE = new BlockSimple("marble_zone_stone", Properties.from(Blocks.STONE)),
    YELLOW_STUDIOPOLIS_CLAPPER = new BlockDirectional("yellow_studiopolis_clapper", Properties.from(Blocks.IRON_BLOCK)),
    BLUE_STUDIOPOLIS_CLAPPER = new BlockDirectional("blue_studiopolis_clapper", Properties.from(Blocks.IRON_BLOCK)),
    SPRING = new BlockSpring(),

    SCARLET_MUSHROOM = new BlockMushroom("scarlet", 7),
    SCARLET_ORCHID = new BlockFlower("scarlet_orchid", 7),
    
    IRON_SCAFFOLDING = new BlockSimple("iron_scaffolding", Properties.from(Blocks.IRON_BLOCK)),
    PURPLE_BRICKS = new BlockSimple("purple_bricks", Properties.from(Blocks.BRICKS)),
    PURPLE_BRICK_STAIRS = new BlockStairs("purple_brick", MubbleBlocks.PURPLE_BRICKS),
    PURPLE_BRICK_SLAB = new BlockSlab("purple_brick", MubbleBlocks.PURPLE_BRICKS),
    PURPLE_BRICK_VERTICAL_SLAB = new BlockSlabVertical("purple_brick", MubbleBlocks.PURPLE_BRICKS),
    PURPLE_BRICK_WALL = new BlockWall("purple_brick", MubbleBlocks.PURPLE_BRICKS),
    CYAN_BRICKS = new BlockSimple("cyan_bricks", Properties.from(Blocks.BRICKS)),
    CYAN_BRICK_STAIRS = new BlockStairs("cyan_brick", MubbleBlocks.CYAN_BRICKS),
    CYAN_BRICK_SLAB = new BlockSlab("cyan_brick", MubbleBlocks.CYAN_BRICKS),
    CYAN_BRICK_VERTICAL_SLAB = new BlockSlabVertical("cyan_brick", MubbleBlocks.CYAN_BRICKS),
    CYAN_BRICK_WALL = new BlockWall("cyan_brick", MubbleBlocks.CYAN_BRICKS),
    ELDER_PEBBLES = new BlockSimple("elder_pebbles", Properties.from(Blocks.BRICKS).lightValue(5)),
    SPACE_MATTER = new BlockSimple("space_matter", Properties.create(Material.SAND).hardnessAndResistance(0.4f).sound(MubbleSoundTypes.SPACE_MATTER)),
    SPACE_OBSIDIAN = new BlockEmptyDrops("space_obsidian", Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F)),
    
    BLUE_PUYO = new BlockPuyo(EnumDyeColor.BLUE),
    TURQUOISE_PUYO = new BlockPuyo("turquoise"),
    GREEN_PUYO = new BlockPuyo(EnumDyeColor.GREEN),
    YELLOW_PUYO = new BlockPuyo(EnumDyeColor.YELLOW),
    RED_PUYO = new BlockPuyo(EnumDyeColor.RED),
    PURPLE_PUYO = new BlockPuyo(EnumDyeColor.PURPLE),
    GARBAGE_PUYO = new BlockDirectional("garbage_puyo", Properties.from(Blocks.STONE)),
    POINT_PUYO = new BlockDirectional("point_puyo", Properties.from(Blocks.STONE).lightValue(10)),
    HARD_PUYO = new BlockDirectional("hard_puyo", Properties.from(Blocks.STONE)),
    IRON_PUYO = new BlockDirectional("iron_puyo", Properties.from(Blocks.IRON_BLOCK)),
    PUYO_BLOCK = new BlockSimple("puyo_block", Properties.from(Blocks.STONE)),
    
    KORETATO_BLOCK = new BlockDirectional("koretato_block", Properties.create(Material.GROUND, MaterialColor.YELLOW_TERRACOTTA).hardnessAndResistance(0.4f, 2f).sound(SoundType.SNOW));
	
    public static void register(Block block)
    {
    	Item.Properties group = new Item.Properties().group(MubbleTabs.MUBBLE_BLOCKS);
		BLOCKS.add(block);
		MubbleItems.ITEMS.add(new ItemBlock(block, group).setRegistryName(block.getRegistryName()));
    }
}
