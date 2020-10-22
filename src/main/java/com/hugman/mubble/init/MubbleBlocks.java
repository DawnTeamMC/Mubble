package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.BlockCreator;
import com.hugman.dawn.api.creator.BlockEntityCreator;
import com.hugman.dawn.api.creator.ScreenHandlerCreator;
import com.hugman.dawn.api.creator.pack.block.*;
import com.hugman.dawn.api.object.block.CakeBlock;
import com.hugman.dawn.api.object.block.DirectionalBlock;
import com.hugman.dawn.api.object.block.SaplingBlock;
import com.hugman.dawn.api.util.BlockSettings;
import com.hugman.dawn.api.util.DefaultBlockGetter;
import com.hugman.dawn.mod.init.DawnEffects;
import com.hugman.mubble.init.data.MubbleSoundTypes;
import com.hugman.mubble.object.block.GrassBlock;
import com.hugman.mubble.object.block.NoteBlock;
import com.hugman.mubble.object.block.*;
import com.hugman.mubble.object.block.block_entity.PlacerBlockEntity;
import com.hugman.mubble.object.block.block_entity.PresentBlockEntity;
import com.hugman.mubble.object.block.block_state_property.FluidLog;
import com.hugman.mubble.object.block.block_state_property.Princess;
import com.hugman.mubble.object.block.sapling_generator.PinkPressGardenSaplingGenerator;
import com.hugman.mubble.object.block.sapling_generator.RedPressGardenSaplingGenerator;
import com.hugman.mubble.object.block.sapling_generator.ScarletSaplingGenerator;
import com.hugman.mubble.object.screen.screen_handler.TimeswapTableScreenHandler;
import com.hugman.mubble.util.MubbleBlockGetter;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;

public class MubbleBlocks extends MubblePack {
	public static void init() {

	}

	/* MUBBLE */
	public static final Block BLUE_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("blue_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.BLUE_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block LIGHT_BLUE_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("light_blue_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.LIGHT_BLUE_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block PURPLE_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("purple_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.PURPLE_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block MAGENTA_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("magenta_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.MAGENTA_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block PINK_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("pink_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.PINK_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block RED_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("red_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.RED_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block ORANGE_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("orange_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.ORANGE_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block YELLOW_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("yellow_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.YELLOW_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block GREEN_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("green_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.GREEN_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block WHITE_CHRISTMAS_BAUBLE = register(new BlockCreator.Builder("white_christmas_bauble", new Block(Settings.CHRISTMAS_BAUBLE.materialColor(MaterialColor.WHITE_TERRACOTTA))).itemGroup(ItemGroup.DECORATIONS));

	public static final Block RED_SHINY_GARLAND = register(new BlockCreator.Builder("red_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.RED).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(30, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block SILVER_SHINY_GARLAND = register(new BlockCreator.Builder("silver_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.CLAY).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(30, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block GOLD_SHINY_GARLAND = register(new BlockCreator.Builder("gold_shiny_garland", new GarlandBlock(FabricBlockSettings.of(Material.LEAVES, MaterialColor.GOLD).hardness(0.2F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(30, 60).render(BlockCreator.Render.CUTOUT_MIPPED));

	public static final Block WHITE_PRESENT = register(new BlockCreator.Builder("white_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block BLACK_PRESENT = register(new BlockCreator.Builder("black_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLACK_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block BLUE_PRESENT = register(new BlockCreator.Builder("blue_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.BLUE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block GREEN_PRESENT = register(new BlockCreator.Builder("green_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GREEN_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block YELLOW_PRESENT = register(new BlockCreator.Builder("yellow_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.YELLOW_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block RED_PRESENT = register(new BlockCreator.Builder("red_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.RED_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block PURPLE_PRESENT = register(new BlockCreator.Builder("purple_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.PURPLE_TERRACOTTA).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block GOLDEN_PRESENT = register(new BlockCreator.Builder("golden_present", new PresentBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.GOLD).hardness(0.8F).sounds(BlockSoundGroup.WOOD))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 60).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final BlockEntityType<PresentBlockEntity> PRESENT_ENTITY = register(new BlockEntityCreator.Builder("present", BlockEntityType.Builder.create(PresentBlockEntity::new, MubbleBlocks.WHITE_PRESENT, MubbleBlocks.BLACK_PRESENT, MubbleBlocks.BLUE_PRESENT, MubbleBlocks.GREEN_PRESENT, MubbleBlocks.YELLOW_PRESENT, MubbleBlocks.RED_PRESENT, MubbleBlocks.PURPLE_PRESENT, MubbleBlocks.GOLDEN_PRESENT)));

	public static final Block FOOTBLOCK = register(new BlockCreator.Builder("footblock", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)).itemGroup(ItemGroup.DECORATIONS));

	public static final MCBlockPack CLOUD_BLOCKS = register(new MCBlockPack.Builder("", MubbleBlockGetter.CLOUD_BLOCK, FabricBlockSettings.of(Material.LEAVES).sounds(BlockSoundGroup.WOOL).hardness(0f).noCollision()));
	public static final Block CHEESE_BLOCK = register(new BlockCreator.Builder("cheese_block", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.YELLOW).hardness(0.5f).sounds(BlockSoundGroup.SNOW))).flammability(60, 60).itemGroup(ItemGroup.FOOD));
	public static final Block CHOCOLATE_CAKE = register(new BlockCreator.Builder("chocolate_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).itemGroup(ItemGroup.FOOD));
	public static final Block MINECRAFT_10TH_ANNIVERSARY_CAKE = register(new BlockCreator.Builder("minecraft_10th_anniversary_cake", new CakeBlock(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).itemGroup(ItemGroup.FOOD));

	public static final MCBlockPack BALLOONS = register(new MCBlockPack.Builder("", MubbleBlockGetter.BALLOON, FabricBlockSettings.of(Material.WOOL).hardness(0F).sounds(BlockSoundGroup.WOOL).nonOpaque()));

	public static final Block UNSTABLE_STONE = register(new BlockCreator.Builder("unstable_stone", new UnstableBlock(FabricBlockSettings.copy(Blocks.STONE).strength(0.1F, 0.0F))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block FLUID_TANK = register(new BlockCreator.Builder("fluid_tank", new FluidTankBlock(FabricBlockSettings.copy(Blocks.OBSIDIAN).nonOpaque())).itemGroup(ItemGroup.REDSTONE).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block PLACER = register(new BlockCreator.Builder("placer", new PlacerBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F))).itemGroup(ItemGroup.REDSTONE));
	public static final BlockEntityType<DispenserBlockEntity> PLACER_ENTITY = register(new BlockEntityCreator.Builder("placer", BlockEntityType.Builder.create(PlacerBlockEntity::new, MubbleBlocks.PLACER)));
	public static final Block TIMESWAP_TABLE = register(new BlockCreator.Builder("timeswap_table", new TimeswapTableBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5F))).itemGroup(ItemGroup.DECORATIONS));
	public static final ScreenHandlerType<TimeswapTableScreenHandler> TIMESWAP_TABLE_SCREEN_HANDLER = register(new ScreenHandlerCreator.Builder("timeswap_table", TimeswapTableScreenHandler::new));

	public static final Block PERMAROCK = register(new BlockCreator.Builder("permarock", DefaultBlockGetter.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.4F)));
	public static final MSBlockPack PERMAFROST_BRICKS = register(new MSBlockPack.Builder("permafrost_bricks", FabricBlockSettings.copyOf(Blocks.NETHER_BRICKS), DefaultBlockGetter.CUBE, DefaultBlockGetter.STAIRS, DefaultBlockGetter.SLAB, DefaultBlockGetter.FENCE));
	public static final MSBlockPack BLUE_PERMAFROST_BRICKS = register(new MSBlockPack.Builder("blue_permafrost_bricks", FabricBlockSettings.copyOf(Blocks.RED_NETHER_BRICKS), DefaultBlockGetter.CUBE, DefaultBlockGetter.STAIRS, DefaultBlockGetter.SLAB, DefaultBlockGetter.WALL));
	public static final Block PERMAFROST_BISMUTH_ORE = register(new BlockCreator.Builder("permafrost_bismuth_ore", DefaultBlockGetter.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.ICE).hardness(0.3F)));
	public static final Block FROZEN_OBSIDIAN = register(new BlockCreator.Builder("frozen_obsidian", DefaultBlockGetter.CUBE, FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).strength(75.0F, 1800.0F)));

	/* SUPER MARIO (MAKER) */
	public static final Block SMB_QUESTION_BLOCK = register(new BlockCreator.Builder("smb", MubbleBlockGetter.QUESTION_BLOCK, Settings.QUESTION_BLOCK));
	public static final Block SMB3_QUESTION_BLOCK = register(new BlockCreator.Builder("smb3", MubbleBlockGetter.QUESTION_BLOCK, Settings.QUESTION_BLOCK));
	public static final Block SMW_QUESTION_BLOCK = register(new BlockCreator.Builder("smw", MubbleBlockGetter.QUESTION_BLOCK, Settings.QUESTION_BLOCK));
	public static final Block NSMBU_QUESTION_BLOCK = register(new BlockCreator.Builder("nsmbu", MubbleBlockGetter.QUESTION_BLOCK, Settings.QUESTION_BLOCK));
	public static final Block SMB_GROUND_GROUND_BLOCK = register(new BlockCreator.Builder("smb_ground_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_UNDERGROUND_GROUND_BLOCK = register(new BlockCreator.Builder("smb_underground_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_UNDERWATER_GROUND_BLOCK = register(new BlockCreator.Builder("smb_underwater_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_GHOST_HOUSE_GROUND_BLOCK = register(new BlockCreator.Builder("smb_ghost_house_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_AIRSHIP_GROUND_BLOCK = register(new BlockCreator.Builder("smb_airship_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
	public static final Block SMB_NIGHT_AIRSHIP_GROUND_BLOCK = register(new BlockCreator.Builder("smb_night_airship_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
	public static final Block SMB_CASTLE_GROUND_BLOCK = register(new BlockCreator.Builder("smb_castle_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_DESERT_GROUND_BLOCK = register(new BlockCreator.Builder("smb_desert_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.SAND)));
	public static final Block SMB_FOREST_GROUND_BLOCK = register(new BlockCreator.Builder("smb_forest_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("smb_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_NIGHT_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("smb_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_SKY_GROUND_BLOCK = register(new BlockCreator.Builder("smb_sky_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));
	public static final Block SMB3_GROUND_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_ground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_UNDERGROUND_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_underground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_UNDERWATER_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_underwater_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_GHOST_HOUSE_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_ghost_house_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_AIRSHIP_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_airship_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
	public static final Block SMB3_NIGHT_AIRSHIP_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_night_airship_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
	public static final Block SMB3_CASTLE_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_castle_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB3_NIGHT_CASTLE_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_night_castle_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB3_DESERT_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_desert_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.SAND))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_NIGHT_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_SKY_GROUND_BLOCK = register(new BlockCreator.Builder("smb3_sky_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.WHITE_WOOL))).itemGroup(ItemGroup.BUILDING_BLOCKS).flammability(30, 60));
	public static final Block SMW_GROUND_GROUND_BLOCK = register(new BlockCreator.Builder("smw_ground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_UNDERGROUND_GROUND_BLOCK = register(new BlockCreator.Builder("smw_underground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_UNDERWATER_GROUND_BLOCK = register(new BlockCreator.Builder("smw_underwater_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_GHOST_HOUSE_GROUND_BLOCK = register(new BlockCreator.Builder("smw_ghost_house_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_AIRSHIP_GROUND_BLOCK = register(new BlockCreator.Builder("smw_airship_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.OAK_WOOD))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_CASTLE_GROUND_BLOCK = register(new BlockCreator.Builder("smw_castle_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_DESERT_GROUND_BLOCK = register(new BlockCreator.Builder("smw_desert_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_FOREST_GROUND_BLOCK = register(new BlockCreator.Builder("smw_forest_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("smw_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_NIGHT_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("smw_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_SKY_GROUND_BLOCK = register(new BlockCreator.Builder("smw_sky_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.WHITE_WOOL))).itemGroup(ItemGroup.BUILDING_BLOCKS).flammability(30, 60));
	public static final Block NSMBU_GROUND_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_ground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_UNDERGROUND_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_underground_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_UNDERWATER_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_underwater_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_GHOST_HOUSE_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_ghost_house_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_CASTLE_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_castle_ground_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block NSMBU_DESERT_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_desert_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.SAND))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_FOREST_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_forest_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_NIGHT_SNOW_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_night_snow_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_SKY_GROUND_BLOCK = register(new BlockCreator.Builder("nsmbu_sky_ground_block", new OverBlock(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_EMPTY_BLOCK = register(new BlockCreator.Builder("smb_empty_block", new EmptyBlock()).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_EMPTY_BLOCK = register(new BlockCreator.Builder("smb3_empty_block", new EmptyBlock()).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_EMPTY_BLOCK = register(new BlockCreator.Builder("smw_empty_block", new EmptyBlock()).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_EMPTY_BLOCK = register(new BlockCreator.Builder("nsmbu_empty_block", new EmptyBlock()).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_ROTATING_BLOCK = register(new BlockCreator.Builder("smb_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_ROTATING_BLOCK = register(new BlockCreator.Builder("smb3_rotating_block", new RotatingBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_ROTATING_BLOCK = register(new BlockCreator.Builder("smw_rotating_block", new RotatingBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_ROTATING_BLOCK = register(new BlockCreator.Builder("nsmbu_rotating_block", new RotatingBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block LIGHT_BLOCK = register(new BlockCreator.Builder("light_block", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(15))));
	public static final Block SMB_GROUND_BRICK_BLOCK = register(new BlockCreator.Builder("smb_ground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_UNDERGROUND_BRICK_BLOCK = register(new BlockCreator.Builder("smb_underground_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_CASTLE_BRICK_BLOCK = register(new BlockCreator.Builder("smb_castle_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_SNOW_BRICK_BLOCK = register(new BlockCreator.Builder("smb_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_NIGHT_SNOW_BRICK_BLOCK = register(new BlockCreator.Builder("smb_night_snow_brick_block", new BrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_BRICK_BLOCK = register(new BlockCreator.Builder("smb3_brick_block", new BrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_BRICK_BLOCK = register(new BlockCreator.Builder("smw_brick_block", new BrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_BRICK_BLOCK = register(new BlockCreator.Builder("nsmbu_brick_block", new BrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_GOLDEN_BRICK_BLOCK = register(new BlockCreator.Builder("smb_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_GOLDEN_BRICK_BLOCK = register(new BlockCreator.Builder("smb3_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMB3_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_GOLDEN_BRICK_BLOCK = register(new BlockCreator.Builder("smw_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.SMW_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_GOLDEN_BRICK_BLOCK = register(new BlockCreator.Builder("nsmbu_golden_brick_block", new GoldenBrickBlock(MubbleSoundTypes.NSMBU_BRICK_BLOCK)).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_GROUND_HARD_BLOCK = register(new BlockCreator.Builder("smb_ground_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_UNDERGROUND_HARD_BLOCK = register(new BlockCreator.Builder("smb_underground_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_UNDERWATER_HARD_BLOCK = register(new BlockCreator.Builder("smb_underwater_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_CASTLE_HARD_BLOCK = register(new BlockCreator.Builder("smb_castle_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_SNOW_HARD_BLOCK = register(new BlockCreator.Builder("smb_snow_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_NIGHT_SNOW_HARD_BLOCK = register(new BlockCreator.Builder("smb_night_snow_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB3_HARD_BLOCK = register(new BlockCreator.Builder("smb3_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMW_STONE_HARD_BLOCK = register(new BlockCreator.Builder("smw_stone_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMW_WOOD_HARD_BLOCK = register(new BlockCreator.Builder("smw_wood_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));
	public static final Block NSMBU_HARD_BLOCK = register(new BlockCreator.Builder("nsmbu_hard_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block SMB_ICE_BLOCK = register(new BlockCreator.Builder("smb_ice_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.PACKED_ICE)));
	public static final Block SMB3_ICE_BLOCK = register(new BlockCreator.Builder("smb3_ice_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.PACKED_ICE)));
	public static final Block SMW_ICE_BLOCK = register(new BlockCreator.Builder("smw_ice_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.PACKED_ICE)));
	public static final Block NSMBU_ICE_BLOCK = register(new BlockCreator.Builder("nsmbu_ice_block", DefaultBlockGetter.CUBE, FabricBlockSettings.copyOf(Blocks.PACKED_ICE)));
	public static final Block SMB_NOTE_BLOCK = register(new BlockCreator.Builder("smb_note_block", new com.hugman.mubble.object.block.NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_NOTE_BLOCK = register(new BlockCreator.Builder("smb3_note_block", new com.hugman.mubble.object.block.NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_NOTE_BLOCK = register(new BlockCreator.Builder("smw_note_block", new com.hugman.mubble.object.block.NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_NOTE_BLOCK = register(new BlockCreator.Builder("nsmbu_note_block", new NoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_SUPER_NOTE_BLOCK = register(new BlockCreator.Builder("smb_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB3_SUPER_NOTE_BLOCK = register(new BlockCreator.Builder("smb3_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMW_SUPER_NOTE_BLOCK = register(new BlockCreator.Builder("smw_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block NSMBU_SUPER_NOTE_BLOCK = register(new BlockCreator.Builder("nsmbu_super_note_block", new SuperNoteBlock(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block SMB_DOOR = register(new BlockCreator.Builder("smb", DefaultBlockGetter.DOOR, FabricBlockSettings.copyOf(Blocks.OAK_DOOR)));
	public static final Block SMB3_DOOR = register(new BlockCreator.Builder("smb3", DefaultBlockGetter.DOOR, FabricBlockSettings.copyOf(Blocks.OAK_DOOR)));
	public static final Block SMW_DOOR = register(new BlockCreator.Builder("smw", DefaultBlockGetter.DOOR, FabricBlockSettings.copyOf(Blocks.OAK_DOOR)));
	public static final Block NSMBU_DOOR = register(new BlockCreator.Builder("nsmbu", DefaultBlockGetter.DOOR, FabricBlockSettings.copyOf(Blocks.OAK_DOOR)));
	public static final Block SMB_KEY_DOOR = register(new BlockCreator.Builder("smb", MubbleBlockGetter.KEY_DOOR, FabricBlockSettings.copyOf(Blocks.IRON_DOOR)));
	public static final Block SMB3_KEY_DOOR = register(new BlockCreator.Builder("smb3", MubbleBlockGetter.KEY_DOOR, FabricBlockSettings.copyOf(Blocks.IRON_DOOR)));
	public static final Block SMW_KEY_DOOR = register(new BlockCreator.Builder("smw", MubbleBlockGetter.KEY_DOOR, FabricBlockSettings.copyOf(Blocks.IRON_DOOR)));
	public static final Block NSMBU_KEY_DOOR = register(new BlockCreator.Builder("nsmbu", MubbleBlockGetter.KEY_DOOR, FabricBlockSettings.copyOf(Blocks.IRON_DOOR)));

	/* SUPER MARIO (OTHERS) */
	public static final PottedPlantPack FIRE_FLOWER = register(new PottedPlantPack.Builder(new BlockCreator.Builder("fire_flower", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 100).render(BlockCreator.Render.CUTOUT)));
	public static final PottedPlantPack ICE_FLOWER = register(new PottedPlantPack.Builder(new BlockCreator.Builder("ice_flower", new FlowerBlock(StatusEffects.MINING_FATIGUE, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 100).render(BlockCreator.Render.CUTOUT)));
	public static final PottedPlantPack BOOMERANG_FLOWER = register(new PottedPlantPack.Builder(new BlockCreator.Builder("boomerang_flower", new FlowerBlock(StatusEffects.HASTE, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 100).render(BlockCreator.Render.CUTOUT)));
	public static final PottedPlantPack CLOUD_FLOWER = register(new PottedPlantPack.Builder(new BlockCreator.Builder("cloud_flower", new CloudFlowerBlock(StatusEffects.SLOW_FALLING, 7, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 100).render(BlockCreator.Render.CUTOUT)));
	public static final PottedPlantPack GOLD_FLOWER = register(new PottedPlantPack.Builder(new BlockCreator.Builder("gold_flower", new FlowerBlock(DawnEffects.HEAVINESS, 6, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(5))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 100).render(BlockCreator.Render.CUTOUT)));

	/* KIRBY */
	public static final Block KIRBY_BLOCK = register(new BlockCreator.Builder("kirby_block", new DirectionalBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.PINK).hardness(0.5F).sounds(BlockSoundGroup.WOOL))).itemGroup(ItemGroup.DECORATIONS));

	/* TETRIS */
	public static final Block WHITE_TETRIS_BLOCK = register(new BlockCreator.Builder("white", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.WHITE)));
	public static final Block LIGHT_GRAY_TETRIS_BLOCK = register(new BlockCreator.Builder("light_gray", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.LIGHT_GRAY)));
	public static final Block GRAY_TETRIS_BLOCK = register(new BlockCreator.Builder("gray", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.GRAY)));
	public static final Block BLACK_TETRIS_BLOCK = register(new BlockCreator.Builder("black", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.BLACK)));
	public static final Block BROWN_TETRIS_BLOCK = register(new BlockCreator.Builder("brown", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.BROWN)));
	public static final Block RED_TETRIS_BLOCK = register(new BlockCreator.Builder("red", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.RED)));
	public static final Block ORANGE_TETRIS_BLOCK = register(new BlockCreator.Builder("orange", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.ORANGE)));
	public static final Block YELLOW_TETRIS_BLOCK = register(new BlockCreator.Builder("yellow", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.YELLOW)));
	public static final Block LIME_TETRIS_BLOCK = register(new BlockCreator.Builder("lime", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.LIME)));
	public static final Block GREEN_TETRIS_BLOCK = register(new BlockCreator.Builder("green", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.GREEN)));
	public static final Block CYAN_TETRIS_BLOCK = register(new BlockCreator.Builder("cyan", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.CYAN)));
	public static final Block LIGHT_BLUE_TETRIS_BLOCK = register(new BlockCreator.Builder("light_blue", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.LIGHT_BLUE)));
	public static final Block BLUE_TETRIS_BLOCK = register(new BlockCreator.Builder("blue", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.BLUE)));
	public static final Block PURPLE_TETRIS_BLOCK = register(new BlockCreator.Builder("purple", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.PURPLE)));
	public static final Block MAGENTA_TETRIS_BLOCK = register(new BlockCreator.Builder("magenta", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.MAGENTA)));
	public static final Block PINK_TETRIS_BLOCK = register(new BlockCreator.Builder("pink", MubbleBlockGetter.TETRIS_BLOCK, Settings.TETRIS_BLOCK.materialColor(DyeColor.PINK)));
	public static final Block TETRIS_GLASS = register(new BlockCreator.Builder("tetris_glass", new TetrisGlassBlock(FabricBlockSettings.copy(Blocks.GLASS))).itemGroup(ItemGroup.BUILDING_BLOCKS).render(BlockCreator.Render.TRANSLUCENT));
	public static final Block JAPANESE_TETRIS_CUSHION = register(new BlockCreator.Builder("japanese_tetris_cushion", new FallingBlock(FabricBlockSettings.copy(Blocks.RED_WOOL))).itemGroup(ItemGroup.BUILDING_BLOCKS).flammability(30, 60));
	public static final Block RAINBOW_TETRIS_SCAFFOLDING = register(new BlockCreator.Builder("rainbow_tetris_scaffolding", new FallingBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block MONOCHROME_TETRIS_SCAFFOLDING = register(new BlockCreator.Builder("monochrome_tetris_scaffolding", new FallingBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).itemGroup(ItemGroup.DECORATIONS));

	/* CASTLEVANIA */
	public static final Block VAMPIRE_STONE = register(new BlockCreator.Builder("vampire_stone", new Block(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));

	public static final Block MEDUSA_STONE = register(new BlockCreator.Builder("medusa_stone", new Block(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final MSBlockPack MEDUSA_BRICKS = register(new MSBlockPack.Builder("medusa_bricks", FabricBlockSettings.copyOf(Blocks.STONE_BRICKS), DefaultBlockGetter.CUBE, DefaultBlockGetter.STAIRS, DefaultBlockGetter.SLAB, DefaultBlockGetter.WALL));

	public static final Block WHITE_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("white_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.WHITE))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block LIGHT_GRAY_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("light_gray_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.LIGHT_GRAY))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block GRAY_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("gray_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.GRAY))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block BLACK_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("black_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.BLACK))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block BROWN_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("brown_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.BROWN))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block RED_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("red_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.RED))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block ORANGE_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("orange_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.ORANGE))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block YELLOW_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("yellow_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.YELLOW))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block LIME_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("lime_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.LIME))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block GREEN_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("green_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.GREEN))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block CYAN_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("cyan_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.CYAN))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block LIGHT_BLUE_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("light_blue_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.LIGHT_BLUE))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block BLUE_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("blue_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.BLUE))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block PURPLE_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("purple_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.PURPLE))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block MAGENTA_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("magenta_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.MAGENTA))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));
	public static final Block PINK_CANDY_CANE_PILLAR = register(new BlockCreator.Builder("pink_candy_cane_pillar", new PillarBlock(Settings.CANDY_CANE_BLOCK.materialColor(DyeColor.PINK))).itemGroup(ItemGroup.DECORATIONS).flammability(5, 10));

	/* SONIC */
	public static final Block GREEN_HILL_GRASS_BLOCK = register(new BlockCreator.Builder("green_hill_grass_block", new GrassBlock(FabricBlockSettings.copy(Blocks.GRASS_BLOCK))).itemGroup(ItemGroup.BUILDING_BLOCKS).render(BlockCreator.Render.CUTOUT_MIPPED));
	public static final Block GREEN_HILL_DIRT = register(new BlockCreator.Builder("green_hill_dirt", new Block(FabricBlockSettings.copy(Blocks.DIRT))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block MARBLE_ZONE_STONE = register(new BlockCreator.Builder("marble_zone_stone", new Block(FabricBlockSettings.copy(Blocks.STONE))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block YELLOW_STUDIOPOLIS_CLAPPER = register(new BlockCreator.Builder("yellow_studiopolis_clapper", new DirectionalBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).itemGroup(ItemGroup.DECORATIONS));
	public static final Block BLUE_STUDIOPOLIS_CLAPPER = register(new BlockCreator.Builder("blue_studiopolis_clapper", new DirectionalBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).itemGroup(ItemGroup.DECORATIONS));

	public static final WoodPack PRESS_GARDEN_WOOD = register(new WoodPack.Builder("press_garden", MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707, false));
	public static final PottedPlantPack RED_PRESS_GARDEN_SAPLING = register(new PottedPlantPack.Builder(new BlockCreator.Builder("red_press_garden_sapling", new SaplingBlock(new RedPressGardenSaplingGenerator(), BlockSettings.SAPLING)).itemGroup(ItemGroup.DECORATIONS).render(BlockCreator.Render.CUTOUT)));
	public static final PottedPlantPack PINK_PRESS_GARDEN_SAPLING = register(new PottedPlantPack.Builder(new BlockCreator.Builder("pink_press_garden_sapling", new SaplingBlock(new PinkPressGardenSaplingGenerator(), BlockSettings.SAPLING)).itemGroup(ItemGroup.DECORATIONS).render(BlockCreator.Render.CUTOUT)));
	public static final LeavesPack RED_PRESS_GARDEN_LEAVES = register(new LeavesPack.Builder("red_press_garden"));
	public static final LeavesPack PINK_PRESS_GARDEN_LEAVES = register(new LeavesPack.Builder("pink_press_garden"));

	public static final Block SPRING = register(new BlockCreator.Builder("spring", new SpringBlock(FabricBlockSettings.of(Material.METAL).hardness(4f))).itemGroup(ItemGroup.TRANSPORTATION));

	/* UNDERTALE / DELTARUNE */
	public static final NormalWoodPack SCARLET_WOOD = register(new NormalWoodPack.Builder("scarlet", new ScarletSaplingGenerator(), MaterialColor.field_25702, MaterialColor.field_25703, MaterialColor.field_25707));
	public static final PottedPlantPack SCARLET_MUSHROOM = register(new PottedPlantPack.Builder(new BlockCreator.Builder("scarlet_mushroom", new MushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7))).itemGroup(ItemGroup.DECORATIONS).render(BlockCreator.Render.CUTOUT)));
	public static final PottedPlantPack SCARLET_ORCHID = register(new PottedPlantPack.Builder(new BlockCreator.Builder("scarlet_orchid", new FlowerBlock(StatusEffects.GLOWING, 8, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(7))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 100).render(BlockCreator.Render.CUTOUT)));

	/* CELESTE */
	public static final Block GIRDER = register(new BlockCreator.Builder("girder", new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).itemGroup(ItemGroup.BUILDING_BLOCKS));

	public static final MSBlockPack MIRROR_TEMPLE_BRICKS = register(new MSBlockPack.Builder("mirror_temple_bricks", FabricBlockSettings.copyOf(Blocks.BRICKS), DefaultBlockGetter.CUBE, DefaultBlockGetter.STAIRS, DefaultBlockGetter.SLAB, DefaultBlockGetter.WALL));
	public static final MSBlockPack OLD_SITE_BRICKS = register(new MSBlockPack.Builder("old_site_bricks", FabricBlockSettings.copyOf(Blocks.BRICKS), DefaultBlockGetter.CUBE, DefaultBlockGetter.STAIRS, DefaultBlockGetter.SLAB, DefaultBlockGetter.WALL));

	public static final Block ELDER_PEBBLES = register(new BlockCreator.Builder("elder_pebbles", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.RED).strength(2.0F, 6.0F).lightLevel(5))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block DREAM_BLOCK = register(new BlockCreator.Builder("dream_block", new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC).hardness(0.4f).sounds(MubbleSoundTypes.DREAM_BLOCK))).itemGroup(ItemGroup.BUILDING_BLOCKS));
	public static final Block DREAM_BEDROCK = register(new BlockCreator.Builder("dream_bedrock", new Block(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing())).itemGroup(ItemGroup.BUILDING_BLOCKS));

	/* PUYO PUYO */
	public static final Block RED_PUYO = register(new BlockCreator.Builder("red_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.RED).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem());
	public static final Block YELLOW_PUYO = register(new BlockCreator.Builder("yellow_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.YELLOW).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem());
	public static final Block GREEN_PUYO = register(new BlockCreator.Builder("green_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GREEN).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem());
	public static final Block TURQUOISE_PUYO = register(new BlockCreator.Builder("turquoise_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.EMERALD).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem());
	public static final Block BLUE_PUYO = register(new BlockCreator.Builder("blue_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.BLUE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem());
	public static final Block PURPLE_PUYO = register(new BlockCreator.Builder("purple_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.PURPLE).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem());
	public static final Block GRAY_PUYO = register(new BlockCreator.Builder("gray_puyo", new PuyoBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, DyeColor.GRAY).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME))).noItem());
	public static final Block GARBAGE_PUYO = register(new BlockCreator.Builder("garbage_puyo", new DirectionalBlock(FabricBlockSettings.copy(Blocks.STONE))).noItem());
	public static final Block POINT_PUYO = register(new BlockCreator.Builder("point_puyo", new DirectionalBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).lightLevel(10))).noItem());
	public static final Block HARD_PUYO = register(new BlockCreator.Builder("hard_puyo", new DirectionalBlock(FabricBlockSettings.copy(Blocks.STONE))).noItem());
	public static final Block IRON_PUYO = register(new BlockCreator.Builder("iron_puyo", new DirectionalBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK))).noItem());

	/* YOUTUBE */
	public static final Block KORETATO_BLOCK = register(new BlockCreator.Builder("koretato_block", new KoretatoBlock()).noItem());
	public static final PottedPlantPack POTATO_FLOWER = register(new PottedPlantPack.Builder(new BlockCreator.Builder("potato_flower", new FlowerBlock(StatusEffects.HUNGER, 9, FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS))).itemGroup(ItemGroup.DECORATIONS).flammability(60, 100).render(BlockCreator.Render.CUTOUT)));

	public static class Settings {
		public static final FabricBlockSettings CHRISTMAS_BAUBLE = FabricBlockSettings.of(Material.GLASS).hardness(0.3F).sounds(BlockSoundGroup.GLASS);
		public static final FabricBlockSettings QUESTION_BLOCK = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.5F, 6.0F);
		public static final FabricBlockSettings TETRIS_BLOCK = FabricBlockSettings.of(Material.STONE).strength(1.5F, 6.0F);
		public static final FabricBlockSettings CANDY_CANE_BLOCK = FabricBlockSettings.of(Material.STONE).hardness(0.2F);
	}

	public static class Properties {
		public static final BooleanProperty OVER = BooleanProperty.of("over");
		public static final BooleanProperty EMPTY = BooleanProperty.of("empty");
		public static final BooleanProperty LOCKED = BooleanProperty.of("locked");
		public static final BooleanProperty ILLUMINATED = BooleanProperty.of("illuminated");
		public static final EnumProperty<FluidLog> FLUIDLOG = EnumProperty.of("fluidlog", FluidLog.class);
		public static final EnumProperty<Princess> PRINCESS = EnumProperty.of("princess", Princess.class);
	}
}
