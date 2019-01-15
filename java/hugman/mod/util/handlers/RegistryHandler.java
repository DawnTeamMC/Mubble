package hugman.mod.util.handlers;

import java.io.IOException;

import hugman.mod.Main;
import hugman.mod.entity.EntityFlyingBlock;
import hugman.mod.init.MubbleBiomes;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleCommands;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleDimensions;
import hugman.mod.init.MubbleEntities;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleRecipes;
import hugman.mod.objects.command.CommandMotion;
import hugman.mod.util.Reference;
import hugman.mod.util.interfaces.IHasModel;
import hugman.mod.world.gen.WorldGenCustomOres;
import hugman.mod.world.gen.WorldGenCustomStructures;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class RegistryHandler
{
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(MubbleBlocks.BLOCKS.toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(MubbleItems.ITEMS.toArray(new Item[0]));
		event.getRegistry().registerAll(MubbleCostumes.COSTUMES.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Block block : MubbleBlocks.BLOCKS)
		{
			if(block instanceof IHasModel)
			{
				((IHasModel)block).registerModels();
			}
		}
		
		for(Item item : MubbleItems.ITEMS)
		{
			if(item instanceof IHasModel)
			{
				((IHasModel)item).registerModels();
			}
		}
		
		for(Item costume : MubbleCostumes.COSTUMES)
		{
			if(costume instanceof IHasModel)
			{
				((IHasModel)costume).registerModels();
			}
		}
	}
	
	@SubscribeEvent
    public static void onEntityRegistry(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityFlyingBlock.class)
                .id(new ResourceLocation(Reference.MODID, "flying_block"), 0)
                .name(Reference.MODID + ".flying_block")
                .tracker(256, 1, true)
                .build()
        );
    }
	
	public static void preInitRegistries() throws IOException
	{
		SoundHandler.registerSounds();
		
		GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);
		MubbleBiomes.registerBiomes();
		MubbleDimensions.registerDimensions();
		
		MubbleEntities.registerEntities();
		Main.proxy.registerEntityRenderers();
	}
	
	public static void initRegistries()
	{
		MubbleRecipes.addRecipes();
	}
	
	public static void serverInitRegistries(FMLServerStartingEvent event)
	{
		MubbleCommands.addCommands(event);
	}
	
	public static void preServerInitRegistries()
	{
		MubbleDimensions.createFiles();
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
	            mapping.remap(MubbleBlocks.PINK_TETRIS_BLOCK);
	            return;
	        }
	        if (RegistryHandler.CLOUD_BLOCK.equals(mapping.key))
	        {
	            mapping.remap(MubbleBlocks.WHITE_CLOUD_BLOCK);
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
	            mapping.remap(Item.getItemFromBlock(MubbleBlocks.PINK_TETRIS_BLOCK));
	            return;
	        }
	        if (RegistryHandler.CLOUD_BLOCK.equals(mapping.key))
	        {
	            mapping.remap(Item.getItemFromBlock(MubbleBlocks.WHITE_CLOUD_BLOCK));
	            return;
	        }
	    }
	}
}
