package hugman.mod.util.handlers;

import java.io.IOException;

import hugman.mod.Main;
import hugman.mod.init.BiomeInit;
import hugman.mod.init.BlockInit;
import hugman.mod.init.CostumeInit;
import hugman.mod.init.DimensionInit;
//import hugman.mod.init.DimensionInit;
import hugman.mod.init.EntityInit;
import hugman.mod.init.ItemInit;
import hugman.mod.init.RecipeInit;
import hugman.mod.util.interfaces.IHasModel;
import hugman.mod.world.gen.WorldGenCustomOres;
import hugman.mod.world.gen.WorldGenCustomStructures;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class RegistryHandler
{
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
		event.getRegistry().registerAll(CostumeInit.COSTUMES.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Block block : BlockInit.BLOCKS)
		{
			if(block instanceof IHasModel)
			{
				((IHasModel)block).registerModels();
			}
		}
		
		for(Item item : ItemInit.ITEMS)
		{
			if(item instanceof IHasModel)
			{
				((IHasModel)item).registerModels();
			}
		}
		
		for(Item costume : CostumeInit.COSTUMES)
		{
			if(costume instanceof IHasModel)
			{
				((IHasModel)costume).registerModels();
			}
		}
	}
	
	public static void preInitRegistries() throws IOException
	{
		SoundHandler.registerSounds();
		
		GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);
		BiomeInit.registerBiomes();
		DimensionInit.registerDimensions();
		
		EntityInit.registerEntities();
		Main.proxy.registerEntityRenderers();
	}
	
	public static void initRegistries()
	{
		RecipeInit.addRecipes();
	}
	
	public static void serverInitRegistries()
	{
		DimensionInit.createFiles();
	}
	
	private static final ResourceLocation PURPLE_TETRIS_BLOCK = new ResourceLocation("mubble", "purple_tetris_block");
	private static final ResourceLocation CLOUD_BLOCK = new ResourceLocation("mubble", "cloud_block");
	
	@SubscribeEvent
	public static void onMissingBlockMappings(final RegistryEvent.MissingMappings<Block> event)
	{
	    for (final RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings())
	    {
	        if (RegistryHandler.PURPLE_TETRIS_BLOCK.equals(mapping.key))
	        {
	            mapping.remap(BlockInit.PINK_TETRIS_BLOCK);
	            return;
	        }
	        if (RegistryHandler.CLOUD_BLOCK.equals(mapping.key))
	        {
	            mapping.remap(BlockInit.WHITE_CLOUD_BLOCK);
	            return;
	        }
	    }
	}
	
	@SubscribeEvent
	public static void onMissingItemMappings(final RegistryEvent.MissingMappings<Item> event)
	{
	    for (final RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings())
	    {
	        if (RegistryHandler.PURPLE_TETRIS_BLOCK.equals(mapping.key))
	        {
	            mapping.remap(Item.getItemFromBlock(BlockInit.PINK_TETRIS_BLOCK));
	            return;
	        }
	        if (RegistryHandler.CLOUD_BLOCK.equals(mapping.key))
	        {
	            mapping.remap(Item.getItemFromBlock(BlockInit.WHITE_CLOUD_BLOCK));
	            return;
	        }
	    }
	}
}
