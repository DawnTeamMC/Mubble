package hugman.mod;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleEntities;
import hugman.mod.init.MubbleItems;
import hugman.mod.proxy.CommonProxy;
import hugman.mod.util.Reference;
import hugman.mod.util.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MODID)
public class Mubble 
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    public Mubble()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
    	RegistryHandler.preInitRegistries();
    	RegistryHandler.initRegistries();
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
    	MubbleEntities.registerEntityRenderers();
    }
    
    @SubscribeEvent
    public static void onServerPreStarting(FMLServerAboutToStartEvent event)
    {
    	RegistryHandler.preServerInitRegistries();
    }
    
    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event)
    {
    	RegistryHandler.serverInitRegistries(event);
    }
}
