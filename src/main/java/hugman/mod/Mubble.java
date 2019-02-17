package hugman.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleTabs;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Mubble.ID)
public class Mubble 
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String ID = "mubble";
	
    public Mubble()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void registerBlocks(final RegistryEvent.Register<Block> event)
    {
    	event.getRegistry().register(MubbleBlocks.QUESTION_BLOCK);
    }
    
    private void registerItems(final RegistryEvent.Register<Item> event)
    {
        Item.Properties blocks = new Item.Properties().group(MubbleTabs.MUBBLE_BLOCKS);

        event.getRegistry().register(new ItemBlock(MubbleBlocks.QUESTION_BLOCK, blocks).setRegistryName("question_block"));
    }
    
    public static Logger getLogger()
    {
        return LOGGER;
    }
}