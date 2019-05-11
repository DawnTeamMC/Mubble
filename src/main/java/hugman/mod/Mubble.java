package hugman.mod;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleColorMaps;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleEntities;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubblePotionEffects;
import hugman.mod.init.MubbleSounds;
import hugman.mod.init.world.MubbleBiomes;
import hugman.mod.init.world.MubbleDimensions;
import hugman.mod.init.world.MubbleGenerators;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
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
    	MubbleGenerators.initOres();
    	LOGGER.info("Registered ores to world generation");
    	MubbleGenerators.initTrees();
    	LOGGER.info("Registered trees to world generation");
    	MubbleGenerators.initSpawns();
    	LOGGER.info("Registered entity spawns to world generation");
    }
    
    @SubscribeEvent
    public void serverSetup(final FMLServerStartingEvent event) throws IOException
    {
    	MubbleDimensions.createFiles(event.getServer());
    	LOGGER.info("Created dimensions files");
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeRegistryEvents
    {
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
        	LOGGER.info("Registered blocks");
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
        	event.getRegistry().registerAll(MubbleItems.ITEMS.toArray(new Item[0]));
        	LOGGER.info("Registered items");
    		event.getRegistry().registerAll(MubbleCostumes.COSTUMES.toArray(new Item[0]));
        	LOGGER.info("Registered costumes");
        }
        
        @SubscribeEvent
        public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event)
        {
        	IForgeRegistry<EntityType<?>> registry = event.getRegistry();
        	MubbleEntities.registerEntities(registry);
        	LOGGER.info("Registered entities");
        }
        
        @SubscribeEvent
        public static void onSoundsRegistry(final RegistryEvent.Register<SoundEvent> event)
        {
        	event.getRegistry().registerAll(MubbleSounds.SOUNDS.toArray(new SoundEvent[0]));
        	LOGGER.info("Registered sounds");
        }
        
        @SubscribeEvent
        public static void onPotionsRegistry(final RegistryEvent.Register<Potion> event)
        {
        	event.getRegistry().registerAll(MubblePotionEffects.EFFECTS.toArray(new Potion[0]));
        	LOGGER.info("Registered potion effects");
        }
        
        @SubscribeEvent
        public static void onBiomesRegistry(final RegistryEvent.Register<Biome> event)
        {
        	event.getRegistry().registerAll(MubbleBiomes.BIOMES.toArray(new Biome[0]));
        	MubbleBiomes.registerGenerations();
        	LOGGER.info("Registered biomes");
        }
    }
}