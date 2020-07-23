package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.init.data.MubbleSoundTypes;
import hugman.mubble.init.world.MubbleConfiguredFeatures;
import hugman.mubble.object.block.CakeBlock;
import hugman.mubble.object.block.DoorBlock;
import hugman.mubble.object.block.GrassBlock;
import hugman.mubble.object.block.MushroomPlantBlock;
import hugman.mubble.object.block.NoteBlock;
import hugman.mubble.object.block.OreBlock;
import hugman.mubble.object.block.PressurePlateBlock;
import hugman.mubble.object.block.RootsBlock;
import hugman.mubble.object.block.StairsBlock;
import hugman.mubble.object.block.TrapdoorBlock;
import hugman.mubble.object.block.WoodButtonBlock;
import hugman.mubble.object.block.*;
import hugman.mubble.object.block.sapling_generator.*;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import hugman.mubble.util.creator.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.block.PressurePlateBlock.ActivationRule;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.List;

public class MubbleBlocks {
	/* Potted Plants (used for render layering) */
	public static final List<Block> POTTED_PLANTS = new ArrayList<Block>();

	/* Templates */
	protected static final Block.Settings pLeaves = FabricBlockSettings.of(Material.LEAVES).hardness(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque();

	/* MUBBLE */
	public static final BlockCreator OAK_VERTICAL_SLAB = new BlockCreator("oak", BlockTemplate.VERTICAL_SLAB, Blocks.OAK_SLAB);
	public static final BlockCreator SPRUCE_VERTICAL_SLAB = new BlockCreator("spruce", BlockTemplate.VERTICAL_SLAB, Blocks.SPRUCE_SLAB);
	public static final BlockCreator BIRCH_VERTICAL_SLAB = new BlockCreator("birch", BlockTemplate.VERTICAL_SLAB, Blocks.BIRCH_SLAB);
	public static final BlockCreator JUNGLE_VERTICAL_SLAB = new BlockCreator("jungle", BlockTemplate.VERTICAL_SLAB, Blocks.JUNGLE_SLAB);
	public static final BlockCreator ACACIA_VERTICAL_SLAB = new BlockCreator("acacia", BlockTemplate.VERTICAL_SLAB, Blocks.ACACIA_SLAB);
	public static final BlockCreator DARK_OAK_VERTICAL_SLAB = new BlockCreator("dark_oak", BlockTemplate.VERTICAL_SLAB, Blocks.DARK_OAK_SLAB);
	public static final BlockCreator CRIMSON_VERTICAL_SLAB = new BlockCreator("crimson", BlockTemplate.VERTICAL_SLAB, Blocks.CRIMSON_SLAB);
	public static final BlockCreator WARPED_VERTICAL_SLAB = new BlockCreator("warped", BlockTemplate.VERTICAL_SLAB, Blocks.WARPED_SLAB);
	public static final BlockCreator STONE_VERTICAL_SLAB = new BlockCreator("stone", BlockTemplate.VERTICAL_SLAB, Blocks.STONE_SLAB);
	public static final BlockCreator SMOOTH_STONE_VERTICAL_SLAB = new BlockCreator("smooth_stone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_STONE_SLAB);
	public static final BlockCreator COBBLESTONE_VERTICAL_SLAB = new BlockCreator("cobblestone", BlockTemplate.VERTICAL_SLAB, Blocks.COBBLESTONE_SLAB);
	public static final BlockCreator MOSSY_COBBLESTONE_VERTICAL_SLAB = new BlockCreator("mossy_cobblestone", BlockTemplate.VERTICAL_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB);
	public static final BlockCreator STONE_BRICK_VERTICAL_SLAB = new BlockCreator("stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.STONE_BRICK_SLAB);
	public static final BlockCreator MOSSY_STONE_BRICK_VERTICAL_SLAB = new BlockCreator("mossy_stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB);
	public static final BlockCreator GRANITE_VERTICAL_SLAB = new BlockCreator("granite", BlockTemplate.VERTICAL_SLAB, Blocks.GRANITE_SLAB);
	public static final BlockCreator POLISHED_GRANITE_VERTICAL_SLAB = new BlockCreator("polished_granite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_GRANITE_SLAB);
	public static final BlockCreator DIORITE_VERTICAL_SLAB = new BlockCreator("diorite", BlockTemplate.VERTICAL_SLAB, Blocks.DIORITE_SLAB);
	public static final BlockCreator POLISHED_DIORITE_VERTICAL_SLAB = new BlockCreator("polished_diorite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_DIORITE_SLAB);
	public static final BlockCreator ANDESITE_VERTICAL_SLAB = new BlockCreator("andesite", BlockTemplate.VERTICAL_SLAB, Blocks.ANDESITE_SLAB);
	public static final BlockCreator POLISHED_ANDESITE_VERTICAL_SLAB = new BlockCreator("polished_andesite", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_ANDESITE_SLAB);
	public static final BlockCreator BRICK_VERTICAL_SLAB = new BlockCreator("brick", BlockTemplate.VERTICAL_SLAB, Blocks.BRICK_SLAB);
	public static final BlockCreator SANDSTONE_VERTICAL_SLAB = new BlockCreator("sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SANDSTONE_SLAB);
	public static final BlockCreator CUT_SANDSTONE_VERTICAL_SLAB = new BlockCreator("cut_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.CUT_SANDSTONE_SLAB);
	public static final BlockCreator SMOOTH_SANDSTONE_VERTICAL_SLAB = new BlockCreator("smooth_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB);
	public static final BlockCreator RED_SANDSTONE_VERTICAL_SLAB = new BlockCreator("red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.RED_SANDSTONE_SLAB);
	public static final BlockCreator CUT_RED_SANDSTONE_VERTICAL_SLAB = new BlockCreator("cut_red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB);
	public static final BlockCreator SMOOTH_RED_SANDSTONE_VERTICAL_SLAB = new BlockCreator("smooth_red_sandstone", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB);
	public static final BlockCreator NETHER_BRICK_VERTICAL_SLAB = new BlockCreator("nether_brick", BlockTemplate.VERTICAL_SLAB, Blocks.NETHER_BRICK_SLAB);
	public static final BlockCreator RED_NETHER_BRICK_VERTICAL_SLAB = new BlockCreator("red_nether_brick", BlockTemplate.VERTICAL_SLAB, Blocks.RED_NETHER_BRICK_SLAB);
	public static final BlockCreator QUARTZ_VERTICAL_SLAB = new BlockCreator("quartz", BlockTemplate.VERTICAL_SLAB, Blocks.QUARTZ_SLAB);
	public static final BlockCreator SMOOTH_QUARTZ_VERTICAL_SLAB = new BlockCreator("smooth_quartz", BlockTemplate.VERTICAL_SLAB, Blocks.SMOOTH_QUARTZ_SLAB);
	public static final BlockCreator END_STONE_BRICK_VERTICAL_SLAB = new BlockCreator("end_stone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.END_STONE_BRICK_SLAB);
	public static final BlockCreator PURPUR_VERTICAL_SLAB = new BlockCreator("purpur", BlockTemplate.VERTICAL_SLAB, Blocks.PURPUR_SLAB);
	public static final BlockCreator PRISMARINE_VERTICAL_SLAB = new BlockCreator("prismarine", BlockTemplate.VERTICAL_SLAB, Blocks.PRISMARINE_SLAB);
	public static final BlockCreator PRISMARINE_BRICK_VERTICAL_SLAB = new BlockCreator("prismarine_brick", BlockTemplate.VERTICAL_SLAB, Blocks.PRISMARINE_BRICK_SLAB);
	public static final BlockCreator DARK_PRISMARINE_VERTICAL_SLAB = new BlockCreator("dark_prismarine", BlockTemplate.VERTICAL_SLAB, Blocks.DARK_PRISMARINE_SLAB);
	public static final BlockCreator BLACKSTONE_VERTICAL_SLAB = new BlockCreator("blackstone", BlockTemplate.VERTICAL_SLAB, Blocks.BLACKSTONE_SLAB);
	public static final BlockCreator POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB = new BlockCreator("polished_blackstone_brick", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
	public static final BlockCreator POLISHED_BLACKSTONE_SLAB = new BlockCreator("polished_blackstone", BlockTemplate.VERTICAL_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);

	public static final BlockCreator DARK_PRISMARINE_WALL = new BlockCreator("dark_prismarine", BlockTemplate.WALL, Blocks.DARK_PRISMARINE);

	public static final MSBlockCreator OAK_WOOD_BLOCKS = new MSBlockCreator("oak_wood", Blocks.OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator SPRUCE_WOOD_BLOCKS = new MSBlockCreator("spruce_wood", Blocks.SPRUCE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator BIRCH_WOOD_BLOCKS = new MSBlockCreator("birch_wood", Blocks.BIRCH_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator JUNGLE_WOOD_BLOCKS = new MSBlockCreator("jungle_wood", Blocks.JUNGLE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator ACACIA_WOOD_BLOCKS = new MSBlockCreator("acacia_wood", Blocks.ACACIA_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator DARK_OAK_WOOD_BLOCKS = new MSBlockCreator("dark_oak_wood", Blocks.DARK_OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator CRIMSON_HYPHAE_BLOCKS = new MSBlockCreator("crimson_hyphae", Blocks.CRIMSON_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);
	public static final MSBlockCreator WARPED_HYPHAE_BLOCKS = new MSBlockCreator("warped_hyphae", Blocks.WARPED_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WOOD_BUTTON);

	public static final Block OAK_LEAF_PILE = register("oak_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block SPRUCE_LEAF_PILE = register("spruce_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block BIRCH_LEAF_PILE = register("birch_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block JUNGLE_LEAF_PILE = register("jungle_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block ACACIA_LEAF_PILE = register("acacia_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block DARK_OAK_LEAF_PILE = register("dark_oak_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);

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

	public static final BlockCreator VANADIUM_ORE = new BlockCreator("vanadium_ore", new OreBlock(Settings.copy(Blocks.DIAMOND_ORE)), ItemGroup.BUILDING_BLOCKS);
	public static final SimpleBlockCreator VANADIUM_BLOCK = new SimpleBlockCreator("vanadium_block", Blocks.DIAMOND_BLOCK, MaterialColor.MAGENTA);


	public static final SaplingCreator AUTUMN_OAK_SAPLING = new SaplingCreator("autumn_oak", new AutumnOakSaplingGenerator());
	public static final LeavesCreator AUTUMN_OAK_LEAVES = new LeavesCreator("autumn_oak");
	public static final SaplingCreator AUTUMN_BIRCH_SAPLING = new SaplingCreator("autumn_birch", new AutumnBirchSaplingGenerator());
	public static final LeavesCreator AUTUMN_BIRCH_LEAVES = new LeavesCreator("autumn_birch");

	public static final Block CHERRY_OAK_PLANKS = register("cherry_oak_planks", new Block(Settings.copy(Blocks.DARK_OAK_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final SaplingCreator PINK_CHERRY_OAK_SAPLING = new SaplingCreator("pink_cherry_oak", new PinkCherryOakSaplingGenerator());
	public static final SaplingCreator WHITE_CHERRY_OAK_SAPLING = new SaplingCreator("white_cherry_oak", new WhiteCherryOakSaplingGenerator());
	public static final Block CHERRY_OAK_LOG = register("cherry_oak_log", new PillarBlock(Settings.copy(Blocks.OAK_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block STRIPPED_CHERRY_OAK_LOG = register("stripped_cherry_oak_log", new PillarBlock(Settings.copy(CHERRY_OAK_LOG)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block CHERRY_OAK_WOOD = register("cherry_oak_wood", new PillarBlock(Settings.copy(CHERRY_OAK_LOG)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block STRIPPED_CHERRY_OAK_WOOD = register("stripped_cherry_oak_wood", new PillarBlock(Settings.copy(CHERRY_OAK_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block PINK_CHERRY_OAK_LEAVES = register("pink_cherry_oak_leaves", new LeavesBlock(pLeaves), ItemGroup.DECORATIONS, 30, 60);
	public static final Block PINK_CHERRY_OAK_LEAF_PILE = register("pink_cherry_oak_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block WHITE_CHERRY_OAK_LEAVES = register("white_cherry_oak_leaves", new LeavesBlock(pLeaves), ItemGroup.DECORATIONS, 30, 60);
	public static final Block WHITE_CHERRY_OAK_LEAF_PILE = register("white_cherry_oak_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block CHERRY_OAK_PRESSURE_PLATE = register("cherry_oak_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, FabricBlockSettings.copy(MubbleBlocks.CHERRY_OAK_PLANKS).strength(0.5F, 0.0F).noCollision()), ItemGroup.REDSTONE);
	public static final Block CHERRY_OAK_TRAPDOOR = register("cherry_oak_trapdoor", new TrapdoorBlock(Settings.copy(MubbleBlocks.CHERRY_OAK_PLANKS)), ItemGroup.REDSTONE);
	public static final Block CHERRY_OAK_BUTTON = register("cherry_oak_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
	public static final Block CHERRY_OAK_STAIRS = register("cherry_oak_stairs", new StairsBlock(CHERRY_OAK_PLANKS), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block CHERRY_OAK_SLAB = register("cherry_oak_slab", new SlabBlock(Settings.copy(CHERRY_OAK_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block CHERRY_OAK_VERTICAL_SLAB = register("cherry_oak_vertical_slab", new VerticalSlabBlock(Settings.copy(CHERRY_OAK_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block CHERRY_OAK_FENCE_GATE = register("cherry_oak_fence_gate", new FenceGateBlock(Settings.copy(CHERRY_OAK_PLANKS)), ItemGroup.REDSTONE, 5, 20);
	public static final Block CHERRY_OAK_FENCE = register("cherry_oak_fence", new FenceBlock(Settings.copy(CHERRY_OAK_PLANKS)), ItemGroup.DECORATIONS, 5, 20);
	public static final Block CHERRY_OAK_DOOR = register("cherry_oak_door", new DoorBlock(Settings.copy(CHERRY_OAK_PLANKS)), ItemGroup.REDSTONE);
	public static final Block CHERRY_OAK_WOOD_STAIRS = register("cherry_oak_wood_stairs", new StairsBlock(CHERRY_OAK_WOOD), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block CHERRY_OAK_WOOD_SLAB = register("cherry_oak_wood_slab", new SlabBlock(Settings.copy(CHERRY_OAK_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block CHERRY_OAK_WOOD_VERTICAL_SLAB = register("cherry_oak_wood_vertical_slab", new VerticalSlabBlock(Settings.copy(CHERRY_OAK_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block CHERRY_OAK_WOOD_BUTTON = register("cherry_oak_wood_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);

	public static final NormalWoodTypeCreator PALM_WOOD = new NormalWoodTypeCreator("palm", new PalmSaplingGenerator(), MaterialColor.ORANGE, MaterialColor.CYAN_TERRACOTTA);

	public static final MSCBlockCreator STAINED_BRICK_BLOCKS = new MSCBlockCreator("bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator TERRACOTTA_BLOCKS = new MSBlockCreator("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator STAINED_TERRACOTTA_BLOCKS = new MSCBlockCreator("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator DARK_PRISMARINE_BLOCKS = new MSCBlockCreator("dark_prismarine", Blocks.DARK_PRISMARINE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSCBlockCreator CONCRETE_BLOCKS = new MSCBlockCreator("concrete", Blocks.BLUE_CONCRETE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.STONE_PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator QUARTZ_PAVING_BLOCKS = new MSCBlockCreator("quartz_paving", Blocks.QUARTZ_BLOCK, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final Block BLUE_CHRISTMAS_BAUBLE = register("blue_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.BLUE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block LIGHT_BLUE_CHRISTMAS_BAUBLE = register("light_blue_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.LIGHT_BLUE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block PURPLE_CHRISTMAS_BAUBLE = register("purple_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.PURPLE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block MAGENTA_CHRISTMAS_BAUBLE = register("magenta_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.MAGENTA_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block PINK_CHRISTMAS_BAUBLE = register("pink_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.PINK_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block RED_CHRISTMAS_BAUBLE = register("red_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.RED_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block ORANGE_CHRISTMAS_BAUBLE = register("orange_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.ORANGE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block YELLOW_CHRISTMAS_BAUBLE = register("yellow_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.YELLOW_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block GREEN_CHRISTMAS_BAUBLE = register("green_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.GREEN_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);
	public static final Block WHITE_CHRISTMAS_BAUBLE = register("white_christmas_bauble", new Block(FabricBlockSettings.of(Material.GLASS, MaterialColor.WHITE_TERRACOTTA).hardness(0.3F).sounds(BlockSoundGroup.GLASS)), ItemGroup.DECORATIONS);

	public static final BlockCreator RED_SHINY_GARLAND = new BlockCreator("red_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.RED).hardness(0.2F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 30, 60);
	public static final BlockCreator SILVER_SHINY_GARLAND = new BlockCreator("silver_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.CLAY).hardness(0.2F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 30, 60);
	public static final BlockCreator GOLD_SHINY_GARLAND = new BlockCreator("gold_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.GOLD).hardness(0.2F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 30, 60);

	public static final Block WHITE_PRESENT = register("white_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block BLACK_PRESENT = register("black_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLACK_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block BLUE_PRESENT = register("blue_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLUE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block GREEN_PRESENT = register("green_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GREEN_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block YELLOW_PRESENT = register("yellow_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.YELLOW_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block RED_PRESENT = register("red_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.RED_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block PURPLE_PRESENT = register("purple_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.PURPLE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block GOLDEN_PRESENT = register("golden_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GOLD).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);

	public static final SimpleBlockCreator FOOTBLOCK = new SimpleBlockCreator("footblock", Blocks.WHITE_WOOL, ItemGroup.DECORATIONS);

	public static final MCBlockCreator CLOUD_BLOCKS = new MCBlockCreator(BlockTemplate.CLOUD_BLOCK, FabricBlockSettings.of(Material.LEAVES).sounds(BlockSoundGroup.WOOL).hardness(0f).noCollision());

	public static final Block TOMATOES = register("tomatoes", new CropsBlock());
	public static final Block SALAD = register("salad", new CropsBlock());

	public static final Block BLUEBERRY_BUSH = register("blueberry_bush", new BerryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)), 60, 100);
	public static final Block CHEESE_BLOCK = register("cheese_block", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.YELLOW).hardness(0.5f).sounds(BlockSoundGroup.SNOW)), ItemGroup.FOOD, 60, 60);
	public static final Block CHOCOLATE_CAKE = register("chocolate_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL)), ItemGroup.FOOD);
	public static final Block MINECRAFT_10TH_ANNIVERSARY_CAKE = register("minecraft_10th_anniversary_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL)), ItemGroup.FOOD);

	public static final Block WHITE_BALLOON = register("white_balloon", new BalloonBlock(DyeColor.WHITE), ItemGroup.DECORATIONS, 30, 60);
	public static final Block LIGHT_GRAY_BALLOON = register("light_gray_balloon", new BalloonBlock(DyeColor.LIGHT_GRAY), ItemGroup.DECORATIONS, 30, 60);
	public static final Block GRAY_BALLOON = register("gray_balloon", new BalloonBlock(DyeColor.GRAY), ItemGroup.DECORATIONS, 30, 60);
	public static final Block BLACK_BALLOON = register("black_balloon", new BalloonBlock(DyeColor.BLACK), ItemGroup.DECORATIONS, 30, 60);
	public static final Block BROWN_BALLOON = register("brown_balloon", new BalloonBlock(DyeColor.BROWN), ItemGroup.DECORATIONS, 30, 60);
	public static final Block RED_BALLOON = register("red_balloon", new BalloonBlock(DyeColor.RED), ItemGroup.DECORATIONS, 30, 60);
	public static final Block ORANGE_BALLOON = register("orange_balloon", new BalloonBlock(DyeColor.ORANGE), ItemGroup.DECORATIONS, 30, 60);
	public static final Block YELLOW_BALLOON = register("yellow_balloon", new BalloonBlock(DyeColor.YELLOW), ItemGroup.DECORATIONS, 30, 60);
	public static final Block LIME_BALLOON = register("lime_balloon", new BalloonBlock(DyeColor.LIME), ItemGroup.DECORATIONS, 30, 60);
	public static final Block GREEN_BALLOON = register("green_balloon", new BalloonBlock(DyeColor.GREEN), ItemGroup.DECORATIONS, 30, 60);
	public static final Block CYAN_BALLOON = register("cyan_balloon", new BalloonBlock(DyeColor.CYAN), ItemGroup.DECORATIONS, 30, 60);
	public static final Block LIGHT_BLUE_BALLOON = register("light_blue_balloon", new BalloonBlock(DyeColor.LIGHT_BLUE), ItemGroup.DECORATIONS, 30, 60);
	public static final Block BLUE_BALLOON = register("blue_balloon", new BalloonBlock(DyeColor.BLUE), ItemGroup.DECORATIONS, 30, 60);
	public static final Block PURPLE_BALLOON = register("purple_balloon", new BalloonBlock(DyeColor.PURPLE), ItemGroup.DECORATIONS, 30, 60);
	public static final Block MAGENTA_BALLOON = register("magenta_balloon", new BalloonBlock(DyeColor.MAGENTA), ItemGroup.DECORATIONS, 30, 60);
	public static final Block PINK_BALLOON = register("pink_balloon", new BalloonBlock(DyeColor.PINK), ItemGroup.DECORATIONS, 30, 60);

	public static final BlockCreator UNSTABLE_STONE = new BlockCreator("unstable_stone", new UnstableBlock(FabricBlockSettings.copy(Blocks.STONE).strength(0.1F, 0.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockEntry FLUID_TANK = new BlockEntry.Builder("fluid_tank", new FluidTankBlock(FabricBlockSettings.copy(Blocks.OBSIDIAN).nonOpaque())).setItemGroup(ItemGroup.REDSTONE).setRenderLayer(RenderLayer.getCutoutMipped()).build();
	public static final BlockCreator PLACER = new BlockCreator("placer", new PlacerBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F)), ItemGroup.REDSTONE);
	public static final BlockCreator TIMESWAP_TABLE = new BlockCreator("timeswap_table", new TimeswapTableBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F)), ItemGroup.DECORATIONS);

	public static final BlockEntry DANDELION_PILE = new BlockEntry.Builder("dandelion_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry POPPY_PILE = new BlockEntry.Builder("poppy_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry BLUE_ORCHID_PILE = new BlockEntry.Builder("blue_orchid_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry ALLIUM_PILE = new BlockEntry.Builder("allium_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry AZURE_BLUET_PILE = new BlockEntry.Builder("azure_bluet_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry RED_TULIP_PILE = new BlockEntry.Builder("red_tulip_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry ORANGE_TULIP_PILE = new BlockEntry.Builder("orange_tulip_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry WHITE_TULIP_PILE = new BlockEntry.Builder("white_tulip_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry PINK_TULIP_PILE = new BlockEntry.Builder("pink_tulip_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry OXEYE_DAISY_PILE = new BlockEntry.Builder("oxeye_daisy_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry CORNFLOWER_PILE = new BlockEntry.Builder("cornflower_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry LILY_OF_THE_VALLEY_PILE = new BlockEntry.Builder("lily_of_the_valley_pile", new PileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();
	public static final BlockEntry WITHER_ROSE_PILE = new BlockEntry.Builder("wither_rose_pile", new WitherRosePileBlock(BlockTemplate.flowerPileSettings.noCollision())).setItemGroup(ItemGroup.DECORATIONS).setFlammability(60, 100).build();

	public static final SimpleBlockCreator PERMAROCK = new SimpleBlockCreator("permarock", FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.4F));
	public static final MSBlockCreator PERMAFROST_BRICKS = new MSBlockCreator("permafrost_bricks", Blocks.NETHER_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.FENCE);
	public static final MSBlockCreator BLUE_PERMAFROST_BRICKS = new MSBlockCreator("blue_permafrost_bricks", Blocks.RED_NETHER_BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final SimpleBlockCreator PERMAFROST_BISMUTH_ORE = new SimpleBlockCreator("permafrost_bismuth_ore", FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.3F));
	public static final SimpleBlockCreator FROZEN_OBSIDIAN = new SimpleBlockCreator("frozen_obsidian", FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).strength(75.0F, 1800.0F));

	public static final BlockCreator AMARANTH_DYLIUM = new BlockCreator("amaranth_dylium", new DyliumBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.field_25702).requiresTool().strength(3.0F, 9.0F).sounds(BlockSoundGroup.NYLIUM).ticksRandomly()), ItemGroup.BUILDING_BLOCKS);
	public static final SimpleBlockCreator AMARANTH_WART_BLOCK = new SimpleBlockCreator("amaranth_wart_block", FabricBlockSettings.of(Material.SOLID_ORGANIC, MaterialColor.field_25708).breakByTool(FabricToolTags.HOES).strength(1.0F).sounds(BlockSoundGroup.WART_BLOCK));
	public static final BlockCreator AMARANTH_ROOTS = new BlockCreator("amaranth_roots", new RootsBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.CYAN).noCollision().breakInstantly().sounds(BlockSoundGroup.ROOTS)), ItemGroup.DECORATIONS);
	/*
	public static final NetherWoodTypeCreator DARK_AMARANTH_WOOD = new NetherWoodTypeCreator("dark_amaranth", () -> {
		return Feature.HUGE_FUNGUS.configure(MubbleConfiguredFeatures.AMARANTH_FUNGUS_CONFIG);
	}, MaterialColor.LIGHT_GRAY, MaterialColor.field_25707);

	 */


	/* SUPER MARIO (MAKER) */
	public static final BlockCreator SMB_QUESTION_BLOCK = new BlockCreator("smb", BlockTemplate.QUESTION_BLOCK, BlockTemplate.questionBlockSettings);
	public static final BlockCreator SMB3_QUESTION_BLOCK = new BlockCreator("smb3", BlockTemplate.QUESTION_BLOCK, BlockTemplate.questionBlockSettings);
	public static final BlockCreator SMW_QUESTION_BLOCK = new BlockCreator("smw", BlockTemplate.QUESTION_BLOCK, BlockTemplate.questionBlockSettings);
	public static final BlockCreator NSMBU_QUESTION_BLOCK = new BlockCreator("nsmbu", BlockTemplate.QUESTION_BLOCK, BlockTemplate.questionBlockSettings);
	public static final SimpleBlockCreator SMB_GROUND_GROUND_BLOCK = new SimpleBlockCreator("smb_ground_ground_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_UNDERGROUND_GROUND_BLOCK = new SimpleBlockCreator("smb_underground_ground_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_UNDERWATER_GROUND_BLOCK = new SimpleBlockCreator("smb_underwater_ground_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_GHOST_HOUSE_GROUND_BLOCK = new SimpleBlockCreator("smb_ghost_house_ground_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_AIRSHIP_GROUND_BLOCK = new SimpleBlockCreator("smb_airship_ground_block", Blocks.IRON_BLOCK);
	public static final SimpleBlockCreator SMB_NIGHT_AIRSHIP_GROUND_BLOCK = new SimpleBlockCreator("smb_night_airship_ground_block", Blocks.IRON_BLOCK);
	public static final SimpleBlockCreator SMB_CASTLE_GROUND_BLOCK = new SimpleBlockCreator("smb_castle_ground_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_DESERT_GROUND_BLOCK = new SimpleBlockCreator("smb_desert_ground_block", Blocks.SAND);
	public static final SimpleBlockCreator SMB_FOREST_GROUND_BLOCK = new SimpleBlockCreator("smb_forest_ground_block", Blocks.STONE);
	public static final BlockCreator SMB_SNOW_GROUND_BLOCK = new BlockCreator("smb_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_NIGHT_SNOW_GROUND_BLOCK = new BlockCreator("smb_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final SimpleBlockCreator SMB_SKY_GROUND_BLOCK = new SimpleBlockCreator("smb_sky_ground_block", Blocks.WHITE_WOOL, ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_GROUND_GROUND_BLOCK = new BlockCreator("smb3_ground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_UNDERGROUND_GROUND_BLOCK = new BlockCreator("smb3_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_UNDERWATER_GROUND_BLOCK = new BlockCreator("smb3_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_GHOST_HOUSE_GROUND_BLOCK = new BlockCreator("smb3_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final SimpleBlockCreator SMB3_AIRSHIP_GROUND_BLOCK = new SimpleBlockCreator("smb3_airship_ground_block", Blocks.OAK_WOOD);
	public static final SimpleBlockCreator SMB3_NIGHT_AIRSHIP_GROUND_BLOCK = new SimpleBlockCreator("smb3_night_airship_ground_block", Blocks.OAK_WOOD);
	public static final SimpleBlockCreator SMB3_CASTLE_GROUND_BLOCK = new SimpleBlockCreator("smb3_castle_ground_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB3_NIGHT_CASTLE_GROUND_BLOCK = new SimpleBlockCreator("smb3_night_castle_ground_block", Blocks.STONE);
	public static final BlockCreator SMB3_DESERT_GROUND_BLOCK = new BlockCreator("smb3_desert_ground_block", new OverBlock(Settings.copy(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_SNOW_GROUND_BLOCK = new BlockCreator("smb3_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_NIGHT_SNOW_GROUND_BLOCK = new BlockCreator("smb3_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_SKY_GROUND_BLOCK = new BlockCreator("smb3_sky_ground_block", new OverBlock(Settings.copy(Blocks.WHITE_WOOL)), ItemGroup.BUILDING_BLOCKS, 30, 60);
	public static final BlockCreator SMW_GROUND_GROUND_BLOCK = new BlockCreator("smw_ground_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_UNDERGROUND_GROUND_BLOCK = new BlockCreator("smw_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_UNDERWATER_GROUND_BLOCK = new BlockCreator("smw_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_GHOST_HOUSE_GROUND_BLOCK = new BlockCreator("smw_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_AIRSHIP_GROUND_BLOCK = new BlockCreator("smw_airship_ground_block", new OverBlock(Settings.copy(Blocks.OAK_WOOD)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_CASTLE_GROUND_BLOCK = new BlockCreator("smw_castle_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_DESERT_GROUND_BLOCK = new BlockCreator("smw_desert_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_FOREST_GROUND_BLOCK = new BlockCreator("smw_forest_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_SNOW_GROUND_BLOCK = new BlockCreator("smw_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_NIGHT_SNOW_GROUND_BLOCK = new BlockCreator("smw_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_SKY_GROUND_BLOCK = new BlockCreator("smw_sky_ground_block", new OverBlock(Settings.copy(Blocks.WHITE_WOOL)), ItemGroup.BUILDING_BLOCKS, 30, 60);
	public static final BlockCreator NSMBU_GROUND_GROUND_BLOCK = new BlockCreator("nsmbu_ground_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_UNDERGROUND_GROUND_BLOCK = new BlockCreator("nsmbu_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_UNDERWATER_GROUND_BLOCK = new BlockCreator("nsmbu_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_GHOST_HOUSE_GROUND_BLOCK = new BlockCreator("nsmbu_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final SimpleBlockCreator NSMBU_CASTLE_GROUND_BLOCK = new SimpleBlockCreator("nsmbu_castle_ground_block", Blocks.STONE);
	public static final BlockCreator NSMBU_DESERT_GROUND_BLOCK = new BlockCreator("nsmbu_desert_ground_block", new OverBlock(Settings.copy(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_FOREST_GROUND_BLOCK = new BlockCreator("nsmbu_forest_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_SNOW_GROUND_BLOCK = new BlockCreator("nsmbu_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_NIGHT_SNOW_GROUND_BLOCK = new BlockCreator("nsmbu_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_SKY_GROUND_BLOCK = new BlockCreator("nsmbu_sky_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_EMPTY_BLOCK = new BlockCreator("smb_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_EMPTY_BLOCK = new BlockCreator("smb3_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_EMPTY_BLOCK = new BlockCreator("smw_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_EMPTY_BLOCK = new BlockCreator("nsmbu_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_ROTATING_BLOCK = new BlockCreator("smb_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_ROTATING_BLOCK = new BlockCreator("smb3_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_ROTATING_BLOCK = new BlockCreator("smw_rotating_block", new RotatingBlock(MubbleSoundTypes.SMW_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_ROTATING_BLOCK = new BlockCreator("nsmbu_rotating_block", new RotatingBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final SimpleBlockCreator LIGHT_BLOCK = new SimpleBlockCreator("light_block", FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(15));
	public static final BlockCreator SMB_GROUND_BRICK_BLOCK = new BlockCreator("smb_ground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_UNDERGROUND_BRICK_BLOCK = new BlockCreator("smb_underground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_CASTLE_BRICK_BLOCK = new BlockCreator("smb_castle_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_SNOW_BRICK_BLOCK = new BlockCreator("smb_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_NIGHT_SNOW_BRICK_BLOCK = new BlockCreator("smb_night_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_BRICK_BLOCK = new BlockCreator("smb3_brick_block", new BrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_BRICK_BLOCK = new BlockCreator("smw_brick_block", new BrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_BRICK_BLOCK = new BlockCreator("nsmbu_brick_block", new BrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_GOLDEN_BRICK_BLOCK = new BlockCreator("smb_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_GOLDEN_BRICK_BLOCK = new BlockCreator("smb3_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_GOLDEN_BRICK_BLOCK = new BlockCreator("smw_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_GOLDEN_BRICK_BLOCK = new BlockCreator("nsmbu_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final SimpleBlockCreator SMB_GROUND_HARD_BLOCK = new SimpleBlockCreator("smb_ground_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_UNDERGROUND_HARD_BLOCK = new SimpleBlockCreator("smb_underground_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_UNDERWATER_HARD_BLOCK = new SimpleBlockCreator("smb_underwater_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_CASTLE_HARD_BLOCK = new SimpleBlockCreator("smb_castle_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_SNOW_HARD_BLOCK = new SimpleBlockCreator("smb_snow_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_NIGHT_SNOW_HARD_BLOCK = new SimpleBlockCreator("smb_night_snow_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB3_HARD_BLOCK = new SimpleBlockCreator("smb3_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMW_STONE_HARD_BLOCK = new SimpleBlockCreator("smw_stone_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMW_WOOD_HARD_BLOCK = new SimpleBlockCreator("smw_wood_hard_block", Blocks.OAK_PLANKS);
	public static final SimpleBlockCreator NSMBU_HARD_BLOCK = new SimpleBlockCreator("nsmbu_hard_block", Blocks.STONE);
	public static final SimpleBlockCreator SMB_ICE_BLOCK = new SimpleBlockCreator("smb_ice_block", Blocks.PACKED_ICE);
	public static final SimpleBlockCreator SMB3_ICE_BLOCK = new SimpleBlockCreator("smb3_ice_block", Blocks.PACKED_ICE);
	public static final SimpleBlockCreator SMW_ICE_BLOCK = new SimpleBlockCreator("smw_ice_block", Blocks.PACKED_ICE);
	public static final SimpleBlockCreator NSMBU_ICE_BLOCK = new SimpleBlockCreator("nsmbu_ice_block", Blocks.PACKED_ICE);
	public static final BlockCreator SMB_NOTE_BLOCK = new BlockCreator("smb_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_NOTE_BLOCK = new BlockCreator("smb3_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_NOTE_BLOCK = new BlockCreator("smw_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_NOTE_BLOCK = new BlockCreator("nsmbu_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_SUPER_NOTE_BLOCK = new BlockCreator("smb_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB3_SUPER_NOTE_BLOCK = new BlockCreator("smb3_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMW_SUPER_NOTE_BLOCK = new BlockCreator("smw_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator NSMBU_SUPER_NOTE_BLOCK = new BlockCreator("nsmbu_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final BlockCreator SMB_DOOR = new BlockCreator("smb", BlockTemplate.DOOR, Blocks.OAK_DOOR);
	public static final BlockCreator SMB3_DOOR = new BlockCreator("smb3", BlockTemplate.DOOR, Blocks.OAK_DOOR);
	public static final BlockCreator SMW_DOOR = new BlockCreator("smw", BlockTemplate.DOOR, Blocks.OAK_DOOR);
	public static final BlockCreator NSMBU_DOOR = new BlockCreator("nsmbu", BlockTemplate.DOOR, Blocks.OAK_DOOR);
	public static final BlockCreator SMB_KEY_DOOR = new BlockCreator("smb", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR);
	public static final BlockCreator SMB3_KEY_DOOR = new BlockCreator("smb3", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR);
	public static final BlockCreator SMW_KEY_DOOR = new BlockCreator("smw", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR);
	public static final BlockCreator NSMBU_KEY_DOOR = new BlockCreator("nsmbu", BlockTemplate.KEY_DOOR, Blocks.IRON_DOOR);

	/* SUPER MARIO (OTHERS) */
	public static final Block FIRE_FLOWER = registerPotable("fire_flower", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 60, 100);
	public static final Block ICE_FLOWER = registerPotable("ice_flower", new FlowerBlock(StatusEffects.MINING_FATIGUE, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 60, 100);
	public static final Block BOOMERANG_FLOWER = registerPotable("boomerang_flower", new FlowerBlock(StatusEffects.HASTE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 60, 100);
	public static final Block CLOUD_FLOWER = registerPotable("cloud_flower", new CloudFlowerBlock(StatusEffects.SLOW_FALLING, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 60, 100);
	public static final Block GOLD_FLOWER = registerPotable("gold_flower", new FlowerBlock(MubbleEffects.HEAVINESS, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(5)), ItemGroup.DECORATIONS, 60, 100);
	public static final Block WHITE_MUSHROOM_BLOCK = register("white_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.WHITE).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block LIGHT_GRAY_MUSHROOM_BLOCK = register("light_gray_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.LIGHT_GRAY).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block GRAY_MUSHROOM_BLOCK = register("gray_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.GRAY).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block BLACK_MUSHROOM_BLOCK = register("black_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.BLACK).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block ORANGE_MUSHROOM_BLOCK = register("orange_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.ORANGE).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block YELLOW_MUSHROOM_BLOCK = register("yellow_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.YELLOW).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block LIME_MUSHROOM_BLOCK = register("lime_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.LIME).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block GREEN_MUSHROOM_BLOCK = register("green_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.GREEN).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block CYAN_MUSHROOM_BLOCK = register("cyan_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.CYAN).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block LIGHT_BLUE_MUSHROOM_BLOCK = register("light_blue_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.LIGHT_BLUE).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block BLUE_MUSHROOM_BLOCK = register("blue_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.BLUE).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block PURPLE_MUSHROOM_BLOCK = register("purple_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.PURPLE).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block MAGENTA_MUSHROOM_BLOCK = register("magenta_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.MAGENTA).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block PINK_MUSHROOM_BLOCK = register("pink_mushroom_block", new MushroomBlock(FabricBlockSettings.of(Material.WOOD, DyeColor.PINK).hardness(0.2F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block WHITE_MUSHROOM = registerPotable("white_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), WHITE_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block LIGHT_GRAY_MUSHROOM = registerPotable("light_gray_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), LIGHT_GRAY_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block GRAY_MUSHROOM = registerPotable("gray_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), GRAY_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block BLACK_MUSHROOM = registerPotable("black_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), BLACK_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block ORANGE_MUSHROOM = registerPotable("orange_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), ORANGE_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block YELLOW_MUSHROOM = registerPotable("yellow_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), YELLOW_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block LIME_MUSHROOM = registerPotable("lime_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), LIME_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block GREEN_MUSHROOM = registerPotable("green_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), GREEN_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block CYAN_MUSHROOM = registerPotable("cyan_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), CYAN_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block LIGHT_BLUE_MUSHROOM = registerPotable("light_blue_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), LIGHT_BLUE_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block BLUE_MUSHROOM = registerPotable("blue_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), BLUE_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block PURPLE_MUSHROOM = registerPotable("purple_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), PURPLE_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block MAGENTA_MUSHROOM = registerPotable("magenta_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), MAGENTA_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);
	public static final Block PINK_MUSHROOM = registerPotable("pink_mushroom", new GrowableMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1), PINK_MUSHROOM_BLOCK), ItemGroup.DECORATIONS);

	/* KIRBY */
	public static final Block KIRBY_BLOCK = register("kirby_block", new DirectionalBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.PINK).hardness(0.5F).sounds(BlockSoundGroup.WOOL)), ItemGroup.DECORATIONS);

	/* TETRIS */
	public static final Block WHITE_TETRIS_BLOCK = register("white_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block LIGHT_GRAY_TETRIS_BLOCK = register("light_gray_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.LIGHT_GRAY).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block GRAY_TETRIS_BLOCK = register("gray_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.GRAY).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BLACK_TETRIS_BLOCK = register("black_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.BLACK).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BROWN_TETRIS_BLOCK = register("brown_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.BROWN).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block RED_TETRIS_BLOCK = register("red_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.RED).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block ORANGE_TETRIS_BLOCK = register("orange_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.ORANGE).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block YELLOW_TETRIS_BLOCK = register("yellow_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.YELLOW).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block LIME_TETRIS_BLOCK = register("lime_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.LIME).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block GREEN_TETRIS_BLOCK = register("green_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.GREEN).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block CYAN_TETRIS_BLOCK = register("cyan_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.CYAN).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block LIGHT_BLUE_TETRIS_BLOCK = register("light_blue_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.LIGHT_BLUE).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BLUE_TETRIS_BLOCK = register("blue_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.BLUE).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PURPLE_TETRIS_BLOCK = register("purple_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.PURPLE).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MAGENTA_TETRIS_BLOCK = register("magenta_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.MAGENTA).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PINK_TETRIS_BLOCK = register("pink_tetris_block", new FallingBlock(FabricBlockSettings.of(Material.STONE, DyeColor.PINK).strength(1.5F, 6.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block TETRIS_GLASS = register("tetris_glass", new TetrisGlassBlock(Settings.copy(Blocks.GLASS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block JAPANESE_TETRIS_CUSHION = register("japanese_tetris_cushion", new FallingBlock(Settings.copy(Blocks.RED_WOOL)), ItemGroup.BUILDING_BLOCKS, 30, 60);
	public static final Block RAINBOW_TETRIS_SCAFFOLDING = register("rainbow_tetris_scaffolding", new FallingBlock(Settings.copy(Blocks.IRON_BLOCK)), ItemGroup.DECORATIONS);
	public static final Block MONOCHROME_TETRIS_SCAFFOLDING = register("monochrome_tetris_scaffolding", new FallingBlock(Settings.copy(Blocks.IRON_BLOCK)), ItemGroup.DECORATIONS);

	/* CASTLEVANIA */
	public static final Block VAMPIRE_STONE = register("vampire_stone", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);

	public static final Block MEDUSA_STONE = register("medusa_stone", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MEDUSA_BRICKS = register("medusa_bricks", new Block(Settings.copy(Blocks.STONE_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MEDUSA_BRICK_STAIRS = register("medusa_brick_stairs", new StairsBlock(MEDUSA_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final Block MEDUSA_BRICK_SLAB = register("medusa_brick_slab", new SlabBlock(Settings.copy(MEDUSA_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MEDUSA_BRICK_VERTICAL_SLAB = register("medusa_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(MEDUSA_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MEDUSA_BRICK_WALL = register("medusa_brick_wall", new WallBlock(Settings.copy(MubbleBlocks.MEDUSA_BRICKS)), ItemGroup.DECORATIONS);

	public static final Block WHITE_CANDY_CANE_PILLAR = register("white_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block LIGHT_GRAY_CANDY_CANE_PILLAR = register("light_gray_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.LIGHT_GRAY).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block GRAY_CANDY_CANE_PILLAR = register("gray_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.GRAY).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block BLACK_CANDY_CANE_PILLAR = register("black_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.BLACK).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block BROWN_CANDY_CANE_PILLAR = register("brown_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.BROWN).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block RED_CANDY_CANE_PILLAR = register("red_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.RED).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block ORANGE_CANDY_CANE_PILLAR = register("orange_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.ORANGE).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block YELLOW_CANDY_CANE_PILLAR = register("yellow_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.YELLOW).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block LIME_CANDY_CANE_PILLAR = register("lime_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.LIME).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block GREEN_CANDY_CANE_PILLAR = register("green_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.GREEN).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block CYAN_CANDY_CANE_PILLAR = register("cyan_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.CYAN).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block LIGHT_BLUE_CANDY_CANE_PILLAR = register("light_blue_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.LIGHT_BLUE).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block BLUE_CANDY_CANE_PILLAR = register("blue_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.BLUE).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block PURPLE_CANDY_CANE_PILLAR = register("purple_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.PURPLE).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block MAGENTA_CANDY_CANE_PILLAR = register("magenta_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.MAGENTA).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);
	public static final Block PINK_CANDY_CANE_PILLAR = register("pink_candy_cane_pillar", new PillarBlock(FabricBlockSettings.of(Material.STONE, DyeColor.PINK).hardness(0.2F)), ItemGroup.DECORATIONS, 5, 10);

	/* SONIC */
	public static final Block GREEN_HILL_GRASS_BLOCK = register("green_hill_grass_block", new GrassBlock(Settings.copy(Blocks.GRASS_BLOCK)), ItemGroup.BUILDING_BLOCKS);
	public static final Block GREEN_HILL_DIRT = register("green_hill_dirt", new Block(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MARBLE_ZONE_STONE = register("marble_zone_stone", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block YELLOW_STUDIOPOLIS_CLAPPER = register("yellow_studiopolis_clapper", new DirectionalBlock(Settings.copy(Blocks.IRON_BLOCK)), ItemGroup.DECORATIONS);
	public static final Block BLUE_STUDIOPOLIS_CLAPPER = register("blue_studiopolis_clapper", new DirectionalBlock(Settings.copy(Blocks.IRON_BLOCK)), ItemGroup.DECORATIONS);

	public static final WoodTypeCreator PRESS_GARDEN_WOOD = new WoodTypeCreator("press_garden", MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707, false);
	public static final SaplingCreator RED_PRESS_GARDEN_SAPLING = new SaplingCreator("red_press_garden", new RedPressGardenSaplingGenerator());
	public static final SaplingCreator PINK_PRESS_GARDEN_SAPLING = new SaplingCreator("pink_press_garden", new PinkPressGardenSaplingGenerator());
	public static final LeavesCreator RED_PRESS_GARDEN_LEAVES = new LeavesCreator("red_press_garden");
	public static final LeavesCreator PINK_PRESS_GARDEN_LEAVES = new LeavesCreator("pink_press_garden");

	public static final Block SPRING = register("spring", new SpringBlock(FabricBlockSettings.of(Material.METAL).hardness(4f)), ItemGroup.TRANSPORTATION);

	/* UNDERTALE / DELTARUNE */
	public static final NormalWoodTypeCreator SCARLET_WOOD = new NormalWoodTypeCreator("scarlet", new ScarletSaplingGenerator(), MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707);
	public static final Block SCARLET_MUSHROOM = registerPotable("scarlet_mushroom", new MushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7)), ItemGroup.DECORATIONS);
	public static final Block SCARLET_ORCHID = registerPotable("scarlet_orchid", new FlowerBlock(StatusEffects.GLOWING, 8, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7)), ItemGroup.DECORATIONS, 60, 100);

	/* CELESTE */
	public static final Block GIRDER = register("girder", new Block(Settings.copy(Blocks.IRON_BLOCK)), ItemGroup.BUILDING_BLOCKS);

	public static final Block MIRROR_TEMPLE_BRICKS = register("mirror_temple_bricks", new Block(Settings.copy(Blocks.BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MIRROR_TEMPLE_BRICK_STAIRS = register("mirror_temple_brick_stairs", new StairsBlock(MIRROR_TEMPLE_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final Block MIRROR_TEMPLE_BRICK_SLAB = register("mirror_temple_brick_slab", new SlabBlock(Settings.copy(MIRROR_TEMPLE_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MIRROR_TEMPLE_BRICK_VERTICAL_SLAB = register("mirror_temple_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(MIRROR_TEMPLE_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MIRROR_TEMPLE_BRICK_WALL = register("mirror_temple_brick_wall", new WallBlock(Settings.copy(MIRROR_TEMPLE_BRICKS)), ItemGroup.DECORATIONS);

	public static final Block OLD_SITE_BRICKS = register("old_site_bricks", new Block(Settings.copy(Blocks.BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block OLD_SITE_BRICK_STAIRS = register("old_site_brick_stairs", new StairsBlock(OLD_SITE_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final Block OLD_SITE_BRICK_SLAB = register("old_site_brick_slab", new SlabBlock(Settings.copy(OLD_SITE_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block OLD_SITE_BRICK_VERTICAL_SLAB = register("old_site_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(OLD_SITE_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block OLD_SITE_BRICK_WALL = register("old_site_brick_wall", new WallBlock(Settings.copy(OLD_SITE_BRICKS)), ItemGroup.DECORATIONS);

	public static final Block ELDER_PEBBLES = register("elder_pebbles", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.RED).strength(2.0F, 6.0F).lightLevel(5)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DREAM_BLOCK = register("dream_block", new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC).hardness(0.4f).sounds(MubbleSoundTypes.DREAM_BLOCK)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DREAM_BEDROCK = register("dream_bedrock", new Block(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing()), ItemGroup.BUILDING_BLOCKS);

	/* PUYO PUYO */
	public static final Block RED_PUYO = register("red_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.RED).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
	public static final Block YELLOW_PUYO = register("yellow_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.YELLOW).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
	public static final Block GREEN_PUYO = register("green_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GREEN).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
	public static final Block TURQUOISE_PUYO = register("turquoise_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.EMERALD).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
	public static final Block BLUE_PUYO = register("blue_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.BLUE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
	public static final Block PURPLE_PUYO = register("purple_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.PURPLE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
	public static final Block GRAY_PUYO = register("gray_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GRAY).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
	public static final Block GARBAGE_PUYO = register("garbage_puyo", new DirectionalBlock(Settings.copy(Blocks.STONE)));
	public static final Block POINT_PUYO = register("point_puyo", new DirectionalBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(10)));
	public static final Block HARD_PUYO = register("hard_puyo", new DirectionalBlock(Settings.copy(Blocks.STONE)));
	public static final Block IRON_PUYO = register("iron_puyo", new DirectionalBlock(Settings.copy(Blocks.IRON_BLOCK)));

	/* YOUTUBE */
	public static final Block KORETATO_BLOCK = register("koretato_block", new KoretatoBlock());
	public static final Block POTATO_FLOWER = registerPotable("potato_flower", new FlowerBlock(StatusEffects.HUNGER, 9, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 60, 100);

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}

	private static Block register(String name, Block block, ItemGroup group) {
		Registry.register(Registry.ITEM, new Identifier(Mubble.MOD_ID, name), new BlockItem(block, new Item.Settings().group(group)));
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}

	private static Block register(String name, Block block, int fireEncouragement, int flammability) {
		FlammableBlockRegistry.getDefaultInstance().add(block, fireEncouragement, flammability);
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}

	private static Block register(String name, Block block, ItemGroup group, int fireEncouragement, int flammability) {
		Registry.register(Registry.ITEM, new Identifier(Mubble.MOD_ID, name), new BlockItem(block, new Item.Settings().group(group)));
		FlammableBlockRegistry.getDefaultInstance().add(block, fireEncouragement, flammability);
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}

	private static Block registerPotable(String name, Block block, ItemGroup group) {
		Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, "potted_" + name), new FlowerPotBlock(block, FabricBlockSettings.of(Material.SUPPORTED).breakInstantly().nonOpaque().lightLevel(block.getDefaultState().getLuminance())));
		POTTED_PLANTS.add(Registry.BLOCK.get(new Identifier(Mubble.MOD_ID, "potted_" + name)));
		Registry.register(Registry.ITEM, new Identifier(Mubble.MOD_ID, name), new BlockItem(block, new Item.Settings().group(group)));
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}

	private static Block registerPotable(String name, Block block, ItemGroup group, int fireEncouragement, int flammability) {
		Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, "potted_" + name), new FlowerPotBlock(block, FabricBlockSettings.of(Material.SUPPORTED).breakInstantly().nonOpaque().lightLevel(block.getDefaultState().getLuminance())));
		POTTED_PLANTS.add(Registry.BLOCK.get(new Identifier(Mubble.MOD_ID, "potted_" + name)));
		Registry.register(Registry.ITEM, new Identifier(Mubble.MOD_ID, name), new BlockItem(block, new Item.Settings().group(group)));
		FlammableBlockRegistry.getDefaultInstance().add(block, fireEncouragement, flammability);
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}
}
