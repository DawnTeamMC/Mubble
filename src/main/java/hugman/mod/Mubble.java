package hugman.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Reference.MOD_ID)
public class Mubble 
{
	private static final Logger LOGGER = LogManager.getLogger();
	
    public Mubble()
    {        
        MinecraftForge.EVENT_BUS.register(this);
    }
	
    public static Logger getLogger()
    {
        return LOGGER;
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
        {
        	event.getRegistry().registerAll(MubbleBlocks.BLOCKS.toArray(new Block[0]));
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
    		event.getRegistry().registerAll(MubbleItems.ITEMS.toArray(new Item[0]));
    		event.getRegistry().registerAll(MubbleCostumes.COSTUMES.toArray(new Item[0]));
        }
        
        @SubscribeEvent
        public static void onSoundsRegistry(final RegistryEvent.Register<SoundEvent> event)
        {
        	 event.getRegistry().registerAll(MubbleSounds.SOUNDS.toArray(new SoundEvent[0]));
        }
    }
}