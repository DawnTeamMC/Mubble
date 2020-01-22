package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MubbleRenderLayers
{
    public static void registerBlockLayers()
	{
    	RenderType typeCM = RenderType.getCutoutMipped();
    	RenderType typeC = RenderType.getCutout();
    	RenderType typeT = RenderType.getTranslucent();
    	List<List<BlockItem>> cutoutMippedBlockItems = new ArrayList<List<BlockItem>>();
    	List<List<BlockItem>> cutoutBlockItems = new ArrayList<List<BlockItem>>();
    	List<List<BlockItem>> translucentBlockItems = new ArrayList<List<BlockItem>>();

    	cutoutMippedBlockItems.add(MubbleBlocks.LEAVES);
    	cutoutMippedBlockItems.add(MubbleBlocks.LEAF_PILES);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.GREEN_HILL_GRASS_BLOCK, typeCM);
		cutoutBlockItems.add(MubbleBlocks.SAPLINGS);
		cutoutBlockItems.add(MubbleBlocks.FLOWERS);
		cutoutBlockItems.add(MubbleBlocks.FLOWER_PILES);
		cutoutBlockItems.add(MubbleBlocks.DOORS);
		cutoutBlockItems.add(MubbleBlocks.TRAPDOORS);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.FLUID_TANK, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.BLACK_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.BLUE_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.GOLDEN_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.GREEN_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.PURPLE_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.RED_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.WHITE_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.YELLOW_PRESENT, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.GOLD_SHINY_GARLAND, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.RED_SHINY_GARLAND, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.SILVER_SHINY_GARLAND, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.TOMATOES, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.SALAD, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.BLUEBERRY_BUSH, typeC);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.TETRIS_GLASS, typeC);
		translucentBlockItems.add(MubbleBlocks.BALLOONS);
		translucentBlockItems.add(MubbleBlocks.CLOUD_BLOCKS);
		RenderTypeLookup.setRenderLayer(MubbleBlocks.PERMAFROST_PORTAL, typeT);
    	
    	for(List<BlockItem> list : cutoutMippedBlockItems)
    	{
        	for(BlockItem item : list)
        	{
        		RenderTypeLookup.setRenderLayer(item.getBlock(), typeCM);
        	}
    	}
    	for(List<BlockItem> list : cutoutBlockItems)
    	{
        	for(BlockItem item : list)
        	{
        		RenderTypeLookup.setRenderLayer(item.getBlock(), typeC);
        	}
    	}
    	for(Block block : MubbleBlocks.POTTED_PLANTS)
    	{
    		RenderTypeLookup.setRenderLayer(block, typeC);
    	}
    	for(List<BlockItem> list : translucentBlockItems)
    	{
        	for(BlockItem item : list)
        	{
        		RenderTypeLookup.setRenderLayer(item.getBlock(), typeT);
        	}
    	}
	}
}