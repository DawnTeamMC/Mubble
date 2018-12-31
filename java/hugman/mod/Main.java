package hugman.mod;

import java.io.IOException;

import hugman.mod.proxy.CommonProxy;
import hugman.mod.util.Reference;
import hugman.mod.util.handlers.RegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Main 
{
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.COMMONPROXY)
	public static CommonProxy proxy;

	public static final CreativeTabs MUBBLE_BLOCKS = new MubbleBlocksTab("mubble_blocks_tab");
	public static final CreativeTabs MUBBLE_ITEMS = new MubbleItemsTab("mubble_items_tab");
	public static final CreativeTabs MUBBLE_COSTUMES = new MubbleCostumesTab("mubble_costumes_tab");
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) throws IOException
	{
		RegistryHandler.preInitRegistries();
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		RegistryHandler.initRegistries();
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public static void serverInit(FMLServerAboutToStartEvent event) throws IOException
	{
		RegistryHandler.serverInitRegistries();
	}
}
