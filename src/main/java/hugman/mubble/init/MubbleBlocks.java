package hugman.mubble.init;

import hugman.mubble.init.data.MubbleSoundTypes;
import hugman.mubble.init.world.MubbleConfiguredFeatures;
import hugman.mubble.object.block.CakeBlock;
import hugman.mubble.object.block.GrassBlock;
import hugman.mubble.object.block.MushroomPlantBlock;
import hugman.mubble.object.block.NoteBlock;
import hugman.mubble.object.block.OreBlock;
import hugman.mubble.object.block.RootsBlock;
import hugman.mubble.object.block.SaplingBlock;
import hugman.mubble.object.block.*;
import hugman.mubble.object.block.sapling_generator.*;
import hugman.mubble.util.entry.BlockEntry.Builder;
import hugman.mubble.util.entry.BlockTemplate;
import hugman.mubble.util.entry.EntryHelper;
import hugman.mubble.util.entry.pack.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

public class MubbleBlocks {
	/* MUBBLE */
	public static final Block OAK_VERTICAL_SLAB = new Builder("oak", BlockTemplate.VERTICAL_SLAB, Blocks.OAK_SLAB).build();
	public static final Block SPRUCE_VERTICAL_SLAB = new Builder("spruce", BlockTemplate.VERTICAL_SLAB, Blocks.SPRUCE_SLAB).build();
	public static final Block BIRCH_VERTICAL_SLAB = new Builder("birch", BlockTemplate.VERTICAL_SLAB, Blocks.BIRCH_SLAB).build();
	public static final Block JUNGLE_VERTICAL_SLAB = new Builder("jungle", BlockTemplate.VERTICAL_SLAB, Blocks.JUNGLE_SLAB).build();
	public static final Block ACACIA_VERTICAL_SLAB = new Builder("acacia", BlockTemplate.VERTICAL_SLAB, Blocks.ACACIA_SLAB).build();
	public static final Block DARK_OAK_VERTICAL_SLAB = new Builder("dark_oak", BlockTemplate.VERTICAL_SLAB, Blocks.DARK_OAK_SLAB).build();
	public static final Block CRIMSON_VERTICAL_SLAB = new Builder("crimson", BlockTemplate.VERTICAL_SLAB, Blocks.CRIMSON_SLAB).build();
	public static final Block WARPED_VERTICAL_SLAB = new Builder("warped", BlockTemplate.VERTICAL_SLAB, Blocks.WARPED_SLAB).build();
	public static final Block STONE_VERTICAL_SLAB = new Builder("stone", BlockTemplate.VERTICAL_SLAB, Blocks.STONE_SLAB).build();
	public static final Block SMOOTH_STONE_VERTICAL_SLAB = new Builder("smooth_stone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_STONE_SLAB).build();
	public static final Block COBBLESTONE_VERTICAL_SLAB = new Builder("cobblestone", BlockTemplate.VERTICAL_SLAB, Blocks.COBBLESTONE_SLAB).build();
	public static final Block MOSSY_COBBLESTONE_VERTICAL_SLAB = new Builder("mossy_cobblestone", BlockTemplate.VERTICAL_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB).build();
	public static final Block STONE_BRICK_VERTICAL_SLAB = new Builder("stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.STONE_BRICK_SLAB).build();
	public static final Block MOSSY_STONE_BRICK_VERTICAL_SLAB = new Builder("mossy_stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB).build();
	public static final Block GRANITE_VERTICAL_SLAB = new Builder("granite", BlockTemplate.VERTICAL_SLAB, Blocks.GRANITE_SLAB).build();
	public static final Block POLISHED_GRANITE_VERTICAL_SLAB = new Builder("polished_granite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_GRANITE_SLAB).build();
	public static final Block DIORITE_VERTICAL_SLAB = new Builder("diorite", BlockTemplate.VERTICAL_SLAB, Blocks.DIORITE_SLAB).build();
	public static final Block POLISHED_DIORITE_VERTICAL_SLAB = new Builder("polished_diorite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_DIORITE_SLAB).build();
	public static final Block ANDESITE_VERTICAL_SLAB = new Builder("andesite", BlockTemplate.VERTICAL_SLAB, Blocks.ANDESITE_SLAB).build();
	public static final Block POLISHED_ANDESITE_VERTICAL_SLAB = new Builder("polished_andesite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_ANDESITE_SLAB).build();
	public static final Block BRICK_VERTICAL_SLAB = new Builder("brick", BlockTemplate.VERTICAL_SLAB, Blocks.BRICK_SLAB).build();
	public static final Block SANDSTONE_VERTICAL_SLAB = new Builder("sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SANDSTONE_SLAB).build();
	public static final Block CUT_SANDSTONE_VERTICAL_SLAB = new Builder("cut_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.CUT_SANDSTONE_SLAB).build();
	public static final Block SMOOTH_SANDSTONE_VERTICAL_SLAB = new Builder("smooth_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB).build();
	public static final Block RED_SANDSTONE_VERTICAL_SLAB = new Builder("red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.RED_SANDSTONE_SLAB).build();
	public static final Block CUT_RED_SANDSTONE_VERTICAL_SLAB = new Builder("cut_red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB).build();
	public static final Block SMOOTH_RED_SANDSTONE_VERTICAL_SLAB = new Builder("smooth_red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB).build();
	public static final Block NETHER_BRICK_VERTICAL_SLAB = new Builder("nether_brick", BlockTemplate.VERTICAL_SLAB, Blocks.NETHER_BRICK_SLAB).build();
	public static final Block RED_NETHER_BRICK_VERTICAL_SLAB = new Builder("red_nether_brick", BlockTemplate.VERTICAL_SLAB, Blocks.RED_NETHER_BRICK_SLAB).build();
	public static final Block QUARTZ_VERTICAL_SLAB = new Builder("quartz", BlockTemplate.VERTICAL_SLAB, Blocks.QUARTZ_SLAB).build();
	public static final Block SMOOTH_QUARTZ_VERTICAL_SLAB = new Builder("smooth_quartz", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_QUARTZ_SLAB).build();
	public static final Block END_STONE_BRICK_VERTICAL_SLAB = new Builder("end_stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.END_STONE_BRICK_SLAB).build();
	public static final Block PURPUR_VERTICAL_SLAB = new Builder("purpur", BlockTemplate.VERTICAL_SLAB, Blocks.PURPUR_SLAB).build();
	public static final Block PRISMARINE_VERTICAL_SLAB = new Builder("prismarine", BlockTemplate.VERTICAL_SLAB, Blocks.PRISMARINE_SLAB).build();
	public static final Block PRISMARINE_BRICK_VERTICAL_SLAB = new Builder("prismarine_brick", BlockTemplate.VERTICAL_SLAB, Blocks.PRISMARINE_BRICK_SLAB).build();
	public static final Block DARK_PRISMARINE_VERTICAL_SLAB = new Builder("dark_prismarine", BlockTemplate.VERTICAL_SLAB, Blocks.DARK_PRISMARINE_SLAB).build();
	public static final Block BLACKSTONE_VERTICAL_SLAB = new Builder("blackstone", BlockTemplate.VERTICAL_SLAB, Blocks.BLACKSTONE_SLAB).build();
	public static final Block POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB = new Builder("polished_blackstone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB).build();
	public static final Block POLISHED_BLACKSTONE_SLAB = new Builder("polished_blackstone", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB).build();

	public static final Block DARK_PRISMARINE_WALL = new Builder("dark_prismarine", BlockTemplate.WALL, Blocks.DARK_PRISMARINE).build();

	public static final MSBlockEntryPack OAK_WOOD_BLOCKS = new MSBlockEntryPack("oak_wood", Blocks.OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockEntryPack SPRUCE_WOOD_BLOCKS = new MSBlockEntryPack("spruce_wood", Blocks.SPRUCE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockEntryPack BIRCH_WOOD_BLOCKS = new MSBlockEntryPack("birch_wood", Blocks.BIRCH_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockEntryPack JUNGLE_WOOD_BLOCKS = new MSBlockEntryPack("jungle_wood", Blocks.JUNGLE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockEntryPack ACACIA_WOOD_BLOCKS = new MSBlockEntryPack("acacia_wood", Blocks.ACACIA_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockEntryPack DARK_OAK_WOOD_BLOCKS = new MSBlockEntryPack("dark_oak_wood", Blocks.DARK_OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockEntryPack CRIMSON_HYPHAE_BLOCKS = new MSBlockEntryPack("crimson_hyphae", Blocks.CRIMSON_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockEntryPack WARPED_HYPHAE_BLOCKS = new MSBlockEntryPack("warped_hyphae", Blocks.WARPED_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);

	public static final Block OAK_LEAF_PILE = new Builder("oak", BlockTemplate.PILE, Settings.LEAF_PILE).copy(Blocks.OAK_LEAVES).build();
	public static final Block SPRUCE_LEAF_PILE = new Builder("spruce", BlockTemplate.PILE, Settings.LEAF_PILE).copy(Blocks.OAK_LEAVES).build();
	public static final Block BIRCH_LEAF_PILE = new Builder("birch", BlockTemplate.PILE, Settings.LEAF_PILE).copy(Blocks.OAK_LEAVES).build();
	public static final Block JUNGLE_LEAF_PILE = new Builder("jungle", BlockTemplate.PILE, Settings.LEAF_PILE).copy(Blocks.OAK_LEAVES).build();
	public static final Block ACACIA_LEAF_PILE = new Builder("acacia", BlockTemplate.PILE, Settings.LEAF_PILE).copy(Blocks.OAK_LEAVES).build();
	public static final Block DARK_OAK_LEAF_PILE = new Builder("dark_oak", BlockTemplate.PILE, Settings.LEAF_PILE).copy(Blocks.OAK_LEAVES).build();

	public static final MSBlockEntryPack COBBLESTONE_BRICKS = new MSBlockEntryPack("cobblestone_bricks", Blocks.MOSSY_COBBLESTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack MOSSY_COBBLESTONE_BRICKS = new MSBlockEntryPack("mossy_cobblestone_bricks", Blocks.MOSSY_COBBLESTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack GRANITE_BRICKS = new MSBlockEntryPack("granite_bricks", Blocks.GRANITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack DIORITE_BRICKS = new MSBlockEntryPack("diorite_bricks", Blocks.DIORITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack ANDESITE_BRICKS = new MSBlockEntryPack("andesite_bricks", Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);

	public static final MSBlockEntryPack SANDSTONE_BRICKS = new MSBlockEntryPack("sandstone_bricks", Blocks.SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack POLISHED_SANDSTONE = new MSBlockEntryPack("polished_sandstone", Blocks.SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);
	public static final MSBlockEntryPack RED_SANDSTONE_BRICKS = new MSBlockEntryPack("red_sandstone_bricks", Blocks.RED_SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack POLISHED_RED_SANDSTONE = new MSBlockEntryPack("polished_red_sandstone", Blocks.RED_SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final MSBlockEntryPack SMOOTH_STONE_PAVING = new MSBlockEntryPack("smooth_stone_paving", Blocks.SMOOTH_STONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);
	public static final MSBlockEntryPack CHISELED_PRISMARINE = new MSBlockEntryPack("chiseled_prismarine", Blocks.PRISMARINE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack PRISMARINE_BRICK_PAVING = new MSBlockEntryPack("prismarine_brick_paving", Blocks.PRISMARINE_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final MSBlockEntryPack BLUNITE_BLOCKS = new MSBlockEntryPack("blunite", MaterialColor.LIGHT_BLUE_TERRACOTTA, Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack CARBONITE_BLOCKS = new MSBlockEntryPack("carbonite", MaterialColor.BLACK, Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack POLISHED_BLUNITE = new MSBlockEntryPack("polished_blunite", MubbleBlocks.BLUNITE_BLOCKS.getBlock(BlockTemplate.CUBE), BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);
	public static final MSBlockEntryPack POLISHED_CARBONITE = new MSBlockEntryPack("polished_carbonite", MubbleBlocks.CARBONITE_BLOCKS.getBlock(BlockTemplate.CUBE), BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final Block VANADIUM_ORE = new Builder("vanadium_ore", new OreBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE))).copy(Blocks.DIAMOND_ORE).build();
	public static final Block VANADIUM_BLOCK = new Builder("vanadium_block", BlockTemplate.CUBE, Blocks.DIAMOND_BLOCK, MaterialColor.MAGENTA).build();

	public static final PottedPlantEntryPack AUTUMN_OAK_SAPLING = new PottedPlantEntryPack(new Builder("autumn_oak_sapling", new SaplingBlock(new AutumnOakSaplingGenerator(), Settings.SAPLING)).build());
	public static final LeavesEntryPack AUTUMN_OAK_LEAVES = new LeavesEntryPack("autumn_oak");
	public static final PottedPlantEntryPack AUTUMN_BIRCH_SAPLING = new PottedPlantEntryPack(new Builder("autumn_birch_sapling", new SaplingBlock(new AutumnBirchSaplingGenerator(), Settings.SAPLING)).build());
	public static final LeavesEntryPack AUTUMN_BIRCH_LEAVES = new LeavesEntryPack("autumn_birch");

	public static final WoodTypeEntryPack CHERRY_OAK_WOOD = new WoodTypeEntryPack("cherry_oak", MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707, false);
	public static final PottedPlantEntryPack PINK_CHERRY_OAK_SAPLING = new PottedPlantEntryPack(new Builder("pink_cherry_oak_sapling", new SaplingBlock(new PinkCherryOakSaplingGenerator(), Settings.SAPLING)).build());
	public static final PottedPlantEntryPack WHITE_CHERRY_OAK_SAPLING = new PottedPlantEntryPack(new Builder("white_cherry_oak_sapling", new SaplingBlock(new WhiteCherryOakSaplingGenerator(), Settings.SAPLING)).build());
	public static final LeavesEntryPack PINK_CHERRY_OAK_LEAVES = new LeavesEntryPack("pink_cherry_oak");
	public static final LeavesEntryPack WHITE_CHERRY_OAK_LEAVES = new LeavesEntryPack("white_cherry_oak");

	public static final NormalWoodTypeEntryPack PALM_WOOD = new NormalWoodTypeEntryPack("palm", new PalmSaplingGenerator(), MaterialColor.ORANGE, MaterialColor.CYAN_TERRACOTTA);

	public static final MSCBlockEntryPack STAINED_BRICK_BLOCKS = new MSCBlockEntryPack("bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack TERRACOTTA_BLOCKS = new MSBlockEntryPack("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockEntryPack STAINED_TERRACOTTA_BLOCKS = new MSCBlockEntryPack("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockEntryPack DARK_PRISMARINE_BLOCKS = new MSCBlockEntryPack("dark_prismarine", Blocks.DARK_PRISMARINE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSCBlockEntryPack CONCRETE_BLOCKS = new MSCBlockEntryPack("concrete", Blocks.BLUE_CONCRETE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockEntryPack QUARTZ_PAVING_BLOCKS = new MSCBlockEntryPack("quartz_paving", Blocks.QUARTZ_BLOCK, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final Block BLUE_CHRISTMAS_BAUBLE = new Builder("blue_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.BLUE_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block LIGHT_BLUE_CHRISTMAS_BAUBLE = new Builder("light_blue_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.LIGHT_BLUE_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block PURPLE_CHRISTMAS_BAUBLE = new Builder("purple_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.PURPLE_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block MAGENTA_CHRISTMAS_BAUBLE = new Builder("magenta_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.MAGENTA_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block PINK_CHRISTMAS_BAUBLE = new Builder("pink_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.PINK_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block RED_CHRISTMAS_BAUBLE = new Builder("red_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.RED_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block ORANGE_CHRISTMAS_BAUBLE = new Builder("orange_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.ORANGE_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block YELLOW_CHRISTMAS_BAUBLE = new Builder("yellow_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.YELLOW_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block GREEN_CHRISTMAS_BAUBLE = new Builder("green_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.GREEN_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block WHITE_CHRISTMAS_BAUBLE = new Builder("white_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.WHITE_TERRACOTTA))).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final Block RED_SHINY_GARLAND = new Builder("red_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.RED).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block SILVER_SHINY_GARLAND = new Builder("silver_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.CLAY).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block GOLD_SHINY_GARLAND = new Builder("gold_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.GOLD).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();

	public static final Block WHITE_PRESENT = new Builder("white_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block BLACK_PRESENT = new Builder("black_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLACK_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block BLUE_PRESENT = new Builder("blue_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLUE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block GREEN_PRESENT = new Builder("green_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GREEN_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block YELLOW_PRESENT = new Builder("yellow_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.YELLOW_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block RED_PRESENT = new Builder("red_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.RED_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block PURPLE_PRESENT = new Builder("purple_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.PURPLE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block GOLDEN_PRESENT = new Builder("golden_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GOLD).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();

	public static final Block FOOTBLOCK = new Builder("footblock", BlockTemplate.CUBE, Blocks.WHITE_WOOL).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final MCBlockEntryPack CLOUD_BLOCKS = new MCBlockEntryPack(BlockTemplate.CLOUD_BLOCK, FabricBlockSettings.of(Material.LEAVES).sounds(BlockSoundGroup.WOOL).hardness(0f).noCollision());

	public static final Block TOMATOES = new Builder("tomatoes", new CropsBlock()).setRenderLayer(RenderLayer.getCutout()).noItem().build();
	public static final Block SALAD = new Builder("salad", new CropsBlock()).setRenderLayer(RenderLayer.getCutout()).noItem().build();

	public static final Block BLUEBERRY_BUSH = new Builder("blueberry_bush", new BerryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH))).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).noItem().build();
	public static final Block CHEESE_BLOCK = new Builder("cheese_block", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.YELLOW).hardness(0.5f).sounds(BlockSoundGroup.SNOW))).setFlammability(60, 60).setItemGroup(ItemGroup.FOOD).build();
	public static final Block CHOCOLATE_CAKE = new Builder("chocolate_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).setItemGroup(ItemGroup.FOOD).build();
	public static final Block MINECRAFT_10TH_ANNIVERSARY_CAKE = new Builder("minecraft_10th_anniversary_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).setItemGroup(ItemGroup.FOOD).build();

	public static final MCBlockEntryPack BALLOONS = new MCBlockEntryPack(BlockTemplate.BALLOON, FabricBlockSettings.of(Material.WOOL).hardness(0F).sounds(BlockSoundGroup.WOOL).nonOpaque());

	public static final Block UNSTABLE_STONE = new Builder("unstable_stone", new UnstableBlock(FabricBlockSettings.copy(Blocks.STONE).strength(0.1F, 0.0F))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block FLUID_TANK = new Builder("fluid_tank", new FluidTankBlock(FabricBlockSettings.copy(Blocks.OBSIDIAN).nonOpaque())).setItemGroup(ItemGroup.REDSTONE).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block PLACER = new Builder("placer", new PlacerBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F))).setItemGroup(ItemGroup.REDSTONE).build();
	public static final Block TIMESWAP_TABLE = new Builder("timeswap_table", new TimeswapTableBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F))).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final Block DANDELION_PILE = new Builder("dandelion_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block POPPY_PILE = new Builder("poppy_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block BLUE_ORCHID_PILE = new Builder("blue_orchid_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block ALLIUM_PILE = new Builder("allium_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block AZURE_BLUET_PILE = new Builder("azure_bluet_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block RED_TULIP_PILE = new Builder("red_tulip_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block ORANGE_TULIP_PILE = new Builder("orange_tulip_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block WHITE_TULIP_PILE = new Builder("white_tulip_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block PINK_TULIP_PILE = new Builder("pink_tulip_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block OXEYE_DAISY_PILE = new Builder("oxeye_daisy_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block CORNFLOWER_PILE = new Builder("cornflower_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block LILY_OF_THE_VALLEY_PILE = new Builder("lily_of_the_valley_pile", new PileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final Block WITHER_ROSE_PILE = new Builder("wither_rose_pile", new WitherRosePileBlock(Settings.FLOWER_PILE.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();

	public static final Block PERMAROCK = new Builder("permarock", BlockTemplate.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.4F)).build();
	public static final MSBlockEntryPack PERMAFROST_BRICKS = new MSBlockEntryPack("permafrost_bricks", Blocks.NETHER_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.FENCE);
	public static final MSBlockEntryPack BLUE_PERMAFROST_BRICKS = new MSBlockEntryPack("blue_permafrost_bricks", Blocks.RED_NETHER_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final Block PERMAFROST_BISMUTH_ORE = new Builder("permafrost_bismuth_ore", BlockTemplate.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.3F)).build();
	public static final Block FROZEN_OBSIDIAN = new Builder("frozen_obsidian", BlockTemplate.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).strength(75.0F, 1800.0F)).build();

	public static final Block AMARANTH_DYLIUM = new Builder("amaranth_dylium", new DyliumBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.field_25702).requiresTool().strength(3.0F, 9.0F).sounds(BlockSoundGroup.NYLIUM).ticksRandomly())).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block AMARANTH_WART_BLOCK = new Builder("amaranth_wart_block", new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC, MaterialColor.field_25708).breakByTool(FabricToolTags.HOES).strength(1.0F).sounds(BlockSoundGroup.WART_BLOCK))).build();
	public static final Block AMARANTH_ROOTS = new Builder("amaranth_roots", new RootsBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.CYAN).noCollision().breakInstantly().sounds(BlockSoundGroup.ROOTS))).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final NetherWoodTypeEntryPack DARK_AMARANTH_WOOD = new NetherWoodTypeEntryPack("dark_amaranth", () -> {
		return MubbleConfiguredFeatures.AMARANTH_FUNGI_PLANTED;
	}, MaterialColor.LIGHT_GRAY, MaterialColor.field_25707);


	/* SUPER MARIO (MAKER) */
	public static final Block SMB_QUESTION_BLOCK = new Builder("smb", BlockTemplate.QUESTION_BLOCK, Settings.QUESTION_BLOCK).build();
	public static final Block SMB3_QUESTION_BLOCK = new Builder("smb3", BlockTemplate.QUESTION_BLOCK, Settings.QUESTION_BLOCK).build();
	public static final Block SMW_QUESTION_BLOCK = new Builder("smw", BlockTemplate.QUESTION_BLOCK, Settings.QUESTION_BLOCK).build();
	public static final Block NSMBU_QUESTION_BLOCK = new Builder("nsmbu", BlockTemplate.QUESTION_BLOCK, Settings.QUESTION_BLOCK).build();
	public static final Block SMB_GROUND_GROUND_BLOCK = new Builder("smb_ground_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_UNDERGROUND_GROUND_BLOCK = new Builder("smb_underground_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_UNDERWATER_GROUND_BLOCK = new Builder("smb_underwater_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_GHOST_HOUSE_GROUND_BLOCK = new Builder("smb_ghost_house_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_AIRSHIP_GROUND_BLOCK = new Builder("smb_airship_ground_block", BlockTemplate.CUBE, Blocks.IRON_BLOCK).build();
	public static final Block SMB_NIGHT_AIRSHIP_GROUND_BLOCK = new Builder("smb_night_airship_ground_block", BlockTemplate.CUBE, Blocks.IRON_BLOCK).build();
	public static final Block SMB_CASTLE_GROUND_BLOCK = new Builder("smb_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_DESERT_GROUND_BLOCK = new Builder("smb_desert_ground_block", BlockTemplate.CUBE, Blocks.SAND).build();
	public static final Block SMB_FOREST_GROUND_BLOCK = new Builder("smb_forest_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_SNOW_GROUND_BLOCK = new Builder("smb_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_NIGHT_SNOW_GROUND_BLOCK = new Builder("smb_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_SKY_GROUND_BLOCK = new Builder("smb_sky_ground_block", BlockTemplate.CUBE, Blocks.WHITE_WOOL).build();
	public static final Block SMB3_GROUND_GROUND_BLOCK = new Builder("smb3_ground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_UNDERGROUND_GROUND_BLOCK = new Builder("smb3_underground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_UNDERWATER_GROUND_BLOCK = new Builder("smb3_underwater_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_GHOST_HOUSE_GROUND_BLOCK = new Builder("smb3_ghost_house_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_AIRSHIP_GROUND_BLOCK = new Builder("smb3_airship_ground_block", BlockTemplate.CUBE, Blocks.OAK_WOOD).build();
	public static final Block SMB3_NIGHT_AIRSHIP_GROUND_BLOCK = new Builder("smb3_night_airship_ground_block", BlockTemplate.CUBE, Blocks.OAK_WOOD).build();
	public static final Block SMB3_CASTLE_GROUND_BLOCK = new Builder("smb3_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB3_NIGHT_CASTLE_GROUND_BLOCK = new Builder("smb3_night_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB3_DESERT_GROUND_BLOCK = new Builder("smb3_desert_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.SAND))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_SNOW_GROUND_BLOCK = new Builder("smb3_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_NIGHT_SNOW_GROUND_BLOCK = new Builder("smb3_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_SKY_GROUND_BLOCK = new Builder("smb3_sky_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.WHITE_WOOL))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(30, 60).build();
	public static final Block SMW_GROUND_GROUND_BLOCK = new Builder("smw_ground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_UNDERGROUND_GROUND_BLOCK = new Builder("smw_underground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_UNDERWATER_GROUND_BLOCK = new Builder("smw_underwater_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_GHOST_HOUSE_GROUND_BLOCK = new Builder("smw_ghost_house_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_AIRSHIP_GROUND_BLOCK = new Builder("smw_airship_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.OAK_WOOD))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_CASTLE_GROUND_BLOCK = new Builder("smw_castle_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_DESERT_GROUND_BLOCK = new Builder("smw_desert_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_FOREST_GROUND_BLOCK = new Builder("smw_forest_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_SNOW_GROUND_BLOCK = new Builder("smw_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_NIGHT_SNOW_GROUND_BLOCK = new Builder("smw_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_SKY_GROUND_BLOCK = new Builder("smw_sky_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.WHITE_WOOL))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(30, 60).build();
	public static final Block NSMBU_GROUND_GROUND_BLOCK = new Builder("nsmbu_ground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_UNDERGROUND_GROUND_BLOCK = new Builder("nsmbu_underground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_UNDERWATER_GROUND_BLOCK = new Builder("nsmbu_underwater_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_GHOST_HOUSE_GROUND_BLOCK = new Builder("nsmbu_ghost_house_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_CASTLE_GROUND_BLOCK = new Builder("nsmbu_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block NSMBU_DESERT_GROUND_BLOCK = new Builder("nsmbu_desert_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.SAND))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_FOREST_GROUND_BLOCK = new Builder("nsmbu_forest_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_SNOW_GROUND_BLOCK = new Builder("nsmbu_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_NIGHT_SNOW_GROUND_BLOCK = new Builder("nsmbu_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_SKY_GROUND_BLOCK = new Builder("nsmbu_sky_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_EMPTY_BLOCK = new Builder("smb_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_EMPTY_BLOCK = new Builder("smb3_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_EMPTY_BLOCK = new Builder("smw_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_EMPTY_BLOCK = new Builder("nsmbu_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_ROTATING_BLOCK = new Builder("smb_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_ROTATING_BLOCK = new Builder("smb3_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_ROTATING_BLOCK = new Builder("smw_rotating_block", new RotatingBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_ROTATING_BLOCK = new Builder("nsmbu_rotating_block", new RotatingBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block LIGHT_BLOCK = new Builder("light_block", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(15))).build();
	public static final Block SMB_GROUND_BRICK_BLOCK = new Builder("smb_ground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_UNDERGROUND_BRICK_BLOCK = new Builder("smb_underground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_CASTLE_BRICK_BLOCK = new Builder("smb_castle_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_SNOW_BRICK_BLOCK = new Builder("smb_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_NIGHT_SNOW_BRICK_BLOCK = new Builder("smb_night_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_BRICK_BLOCK = new Builder("smb3_brick_block", new BrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_BRICK_BLOCK = new Builder("smw_brick_block", new BrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_BRICK_BLOCK = new Builder("nsmbu_brick_block", new BrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_GOLDEN_BRICK_BLOCK = new Builder("smb_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_GOLDEN_BRICK_BLOCK = new Builder("smb3_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_GOLDEN_BRICK_BLOCK = new Builder("smw_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_GOLDEN_BRICK_BLOCK = new Builder("nsmbu_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_GROUND_HARD_BLOCK = new Builder("smb_ground_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_UNDERGROUND_HARD_BLOCK = new Builder("smb_underground_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_UNDERWATER_HARD_BLOCK = new Builder("smb_underwater_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_CASTLE_HARD_BLOCK = new Builder("smb_castle_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_SNOW_HARD_BLOCK = new Builder("smb_snow_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_NIGHT_SNOW_HARD_BLOCK = new Builder("smb_night_snow_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB3_HARD_BLOCK = new Builder("smb3_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMW_STONE_HARD_BLOCK = new Builder("smw_stone_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMW_WOOD_HARD_BLOCK = new Builder("smw_wood_hard_block", BlockTemplate.CUBE, Blocks.OAK_PLANKS).build();
	public static final Block NSMBU_HARD_BLOCK = new Builder("nsmbu_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final Block SMB_ICE_BLOCK = new Builder("smb_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final Block SMB3_ICE_BLOCK = new Builder("smb3_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final Block SMW_ICE_BLOCK = new Builder("smw_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final Block NSMBU_ICE_BLOCK = new Builder("nsmbu_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final Block SMB_NOTE_BLOCK = new Builder("smb_note_block", new NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_NOTE_BLOCK = new Builder("smb3_note_block", new NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_NOTE_BLOCK = new Builder("smw_note_block", new NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_NOTE_BLOCK = new Builder("nsmbu_note_block", new NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_SUPER_NOTE_BLOCK = new Builder("smb_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB3_SUPER_NOTE_BLOCK = new Builder("smb3_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMW_SUPER_NOTE_BLOCK = new Builder("smw_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block NSMBU_SUPER_NOTE_BLOCK = new Builder("nsmbu_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block SMB_DOOR = new Builder("smb", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final Block SMB3_DOOR = new Builder("smb3", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final Block SMW_DOOR = new Builder("smw", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final Block NSMBU_DOOR = new Builder("nsmbu", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final Block SMB_KEY_DOOR = new Builder("smb", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();
	public static final Block SMB3_KEY_DOOR = new Builder("smb3", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();
	public static final Block SMW_KEY_DOOR = new Builder("smw", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();
	public static final Block NSMBU_KEY_DOOR = new Builder("nsmbu", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();

	/* SUPER MARIO (OTHERS) */
	public static final PottedPlantEntryPack FIRE_FLOWER = new PottedPlantEntryPack(new Builder("fire_flower", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantEntryPack ICE_FLOWER = new PottedPlantEntryPack(new Builder("ice_flower", new FlowerBlock(StatusEffects.MINING_FATIGUE, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantEntryPack BOOMERANG_FLOWER = new PottedPlantEntryPack(new Builder("boomerang_flower", new FlowerBlock(StatusEffects.HASTE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantEntryPack CLOUD_FLOWER = new PottedPlantEntryPack(new Builder("cloud_flower", new CloudFlowerBlock(StatusEffects.SLOW_FALLING, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantEntryPack GOLD_FLOWER = new PottedPlantEntryPack(new Builder("gold_flower", new FlowerBlock(MubbleStatusEffects.HEAVINESS, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(5))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());

	public static final Block WHITE_MUSHROOM_BLOCK = new Builder("white", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.WHITE)).build();
	public static final Block LIGHT_GRAY_MUSHROOM_BLOCK = new Builder("light_gray", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.LIGHT_GRAY)).build();
	public static final Block GRAY_MUSHROOM_BLOCK = new Builder("gray", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.GRAY)).build();
	public static final Block BLACK_MUSHROOM_BLOCK = new Builder("black", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.BLACK)).build();
	public static final Block ORANGE_MUSHROOM_BLOCK = new Builder("orange", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.ORANGE)).build();
	public static final Block YELLOW_MUSHROOM_BLOCK = new Builder("yellow", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.YELLOW)).build();
	public static final Block LIME_MUSHROOM_BLOCK = new Builder("lime", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.LIME)).build();
	public static final Block GREEN_MUSHROOM_BLOCK = new Builder("green", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.GREEN)).build();
	public static final Block CYAN_MUSHROOM_BLOCK = new Builder("cyan", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.CYAN)).build();
	public static final Block LIGHT_BLUE_MUSHROOM_BLOCK = new Builder("light_blue", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.LIGHT_BLUE)).build();
	public static final Block BLUE_MUSHROOM_BLOCK = new Builder("blue", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.BLUE)).build();
	public static final Block PURPLE_MUSHROOM_BLOCK = new Builder("purple", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.PURPLE)).build();
	public static final Block MAGENTA_MUSHROOM_BLOCK = new Builder("magenta", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.MAGENTA)).build();
	public static final Block PINK_MUSHROOM_BLOCK = new Builder("pink", BlockTemplate.MUSHROOM_BLOCK, Settings.MUSHROOM_BLOCK.materialColor(DyeColor.PINK)).build();
	public static final Block WHITE_MUSHROOM = new Builder("white_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, WHITE_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block LIGHT_GRAY_MUSHROOM = new Builder("light_gray_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, LIGHT_GRAY_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block GRAY_MUSHROOM = new Builder("gray_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, GRAY_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block BLACK_MUSHROOM = new Builder("black_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, BLACK_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block ORANGE_MUSHROOM = new Builder("orange_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, ORANGE_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block YELLOW_MUSHROOM = new Builder("yellow_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, YELLOW_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block LIME_MUSHROOM = new Builder("lime_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, LIME_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block GREEN_MUSHROOM = new Builder("green_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, GREEN_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block CYAN_MUSHROOM = new Builder("cyan_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, CYAN_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block LIGHT_BLUE_MUSHROOM = new Builder("light_blue_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, LIGHT_BLUE_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block BLUE_MUSHROOM = new Builder("blue_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, BLUE_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block PURPLE_MUSHROOM = new Builder("purple_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, PURPLE_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block MAGENTA_MUSHROOM = new Builder("magenta_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, MAGENTA_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final Block PINK_MUSHROOM = new Builder("pink_mushroom", new GrowableMushroomPlantBlock(Settings.MUSHROOM, PINK_MUSHROOM_BLOCK)).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();

	/* KIRBY */
	public static final Block KIRBY_BLOCK = new Builder("kirby_block", new DirectionalBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.PINK).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).setItemGroup(ItemGroup.DECORATIONS).build();

	/* TETRIS */
	public static final Block WHITE_TETRIS_BLOCK = new Builder("white", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.WHITE)).build();
	public static final Block LIGHT_GRAY_TETRIS_BLOCK = new Builder("light_gray", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.LIGHT_GRAY)).build();
	public static final Block GRAY_TETRIS_BLOCK = new Builder("gray", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.GRAY)).build();
	public static final Block BLACK_TETRIS_BLOCK = new Builder("black", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.BLACK)).build();
	public static final Block BROWN_TETRIS_BLOCK = new Builder("brown", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.BROWN)).build();
	public static final Block RED_TETRIS_BLOCK = new Builder("red", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.RED)).build();
	public static final Block ORANGE_TETRIS_BLOCK = new Builder("orange", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.ORANGE)).build();
	public static final Block YELLOW_TETRIS_BLOCK = new Builder("yellow", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.YELLOW)).build();
	public static final Block LIME_TETRIS_BLOCK = new Builder("lime", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.LIME)).build();
	public static final Block GREEN_TETRIS_BLOCK = new Builder("green", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.GREEN)).build();
	public static final Block CYAN_TETRIS_BLOCK = new Builder("cyan", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.CYAN)).build();
	public static final Block LIGHT_BLUE_TETRIS_BLOCK = new Builder("light_blue", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.LIGHT_BLUE)).build();
	public static final Block BLUE_TETRIS_BLOCK = new Builder("blue", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.BLUE)).build();
	public static final Block PURPLE_TETRIS_BLOCK = new Builder("purple", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.PURPLE)).build();
	public static final Block MAGENTA_TETRIS_BLOCK = new Builder("magenta", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.MAGENTA)).build();
	public static final Block PINK_TETRIS_BLOCK = new Builder("pink", BlockTemplate.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.PINK)).build();
	public static final Block TETRIS_GLASS = new Builder("tetris_glass", new TetrisGlassBlock(FabricBlockSettings.copy(Blocks.GLASS))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final Block JAPANESE_TETRIS_CUSHION = new Builder("japanese_tetris_cushion", new FallingBlock(FabricBlockSettings.copy(Blocks.RED_WOOL))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(30, 60).build();
	public static final Block RAINBOW_TETRIS_SCAFFOLDING = new Builder("rainbow_tetris_scaffolding", new FallingBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block MONOCHROME_TETRIS_SCAFFOLDING = new Builder("monochrome_tetris_scaffolding", new FallingBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();

	/* CASTLEVANIA */
	public static final Block VAMPIRE_STONE = new Builder("vampire_stone", new Block(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();

	public static final Block MEDUSA_STONE = new Builder("medusa_stone", new Block(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final MSBlockEntryPack MEDUSA_BRICKS = new MSBlockEntryPack("medusa_bricks", Blocks.STONE_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);

	public static final Block WHITE_CANDY_CANE_PILLAR = new Builder("white_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.WHITE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block LIGHT_GRAY_CANDY_CANE_PILLAR = new Builder("light_gray_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.LIGHT_GRAY))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block GRAY_CANDY_CANE_PILLAR = new Builder("gray_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.GRAY))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block BLACK_CANDY_CANE_PILLAR = new Builder("black_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.BLACK))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block BROWN_CANDY_CANE_PILLAR = new Builder("brown_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.BROWN))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block RED_CANDY_CANE_PILLAR = new Builder("red_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.RED))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block ORANGE_CANDY_CANE_PILLAR = new Builder("orange_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.ORANGE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block YELLOW_CANDY_CANE_PILLAR = new Builder("yellow_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.YELLOW))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block LIME_CANDY_CANE_PILLAR = new Builder("lime_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.LIME))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block GREEN_CANDY_CANE_PILLAR = new Builder("green_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.GREEN))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block CYAN_CANDY_CANE_PILLAR = new Builder("cyan_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.CYAN))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block LIGHT_BLUE_CANDY_CANE_PILLAR = new Builder("light_blue_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.LIGHT_BLUE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block BLUE_CANDY_CANE_PILLAR = new Builder("blue_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.BLUE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block PURPLE_CANDY_CANE_PILLAR = new Builder("purple_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.PURPLE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block MAGENTA_CANDY_CANE_PILLAR = new Builder("magenta_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.MAGENTA))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final Block PINK_CANDY_CANE_PILLAR = new Builder("pink_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.PINK))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();

	/* SONIC */
	public static final Block GREEN_HILL_GRASS_BLOCK = new Builder("green_hill_grass_block", new GrassBlock(FabricBlockSettings.copy(Blocks.GRASS_BLOCK))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final Block GREEN_HILL_DIRT = new Builder("green_hill_dirt", new Block(FabricBlockSettings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block MARBLE_ZONE_STONE = new Builder("marble_zone_stone", new Block(FabricBlockSettings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block YELLOW_STUDIOPOLIS_CLAPPER = new Builder("yellow_studiopolis_clapper", new DirectionalBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final Block BLUE_STUDIOPOLIS_CLAPPER = new Builder("blue_studiopolis_clapper", new DirectionalBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final WoodTypeEntryPack PRESS_GARDEN_WOOD = new WoodTypeEntryPack("press_garden", MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707, false);
	public static final PottedPlantEntryPack RED_PRESS_GARDEN_SAPLING = new PottedPlantEntryPack(new Builder("red_press_garden_sapling", new SaplingBlock(new RedPressGardenSaplingGenerator(), Settings.SAPLING)).build());
	public static final PottedPlantEntryPack PINK_PRESS_GARDEN_SAPLING = new PottedPlantEntryPack(new Builder("pink_press_garden_sapling", new SaplingBlock(new PinkPressGardenSaplingGenerator(), Settings.SAPLING)).build());
	public static final LeavesEntryPack RED_PRESS_GARDEN_LEAVES = new LeavesEntryPack("red_press_garden");
	public static final LeavesEntryPack PINK_PRESS_GARDEN_LEAVES = new LeavesEntryPack("pink_press_garden");

	public static final Block SPRING = new Builder("spring", new SpringBlock(FabricBlockSettings.of(Material.METAL).hardness(4f))).setItemGroup(ItemGroup.TRANSPORTATION).build();

	/* UNDERTALE / DELTARUNE */
	public static final NormalWoodTypeEntryPack SCARLET_WOOD = new NormalWoodTypeEntryPack("scarlet", new ScarletSaplingGenerator(), MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707);
	public static final PottedPlantEntryPack SCARLET_MUSHROOM = new PottedPlantEntryPack(new Builder("scarlet_mushroom", new MushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7))).setItemGroup(ItemGroup.DECORATIONS).build());
	public static final PottedPlantEntryPack SCARLET_ORCHID = new PottedPlantEntryPack(new Builder("scarlet_orchid", new FlowerBlock(StatusEffects.GLOWING, 8, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build());

	/* CELESTE */
	public static final Block GIRDER = new Builder("girder", new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();

	public static final MSBlockEntryPack MIRROR_TEMPLE_BRICKS = new MSBlockEntryPack("mirror_temple_bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockEntryPack OLD_SITE_BRICKS = new MSBlockEntryPack("old_site_bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);

	public static final Block ELDER_PEBBLES = new Builder("elder_pebbles", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.RED).strength(2.0F, 6.0F).lightLevel(5))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block DREAM_BLOCK = new Builder("dream_block", new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC).hardness(0.4f).sounds(MubbleSoundTypes.DREAM_BLOCK))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final Block DREAM_BEDROCK = new Builder("dream_bedrock", new Block(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing())).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();

	/* PUYO PUYO */
	public static final Block RED_PUYO = new Builder("red_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.RED).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final Block YELLOW_PUYO = new Builder("yellow_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.YELLOW).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final Block GREEN_PUYO = new Builder("green_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GREEN).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final Block TURQUOISE_PUYO = new Builder("turquoise_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.EMERALD).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final Block BLUE_PUYO = new Builder("blue_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.BLUE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final Block PURPLE_PUYO = new Builder("purple_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.PURPLE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final Block GRAY_PUYO = new Builder("gray_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GRAY).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final Block GARBAGE_PUYO = new Builder("garbage_puyo", new DirectionalBlock(FabricBlockSettings.copy(Blocks.STONE))).noItem().build();
	public static final Block POINT_PUYO = new Builder("point_puyo", new DirectionalBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(10))).noItem().build();
	public static final Block HARD_PUYO = new Builder("hard_puyo", new DirectionalBlock(FabricBlockSettings.copy(Blocks.STONE))).noItem().build();
	public static final Block IRON_PUYO = new Builder("iron_puyo", new DirectionalBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).noItem().build();

	/* YOUTUBE */
	public static final Block KORETATO_BLOCK = new Builder("koretato_block", new KoretatoBlock()).noItem().build();
	public static final PottedPlantEntryPack POTATO_FLOWER = new PottedPlantEntryPack(new Builder("potato_flower", new FlowerBlock(StatusEffects.HUNGER, 9, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());

	public static class Settings {
		public static final FabricBlockSettings LOG = FabricBlockSettings.of(Material.WOOD).strength(2.0F).sounds(BlockSoundGroup.WOOD);
		public static final FabricBlockSettings STEM = FabricBlockSettings.of(Material.NETHER_WOOD).strength(2.0F).sounds(BlockSoundGroup.NETHER_STEM);

		public static final FabricBlockSettings LEAVES = FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(EntryHelper::canSpawnOnLeaves).suffocates(EntryHelper::never).blockVision(EntryHelper::never);
		public static final FabricBlockSettings LEAF_PILE = FabricBlockSettings.of(Material.LEAVES).strength(0.1F).ticksRandomly().sounds(BlockSoundGroup.GRASS).noCollision().nonOpaque();
		public static final FabricBlockSettings FLOWER_PILE = FabricBlockSettings.of(Material.PLANT).breakInstantly().sounds(BlockSoundGroup.GRASS).noCollision();
		public static final FabricBlockSettings SAPLING = FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.GRASS).breakInstantly().noCollision().ticksRandomly();
		public static final FabricBlockSettings FUNGUS = FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.FUNGUS).breakInstantly().noCollision();
		public static final FabricBlockSettings POTTED_PLANT = FabricBlockSettings.of(Material.SUPPORTED).breakInstantly().nonOpaque();

		public static final FabricBlockSettings MUSHROOM_BLOCK = FabricBlockSettings.of(Material.WOOD).hardness(0.2F).sounds(BlockSoundGroup.WOOD);
		public static final FabricBlockSettings MUSHROOM = FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1);

		public static final FabricBlockSettings CHRISTMAS_BAUBLE = FabricBlockSettings.of(Material.GLASS).hardness(0.3F).sounds(BlockSoundGroup.GLASS);

		public static final FabricBlockSettings QUESTION_BLOCK = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.5F, 6.0F);

		public static final FabricBlockSettings TETRIS_BLOCK = FabricBlockSettings.of(Material.STONE).strength(1.5F, 6.0F);

		public static final FabricBlockSettings CANDY_CANE_BLOCK = FabricBlockSettings.of(Material.STONE).hardness(0.2F);
	}
}
