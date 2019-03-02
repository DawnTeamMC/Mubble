package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.block.BlockBrick;
import hugman.mod.objects.block.BlockCloud;
import hugman.mod.objects.block.BlockEmpty;
import hugman.mod.objects.block.BlockFalling;
import hugman.mod.objects.block.BlockFlower;
import hugman.mod.objects.block.BlockHugeMushroom;
import hugman.mod.objects.block.BlockMushroom;
import hugman.mod.objects.block.BlockNote;
import hugman.mod.objects.block.BlockQuestion;
import hugman.mod.objects.block.BlockRotating;
import hugman.mod.objects.block.BlockSimple;
import hugman.mod.objects.block.BlockSlab;
import hugman.mod.objects.block.BlockSlabVertical;
import hugman.mod.objects.block.BlockStairs;
import hugman.mod.objects.block.BlockTetris;
import hugman.mod.objects.block.BlockTetrisGlass;
import hugman.mod.objects.block.BlockWall;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class MubbleBlocks
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block VANADIUM_BLOCK = new BlockSimple("vanadium_block", Properties.from(Blocks.DIAMOND_BLOCK));
    public static final Block VANADIUM_ORE = new BlockSimple("vanadium_ore", Properties.from(Blocks.DIAMOND_ORE));
    public static final Block RED_BRICKS = new BlockSimple("red_bricks", Properties.from(Blocks.BRICKS));
    public static final Block RED_BRICK_STAIRS = new BlockStairs("red_brick", MubbleBlocks.RED_BRICKS);
    public static final Block RED_BRICK_SLAB = new BlockSlab("red_brick", MubbleBlocks.RED_BRICKS);
    public static final Block RED_BRICK_VERTICAL_SLAB = new BlockSlabVertical("red_brick", MubbleBlocks.RED_BRICKS);
    public static final Block RED_BRICK_WALL = new BlockWall("red_brick", MubbleBlocks.RED_BRICKS);
    public static final Block WHITE_BRICKS = new BlockSimple("white_bricks", Properties.from(Blocks.BRICKS));
    public static final Block WHITE_BRICK_STAIRS = new BlockStairs("white_brick", MubbleBlocks.WHITE_BRICKS);
    public static final Block WHITE_BRICK_SLAB = new BlockSlab("white_brick", MubbleBlocks.WHITE_BRICKS);
    public static final Block WHITE_BRICK_VERTICAL_SLAB = new BlockSlabVertical("white_brick", MubbleBlocks.WHITE_BRICKS);
    public static final Block WHITE_BRICK_WALL = new BlockWall("white_brick", MubbleBlocks.WHITE_BRICKS);
    public static final Block BLUNITE = new BlockSimple("blunite", Properties.from(Blocks.STONE));
    public static final Block BLUNITE_STAIRS = new BlockStairs(MubbleBlocks.BLUNITE);
    public static final Block BLUNITE_SLAB = new BlockSlab(MubbleBlocks.BLUNITE);
    public static final Block BLUNITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.BLUNITE);
    public static final Block BLUNITE_WALL = new BlockWall(MubbleBlocks.BLUNITE);
    public static final Block POLISHED_BLUNITE = new BlockSimple("polished_blunite", Properties.from(MubbleBlocks.BLUNITE));
    public static final Block POLISHED_BLUNITE_STAIRS = new BlockStairs(MubbleBlocks.POLISHED_BLUNITE);
    public static final Block POLISHED_BLUNITE_SLAB = new BlockSlab(MubbleBlocks.POLISHED_BLUNITE);
    public static final Block POLISHED_BLUNITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.POLISHED_BLUNITE);
    public static final Block CARBONITE = new BlockSimple("carbonite", Properties.from(Blocks.STONE));
    public static final Block CARBONITE_STAIRS = new BlockStairs(MubbleBlocks.CARBONITE);
    public static final Block CARBONITE_SLAB = new BlockSlab(MubbleBlocks.CARBONITE);
    public static final Block CARBONITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.CARBONITE);
    public static final Block CARBONITE_WALL = new BlockWall(MubbleBlocks.CARBONITE);
    public static final Block POLISHED_CARBONITE = new BlockSimple("polished_carbonite", Properties.from(MubbleBlocks.CARBONITE));
    public static final Block POLISHED_CARBONITE_STAIRS = new BlockStairs(MubbleBlocks.POLISHED_CARBONITE);
    public static final Block POLISHED_CARBONITE_SLAB = new BlockSlab(MubbleBlocks.POLISHED_CARBONITE);
    public static final Block POLISHED_CARBONITE_VERTICAL_SLAB = new BlockSlabVertical(MubbleBlocks.POLISHED_CARBONITE);
    public static final Block FOOTBLOCK = new BlockSimple("footblock", Properties.from(Blocks.WHITE_WOOL));
    public static final Block WHITE_CLOUD_BLOCK = new BlockCloud("white");
    public static final Block LIGHT_GRAY_CLOUD_BLOCK = new BlockCloud("light_gray");
    public static final Block GRAY_CLOUD_BLOCK = new BlockCloud("gray");
    public static final Block BLACK_CLOUD_BLOCK = new BlockCloud("black");
    
    public static final Block QUESTION_BLOCK = new BlockQuestion();
    public static final Block EMPTY_BLOCK = new BlockEmpty();
    public static final Block ROTATING_BLOCK = new BlockRotating();
    public static final Block BRICK_BLOCK = new BlockBrick("brick_block");
    public static final Block GOLDEN_BRICK_BLOCK = new BlockBrick("golden_brick_block");
    public static final Block NOTE_BLOCK = new BlockNote("note_block");
    public static final Block SUPER_NOTE_BLOCK = new BlockNote("super_note_block");
    public static final Block FIRE_FLOWER = new BlockFlower("fire_flower");
    public static final Block ICE_FLOWER = new BlockFlower("ice_flower");
    public static final Block BOOMERANG_FLOWER = new BlockFlower("boomerang_flower");
    public static final Block CLOUD_FLOWER = new BlockFlower("cloud_flower");
    public static final Block GOLD_FLOWER = new BlockFlower("gold_flower", 5);
    public static final Block BLUE_MUSHROOM = new BlockMushroom(EnumDyeColor.BLUE);
    public static final Block LIGHT_BLUE_MUSHROOM = new BlockMushroom(EnumDyeColor.LIGHT_BLUE);
    public static final Block CYAN_MUSHROOM = new BlockMushroom(EnumDyeColor.CYAN);
    public static final Block GREEN_MUSHROOM = new BlockMushroom(EnumDyeColor.GREEN);
    public static final Block YELLOW_MUSHROOM = new BlockMushroom(EnumDyeColor.YELLOW);
    public static final Block ORANGE_MUSHROOM = new BlockMushroom(EnumDyeColor.ORANGE);
    public static final Block PINK_MUSHROOM = new BlockMushroom(EnumDyeColor.PINK);
    public static final Block MAGENTA_MUSHROOM = new BlockMushroom(EnumDyeColor.MAGENTA);
    public static final Block PURPLE_MUSHROOM = new BlockMushroom(EnumDyeColor.PURPLE);
    public static final Block WHITE_MUSHROOM = new BlockMushroom(EnumDyeColor.WHITE);
    public static final Block LIGHT_GRAY_MUSHROOM = new BlockMushroom(EnumDyeColor.LIGHT_GRAY);
    public static final Block GRAY_MUSHROOM = new BlockMushroom(EnumDyeColor.GRAY);
    public static final Block BLACK_MUSHROOM = new BlockMushroom(EnumDyeColor.BLACK);
    public static final Block BLUE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.BLUE_MUSHROOM, EnumDyeColor.BLUE);
    public static final Block LIGHT_BLUE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.LIGHT_BLUE_MUSHROOM, EnumDyeColor.LIGHT_BLUE);
    public static final Block CYAN_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.CYAN_MUSHROOM, EnumDyeColor.CYAN);
    public static final Block GREEN_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.GREEN_MUSHROOM, EnumDyeColor.GREEN);
    public static final Block YELLOW_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.YELLOW_MUSHROOM, EnumDyeColor.YELLOW);
    public static final Block ORANGE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.ORANGE_MUSHROOM, EnumDyeColor.ORANGE);
    public static final Block PINK_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.PINK_MUSHROOM, EnumDyeColor.PINK);
    public static final Block MAGENTA_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.MAGENTA_MUSHROOM, EnumDyeColor.MAGENTA);
    public static final Block PURPLE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.PURPLE_MUSHROOM, EnumDyeColor.PURPLE);
    public static final Block WHITE_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.WHITE_MUSHROOM, EnumDyeColor.WHITE);
    public static final Block LIGHT_GRAY_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.LIGHT_GRAY_MUSHROOM, EnumDyeColor.LIGHT_GRAY);
    public static final Block GRAY_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.GRAY_MUSHROOM, EnumDyeColor.GRAY);
    public static final Block BLACK_MUSHROOM_BLOCK = new BlockHugeMushroom(MubbleBlocks.BLACK_MUSHROOM, EnumDyeColor.BLACK);

    public static final Block BLUE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.BLUE);
    public static final Block LIGHT_BLUE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.LIGHT_BLUE);
    public static final Block CYAN_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.CYAN);
    public static final Block GREEN_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.GREEN);
    public static final Block YELLOW_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.YELLOW);
    public static final Block ORANGE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.ORANGE);
    public static final Block RED_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.RED);
    public static final Block PINK_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.PINK);
    public static final Block MAGENTA_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.MAGENTA);
    public static final Block PURPLE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.PURPLE);
    public static final Block BROWN_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.BROWN);
    public static final Block WHITE_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.WHITE);
    public static final Block LIGHT_GRAY_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.LIGHT_GRAY);
    public static final Block GRAY_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.GRAY);
    public static final Block BLACK_TETRIS_BLOCK = new BlockTetris(EnumDyeColor.BLACK);
    public static final Block TETRIS_GLASS = new BlockTetrisGlass();
    public static final Block JAPANESE_TETRIS_CUSHION = new BlockFalling("japanese_tetris_cushion", Properties.from(Blocks.RED_WOOL));
    public static final Block RAINBOW_TETRIS_SCAFFOLDING = new BlockFalling("rainbow_tetris_scaffolding", Properties.from(Blocks.IRON_BLOCK));
    public static final Block MONOCHROME_TETRIS_SCAFFOLDING = new BlockFalling("monochrome_tetris_scaffolding", Properties.from(Blocks.IRON_BLOCK));

    public static final Block VAMPIRE_STONE = new BlockSimple("vampire_stone", Properties.from(Blocks.STONE));
    public static final Block MEDUSA_STONE = new BlockSimple("medusa_stone", Properties.from(Blocks.STONE));
    public static final Block MEDUSA_BRICKS = new BlockSimple("medusa_bricks", Properties.from(Blocks.STONE_BRICKS));
    public static final Block MEDUSA_BRICK_STAIRS = new BlockStairs("medusa_brick", MubbleBlocks.MEDUSA_BRICKS);
    public static final Block MEDUSA_BRICK_SLAB = new BlockSlab("medusa_brick", MubbleBlocks.MEDUSA_BRICKS);
    public static final Block MEDUSA_BRICK_VERTICAL_SLAB = new BlockSlabVertical("medusa_brick", MubbleBlocks.MEDUSA_BRICKS);
    public static final Block MEDUSA_BRICK_WALL = new BlockWall("medusa_brick", MubbleBlocks.MEDUSA_BRICKS);

    public static final Block PURPLE_BRICKS = new BlockSimple("purple_bricks", Properties.from(Blocks.BRICKS));
    public static final Block PURPLE_BRICK_STAIRS = new BlockStairs("purple_brick", MubbleBlocks.PURPLE_BRICKS);
    public static final Block PURPLE_BRICK_SLAB = new BlockSlab("purple_brick", MubbleBlocks.PURPLE_BRICKS);
    public static final Block PURPLE_BRICK_VERTICAL_SLAB = new BlockSlabVertical("purple_brick", MubbleBlocks.PURPLE_BRICKS);
    public static final Block PURPLE_BRICK_WALL = new BlockWall("purple_brick", MubbleBlocks.PURPLE_BRICKS);
    public static final Block CYAN_BRICKS = new BlockSimple("cyan_bricks", Properties.from(Blocks.BRICKS));
    public static final Block CYAN_BRICK_STAIRS = new BlockStairs("cyan_brick", MubbleBlocks.CYAN_BRICKS);
    public static final Block CYAN_BRICK_SLAB = new BlockSlab("cyan_brick", MubbleBlocks.CYAN_BRICKS);
    public static final Block CYAN_BRICK_VERTICAL_SLAB = new BlockSlabVertical("cyan_brick", MubbleBlocks.CYAN_BRICKS);
    public static final Block CYAN_BRICK_WALL = new BlockWall("cyan_brick", MubbleBlocks.CYAN_BRICKS);
	
    public static void register(Block block)
    {
    	Item.Properties group = new Item.Properties().group(MubbleTabs.MUBBLE_BLOCKS);
		BLOCKS.add(block);
		MubbleItems.ITEMS.add(new ItemBlock(block, group).setRegistryName(block.getRegistryName()));
    }
}
