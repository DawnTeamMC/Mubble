package hugman.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class Mubble 
{
	private static final Logger LOGGER = LogManager.getLogger();
	
    public Mubble()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(SoundEvent.class, this::registerSounds);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
	public void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(MubbleBlocks.BLOCKS.toArray(new Block[0]));
	}
	
	public void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(MubbleItems.ITEMS.toArray(new Item[0]));
	}
    
    public void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
    	MubbleSounds.registerSounds();
    }
	
    public static Logger getLogger()
    {
        return LOGGER;
    }
}