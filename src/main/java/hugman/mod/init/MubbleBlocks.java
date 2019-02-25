package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.block.BlockBrick;
import hugman.mod.objects.block.BlockEmpty;
import hugman.mod.objects.block.BlockNote;
import hugman.mod.objects.block.BlockQuestion;
import hugman.mod.objects.block.BlockRotating;
import hugman.mod.objects.block.BlockSimple;
import hugman.mod.objects.block.BlockSlab;
import hugman.mod.objects.block.BlockSlabVertical;
import hugman.mod.objects.block.BlockStairs;
import hugman.mod.objects.block.BlockWall;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.init.Blocks;
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
    //public static final Block WHITE_CLOUD_BLOCK = new BlockCloud("white");
    //public static final Block LIGHT_GRAY_CLOUD_BLOCK = new BlockCloud("light_gray");
    //public static final Block GRAY_CLOUD_BLOCK = new BlockCloud("gray");
    //public static final Block BLACK_CLOUD_BLOCK = new BlockCloud("black");
    
    public static final Block QUESTION_BLOCK = new BlockQuestion();
    public static final Block EMPTY_BLOCK = new BlockEmpty();
    public static final Block ROTATING_BLOCK = new BlockRotating();
    public static final Block BRICK_BLOCK = new BlockBrick("brick_block");
    public static final Block GOLDEN_BRICK_BLOCK = new BlockBrick("golden_brick_block");
    public static final Block NOTE_BLOCK = new BlockNote("note_block");
    public static final Block SUPER_NOTE_BLOCK = new BlockNote("super_note_block");
    
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
