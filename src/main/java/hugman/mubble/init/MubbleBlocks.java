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
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockEntry.Builder;
import hugman.mubble.util.creator.BlockTemplate;
import hugman.mubble.util.creator.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class MubbleBlocks {
	/* Potted Plants (used for render layering) */
	public static final List<Block> POTTED_PLANTS = new ArrayList<Block>();

	/* Templates */
	protected static final Block.Settings pLeaves = FabricBlockSettings.of(Material.LEAVES).hardness(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque();

	/* MUBBLE */
	public static final BlockEntry OAK_VERTICAL_SLAB = new Builder("oak", BlockTemplate.VERTICAL_SLAB, Blocks.OAK_SLAB).build();
	public static final BlockEntry SPRUCE_VERTICAL_SLAB = new Builder("spruce", BlockTemplate.VERTICAL_SLAB, Blocks.SPRUCE_SLAB).build();
	public static final BlockEntry BIRCH_VERTICAL_SLAB = new Builder("birch", BlockTemplate.VERTICAL_SLAB, Blocks.BIRCH_SLAB).build();
	public static final BlockEntry JUNGLE_VERTICAL_SLAB = new Builder("jungle", BlockTemplate.VERTICAL_SLAB, Blocks.JUNGLE_SLAB).build();
	public static final BlockEntry ACACIA_VERTICAL_SLAB = new Builder("acacia", BlockTemplate.VERTICAL_SLAB, Blocks.ACACIA_SLAB).build();
	public static final BlockEntry DARK_OAK_VERTICAL_SLAB = new Builder("dark_oak", BlockTemplate.VERTICAL_SLAB, Blocks.DARK_OAK_SLAB).build();
	public static final BlockEntry CRIMSON_VERTICAL_SLAB = new Builder("crimson", BlockTemplate.VERTICAL_SLAB, Blocks.CRIMSON_SLAB).build();
	public static final BlockEntry WARPED_VERTICAL_SLAB = new Builder("warped", BlockTemplate.VERTICAL_SLAB, Blocks.WARPED_SLAB).build();
	public static final BlockEntry STONE_VERTICAL_SLAB = new Builder("stone", BlockTemplate.VERTICAL_SLAB, Blocks.STONE_SLAB).build();
	public static final BlockEntry SMOOTH_STONE_VERTICAL_SLAB = new Builder("smooth_stone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_STONE_SLAB).build();
	public static final BlockEntry COBBLESTONE_VERTICAL_SLAB = new Builder("cobblestone", BlockTemplate.VERTICAL_SLAB, Blocks.COBBLESTONE_SLAB).build();
	public static final BlockEntry MOSSY_COBBLESTONE_VERTICAL_SLAB = new Builder("mossy_cobblestone", BlockTemplate.VERTICAL_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB).build();
	public static final BlockEntry STONE_BRICK_VERTICAL_SLAB = new Builder("stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.STONE_BRICK_SLAB).build();
	public static final BlockEntry MOSSY_STONE_BRICK_VERTICAL_SLAB = new Builder("mossy_stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB).build();
	public static final BlockEntry GRANITE_VERTICAL_SLAB = new Builder("granite", BlockTemplate.VERTICAL_SLAB, Blocks.GRANITE_SLAB).build();
	public static final BlockEntry POLISHED_GRANITE_VERTICAL_SLAB = new Builder("polished_granite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_GRANITE_SLAB).build();
	public static final BlockEntry DIORITE_VERTICAL_SLAB = new Builder("diorite", BlockTemplate.VERTICAL_SLAB, Blocks.DIORITE_SLAB).build();
	public static final BlockEntry POLISHED_DIORITE_VERTICAL_SLAB = new Builder("polished_diorite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_DIORITE_SLAB).build();
	public static final BlockEntry ANDESITE_VERTICAL_SLAB = new Builder("andesite", BlockTemplate.VERTICAL_SLAB, Blocks.ANDESITE_SLAB).build();
	public static final BlockEntry POLISHED_ANDESITE_VERTICAL_SLAB = new Builder("polished_andesite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_ANDESITE_SLAB).build();
	public static final BlockEntry BRICK_VERTICAL_SLAB = new Builder("brick", BlockTemplate.VERTICAL_SLAB, Blocks.BRICK_SLAB).build();
	public static final BlockEntry SANDSTONE_VERTICAL_SLAB = new Builder("sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SANDSTONE_SLAB).build();
	public static final BlockEntry CUT_SANDSTONE_VERTICAL_SLAB = new Builder("cut_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.CUT_SANDSTONE_SLAB).build();
	public static final BlockEntry SMOOTH_SANDSTONE_VERTICAL_SLAB = new Builder("smooth_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB).build();
	public static final BlockEntry RED_SANDSTONE_VERTICAL_SLAB = new Builder("red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.RED_SANDSTONE_SLAB).build();
	public static final BlockEntry CUT_RED_SANDSTONE_VERTICAL_SLAB = new Builder("cut_red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB).build();
	public static final BlockEntry SMOOTH_RED_SANDSTONE_VERTICAL_SLAB = new Builder("smooth_red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB).build();
	public static final BlockEntry NETHER_BRICK_VERTICAL_SLAB = new Builder("nether_brick", BlockTemplate.VERTICAL_SLAB, Blocks.NETHER_BRICK_SLAB).build();
	public static final BlockEntry RED_NETHER_BRICK_VERTICAL_SLAB = new Builder("red_nether_brick", BlockTemplate.VERTICAL_SLAB, Blocks.RED_NETHER_BRICK_SLAB).build();
	public static final BlockEntry QUARTZ_VERTICAL_SLAB = new Builder("quartz", BlockTemplate.VERTICAL_SLAB, Blocks.QUARTZ_SLAB).build();
	public static final BlockEntry SMOOTH_QUARTZ_VERTICAL_SLAB = new Builder("smooth_quartz", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_QUARTZ_SLAB).build();
	public static final BlockEntry END_STONE_BRICK_VERTICAL_SLAB = new Builder("end_stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.END_STONE_BRICK_SLAB).build();
	public static final BlockEntry PURPUR_VERTICAL_SLAB = new Builder("purpur", BlockTemplate.VERTICAL_SLAB, Blocks.PURPUR_SLAB).build();
	public static final BlockEntry PRISMARINE_VERTICAL_SLAB = new Builder("prismarine", BlockTemplate.VERTICAL_SLAB, Blocks.PRISMARINE_SLAB).build();
	public static final BlockEntry PRISMARINE_BRICK_VERTICAL_SLAB = new Builder("prismarine_brick", BlockTemplate.VERTICAL_SLAB, Blocks.PRISMARINE_BRICK_SLAB).build();
	public static final BlockEntry DARK_PRISMARINE_VERTICAL_SLAB = new Builder("dark_prismarine", BlockTemplate.VERTICAL_SLAB, Blocks.DARK_PRISMARINE_SLAB).build();
	public static final BlockEntry BLACKSTONE_VERTICAL_SLAB = new Builder("blackstone", BlockTemplate.VERTICAL_SLAB, Blocks.BLACKSTONE_SLAB).build();
	public static final BlockEntry POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB = new Builder("polished_blackstone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB).build();
	public static final BlockEntry POLISHED_BLACKSTONE_SLAB = new Builder("polished_blackstone", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB).build();

	public static final BlockEntry DARK_PRISMARINE_WALL = new Builder("dark_prismarine", BlockTemplate.WALL, Blocks.DARK_PRISMARINE).build();

	public static final MSBlockCreator OAK_WOOD_BLOCKS = new MSBlockCreator("oak_wood", Blocks.OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator SPRUCE_WOOD_BLOCKS = new MSBlockCreator("spruce_wood", Blocks.SPRUCE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator BIRCH_WOOD_BLOCKS = new MSBlockCreator("birch_wood", Blocks.BIRCH_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator JUNGLE_WOOD_BLOCKS = new MSBlockCreator("jungle_wood", Blocks.JUNGLE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator ACACIA_WOOD_BLOCKS = new MSBlockCreator("acacia_wood", Blocks.ACACIA_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator DARK_OAK_WOOD_BLOCKS = new MSBlockCreator("dark_oak_wood", Blocks.DARK_OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator CRIMSON_HYPHAE_BLOCKS = new MSBlockCreator("crimson_hyphae", Blocks.CRIMSON_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator WARPED_HYPHAE_BLOCKS = new MSBlockCreator("warped_hyphae", Blocks.WARPED_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);

	public static final BlockEntry OAK_LEAF_PILE = new Builder("oak", BlockTemplate.PILE, BlockTemplate.LEAF_PILE_SETTINGS).copy(Blocks.OAK_LEAVES).build();
	public static final BlockEntry SPRUCE_LEAF_PILE = new Builder("spruce", BlockTemplate.PILE, BlockTemplate.LEAF_PILE_SETTINGS).copy(Blocks.OAK_LEAVES).build();
	public static final BlockEntry BIRCH_LEAF_PILE = new Builder("birch", BlockTemplate.PILE, BlockTemplate.LEAF_PILE_SETTINGS).copy(Blocks.OAK_LEAVES).build();
	public static final BlockEntry JUNGLE_LEAF_PILE = new Builder("jungle", BlockTemplate.PILE, BlockTemplate.LEAF_PILE_SETTINGS).copy(Blocks.OAK_LEAVES).build();
	public static final BlockEntry ACACIA_LEAF_PILE = new Builder("acacia", BlockTemplate.PILE, BlockTemplate.LEAF_PILE_SETTINGS).copy(Blocks.OAK_LEAVES).build();
	public static final BlockEntry DARK_OAK_LEAF_PILE = new Builder("dark_oak", BlockTemplate.PILE, BlockTemplate.LEAF_PILE_SETTINGS).copy(Blocks.OAK_LEAVES).build();

	public static final MSBlockCreator COBBLESTONE_BRICKS = new MSBlockCreator("cobblestone_bricks", Blocks.MOSSY_COBBLESTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator MOSSY_COBBLESTONE_BRICKS = new MSBlockCreator("mossy_cobblestone_bricks", Blocks.MOSSY_COBBLESTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator GRANITE_BRICKS = new MSBlockCreator("granite_bricks", Blocks.GRANITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator DIORITE_BRICKS = new MSBlockCreator("diorite_bricks", Blocks.DIORITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator ANDESITE_BRICKS = new MSBlockCreator("andesite_bricks", Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);

	public static final MSBlockCreator SANDSTONE_BRICKS = new MSBlockCreator("sandstone_bricks", Blocks.SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator POLISHED_SANDSTONE = new MSBlockCreator("polished_sandstone", Blocks.SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);
	public static final MSBlockCreator RED_SANDSTONE_BRICKS = new MSBlockCreator("red_sandstone_bricks", Blocks.RED_SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator POLISHED_RED_SANDSTONE = new MSBlockCreator("polished_red_sandstone", Blocks.RED_SANDSTONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final MSBlockCreator SMOOTH_STONE_PAVING = new MSBlockCreator("smooth_stone_paving", Blocks.SMOOTH_STONE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);
	public static final MSBlockCreator CHISELED_PRISMARINE = new MSBlockCreator("chiseled_prismarine", Blocks.PRISMARINE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator PRISMARINE_BRICK_PAVING = new MSBlockCreator("prismarine_brick_paving", Blocks.PRISMARINE_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final MSBlockCreator BLUNITE_BLOCKS = new MSBlockCreator("blunite", MaterialColor.LIGHT_BLUE_TERRACOTTA, Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator CARBONITE_BLOCKS = new MSBlockCreator("carbonite", MaterialColor.BLACK, Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator POLISHED_BLUNITE = new MSBlockCreator("polished_blunite", MubbleBlocks.BLUNITE_BLOCKS.getBlock(BlockTemplate.CUBE), BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);
	public static final MSBlockCreator POLISHED_CARBONITE = new MSBlockCreator("polished_carbonite", MubbleBlocks.CARBONITE_BLOCKS.getBlock(BlockTemplate.CUBE), BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final BlockEntry VANADIUM_ORE = new Builder("vanadium_ore", new OreBlock(Settings.copy(Blocks.DIAMOND_ORE))).copy(Blocks.DIAMOND_ORE).build();
	public static final BlockEntry VANADIUM_BLOCK = new Builder("vanadium_block", BlockTemplate.CUBE, Blocks.DIAMOND_BLOCK, MaterialColor.MAGENTA).build();

	public static final PottedPlantCreator AUTUMN_OAK_SAPLING = new PottedPlantCreator(new Builder("autumn_oak_sapling", new SaplingBlock(new AutumnOakSaplingGenerator(), BlockTemplate.SAPLING_SETTINGS)).build());
	public static final LeavesCreator AUTUMN_OAK_LEAVES = new LeavesCreator("autumn_oak");
	public static final PottedPlantCreator AUTUMN_BIRCH_SAPLING = new PottedPlantCreator(new Builder("autumn_birch_sapling", new SaplingBlock(new AutumnBirchSaplingGenerator(), BlockTemplate.SAPLING_SETTINGS)).build());
	public static final LeavesCreator AUTUMN_BIRCH_LEAVES = new LeavesCreator("autumn_birch");

	public static final WoodTypeCreator CHERRY_OAK_WOOD = new WoodTypeCreator("cherry_oak", MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707, false);
	public static final PottedPlantCreator PINK_CHERRY_OAK_SAPLING = new PottedPlantCreator(new Builder("pink_cherry_oak_sapling", new SaplingBlock(new PinkCherryOakSaplingGenerator(), BlockTemplate.SAPLING_SETTINGS)).build());
	public static final PottedPlantCreator WHITE_CHERRY_OAK_SAPLING = new PottedPlantCreator(new Builder("white_cherry_oak_sapling", new SaplingBlock(new WhiteCherryOakSaplingGenerator(), BlockTemplate.SAPLING_SETTINGS)).build());
	public static final LeavesCreator PINK_CHERRY_OAK_LEAVES = new LeavesCreator("pink_cherry_oak");
	public static final LeavesCreator WHITE_CHERRY_OAK_LEAVES = new LeavesCreator("white_cherry_oak");

	public static final NormalWoodTypeCreator PALM_WOOD = new NormalWoodTypeCreator("palm", new PalmSaplingGenerator(), MaterialColor.ORANGE, MaterialColor.CYAN_TERRACOTTA);

	public static final MSCBlockCreator STAINED_BRICK_BLOCKS = new MSCBlockCreator("bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator TERRACOTTA_BLOCKS = new MSBlockCreator("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator STAINED_TERRACOTTA_BLOCKS = new MSCBlockCreator("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator DARK_PRISMARINE_BLOCKS = new MSCBlockCreator("dark_prismarine", Blocks.DARK_PRISMARINE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSCBlockCreator CONCRETE_BLOCKS = new MSCBlockCreator("concrete", Blocks.BLUE_CONCRETE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator QUARTZ_PAVING_BLOCKS = new MSCBlockCreator("quartz_paving", Blocks.QUARTZ_BLOCK, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final BlockEntry BLUE_CHRISTMAS_BAUBLE = new Builder("blue_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.BLUE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry LIGHT_BLUE_CHRISTMAS_BAUBLE = new Builder("light_blue_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.LIGHT_BLUE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry PURPLE_CHRISTMAS_BAUBLE = new Builder("purple_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.PURPLE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry MAGENTA_CHRISTMAS_BAUBLE = new Builder("magenta_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.MAGENTA_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry PINK_CHRISTMAS_BAUBLE = new Builder("pink_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.PINK_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry RED_CHRISTMAS_BAUBLE = new Builder("red_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.RED_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry ORANGE_CHRISTMAS_BAUBLE = new Builder("orange_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.ORANGE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry YELLOW_CHRISTMAS_BAUBLE = new Builder("yellow_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.YELLOW_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry GREEN_CHRISTMAS_BAUBLE = new Builder("green_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.GREEN_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry WHITE_CHRISTMAS_BAUBLE = new Builder("white_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.WHITE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS))).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final BlockEntry RED_SHINY_GARLAND = new Builder("red_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.RED).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry SILVER_SHINY_GARLAND = new Builder("silver_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.CLAY).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry GOLD_SHINY_GARLAND = new Builder("gold_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.GOLD).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();

	public static final BlockEntry WHITE_PRESENT = new Builder("white_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry BLACK_PRESENT = new Builder("black_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLACK_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry BLUE_PRESENT = new Builder("blue_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLUE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry GREEN_PRESENT = new Builder("green_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GREEN_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry YELLOW_PRESENT = new Builder("yellow_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.YELLOW_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry RED_PRESENT = new Builder("red_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.RED_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry PURPLE_PRESENT = new Builder("purple_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.PURPLE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry GOLDEN_PRESENT = new Builder("golden_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GOLD).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 60).setRenderLayer(RenderLayer.getCutoutMipped()).build();

	public static final BlockEntry FOOTBLOCK = new Builder("footblock", BlockTemplate.CUBE, Blocks.WHITE_WOOL).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final MCBlockCreator CLOUD_BLOCKS = new MCBlockCreator(BlockTemplate.CLOUD_BLOCK, FabricBlockSettings.of(Material.LEAVES).sounds(BlockSoundGroup.WOOL).hardness(0f).noCollision());

	public static final BlockEntry TOMATOES = new Builder("tomatoes", new CropsBlock()).setRenderLayer(RenderLayer.getCutout()).noItem().build();
	public static final BlockEntry SALAD = new Builder("salad", new CropsBlock()).setRenderLayer(RenderLayer.getCutout()).noItem().build();

	public static final BlockEntry BLUEBERRY_BUSH = new Builder("blueberry_bush", new BerryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH))).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).noItem().build();
	public static final BlockEntry CHEESE_BLOCK = new Builder("cheese_block", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.YELLOW).hardness(0.5f).sounds(BlockSoundGroup.SNOW))).setFlammability(60, 60).setItemGroup(ItemGroup.FOOD).build();
	public static final BlockEntry CHOCOLATE_CAKE = new Builder("chocolate_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).setItemGroup(ItemGroup.FOOD).build();
	public static final BlockEntry MINECRAFT_10TH_ANNIVERSARY_CAKE = new Builder("minecraft_10th_anniversary_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).setItemGroup(ItemGroup.FOOD).build();

	public static final BlockEntry WHITE_BALLOON = new Builder("white_balloon", new BalloonBlock(DyeColor.WHITE)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry LIGHT_GRAY_BALLOON = new Builder("light_gray_balloon", new BalloonBlock(DyeColor.LIGHT_GRAY)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry GRAY_BALLOON = new Builder("gray_balloon", new BalloonBlock(DyeColor.GRAY)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry BLACK_BALLOON = new Builder("black_balloon", new BalloonBlock(DyeColor.BLACK)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry BROWN_BALLOON = new Builder("brown_balloon", new BalloonBlock(DyeColor.BROWN)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry RED_BALLOON = new Builder("red_balloon", new BalloonBlock(DyeColor.RED)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry ORANGE_BALLOON = new Builder("orange_balloon", new BalloonBlock(DyeColor.ORANGE)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry YELLOW_BALLOON = new Builder("yellow_balloon", new BalloonBlock(DyeColor.YELLOW)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry LIME_BALLOON = new Builder("lime_balloon", new BalloonBlock(DyeColor.LIME)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry GREEN_BALLOON = new Builder("green_balloon", new BalloonBlock(DyeColor.GREEN)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry CYAN_BALLOON = new Builder("cyan_balloon", new BalloonBlock(DyeColor.CYAN)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry LIGHT_BLUE_BALLOON = new Builder("light_blue_balloon", new BalloonBlock(DyeColor.LIGHT_BLUE)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry BLUE_BALLOON = new Builder("blue_balloon", new BalloonBlock(DyeColor.BLUE)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry PURPLE_BALLOON = new Builder("purple_balloon", new BalloonBlock(DyeColor.PURPLE)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry MAGENTA_BALLOON = new Builder("magenta_balloon", new BalloonBlock(DyeColor.MAGENTA)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry PINK_BALLOON = new Builder("pink_balloon", new BalloonBlock(DyeColor.PINK)).setItemGroup(ItemGroup.DECORATIONS).setFlammability(30, 60).setRenderLayer(RenderLayer.getTranslucent()).build();

	public static final BlockEntry UNSTABLE_STONE = new Builder("unstable_stone", new UnstableBlock(FabricBlockSettings.copy(Blocks.STONE).strength(0.1F, 0.0F))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry FLUID_TANK = new Builder("fluid_tank", new FluidTankBlock(FabricBlockSettings.copy(Blocks.OBSIDIAN).nonOpaque())).setItemGroup(ItemGroup.REDSTONE).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry PLACER = new Builder("placer", new PlacerBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F))).setItemGroup(ItemGroup.REDSTONE).build();
	public static final BlockEntry TIMESWAP_TABLE = new Builder("timeswap_table", new TimeswapTableBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F))).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final BlockEntry DANDELION_PILE = new Builder("dandelion_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry POPPY_PILE = new Builder("poppy_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry BLUE_ORCHID_PILE = new Builder("blue_orchid_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry ALLIUM_PILE = new Builder("allium_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry AZURE_BLUET_PILE = new Builder("azure_bluet_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry RED_TULIP_PILE = new Builder("red_tulip_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry ORANGE_TULIP_PILE = new Builder("orange_tulip_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry WHITE_TULIP_PILE = new Builder("white_tulip_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry PINK_TULIP_PILE = new Builder("pink_tulip_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry OXEYE_DAISY_PILE = new Builder("oxeye_daisy_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry CORNFLOWER_PILE = new Builder("cornflower_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry LILY_OF_THE_VALLEY_PILE = new Builder("lily_of_the_valley_pile", new PileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry WITHER_ROSE_PILE = new Builder("wither_rose_pile", new WitherRosePileBlock(BlockTemplate.FLOWER_PILE_SETTINGS.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();

	public static final BlockEntry PERMAROCK = new Builder("permarock", BlockTemplate.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.4F)).build();
	public static final MSBlockCreator PERMAFROST_BRICKS = new MSBlockCreator("permafrost_bricks", Blocks.NETHER_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.FENCE);
	public static final MSBlockCreator BLUE_PERMAFROST_BRICKS = new MSBlockCreator("blue_permafrost_bricks", Blocks.RED_NETHER_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final BlockEntry PERMAFROST_BISMUTH_ORE = new Builder("permafrost_bismuth_ore", BlockTemplate.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.3F)).build();
	public static final BlockEntry FROZEN_OBSIDIAN = new Builder("frozen_obsidian", BlockTemplate.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).strength(75.0F, 1800.0F)).build();

	public static final BlockEntry AMARANTH_DYLIUM = new Builder("amaranth_dylium", new DyliumBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.field_25702).requiresTool().strength(3.0F, 9.0F).sounds(BlockSoundGroup.NYLIUM).ticksRandomly())).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry AMARANTH_WART_BLOCK = new Builder("amaranth_wart_block", new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC, MaterialColor.field_25708).breakByTool(FabricToolTags.HOES).strength(1.0F).sounds(BlockSoundGroup.WART_BLOCK))).build();
	public static final BlockEntry AMARANTH_ROOTS = new Builder("amaranth_roots", new RootsBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.CYAN).noCollision().breakInstantly().sounds(BlockSoundGroup.ROOTS))).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final NetherWoodTypeCreator DARK_AMARANTH_WOOD = new NetherWoodTypeCreator("dark_amaranth", () -> {
		return MubbleConfiguredFeatures.AMARANTH_FUNGI_PLANTED;
	}, MaterialColor.LIGHT_GRAY, MaterialColor.field_25707);


	/* SUPER MARIO (MAKER) */
	public static final BlockEntry SMB_QUESTION_BLOCK = new Builder("smb", BlockTemplate.QUESTION_BLOCK, BlockTemplate.QUESTION_BLOCK_SETTINGS).build();
	public static final BlockEntry SMB3_QUESTION_BLOCK = new Builder("smb3", BlockTemplate.QUESTION_BLOCK, BlockTemplate.QUESTION_BLOCK_SETTINGS).build();
	public static final BlockEntry SMW_QUESTION_BLOCK = new Builder("smw", BlockTemplate.QUESTION_BLOCK, BlockTemplate.QUESTION_BLOCK_SETTINGS).build();
	public static final BlockEntry NSMBU_QUESTION_BLOCK = new Builder("nsmbu", BlockTemplate.QUESTION_BLOCK, BlockTemplate.QUESTION_BLOCK_SETTINGS).build();
	public static final BlockEntry SMB_GROUND_GROUND_BLOCK = new Builder("smb_ground_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_UNDERGROUND_GROUND_BLOCK = new Builder("smb_underground_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_UNDERWATER_GROUND_BLOCK = new Builder("smb_underwater_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_GHOST_HOUSE_GROUND_BLOCK = new Builder("smb_ghost_house_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_AIRSHIP_GROUND_BLOCK = new Builder("smb_airship_ground_block", BlockTemplate.CUBE, Blocks.IRON_BLOCK).build();
	public static final BlockEntry SMB_NIGHT_AIRSHIP_GROUND_BLOCK = new Builder("smb_night_airship_ground_block", BlockTemplate.CUBE, Blocks.IRON_BLOCK).build();
	public static final BlockEntry SMB_CASTLE_GROUND_BLOCK = new Builder("smb_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_DESERT_GROUND_BLOCK = new Builder("smb_desert_ground_block", BlockTemplate.CUBE, Blocks.SAND).build();
	public static final BlockEntry SMB_FOREST_GROUND_BLOCK = new Builder("smb_forest_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_SNOW_GROUND_BLOCK = new Builder("smb_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_NIGHT_SNOW_GROUND_BLOCK = new Builder("smb_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_SKY_GROUND_BLOCK = new Builder("smb_sky_ground_block", BlockTemplate.CUBE, Blocks.WHITE_WOOL).build();
	public static final BlockEntry SMB3_GROUND_GROUND_BLOCK = new Builder("smb3_ground_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_UNDERGROUND_GROUND_BLOCK = new Builder("smb3_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_UNDERWATER_GROUND_BLOCK = new Builder("smb3_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_GHOST_HOUSE_GROUND_BLOCK = new Builder("smb3_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_AIRSHIP_GROUND_BLOCK = new Builder("smb3_airship_ground_block", BlockTemplate.CUBE, Blocks.OAK_WOOD).build();
	public static final BlockEntry SMB3_NIGHT_AIRSHIP_GROUND_BLOCK = new Builder("smb3_night_airship_ground_block", BlockTemplate.CUBE, Blocks.OAK_WOOD).build();
	public static final BlockEntry SMB3_CASTLE_GROUND_BLOCK = new Builder("smb3_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB3_NIGHT_CASTLE_GROUND_BLOCK = new Builder("smb3_night_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB3_DESERT_GROUND_BLOCK = new Builder("smb3_desert_ground_block", new OverBlock(Settings.copy(Blocks.SAND))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_SNOW_GROUND_BLOCK = new Builder("smb3_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_NIGHT_SNOW_GROUND_BLOCK = new Builder("smb3_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_SKY_GROUND_BLOCK = new Builder("smb3_sky_ground_block", new OverBlock(Settings.copy(Blocks.WHITE_WOOL))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(30, 60).build();
	public static final BlockEntry SMW_GROUND_GROUND_BLOCK = new Builder("smw_ground_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_UNDERGROUND_GROUND_BLOCK = new Builder("smw_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_UNDERWATER_GROUND_BLOCK = new Builder("smw_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_GHOST_HOUSE_GROUND_BLOCK = new Builder("smw_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_AIRSHIP_GROUND_BLOCK = new Builder("smw_airship_ground_block", new OverBlock(Settings.copy(Blocks.OAK_WOOD))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_CASTLE_GROUND_BLOCK = new Builder("smw_castle_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_DESERT_GROUND_BLOCK = new Builder("smw_desert_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_FOREST_GROUND_BLOCK = new Builder("smw_forest_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_SNOW_GROUND_BLOCK = new Builder("smw_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_NIGHT_SNOW_GROUND_BLOCK = new Builder("smw_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_SKY_GROUND_BLOCK = new Builder("smw_sky_ground_block", new OverBlock(Settings.copy(Blocks.WHITE_WOOL))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(30, 60).build();
	public static final BlockEntry NSMBU_GROUND_GROUND_BLOCK = new Builder("nsmbu_ground_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_UNDERGROUND_GROUND_BLOCK = new Builder("nsmbu_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_UNDERWATER_GROUND_BLOCK = new Builder("nsmbu_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_GHOST_HOUSE_GROUND_BLOCK = new Builder("nsmbu_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_CASTLE_GROUND_BLOCK = new Builder("nsmbu_castle_ground_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry NSMBU_DESERT_GROUND_BLOCK = new Builder("nsmbu_desert_ground_block", new OverBlock(Settings.copy(Blocks.SAND))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_FOREST_GROUND_BLOCK = new Builder("nsmbu_forest_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_SNOW_GROUND_BLOCK = new Builder("nsmbu_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_NIGHT_SNOW_GROUND_BLOCK = new Builder("nsmbu_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_SKY_GROUND_BLOCK = new Builder("nsmbu_sky_ground_block", new OverBlock(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_EMPTY_BLOCK = new Builder("smb_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_EMPTY_BLOCK = new Builder("smb3_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_EMPTY_BLOCK = new Builder("smw_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_EMPTY_BLOCK = new Builder("nsmbu_empty_block", new EmptyBlock()).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_ROTATING_BLOCK = new Builder("smb_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_ROTATING_BLOCK = new Builder("smb3_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_ROTATING_BLOCK = new Builder("smw_rotating_block", new RotatingBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_ROTATING_BLOCK = new Builder("nsmbu_rotating_block", new RotatingBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry LIGHT_BLOCK = new Builder("light_block", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(15))).build();
	public static final BlockEntry SMB_GROUND_BRICK_BLOCK = new Builder("smb_ground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_UNDERGROUND_BRICK_BLOCK = new Builder("smb_underground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_CASTLE_BRICK_BLOCK = new Builder("smb_castle_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_SNOW_BRICK_BLOCK = new Builder("smb_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_NIGHT_SNOW_BRICK_BLOCK = new Builder("smb_night_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_BRICK_BLOCK = new Builder("smb3_brick_block", new BrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_BRICK_BLOCK = new Builder("smw_brick_block", new BrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_BRICK_BLOCK = new Builder("nsmbu_brick_block", new BrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_GOLDEN_BRICK_BLOCK = new Builder("smb_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_GOLDEN_BRICK_BLOCK = new Builder("smb3_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_GOLDEN_BRICK_BLOCK = new Builder("smw_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_GOLDEN_BRICK_BLOCK = new Builder("nsmbu_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_GROUND_HARD_BLOCK = new Builder("smb_ground_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_UNDERGROUND_HARD_BLOCK = new Builder("smb_underground_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_UNDERWATER_HARD_BLOCK = new Builder("smb_underwater_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_CASTLE_HARD_BLOCK = new Builder("smb_castle_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_SNOW_HARD_BLOCK = new Builder("smb_snow_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_NIGHT_SNOW_HARD_BLOCK = new Builder("smb_night_snow_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB3_HARD_BLOCK = new Builder("smb3_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMW_STONE_HARD_BLOCK = new Builder("smw_stone_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMW_WOOD_HARD_BLOCK = new Builder("smw_wood_hard_block", BlockTemplate.CUBE, Blocks.OAK_PLANKS).build();
	public static final BlockEntry NSMBU_HARD_BLOCK = new Builder("nsmbu_hard_block", BlockTemplate.CUBE, Blocks.STONE).build();
	public static final BlockEntry SMB_ICE_BLOCK = new Builder("smb_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final BlockEntry SMB3_ICE_BLOCK = new Builder("smb3_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final BlockEntry SMW_ICE_BLOCK = new Builder("smw_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final BlockEntry NSMBU_ICE_BLOCK = new Builder("nsmbu_ice_block", BlockTemplate.CUBE, Blocks.PACKED_ICE).build();
	public static final BlockEntry SMB_NOTE_BLOCK = new Builder("smb_note_block", new NoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_NOTE_BLOCK = new Builder("smb3_note_block", new NoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_NOTE_BLOCK = new Builder("smw_note_block", new NoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_NOTE_BLOCK = new Builder("nsmbu_note_block", new NoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_SUPER_NOTE_BLOCK = new Builder("smb_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB3_SUPER_NOTE_BLOCK = new Builder("smb3_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMW_SUPER_NOTE_BLOCK = new Builder("smw_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry NSMBU_SUPER_NOTE_BLOCK = new Builder("nsmbu_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry SMB_DOOR = new Builder("smb", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final BlockEntry SMB3_DOOR = new Builder("smb3", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final BlockEntry SMW_DOOR = new Builder("smw", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final BlockEntry NSMBU_DOOR = new Builder("nsmbu", BlockTemplate.DOOR, Blocks.OAK_DOOR).build();
	public static final BlockEntry SMB_KEY_DOOR = new Builder("smb", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();
	public static final BlockEntry SMB3_KEY_DOOR = new Builder("smb3", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();
	public static final BlockEntry SMW_KEY_DOOR = new Builder("smw", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();
	public static final BlockEntry NSMBU_KEY_DOOR = new Builder("nsmbu", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR).build();

	/* SUPER MARIO (OTHERS) */
	public static final PottedPlantCreator FIRE_FLOWER = new PottedPlantCreator(new Builder("fire_flower", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantCreator ICE_FLOWER = new PottedPlantCreator(new Builder("ice_flower", new FlowerBlock(StatusEffects.MINING_FATIGUE, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantCreator BOOMERANG_FLOWER = new PottedPlantCreator(new Builder("boomerang_flower", new FlowerBlock(StatusEffects.HASTE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantCreator CLOUD_FLOWER = new PottedPlantCreator(new Builder("cloud_flower", new CloudFlowerBlock(StatusEffects.SLOW_FALLING, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
	public static final PottedPlantCreator GOLD_FLOWER = new PottedPlantCreator(new Builder("gold_flower", new FlowerBlock(MubbleEffects.HEAVINESS, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(5))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());

	public static final BlockEntry WHITE_MUSHROOM_BLOCK = new Builder("white", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.WHITE)).build();
	public static final BlockEntry LIGHT_GRAY_MUSHROOM_BLOCK = new Builder("light_gray", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.LIGHT_GRAY)).build();
	public static final BlockEntry GRAY_MUSHROOM_BLOCK = new Builder("gray", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.GRAY)).build();
	public static final BlockEntry BLACK_MUSHROOM_BLOCK = new Builder("black", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.BLACK)).build();
	public static final BlockEntry ORANGE_MUSHROOM_BLOCK = new Builder("orange", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.ORANGE)).build();
	public static final BlockEntry YELLOW_MUSHROOM_BLOCK = new Builder("yellow", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.YELLOW)).build();
	public static final BlockEntry LIME_MUSHROOM_BLOCK = new Builder("lime", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.LIME)).build();
	public static final BlockEntry GREEN_MUSHROOM_BLOCK = new Builder("green", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.GREEN)).build();
	public static final BlockEntry CYAN_MUSHROOM_BLOCK = new Builder("cyan", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.CYAN)).build();
	public static final BlockEntry LIGHT_BLUE_MUSHROOM_BLOCK = new Builder("light_blue", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.LIGHT_BLUE)).build();
	public static final BlockEntry BLUE_MUSHROOM_BLOCK = new Builder("blue", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.BLUE)).build();
	public static final BlockEntry PURPLE_MUSHROOM_BLOCK = new Builder("purple", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.PURPLE)).build();
	public static final BlockEntry MAGENTA_MUSHROOM_BLOCK = new Builder("magenta", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.MAGENTA)).build();
	public static final BlockEntry PINK_MUSHROOM_BLOCK = new Builder("pink", BlockTemplate.MUSHROOM_BLOCK, BlockTemplate.MUSHROOM_BLOCK_SETTINGS.materialColor(DyeColor.PINK)).build();
	public static final BlockEntry WHITE_MUSHROOM = new Builder("white_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, WHITE_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry LIGHT_GRAY_MUSHROOM = new Builder("light_gray_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, LIGHT_GRAY_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry GRAY_MUSHROOM = new Builder("gray_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, GRAY_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry BLACK_MUSHROOM = new Builder("black_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, BLACK_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry ORANGE_MUSHROOM = new Builder("orange_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, ORANGE_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry YELLOW_MUSHROOM = new Builder("yellow_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, YELLOW_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry LIME_MUSHROOM = new Builder("lime_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, LIME_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry GREEN_MUSHROOM = new Builder("green_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, GREEN_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry CYAN_MUSHROOM = new Builder("cyan_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, CYAN_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry LIGHT_BLUE_MUSHROOM = new Builder("light_blue_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, LIGHT_BLUE_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry BLUE_MUSHROOM = new Builder("blue_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, BLUE_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry PURPLE_MUSHROOM = new Builder("purple_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, PURPLE_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry MAGENTA_MUSHROOM = new Builder("magenta_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, MAGENTA_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
	public static final BlockEntry PINK_MUSHROOM = new Builder("pink_mushroom", new GrowableMushroomPlantBlock(BlockTemplate.MUSHROOM_SETTINGS, PINK_MUSHROOM_BLOCK.getBlock())).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();

	/* KIRBY */
	public static final BlockEntry KIRBY_BLOCK = new Builder("kirby_block", new DirectionalBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.PINK).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).setItemGroup(ItemGroup.DECORATIONS).build();

	/* TETRIS */
	public static final BlockEntry WHITE_TETRIS_BLOCK = new Builder("white", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.WHITE)).build();
	public static final BlockEntry LIGHT_GRAY_TETRIS_BLOCK = new Builder("light_gray", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.LIGHT_GRAY)).build();
	public static final BlockEntry GRAY_TETRIS_BLOCK = new Builder("gray", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.GRAY)).build();
	public static final BlockEntry BLACK_TETRIS_BLOCK = new Builder("black", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.BLACK)).build();
	public static final BlockEntry BROWN_TETRIS_BLOCK = new Builder("brown", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.BROWN)).build();
	public static final BlockEntry RED_TETRIS_BLOCK = new Builder("red", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.RED)).build();
	public static final BlockEntry ORANGE_TETRIS_BLOCK = new Builder("orange", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.ORANGE)).build();
	public static final BlockEntry YELLOW_TETRIS_BLOCK = new Builder("yellow", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.YELLOW)).build();
	public static final BlockEntry LIME_TETRIS_BLOCK = new Builder("lime", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.LIME)).build();
	public static final BlockEntry GREEN_TETRIS_BLOCK = new Builder("green", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.GREEN)).build();
	public static final BlockEntry CYAN_TETRIS_BLOCK = new Builder("cyan", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.CYAN)).build();
	public static final BlockEntry LIGHT_BLUE_TETRIS_BLOCK = new Builder("light_blue", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.LIGHT_BLUE)).build();
	public static final BlockEntry BLUE_TETRIS_BLOCK = new Builder("blue", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.BLUE)).build();
	public static final BlockEntry PURPLE_TETRIS_BLOCK = new Builder("purple", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.PURPLE)).build();
	public static final BlockEntry MAGENTA_TETRIS_BLOCK = new Builder("magenta", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.MAGENTA)).build();
	public static final BlockEntry PINK_TETRIS_BLOCK = new Builder("pink", BlockTemplate.TETRIS_BLOCK, BlockTemplate.TETRIS_BLOCK_SETTINGS.materialColor(DyeColor.PINK)).build();
	public static final BlockEntry TETRIS_GLASS = new Builder("tetris_glass", new TetrisGlassBlock(Settings.copy(Blocks.GLASS))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setRenderLayer(RenderLayer.getTranslucent()).build();
	public static final BlockEntry JAPANESE_TETRIS_CUSHION = new Builder("japanese_tetris_cushion", new FallingBlock(Settings.copy(Blocks.RED_WOOL))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(30, 60).build();
	public static final BlockEntry RAINBOW_TETRIS_SCAFFOLDING = new Builder("rainbow_tetris_scaffolding", new FallingBlock(Settings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry MONOCHROME_TETRIS_SCAFFOLDING = new Builder("monochrome_tetris_scaffolding", new FallingBlock(Settings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();

	/* CASTLEVANIA */
	public static final BlockEntry VAMPIRE_STONE = new Builder("vampire_stone", new Block(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();

	public static final BlockEntry MEDUSA_STONE = new Builder("medusa_stone", new Block(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final MSBlockCreator MEDUSA_BRICKS = new MSBlockCreator("medusa_bricks", Blocks.STONE_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);

	public static final BlockEntry WHITE_CANDY_CANE_PILLAR = new Builder("white_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.WHITE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry LIGHT_GRAY_CANDY_CANE_PILLAR = new Builder("light_gray_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.LIGHT_GRAY))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry GRAY_CANDY_CANE_PILLAR = new Builder("gray_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.GRAY))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry BLACK_CANDY_CANE_PILLAR = new Builder("black_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.BLACK))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry BROWN_CANDY_CANE_PILLAR = new Builder("brown_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.BROWN))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry RED_CANDY_CANE_PILLAR = new Builder("red_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.RED))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry ORANGE_CANDY_CANE_PILLAR = new Builder("orange_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.ORANGE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry YELLOW_CANDY_CANE_PILLAR = new Builder("yellow_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.YELLOW))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry LIME_CANDY_CANE_PILLAR = new Builder("lime_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.LIME))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry GREEN_CANDY_CANE_PILLAR = new Builder("green_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.GREEN))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry CYAN_CANDY_CANE_PILLAR = new Builder("cyan_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.CYAN))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry LIGHT_BLUE_CANDY_CANE_PILLAR = new Builder("light_blue_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.LIGHT_BLUE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry BLUE_CANDY_CANE_PILLAR = new Builder("blue_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.BLUE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry PURPLE_CANDY_CANE_PILLAR = new Builder("purple_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.PURPLE))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry MAGENTA_CANDY_CANE_PILLAR = new Builder("magenta_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.MAGENTA))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();
	public static final BlockEntry PINK_CANDY_CANE_PILLAR = new Builder("pink_candy_cane_pillar", new PillarBlock(BlockTemplate.CANDY_CANE_BLOCK_SETTINGS.materialColor(DyeColor.PINK))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(5, 10).build();

	/* SONIC */
	public static final BlockEntry GREEN_HILL_GRASS_BLOCK = new Builder("green_hill_grass_block", new GrassBlock(Settings.copy(Blocks.GRASS_BLOCK))).setItemGroup(ItemGroup.BUILDING_BLOCKS).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockEntry GREEN_HILL_DIRT = new Builder("green_hill_dirt", new Block(Settings.copy(Blocks.DIRT))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry MARBLE_ZONE_STONE = new Builder("marble_zone_stone", new Block(Settings.copy(Blocks.STONE))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry YELLOW_STUDIOPOLIS_CLAPPER = new Builder("yellow_studiopolis_clapper", new DirectionalBlock(Settings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();
	public static final BlockEntry BLUE_STUDIOPOLIS_CLAPPER = new Builder("blue_studiopolis_clapper", new DirectionalBlock(Settings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.DECORATIONS).build();

	public static final WoodTypeCreator PRESS_GARDEN_WOOD = new WoodTypeCreator("press_garden", MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707, false);
	public static final PottedPlantCreator RED_PRESS_GARDEN_SAPLING = new PottedPlantCreator(new Builder("red_press_garden_sapling", new SaplingBlock(new RedPressGardenSaplingGenerator(), BlockTemplate.SAPLING_SETTINGS)).build());
	public static final PottedPlantCreator PINK_PRESS_GARDEN_SAPLING = new PottedPlantCreator(new Builder("pink_press_garden_sapling", new SaplingBlock(new PinkPressGardenSaplingGenerator(), BlockTemplate.SAPLING_SETTINGS)).build());
	public static final LeavesCreator RED_PRESS_GARDEN_LEAVES = new LeavesCreator("red_press_garden");
	public static final LeavesCreator PINK_PRESS_GARDEN_LEAVES = new LeavesCreator("pink_press_garden");

	public static final BlockEntry SPRING = new Builder("spring", new SpringBlock(FabricBlockSettings.of(Material.METAL).hardness(4f))).setItemGroup(ItemGroup.TRANSPORTATION).build();

	/* UNDERTALE / DELTARUNE */
	public static final NormalWoodTypeCreator SCARLET_WOOD = new NormalWoodTypeCreator("scarlet", new ScarletSaplingGenerator(), MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707);
	public static final PottedPlantCreator SCARLET_MUSHROOM = new PottedPlantCreator(new Builder("scarlet_mushroom", new MushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7))).setItemGroup(ItemGroup.DECORATIONS).build());
	public static final PottedPlantCreator SCARLET_ORCHID = new PottedPlantCreator(new Builder("scarlet_orchid", new FlowerBlock(StatusEffects.GLOWING, 8, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build());

	/* CELESTE */
	public static final BlockEntry GIRDER = new Builder("girder", new Block(Settings.copy(Blocks.IRON_BLOCK))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();

	public static final MSBlockCreator MIRROR_TEMPLE_BRICKS = new MSBlockCreator("mirror_temple_bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator OLD_SITE_BRICKS = new MSBlockCreator("old_site_bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);

	public static final BlockEntry ELDER_PEBBLES = new Builder("elder_pebbles", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.RED).strength(2.0F, 6.0F).lightLevel(5))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry DREAM_BLOCK = new Builder("dream_block", new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC).hardness(0.4f).sounds(MubbleSoundTypes.DREAM_BLOCK))).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();
	public static final BlockEntry DREAM_BEDROCK = new Builder("dream_bedrock", new Block(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing())).setItemGroup(ItemGroup.BUILDING_BLOCKS).build();

	/* PUYO PUYO */
	public static final BlockEntry RED_PUYO = new Builder("red_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.RED).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final BlockEntry YELLOW_PUYO = new Builder("yellow_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.YELLOW).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final BlockEntry GREEN_PUYO = new Builder("green_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GREEN).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final BlockEntry TURQUOISE_PUYO = new Builder("turquoise_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.EMERALD).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final BlockEntry BLUE_PUYO = new Builder("blue_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.BLUE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final BlockEntry PURPLE_PUYO = new Builder("purple_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.PURPLE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final BlockEntry GRAY_PUYO = new Builder("gray_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GRAY).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem().build();
	public static final BlockEntry GARBAGE_PUYO = new Builder("garbage_puyo", new DirectionalBlock(Settings.copy(Blocks.STONE))).noItem().build();
	public static final BlockEntry POINT_PUYO = new Builder("point_puyo", new DirectionalBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(10))).noItem().build();
	public static final BlockEntry HARD_PUYO = new Builder("hard_puyo", new DirectionalBlock(Settings.copy(Blocks.STONE))).noItem().build();
	public static final BlockEntry IRON_PUYO = new Builder("iron_puyo", new DirectionalBlock(Settings.copy(Blocks.IRON_BLOCK))).noItem().build();

	/* YOUTUBE */
	public static final BlockEntry KORETATO_BLOCK = new Builder("koretato_block", new KoretatoBlock()).noItem().build();
	public static final PottedPlantCreator POTATO_FLOWER = new PottedPlantCreator(new Builder("potato_flower", new FlowerBlock(StatusEffects.HUNGER, 9, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).setRenderLayer(RenderLayer.getCutout()).build());
}
