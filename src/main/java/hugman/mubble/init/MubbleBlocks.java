package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.init.data.MubbleSoundTypes;
import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.object.block.CakeBlock;
import hugman.mubble.object.block.DoorBlock;
import hugman.mubble.object.block.FungusBlock;
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
import hugman.mubble.util.creator.BlockTemplate;
import hugman.mubble.util.creator.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.block.PressurePlateBlock.ActivationRule;
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
	public static final Block OAK_VERTICAL_SLAB = register("oak_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.OAK_SLAB)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block SPRUCE_VERTICAL_SLAB = register("spruce_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.SPRUCE_SLAB)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block BIRCH_VERTICAL_SLAB = register("birch_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.BIRCH_SLAB)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block JUNGLE_VERTICAL_SLAB = register("jungle_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.JUNGLE_SLAB)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block ACACIA_VERTICAL_SLAB = register("acacia_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.ACACIA_SLAB)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block DARK_OAK_VERTICAL_SLAB = register("dark_oak_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.DARK_OAK_SLAB)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block CRIMSON_VERTICAL_SLAB = register("crimson_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.CRIMSON_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block WARPED_VERTICAL_SLAB = register("warped_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.WARPED_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block STONE_VERTICAL_SLAB = register("stone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.STONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMOOTH_STONE_VERTICAL_SLAB = register("smooth_stone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.SMOOTH_STONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block COBBLESTONE_VERTICAL_SLAB = register("cobblestone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.COBBLESTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MOSSY_COBBLESTONE_VERTICAL_SLAB = register("mossy_cobblestone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.MOSSY_COBBLESTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block STONE_BRICK_VERTICAL_SLAB = register("stone_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.STONE_BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block MOSSY_STONE_BRICK_VERTICAL_SLAB = register("mossy_stone_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.MOSSY_STONE_BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block GRANITE_VERTICAL_SLAB = register("granite_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.GRANITE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block POLISHED_GRANITE_VERTICAL_SLAB = register("polished_granite_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.POLISHED_GRANITE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DIORITE_VERTICAL_SLAB = register("diorite_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.DIORITE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block POLISHED_DIORITE_VERTICAL_SLAB = register("polished_diorite_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.POLISHED_DIORITE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block ANDESITE_VERTICAL_SLAB = register("andesite_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.ANDESITE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block POLISHED_ANDESITE_VERTICAL_SLAB = register("polished_andesite_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.POLISHED_ANDESITE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BRICK_VERTICAL_SLAB = register("brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SANDSTONE_VERTICAL_SLAB = register("sandstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.SANDSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block CUT_SANDSTONE_VERTICAL_SLAB = register("cut_sandstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.CUT_SANDSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMOOTH_SANDSTONE_VERTICAL_SLAB = register("smooth_sandstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.SMOOTH_SANDSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block RED_SANDSTONE_VERTICAL_SLAB = register("red_sandstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.RED_SANDSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block CUT_RED_SANDSTONE_VERTICAL_SLAB = register("cut_red_sandstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.CUT_RED_SANDSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMOOTH_RED_SANDSTONE_VERTICAL_SLAB = register("smooth_red_sandstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.SMOOTH_RED_SANDSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NETHER_BRICK_VERTICAL_SLAB = register("nether_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.NETHER_BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block RED_NETHER_BRICK_VERTICAL_SLAB = register("red_nether_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.RED_NETHER_BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block QUARTZ_VERTICAL_SLAB = register("quartz_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.QUARTZ_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMOOTH_QUARTZ_VERTICAL_SLAB = register("smooth_quartz_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.SMOOTH_QUARTZ_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block END_STONE_BRICK_VERTICAL_SLAB = register("end_stone_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.END_STONE_BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PURPUR_VERTICAL_SLAB = register("purpur_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.PURPUR_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PRISMARINE_VERTICAL_SLAB = register("prismarine_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.PRISMARINE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PRISMARINE_BRICK_VERTICAL_SLAB = register("prismarine_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.PRISMARINE_BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_PRISMARINE_VERTICAL_SLAB = register("dark_prismarine_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.DARK_PRISMARINE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BLACKSTONE_VERTICAL_SLAB = register("blackstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.BLACKSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB = register("polished_blackstone_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB)), ItemGroup.BUILDING_BLOCKS);
	public static final Block POLISHED_BLACKSTONE_SLAB = register("polished_blackstone_vertical_slab", new VerticalSlabBlock(Settings.copy(Blocks.POLISHED_BLACKSTONE_SLAB)), ItemGroup.BUILDING_BLOCKS);

	public static final Block DARK_PRISMARINE_WALL = register("dark_prismarine_wall", new WallBlock(Settings.copy(Blocks.DARK_PRISMARINE)), ItemGroup.DECORATIONS);

	public static final MSBlockCreator OAK_WOOD = new MSBlockCreator("oak_wood", Blocks.OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);
	public static final MSBlockCreator SPRUCE_WOOD = new MSBlockCreator("spruce_wood", Blocks.SPRUCE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);
	public static final MSBlockCreator BIRCH_WOOD = new MSBlockCreator("birch_wood", Blocks.BIRCH_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);
	public static final MSBlockCreator JUNGLE_WOOD = new MSBlockCreator("jungle_wood", Blocks.JUNGLE_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);
	public static final MSBlockCreator ACACIA_WOOD = new MSBlockCreator("acacia_wood", Blocks.ACACIA_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);
	public static final MSBlockCreator DARK_OAK_WOOD = new MSBlockCreator("dark_oak_wood", Blocks.DARK_OAK_WOOD, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);
	public static final MSBlockCreator CRIMSON_HYPHAE = new MSBlockCreator("crimson_hyphae", Blocks.CRIMSON_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);
	public static final MSBlockCreator WARPED_HYPHAE = new MSBlockCreator("warped_hyphae", Blocks.WARPED_HYPHAE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.STONE_BUTTON);

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

	public static final MSBlockCreator BLUNITE = new MSBlockCreator("blunite", MaterialColor.LIGHT_BLUE_TERRACOTTA, Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator CARBONITE = new MSBlockCreator("carbonite", MaterialColor.BLACK, Blocks.ANDESITE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator POLISHED_BLUNITE = new MSBlockCreator("polished_blunite", MubbleBlocks.BLUNITE.getBlock(BlockTemplate.CUBE), BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);
	public static final MSBlockCreator POLISHED_CARBONITE = new MSBlockCreator("polished_carbonite", MubbleBlocks.CARBONITE.getBlock(BlockTemplate.CUBE), BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

	public static final Block VANADIUM_ORE = register("vanadium_ore", new OreBlock(Settings.copy(Blocks.DIAMOND_ORE)), ItemGroup.BUILDING_BLOCKS);
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

	public static final SimpleBlockCreator PALM_PLANKS = new SimpleBlockCreator("palm_planks", Blocks.OAK_PLANKS, MaterialColor.ORANGE);
	public static final SaplingCreator PALM_SAPLING = new SaplingCreator("palm", new PalmSaplingGenerator());
	public static final LogCreator PALM_LOGS = new LogCreator("palm", MaterialColor.ORANGE, MaterialColor.CYAN_TERRACOTTA, false);
	public static final LeavesCreator PALM_LEAVES = new LeavesCreator("palm");
	public static final Block PALM_PRESSURE_PLATE = register("palm_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, FabricBlockSettings.copy(MubbleBlocks.PALM_PLANKS.getBlock()).noCollision().strength(0.5F, 0.0F)), ItemGroup.REDSTONE);
	public static final Block PALM_TRAPDOOR = register("palm_trapdoor", new TrapdoorBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.REDSTONE);
	public static final Block PALM_BUTTON = register("palm_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
	public static final Block PALM_STAIRS = register("palm_stairs", new StairsBlock(PALM_PLANKS.getBlock()), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PALM_SLAB = register("palm_slab", new SlabBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PALM_VERTICAL_SLAB = register("palm_vertical_slab", new VerticalSlabBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PALM_FENCE_GATE = register("palm_fence_gate", new FenceGateBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.REDSTONE, 5, 20);
	public static final Block PALM_FENCE = register("palm_fence", new FenceBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.DECORATIONS, 5, 20);
	public static final Block PALM_DOOR = register("palm_door", new DoorBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.REDSTONE);
	public static final Block PALM_WOOD_STAIRS = register("palm_wood_stairs", new StairsBlock(PALM_PLANKS.getBlock()), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PALM_WOOD_SLAB = register("palm_wood_slab", new SlabBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PALM_WOOD_VERTICAL_SLAB = register("palm_wood_vertical_slab", new VerticalSlabBlock(Settings.copy(PALM_PLANKS.getBlock())), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PALM_WOOD_BUTTON = register("palm_wood_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);

	public static final MSCBlockCreator STAINED_BRICKS = new MSCBlockCreator("bricks", Blocks.BRICKS, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSBlockCreator TERRACOTTA = new MSBlockCreator("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator STAINED_TERRACOTTA = new MSCBlockCreator("terracotta", Blocks.TERRACOTTA, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator DARK_PRISMARINE = new MSCBlockCreator("dark_prismarine", Blocks.DARK_PRISMARINE, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL);
	public static final MSCBlockCreator CONCRETE = new MSCBlockCreator("concrete", Blocks.BLUE_CONCRETE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB, BlockTemplate.WALL, BlockTemplate.PRESSURE_PLATE, BlockTemplate.STONE_BUTTON);
	public static final MSCBlockCreator QUARTZ_PAVING = new MSCBlockCreator("quartz_paving", Blocks.QUARTZ_BLOCK, BlockTemplate.CUBE, BlockTemplate.STAIRS, BlockTemplate.SLAB, BlockTemplate.VERTICAL_SLAB);

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

	public static final Block RED_SHINY_GARLAND = register("red_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.RED).hardness(0.2F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 30, 60);
	public static final Block SILVER_SHINY_GARLAND = register("silver_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.CLAY).hardness(0.2F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 30, 60);
	public static final Block GOLD_SHINY_GARLAND = register("gold_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.GOLD).hardness(0.2F).sounds(BlockSoundGroup.GRASS)), ItemGroup.DECORATIONS, 30, 60);

	public static final Block WHITE_PRESENT = register("white_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block BLACK_PRESENT = register("black_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLACK_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block BLUE_PRESENT = register("blue_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLUE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block GREEN_PRESENT = register("green_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GREEN_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block YELLOW_PRESENT = register("yellow_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.YELLOW_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block RED_PRESENT = register("red_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.RED_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block PURPLE_PRESENT = register("purple_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.PURPLE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);
	public static final Block GOLDEN_PRESENT = register("golden_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GOLD).hardness(0.8F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS, 60, 60);

	public static final Block FOOTBLOCK = register("footblock", new Block(Settings.copy(Blocks.WHITE_WOOL)), ItemGroup.DECORATIONS, 60, 60);

	public static final Block WHITE_CLOUD_BLOCK = register("white_cloud_block", new CloudBlock(DyeColor.WHITE), ItemGroup.DECORATIONS);
	public static final Block LIGHT_GRAY_CLOUD_BLOCK = register("light_gray_cloud_block", new CloudBlock(DyeColor.LIGHT_GRAY), ItemGroup.DECORATIONS);
	public static final Block GRAY_CLOUD_BLOCK = register("gray_cloud_block", new CloudBlock(DyeColor.GRAY), ItemGroup.DECORATIONS);
	public static final Block BLACK_CLOUD_BLOCK = register("black_cloud_block", new CloudBlock(DyeColor.BLACK), ItemGroup.DECORATIONS);
	public static final Block BROWN_CLOUD_BLOCK = register("brown_cloud_block", new CloudBlock(DyeColor.BROWN), ItemGroup.DECORATIONS);
	public static final Block RED_CLOUD_BLOCK = register("red_cloud_block", new CloudBlock(DyeColor.RED), ItemGroup.DECORATIONS);
	public static final Block ORANGE_CLOUD_BLOCK = register("orange_cloud_block", new CloudBlock(DyeColor.ORANGE), ItemGroup.DECORATIONS);
	public static final Block YELLOW_CLOUD_BLOCK = register("yellow_cloud_block", new CloudBlock(DyeColor.YELLOW), ItemGroup.DECORATIONS);
	public static final Block LIME_CLOUD_BLOCK = register("lime_cloud_block", new CloudBlock(DyeColor.LIME), ItemGroup.DECORATIONS);
	public static final Block GREEN_CLOUD_BLOCK = register("green_cloud_block", new CloudBlock(DyeColor.GREEN), ItemGroup.DECORATIONS);
	public static final Block CYAN_CLOUD_BLOCK = register("cyan_cloud_block", new CloudBlock(DyeColor.CYAN), ItemGroup.DECORATIONS);
	public static final Block LIGHT_BLUE_CLOUD_BLOCK = register("light_blue_cloud_block", new CloudBlock(DyeColor.LIGHT_BLUE), ItemGroup.DECORATIONS);
	public static final Block BLUE_CLOUD_BLOCK = register("blue_cloud_block", new CloudBlock(DyeColor.BLUE), ItemGroup.DECORATIONS);
	public static final Block PURPLE_CLOUD_BLOCK = register("purple_cloud_block", new CloudBlock(DyeColor.PURPLE), ItemGroup.DECORATIONS);
	public static final Block MAGENTA_CLOUD_BLOCK = register("magenta_cloud_block", new CloudBlock(DyeColor.MAGENTA), ItemGroup.DECORATIONS);
	public static final Block PINK_CLOUD_BLOCK = register("pink_cloud_block", new CloudBlock(DyeColor.PINK), ItemGroup.DECORATIONS);

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

	public static final Block UNSTABLE_STONE = register("unstable_stone", new UnstableBlock(FabricBlockSettings.copy(Blocks.STONE).strength(0.1F, 0.0F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block FLUID_TANK = register("fluid_tank", new FluidTankBlock(FabricBlockSettings.copy(Blocks.OBSIDIAN).nonOpaque()), ItemGroup.REDSTONE);
	public static final Block PLACER = register("placer", new PlacerBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F)), ItemGroup.REDSTONE);
	public static final Block TIMESWAP_TABLE = register("timeswap_table", new TimeswapTableBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F)), ItemGroup.DECORATIONS);

	public static final Block DANDELION_PILE = register("dandelion_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block POPPY_PILE = register("poppy_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block BLUE_ORCHID_PILE = register("blue_orchid_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block ALLIUM_PILE = register("allium_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block AZURE_BLUET_PILE = register("azure_bluet_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block RED_TULIP_PILE = register("red_tulip_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block ORANGE_TULIP_PILE = register("orange_tulip_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block WHITE_TULIP_PILE = register("white_tulip_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block PINK_TULIP_PILE = register("pink_tulip_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block OXEYE_DAISY_PILE = register("oxeye_daisy_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block CORNFLOWER_PILE = register("cornflower_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block LILY_OF_THE_VALLEY_PILE = register("lily_of_the_valley_pile", new PileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block WITHER_ROSE_PILE = register("wither_rose_pile", new WitherRosePileBlock(FabricBlockSettings.of(Material.PLANT).hardness(0.0F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);

	public static final Block PERMAROCK = register("permarock", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.4F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PERMAFROST_BRICKS = register("permafrost_bricks", new Block(FabricBlockSettings.copy(Blocks.NETHER_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PERMAFROST_BRICK_STAIRS = register("permafrost_brick_stairs", new StairsBlock(PERMAFROST_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final Block PERMAFROST_BRICK_SLAB = register("permafrost_brick_slab", new SlabBlock(FabricBlockSettings.copy(PERMAFROST_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PERMAFROST_BRICK_VERTICAL_SLAB = register("permafrost_brick_vertical_slab", new VerticalSlabBlock(FabricBlockSettings.copy(PERMAFROST_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block PERMAFROST_BRICK_FENCE = register("permafrost_brick_fence", new FenceBlock(FabricBlockSettings.copy(PERMAFROST_BRICKS)), ItemGroup.DECORATIONS);
	public static final Block BLUE_PERMAFROST_BRICKS = register("blue_permafrost_bricks", new Block(FabricBlockSettings.copy(PERMAFROST_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BLUE_PERMAFROST_BRICK_STAIRS = register("blue_permafrost_brick_stairs", new StairsBlock(BLUE_PERMAFROST_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final Block BLUE_PERMAFROST_BRICK_SLAB = register("blue_permafrost_brick_slab", new SlabBlock(FabricBlockSettings.copy(BLUE_PERMAFROST_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BLUE_PERMAFROST_BRICK_VERTICAL_SLAB = register("blue_permafrost_brick_vertical_slab", new VerticalSlabBlock(Settings.copy(BLUE_PERMAFROST_BRICKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block BLUE_PERMAFROST_BRICK_WALL = register("blue_permafrost_brick_wall", new WallBlock(FabricBlockSettings.copy(BLUE_PERMAFROST_BRICKS)), ItemGroup.DECORATIONS);
	public static final Block PERMAFROST_BISMUTH_ORE = register("permafrost_bismuth_ore", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.3F)), ItemGroup.BUILDING_BLOCKS);
	public static final Block FROZEN_OBSIDIAN = register("frozen_obsidian", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).strength(75.0F, 1800.0F)), ItemGroup.BUILDING_BLOCKS);

	public static final Block AMARANTH_DYLIUM = register("amaranth_dylium", new DyliumBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.field_25702).requiresTool().strength(3.0F, 9.0F).sounds(BlockSoundGroup.NYLIUM).ticksRandomly()), ItemGroup.BUILDING_BLOCKS);
	public static final Block AMARANTH_ROOTS = register("amaranth_roots", new RootsBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.CYAN).noCollision().breakInstantly().sounds(BlockSoundGroup.ROOTS)), ItemGroup.DECORATIONS);
	public static final Block AMARANTH_WART_BLOCK = register("amaranth_wart_block", new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC, MaterialColor.field_25708).breakByTool(FabricToolTags.HOES).strength(1.0F).sounds(BlockSoundGroup.WART_BLOCK)), ItemGroup.BUILDING_BLOCKS);

	public static final Block DARK_AMARANTH_PLANKS = register("dark_amaranth_planks", new Block(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.field_25706).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_FUNGUS = registerPotable("dark_amaranth_fungus", new FungusBlock(FabricBlockSettings.of(Material.PLANT, MaterialColor.CYAN).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS), () -> {
		return Feature.HUGE_FUNGUS.configure(MubbleFeatureConfigs.AMARANTH_FUNGUS_CONFIG);
	}), ItemGroup.DECORATIONS);
	public static final Block DARK_AMARANTH_STEM = register("dark_amaranth_stem", new PillarBlock(FabricBlockSettings.copy(Blocks.CRIMSON_HYPHAE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block STRIPPED_DARK_AMARANTH_STEM = register("stripped_dark_amaranth_stem", new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_CRIMSON_HYPHAE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_HYPHAE = register("dark_amaranth_hyphae", new PillarBlock(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.field_25707).strength(2.0F).sounds(BlockSoundGroup.NETHER_STEM)), ItemGroup.BUILDING_BLOCKS);
	public static final Block STRIPPED_DARK_AMARANTH_HYPHAE = register("stripped_dark_amaranth_hyphae", new PillarBlock(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.field_25707).strength(2.0F).sounds(BlockSoundGroup.NETHER_STEM)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_PRESSURE_PLATE = register("dark_amaranth_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.CYAN).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
	public static final Block DARK_AMARANTH_TRAPDOOR = register("dark_amaranth_trapdoor", new TrapdoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.CYAN).strength(3.0F).sounds(BlockSoundGroup.WOOD).nonOpaque()), ItemGroup.REDSTONE);
	public static final Block DARK_AMARANTH_BUTTON = register("dark_amaranth_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
	public static final Block DARK_AMARANTH_STAIRS = register("dark_amaranth_stairs", new StairsBlock(DARK_AMARANTH_PLANKS), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_SLAB = register("dark_amaranth_slab", new SlabBlock(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.CYAN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_VERTICAL_SLAB = register("dark_amaranth_vertical_slab", new VerticalSlabBlock(Settings.copy(DARK_AMARANTH_PLANKS)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_FENCE_GATE = register("dark_amaranth_fence_gate", new FenceGateBlock(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.CYAN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
	public static final Block DARK_AMARANTH_FENCE = register("dark_amaranth_fence", new FenceBlock(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.CYAN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
	public static final Block DARK_AMARANTH_DOOR = register("dark_amaranth_door", new DoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, DARK_AMARANTH_PLANKS.getDefaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroup.WOOD).nonOpaque()), ItemGroup.REDSTONE);
	public static final Block DARK_AMARANTH_HYPHAE_STAIRS = register("dark_amaranth_hyphae_stairs", new StairsBlock(DARK_AMARANTH_HYPHAE), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_HYPHAE_SLAB = register("dark_amaranth_hyphae_slab", new SlabBlock(FabricBlockSettings.copy(DARK_AMARANTH_HYPHAE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_HYPHAE_VERTICAL_SLAB = register("dark_amaranth_hyphae_vertical_slab", new VerticalSlabBlock(FabricBlockSettings.copy(DARK_AMARANTH_HYPHAE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block DARK_AMARANTH_HYPHAE_BUTTON = register("dark_amaranth_hyphae_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.NETHER_STEM)), ItemGroup.REDSTONE);

	/* SUPER MARIO (MAKER) */
	public static final Block SMB_QUESTION_BLOCK = register("smb_question_block", new QuestionBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_QUESTION_BLOCK = register("smb3_question_block", new QuestionBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_QUESTION_BLOCK = register("smw_question_block", new QuestionBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_QUESTION_BLOCK = register("nsmbu_question_block", new QuestionBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_GROUND_GROUND_BLOCK = register("smb_ground_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_UNDERGROUND_GROUND_BLOCK = register("smb_underground_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_UNDERWATER_GROUND_BLOCK = register("smb_underwater_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_GHOST_HOUSE_GROUND_BLOCK = register("smb_ghost_house_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_AIRSHIP_GROUND_BLOCK = register("smb_airship_ground_block", new Block(Settings.copy(Blocks.IRON_BLOCK)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_NIGHT_AIRSHIP_GROUND_BLOCK = register("smb_night_airship_ground_block", new Block(Settings.copy(Blocks.IRON_BLOCK)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_CASTLE_GROUND_BLOCK = register("smb_castle_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_DESERT_GROUND_BLOCK = register("smb_desert_ground_block", new Block(Settings.copy(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_FOREST_GROUND_BLOCK = register("smb_forest_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_SNOW_GROUND_BLOCK = register("smb_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_NIGHT_SNOW_GROUND_BLOCK = register("smb_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_SKY_GROUND_BLOCK = register("smb_sky_ground_block", new Block(Settings.copy(Blocks.WHITE_WOOL)), ItemGroup.BUILDING_BLOCKS, 30, 60);
	public static final Block SMB3_GROUND_GROUND_BLOCK = register("smb3_ground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_UNDERGROUND_GROUND_BLOCK = register("smb3_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_UNDERWATER_GROUND_BLOCK = register("smb3_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_GHOST_HOUSE_GROUND_BLOCK = register("smb3_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_AIRSHIP_GROUND_BLOCK = register("smb3_airship_ground_block", new Block(Settings.copy(Blocks.OAK_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block SMB3_NIGHT_AIRSHIP_GROUND_BLOCK = register("smb3_night_airship_ground_block", new Block(Settings.copy(Blocks.OAK_WOOD)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_CASTLE_GROUND_BLOCK = register("smb3_castle_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_NIGHT_CASTLE_GROUND_BLOCK = register("smb3_night_castle_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_DESERT_GROUND_BLOCK = register("smb3_desert_ground_block", new OverBlock(Settings.copy(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_SNOW_GROUND_BLOCK = register("smb3_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_NIGHT_SNOW_GROUND_BLOCK = register("smb3_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_SKY_GROUND_BLOCK = register("smb3_sky_ground_block", new OverBlock(Settings.copy(Blocks.WHITE_WOOL)), ItemGroup.BUILDING_BLOCKS, 30, 60);
	public static final Block SMW_GROUND_GROUND_BLOCK = register("smw_ground_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_UNDERGROUND_GROUND_BLOCK = register("smw_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_UNDERWATER_GROUND_BLOCK = register("smw_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_GHOST_HOUSE_GROUND_BLOCK = register("smw_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_AIRSHIP_GROUND_BLOCK = register("smw_airship_ground_block", new OverBlock(Settings.copy(Blocks.OAK_WOOD)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_CASTLE_GROUND_BLOCK = register("smw_castle_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_DESERT_GROUND_BLOCK = register("smw_desert_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_FOREST_GROUND_BLOCK = register("smw_forest_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_SNOW_GROUND_BLOCK = register("smw_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_NIGHT_SNOW_GROUND_BLOCK = register("smw_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_SKY_GROUND_BLOCK = register("smw_sky_ground_block", new OverBlock(Settings.copy(Blocks.WHITE_WOOL)), ItemGroup.BUILDING_BLOCKS, 30, 60);
	public static final Block NSMBU_GROUND_GROUND_BLOCK = register("nsmbu_ground_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_UNDERGROUND_GROUND_BLOCK = register("nsmbu_underground_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_UNDERWATER_GROUND_BLOCK = register("nsmbu_underwater_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_GHOST_HOUSE_GROUND_BLOCK = register("nsmbu_ghost_house_ground_block", new OverBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_CASTLE_GROUND_BLOCK = register("nsmbu_castle_ground_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_DESERT_GROUND_BLOCK = register("nsmbu_desert_ground_block", new OverBlock(Settings.copy(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_FOREST_GROUND_BLOCK = register("nsmbu_forest_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_SNOW_GROUND_BLOCK = register("nsmbu_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_NIGHT_SNOW_GROUND_BLOCK = register("nsmbu_night_snow_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_SKY_GROUND_BLOCK = register("nsmbu_sky_ground_block", new OverBlock(Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_EMPTY_BLOCK = register("smb_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_EMPTY_BLOCK = register("smb3_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_EMPTY_BLOCK = register("smw_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_EMPTY_BLOCK = register("nsmbu_empty_block", new EmptyBlock(), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_ROTATING_BLOCK = register("smb_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_ROTATING_BLOCK = register("smb3_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_ROTATING_BLOCK = register("smw_rotating_block", new RotatingBlock(MubbleSoundTypes.SMW_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_ROTATING_BLOCK = register("nsmbu_rotating_block", new RotatingBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block LIGHT_BLOCK = register("light_block", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(15)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_GROUND_BRICK_BLOCK = register("smb_ground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_UNDERGROUND_BRICK_BLOCK = register("smb_underground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_CASTLE_BRICK_BLOCK = register("smb_castle_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_SNOW_BRICK_BLOCK = register("smb_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_NIGHT_SNOW_BRICK_BLOCK = register("smb_night_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_BRICK_BLOCK = register("smb3_brick_block", new BrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_BRICK_BLOCK = register("smw_brick_block", new BrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_BRICK_BLOCK = register("nsmbu_brick_block", new BrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_GOLDEN_BRICK_BLOCK = register("smb_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_GOLDEN_BRICK_BLOCK = register("smb3_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_GOLDEN_BRICK_BLOCK = register("smw_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_GOLDEN_BRICK_BLOCK = register("nsmbu_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_GROUND_HARD_BLOCK = register("smb_ground_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_UNDERGROUND_HARD_BLOCK = register("smb_underground_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_UNDERWATER_HARD_BLOCK = register("smb_underwater_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_CASTLE_HARD_BLOCK = register("smb_castle_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_SNOW_HARD_BLOCK = register("smb_snow_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_NIGHT_SNOW_HARD_BLOCK = register("smb_night_snow_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_HARD_BLOCK = register("smb3_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_STONE_HARD_BLOCK = register("smw_stone_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_WOOD_HARD_BLOCK = register("smw_wood_hard_block", new Block(Settings.copy(Blocks.OAK_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block NSMBU_HARD_BLOCK = register("nsmbu_hard_block", new Block(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_ICE_BLOCK = register("smb_ice_block", new Block(Settings.copy(Blocks.PACKED_ICE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_ICE_BLOCK = register("smb3_ice_block", new Block(Settings.copy(Blocks.PACKED_ICE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_ICE_BLOCK = register("smw_ice_block", new Block(Settings.copy(Blocks.PACKED_ICE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_ICE_BLOCK = register("nsmbu_ice_block", new Block(Settings.copy(Blocks.PACKED_ICE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_NOTE_BLOCK = register("smb_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_NOTE_BLOCK = register("smb3_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_NOTE_BLOCK = register("smw_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_NOTE_BLOCK = register("nsmbu_note_block", new NoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_SUPER_NOTE_BLOCK = register("smb_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB3_SUPER_NOTE_BLOCK = register("smb3_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMW_SUPER_NOTE_BLOCK = register("smw_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block NSMBU_SUPER_NOTE_BLOCK = register("nsmbu_super_note_block", new SuperNoteBlock(Settings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);
	public static final Block SMB_DOOR = register("smb_door", new DoorBlock(Settings.copy(Blocks.OAK_DOOR)), ItemGroup.REDSTONE);
	public static final Block SMB3_DOOR = register("smb3_door", new DoorBlock(Settings.copy(Blocks.OAK_DOOR)), ItemGroup.REDSTONE);
	public static final Block SMW_DOOR = register("smw_door", new DoorBlock(Settings.copy(Blocks.OAK_DOOR)), ItemGroup.REDSTONE);
	public static final Block NSMBU_DOOR = register("nsmbu_door", new DoorBlock(Settings.copy(Blocks.OAK_DOOR)), ItemGroup.REDSTONE);
	public static final Block SMB_KEY_DOOR = register("smb_key_door", new KeyDoorBlock(Settings.copy(Blocks.IRON_DOOR)), ItemGroup.REDSTONE);
	public static final Block SMB3_KEY_DOOR = register("smb3_key_door", new KeyDoorBlock(Settings.copy(Blocks.IRON_DOOR)), ItemGroup.REDSTONE);
	public static final Block SMW_KEY_DOOR = register("smw_key_door", new KeyDoorBlock(Settings.copy(Blocks.IRON_DOOR)), ItemGroup.REDSTONE);
	public static final Block NSMBU_KEY_DOOR = register("nsmbu_key_door", new KeyDoorBlock(Settings.copy(Blocks.IRON_DOOR)), ItemGroup.REDSTONE);

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

	public static final Block PRESS_GARDEN_PLANKS = register("press_garden_planks", new Block(Settings.copy(Blocks.OAK_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final SaplingCreator RED_PRESS_GARDEN_SAPLING = new SaplingCreator("red_press_garden", new RedPressGardenSaplingGenerator());
	public static final SaplingCreator PINK_PRESS_GARDEN_SAPLING = new SaplingCreator("pink_press_garden", new PinkPressGardenSaplingGenerator());
	public static final Block PRESS_GARDEN_LOG = register("press_garden_log", new PillarBlock(Settings.copy(Blocks.OAK_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block STRIPPED_PRESS_GARDEN_LOG = register("stripped_press_garden_log", new PillarBlock(Settings.copy(PRESS_GARDEN_LOG)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block PRESS_GARDEN_WOOD = register("press_garden_wood", new PillarBlock(Settings.copy(PRESS_GARDEN_LOG)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block STRIPPED_PRESS_GARDEN_WOOD = register("stripped_press_garden_wood", new PillarBlock(Settings.copy(PRESS_GARDEN_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block RED_PRESS_GARDEN_LEAVES = register("red_press_garden_leaves", new LeavesBlock(pLeaves), ItemGroup.DECORATIONS, 30, 60);
	public static final Block RED_PRESS_GARDEN_LEAF_PILE = register("red_press_garden_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block PINK_PRESS_GARDEN_LEAVES = register("pink_press_garden_leaves", new LeavesBlock(pLeaves), ItemGroup.DECORATIONS, 30, 60);
	public static final Block PINK_PRESS_GARDEN_LEAF_PILE = register("pink_press_garden_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block PRESS_GARDEN_PRESSURE_PLATE = register("press_garden_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, FabricBlockSettings.copy(MubbleBlocks.PRESS_GARDEN_PLANKS).noCollision().strength(0.5F, 0.0F)), ItemGroup.REDSTONE);
	public static final Block PRESS_GARDEN_TRAPDOOR = register("press_garden_trapdoor", new TrapdoorBlock(Settings.copy(MubbleBlocks.PRESS_GARDEN_PLANKS)), ItemGroup.REDSTONE);
	public static final Block PRESS_GARDEN_BUTTON = register("press_garden_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
	public static final Block PRESS_GARDEN_STAIRS = register("press_garden_stairs", new StairsBlock(PRESS_GARDEN_PLANKS), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PRESS_GARDEN_SLAB = register("press_garden_slab", new SlabBlock(Settings.copy(PRESS_GARDEN_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PRESS_GARDEN_VERTICAL_SLAB = register("press_garden_vertical_slab", new VerticalSlabBlock(Settings.copy(PRESS_GARDEN_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PRESS_GARDEN_FENCE_GATE = register("press_garden_fence_gate", new FenceGateBlock(Settings.copy(PRESS_GARDEN_PLANKS)), ItemGroup.REDSTONE, 5, 20);
	public static final Block PRESS_GARDEN_FENCE = register("press_garden_fence", new FenceBlock(Settings.copy(PRESS_GARDEN_PLANKS)), ItemGroup.DECORATIONS, 5, 20);
	public static final Block PRESS_GARDEN_DOOR = register("press_garden_door", new DoorBlock(Settings.copy(PRESS_GARDEN_PLANKS)), ItemGroup.REDSTONE);
	public static final Block PRESS_GARDEN_WOOD_STAIRS = register("press_garden_wood_stairs", new StairsBlock(PRESS_GARDEN_WOOD), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PRESS_GARDEN_WOOD_SLAB = register("press_garden_wood_slab", new SlabBlock(Settings.copy(PRESS_GARDEN_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PRESS_GARDEN_WOOD_VERTICAL_SLAB = register("press_garden_wood_vertical_slab", new VerticalSlabBlock(Settings.copy(PRESS_GARDEN_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block PRESS_GARDEN_WOOD_BUTTON = register("press_garden_wood_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);

	public static final Block SPRING = register("spring", new SpringBlock(FabricBlockSettings.of(Material.METAL).hardness(4f)), ItemGroup.TRANSPORTATION);

	/* UNDERTALE / DELTARUNE */
	public static final Block SCARLET_PLANKS = register("scarlet_planks", new Block(Settings.copy(Blocks.OAK_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final SaplingCreator SCARLET_SAPLING = new SaplingCreator("scarlet", new ScarletSaplingGenerator());
	public static final Block SCARLET_LOG = register("scarlet_log", new PillarBlock(Settings.copy(Blocks.OAK_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block STRIPPED_SCARLET_LOG = register("stripped_scarlet_log", new PillarBlock(Settings.copy(SCARLET_LOG)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block SCARLET_WOOD = register("scarlet_wood", new PillarBlock(Settings.copy(SCARLET_LOG)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block STRIPPED_SCARLET_WOOD = register("stripped_scarlet_wood", new PillarBlock(Settings.copy(SCARLET_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 5);
	public static final Block SCARLET_LEAVES = register("scarlet_leaves", new LeavesBlock(pLeaves), ItemGroup.DECORATIONS, 30, 60);
	public static final Block SCARLET_LEAF_PILE = register("scarlet_leaf_pile", new PileBlock(FabricBlockSettings.of(Material.LEAVES).hardness(0.1F).sounds(BlockSoundGroup.GRASS).noCollision()), ItemGroup.DECORATIONS, 60, 20);
	public static final Block SCARLET_PRESSURE_PLATE = register("scarlet_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, FabricBlockSettings.copy(SCARLET_PLANKS).noCollision().strength(0.5F, 0.0F)), ItemGroup.REDSTONE);
	public static final Block SCARLET_TRAPDOOR = register("scarlet_trapdoor", new TrapdoorBlock(Settings.copy(SCARLET_PLANKS)), ItemGroup.REDSTONE);
	public static final Block SCARLET_BUTTON = register("scarlet_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
	public static final Block SCARLET_STAIRS = register("scarlet_stairs", new StairsBlock(SCARLET_PLANKS), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block SCARLET_SLAB = register("scarlet_slab", new SlabBlock(Settings.copy(SCARLET_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block SCARLET_VERTICAL_SLAB = register("scarlet_vertical_slab", new VerticalSlabBlock(Settings.copy(SCARLET_PLANKS)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block SCARLET_FENCE_GATE = register("scarlet_fence_gate", new FenceGateBlock(Settings.copy(SCARLET_PLANKS)), ItemGroup.REDSTONE, 5, 20);
	public static final Block SCARLET_FENCE = register("scarlet_fence", new FenceBlock(Settings.copy(SCARLET_PLANKS)), ItemGroup.DECORATIONS, 5, 20);
	public static final Block SCARLET_DOOR = register("scarlet_door", new DoorBlock(Settings.copy(SCARLET_PLANKS)), ItemGroup.REDSTONE);
	public static final Block SCARLET_WOOD_STAIRS = register("scarlet_wood_stairs", new StairsBlock(SCARLET_WOOD), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block SCARLET_WOOD_SLAB = register("scarlet_wood_slab", new SlabBlock(Settings.copy(SCARLET_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block SCARLET_WOOD_VERTICAL_SLAB = register("scarlet_wood_vertical_slab", new VerticalSlabBlock(Settings.copy(SCARLET_WOOD)), ItemGroup.BUILDING_BLOCKS, 5, 20);
	public static final Block SCARLET_WOOD_BUTTON = register("scarlet_wood_button", new WoodButtonBlock(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).hardness(0.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.REDSTONE);
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
