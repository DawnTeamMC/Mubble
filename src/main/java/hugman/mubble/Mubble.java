package hugman.mubble;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleColorMaps;
import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.init.world.MubbleGenerators;
import hugman.mubble.init.world.MubbleWorldTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(Mubble.MOD_ID)
public class Mubble 
{
	public static final String MOD_ID = "mubble";
	public static final String MOD_PREFIX = MOD_ID + ":";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
    public Mubble()
    {        
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }
    
    private void clientSetup(final FMLClientSetupEvent event)
    {
    	MubbleEntities.registerRenders();
    	LOGGER.info("Registered entities renders");
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
    	MubbleGenerators.registerOres();
    	LOGGER.info("Registered ores");
    	MubbleGenerators.registerTrees();
    	LOGGER.info("Registered trees");
    	MubbleGenerators.registerSpawns();
    	LOGGER.info("Registered entity spawns");
    	MubbleWorldTypes.registerWorldTypes();
    	LOGGER.info("Registered world types");
    }
    
    @SubscribeEvent
    public void serverSetup(final FMLServerStartingEvent event)
    {
    	//TODO
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeRegistryEvents
    {        
        @SubscribeEvent
        public static void onDimensionsRegistry(final RegisterDimensionsEvent event)
        {
        	MubbleDimensions.registerDimensions();
        	LOGGER.info("Registered dimensions");
        }
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ModRegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
        {
        	event.getRegistry().registerAll(MubbleBlocks.BLOCKS.toArray(new Block[0]));
        	LOGGER.info("Registered " + MubbleBlocks.BLOCKS.size() + " blocks");
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
        	int count = MubbleBlocks.CUBES.size() +
        			MubbleBlocks.STAIRS.size() +
        			MubbleBlocks.SLABS.size() +
        			MubbleBlocks.VERTICAL_SLABS.size() +
        			MubbleBlocks.FENCES.size() +
        			MubbleBlocks.WALLS.size() +
        			MubbleBlocks.SAPLINGS.size() +
        			MubbleBlocks.LEAVES.size() +
        			MubbleBlocks.LEAF_PILES.size() +
        			MubbleBlocks.PRESSURE_PLATES.size() +
        			MubbleBlocks.TRAPDOORS.size() +
        			MubbleBlocks.BUTTONS.size() +
        			MubbleBlocks.FENCE_GATES.size() +
        			MubbleBlocks.DOORS.size() +
        			MubbleBlocks.FLOWERS.size() +
        			MubbleBlocks.FLOWER_PILES.size() +
        			MubbleBlocks.OTHERS.size() +
        			MubbleItems.ITEMS.size();
        	LOGGER.info("Registered " + count + " items");
    		event.getRegistry().registerAll(MubbleCostumes.COSTUMES.toArray(new Item[0]));
        	LOGGER.info("Registered " + MubbleCostumes.COSTUMES.size() + " costumes");
        }
        
        @SubscribeEvent
        public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event)
        {
        	IForgeRegistry<EntityType<?>> registry = event.getRegistry();
        	MubbleEntities.registerEntities(registry);
        	LOGGER.info("Registered entities");
        	MubbleEntities.registerPlacements();
        	LOGGER.info("Registered entity spawn placements");
        }
        
        @SubscribeEvent
        public static void onSoundsRegistry(final RegistryEvent.Register<SoundEvent> event)
        {
        	event.getRegistry().registerAll(MubbleSounds.SOUNDS.toArray(new SoundEvent[0]));
        	LOGGER.info("Registered " + MubbleSounds.SOUNDS.size() + " sounds");
        }
        
        @SubscribeEvent
        public static void onPotionsRegistry(final RegistryEvent.Register<Effect> event)
        {
        	event.getRegistry().registerAll(MubbleEffects.EFFECTS.toArray(new Effect[0]));
        	LOGGER.info("Registered " + MubbleEffects.EFFECTS.size() + " effects");
        }
        
        @SubscribeEvent
        public static void onBiomesRegistry(final RegistryEvent.Register<Biome> event)
        {
        	event.getRegistry().registerAll(MubbleBiomes.BIOMES.toArray(new Biome[0]));
        	MubbleBiomes.registerGenerations();
        	LOGGER.info("Registered " + MubbleBiomes.BIOMES.size() + " biomes");
        }
        
    	@OnlyIn(Dist.CLIENT)
    	@SubscribeEvent
        public static void blockColorsRegistry(final ColorHandlerEvent.Block event)
        {
        	MubbleColorMaps.registerBlockColors(event);
        	LOGGER.info("Registered color maps for blocks");
        }

    	@OnlyIn(Dist.CLIENT)
    	@SubscribeEvent
        public static void itemColorsRegistry(final ColorHandlerEvent.Item event)
        {
        	MubbleColorMaps.registerItemColors(event);
        	LOGGER.info("Registered color maps for items");
        }
    }
}