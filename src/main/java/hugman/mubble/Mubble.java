package hugman.mubble;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleCommands;
import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleScreens;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleContainerTypes;
import hugman.mubble.init.data.MubbleTileEntityTypes;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleCarvers;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.init.world.MubbleGenerators;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import hugman.mubble.util.MoreWordUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;

public class Mubble implements ModInitializer
{
	public static final String MOD_ID = "mubble";
	public static final String MOD_PREFIX = MOD_ID + ":";
	public static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public void onInitialize()
	{
		new MubbleCommands();
		new MubbleEffects();
		new MubbleEntities();
		new MubbleSounds();
		
		// Data
		new MubbleContainerTypes();
		new MubbleTileEntityTypes();
		
		// World
		new MubbleCarvers();
		new MubbleSurfaceBuilders();
		initGenerators();
	}
	
	private void initGenerators()
	{
		MubbleGenerators.registerOres();
		MubbleGenerators.registerTrees();
		MubbleGenerators.registerSpawns();
	}
    
    private void clientSetup()
    {
    	MubbleEntities.registerRenders();
    	LOGGER.info("Registered entities renders");
    	MubbleScreens.registerScreens();
    	LOGGER.info("Registered screens");
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ModRegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
        {
        	event.getRegistry().registerAll(MubbleBlocks.BLOCKS.toArray(new Block[0]));
        	LOGGER.info("Registered " + MoreWordUtils.pluralize(MubbleBlocks.BLOCKS.size(), "block"));
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
            event.getRegistry().registerAll(MubbleBlocks.CUBES.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.STAIRS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.SLABS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.VERTICAL_SLABS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.FENCES.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.WALLS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.SAPLINGS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.LEAVES.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.LEAF_PILES.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.PRESSURE_PLATES.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.TRAPDOORS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.BUTTONS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.FENCE_GATES.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.DOORS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.FLOWERS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.FLOWER_PILES.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.OTHERS.toArray(new Item[0]));
        	event.getRegistry().registerAll(MubbleItems.ITEMS.toArray(new Item[0]));
        	LOGGER.info("Registered " + MoreWordUtils.pluralize(MubbleItems.ITEMS.size(), "item"));
    		event.getRegistry().registerAll(MubbleCostumes.COSTUMES.toArray(new Item[0]));
        	LOGGER.info("Registered " + MoreWordUtils.pluralize(MubbleCostumes.COSTUMES.size(), "costume"));
        }
        
        @SubscribeEvent
        public static void onBiomesRegistry(final RegistryEvent.Register<Biome> event)
        {
        	event.getRegistry().registerAll(MubbleBiomes.BIOMES.toArray(new Biome[0]));
        	MubbleBiomes.registerGenerations();
        	LOGGER.info("Registered " + MoreWordUtils.pluralize(MubbleBiomes.BIOMES.size(), "biome"));
        }
        
        @SubscribeEvent
        public static void onDimensionsRegistry(final RegistryEvent.Register<ModDimension> event)
        {
        	event.getRegistry().registerAll(MubbleDimensions.DIMENSIONS.toArray(new ModDimension[0]));
        	LOGGER.info("Registered " + MoreWordUtils.pluralize(MubbleDimensions.DIMENSIONS.size(), "dimension"));
        }
    }
}