package hugman.mubble;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleCommands;
import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleRenderLayers;
import hugman.mubble.init.MubbleScreens;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleColorMaps;
import hugman.mubble.init.data.MubbleContainerTypes;
import hugman.mubble.init.data.MubbleTileEntityTypes;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.init.world.MubbleGenerators;
import hugman.mubble.objects.event_handler.KonamiHandler;
import hugman.mubble.util.MoreWordUtils;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Mubble.MOD_ID)
public class Mubble 
{
	public static final String MOD_ID = "mubble";
	public static final String MOD_PREFIX = MOD_ID + ":";
	public static final Logger LOGGER = LogManager.getLogger();
	
    public Mubble()
    {
    	IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    	MinecraftForge.EVENT_BUS.register(this);
        modBus.addListener(this::setup);
        modBus.addListener(this::clientSetup);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	MubbleGenerators.registerOres();
    	LOGGER.info("Registered ores");
    	MubbleGenerators.registerTrees();
    	LOGGER.info("Registered trees");
    	MubbleGenerators.registerSpawns();
    	LOGGER.info("Registered entity spawns");
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
    	MubbleRenderLayers.registerBlockLayers();
    	LOGGER.info("Registered block render layers");
    	MubbleEntities.registerRenders();
    	LOGGER.info("Registered entity renders");
    	MubbleScreens.registerScreens();
    	LOGGER.info("Registered screens");
    	MinecraftForge.EVENT_BUS.register(new KonamiHandler());
    }
    
    @SubscribeEvent
    public void serverSetup(final FMLServerStartingEvent event)
    {
    	MubbleCommands.registerCommands(event.getCommandDispatcher());
    	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleCommands.COMMANDS.size(), "command"));
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ModRegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
        {
        	event.getRegistry().registerAll(MubbleBlocks.BLOCKS.toArray(new Block[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleBlocks.BLOCKS.size(), "block"));
        }
        
        @SubscribeEvent
        public static void onTileEntitiesRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
        {
        	event.getRegistry().registerAll(MubbleTileEntityTypes.TILE_ENTITY_TYPES.toArray(new TileEntityType<?>[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleTileEntityTypes.TILE_ENTITY_TYPES.size(), "tile entity"));
        }
        
        @SubscribeEvent
        public static void onContainersRegistry(final RegistryEvent.Register<ContainerType<?>> event)
        {
        	event.getRegistry().registerAll(MubbleContainerTypes.CONTAINER_TYPES.toArray(new ContainerType<?>[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleContainerTypes.CONTAINER_TYPES.size(), "container"));
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
            event.getRegistry().registerAll(MubbleBlocks.BALLOONS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.CLOUD_BLOCKS.toArray(new Item[0]));
            event.getRegistry().registerAll(MubbleBlocks.OTHERS.toArray(new Item[0]));
        	event.getRegistry().registerAll(MubbleItems.ITEMS.toArray(new Item[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleItems.ITEMS.size(), "item"));
    		event.getRegistry().registerAll(MubbleCostumes.COSTUMES.toArray(new Item[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleCostumes.COSTUMES.size(), "costume"));
        }
        
        @SubscribeEvent
        public static void onEnchantmentsRegistry(final RegistryEvent.Register<Enchantment> event)
        {
        	event.getRegistry().registerAll(MubbleEnchantments.ENCHANTMENTS.toArray(new Enchantment[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleEnchantments.ENCHANTMENTS.size(), "enchantment"));
        }
        
        @SubscribeEvent
        public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event)
        {
        	event.getRegistry().registerAll(MubbleEntities.ENTITY_TYPES.toArray(new EntityType<?>[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleEntities.ENTITY_TYPES.size(), "entity"));
        	MubbleEntities.registerPlacements();
        	LOGGER.info("Registered entity spawn placements");
        }
        
        @SubscribeEvent
        public static void onSoundsRegistry(final RegistryEvent.Register<SoundEvent> event)
        {
        	event.getRegistry().registerAll(MubbleSounds.SOUNDS.toArray(new SoundEvent[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleSounds.SOUNDS.size(), "sound"));
        }
        
        @SubscribeEvent
        public static void onPotionsRegistry(final RegistryEvent.Register<Effect> event)
        {
        	event.getRegistry().registerAll(MubbleEffects.EFFECTS.toArray(new Effect[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleEffects.EFFECTS.size(), "effect"));
        }
        
        @SubscribeEvent
        public static void onBiomesRegistry(final RegistryEvent.Register<Biome> event)
        {
        	event.getRegistry().registerAll(MubbleBiomes.BIOMES.toArray(new Biome[0]));
        	MubbleBiomes.registerGenerations();
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleBiomes.BIOMES.size(), "biome"));
        }
        
        @SubscribeEvent
        public static void onModDimensionsRegistry(final RegistryEvent.Register<ModDimension> event)
        {
        	event.getRegistry().registerAll(MubbleDimensions.MOD_DIMENSIONS.toArray(new ModDimension[0]));
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleDimensions.MOD_DIMENSIONS.size(), "mod dimension"));
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
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeRegistryEvents
    {
        @SubscribeEvent
        public static void onDimensionTypesRegistry(final RegisterDimensionsEvent event)
        {
        	MubbleDimensions.registerDimensions();
        	LOGGER.info("Registered " + MoreWordUtils.numerate(MubbleDimensions.DIMENSIONS_TYPES.size(), "dimension type"));
        }
    }
}