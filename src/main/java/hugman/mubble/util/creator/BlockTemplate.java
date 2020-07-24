package hugman.mubble.util.creator;

import hugman.mubble.object.block.DoorBlock;
import hugman.mubble.object.block.PressurePlateBlock;
import hugman.mubble.object.block.StairsBlock;
import hugman.mubble.object.block.StoneButtonBlock;
import hugman.mubble.object.block.TrapdoorBlock;
import hugman.mubble.object.block.WoodButtonBlock;
import hugman.mubble.object.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

public enum BlockTemplate {
	CUBE("", ItemGroup.BUILDING_BLOCKS),
	PLANKS("planks", ItemGroup.BUILDING_BLOCKS),
	STAIRS("stairs", ItemGroup.BUILDING_BLOCKS),
	SLAB("slab", ItemGroup.BUILDING_BLOCKS),
	VERTICAL_SLAB("vertical_slab", ItemGroup.BUILDING_BLOCKS),
	WALL("wall", ItemGroup.DECORATIONS),
	STONE_BUTTON("button", ItemGroup.REDSTONE),
	WOOD_BUTTON("button", ItemGroup.REDSTONE),
	STONE_PRESSURE_PLATE("pressure_plate", ItemGroup.REDSTONE),
	WOOD_PRESSURE_PLATE("pressure_plate", ItemGroup.REDSTONE),
	TRAPDOOR("trapdoor", ItemGroup.REDSTONE, RenderLayer.getCutout()),
	DOOR("door", ItemGroup.REDSTONE, RenderLayer.getCutout()),
	KEY_DOOR("key_door", ItemGroup.REDSTONE, RenderLayer.getCutout()),
	FENCE("fence", ItemGroup.DECORATIONS),
	FENCE_GATE("fence_gate", ItemGroup.REDSTONE),
	LEAVES("leaves", ItemGroup.DECORATIONS, RenderLayer.getCutoutMipped()),
	PILE("leaf_pile", ItemGroup.DECORATIONS, RenderLayer.getCutoutMipped()),
	CLOUD_BLOCK("cloud_block", ItemGroup.DECORATIONS, RenderLayer.getTranslucent()),
	MUSHROOM_BLOCK("mushroom_block", ItemGroup.DECORATIONS),
	MUSHROOM("mushroom", ItemGroup.DECORATIONS),
	QUESTION_BLOCK("question_block", ItemGroup.BUILDING_BLOCKS),
	TETRIS_BLOCK("tetris_block", ItemGroup.BUILDING_BLOCKS);

	private final String suffix;
	private final ItemGroup itemGroup;
	private final RenderLayer renderLayer;

	BlockTemplate(String suffix, ItemGroup itemGroup) {
		this(suffix, itemGroup, RenderLayer.getSolid());
	}

	BlockTemplate(String suffix, ItemGroup itemGroup, RenderLayer renderLayer) {
		this.suffix = suffix;
		this.itemGroup = itemGroup;
		this.renderLayer = renderLayer;
	}

	public String getSuffix() {
		if(suffix.isEmpty()) {
			return suffix;
		}
		else {
			return "_" + suffix;
		}
	}

	public ItemGroup getItemGroup() {
		return itemGroup;
	}

	public RenderLayer getRenderLayer() {
		return renderLayer;
	}

	public Block getBlock(FabricBlockSettings settings) {
		switch(this) {
			case CUBE:
			default:
				return new Block(settings);
			case SLAB:
				return new SlabBlock(settings);
			case STAIRS:
				return new StairsBlock(Blocks.STONE, settings);
			case VERTICAL_SLAB:
				return new VerticalSlabBlock(settings);
			case WALL:
				return new WallBlock(settings);
			case STONE_PRESSURE_PLATE:
				return new PressurePlateBlock(net.minecraft.block.PressurePlateBlock.ActivationRule.MOBS, settings.noCollision().strength(0.5F));
			case WOOD_PRESSURE_PLATE:
				return new PressurePlateBlock(net.minecraft.block.PressurePlateBlock.ActivationRule.EVERYTHING, settings.noCollision().strength(0.5F, 0.0F));
			case STONE_BUTTON:
				return new StoneButtonBlock(settings.noCollision().hardness(0.5F).materialColor(MaterialColor.CLEAR));
			case WOOD_BUTTON:
				return new WoodButtonBlock(settings.noCollision().hardness(0.5F).materialColor(MaterialColor.CLEAR));
			case TRAPDOOR:
				return new TrapdoorBlock(settings);
			case DOOR:
				return new DoorBlock(settings);
			case KEY_DOOR:
				return new KeyDoorBlock(settings);
			case FENCE:
				return new FenceBlock(settings);
			case FENCE_GATE:
				return new FenceGateBlock(settings);
			case LEAVES:
				return new LeavesBlock(settings);
			case PILE:
				return new PileBlock(settings);
			case CLOUD_BLOCK:
				return new CloudBlock(settings);
			case MUSHROOM_BLOCK:
				return new MushroomBlock(settings);
			case TETRIS_BLOCK:
				return new FallingBlock(settings);
		}
	}

	public static final FabricBlockSettings NORMAL_LOG_SETTINGS = FabricBlockSettings.of(Material.WOOD).strength(2.0F).sounds(BlockSoundGroup.WOOD);
	public static final FabricBlockSettings STEM_SETTINGS = FabricBlockSettings.of(Material.NETHER_WOOD).strength(2.0F).sounds(BlockSoundGroup.NETHER_STEM);

	public static final FabricBlockSettings LEAVES_SETTINGS = FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(CreatorHelper::canSpawnOnLeaves).suffocates(CreatorHelper::never).blockVision(CreatorHelper::never);
	public static final FabricBlockSettings LEAF_PILE_SETTINGS = FabricBlockSettings.of(Material.LEAVES).strength(0.1F).ticksRandomly().sounds(BlockSoundGroup.GRASS).noCollision().nonOpaque();
	public static final FabricBlockSettings FLOWER_PILE_SETTINGS = FabricBlockSettings.of(Material.PLANT).breakInstantly().sounds(BlockSoundGroup.GRASS).noCollision();
	public static final FabricBlockSettings SAPLING_SETTINGS = FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.GRASS).breakInstantly().noCollision().ticksRandomly();
	public static final FabricBlockSettings FUNGUS_SETTINGS = FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.FUNGUS).breakInstantly().noCollision();
	public static final FabricBlockSettings POTTED_PLANT_SETTINGS = FabricBlockSettings.of(Material.SUPPORTED).breakInstantly().nonOpaque();

	public static final FabricBlockSettings MUSHROOM_BLOCK_SETTINGS = FabricBlockSettings.of(Material.WOOD).hardness(0.2F).sounds(BlockSoundGroup.WOOD);
	public static final FabricBlockSettings MUSHROOM_SETTINGS = FabricBlockSettings.of(Material.PLANT).noCollision().hardness(0.0F).sounds(BlockSoundGroup.GRASS).lightLevel(1);

	public static final FabricBlockSettings QUESTION_BLOCK_SETTINGS = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.5F, 6.0F);

	public static final FabricBlockSettings TETRIS_BLOCK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(1.5F, 6.0F);

	public static final FabricBlockSettings CANDY_CANE_BLOCK_SETTINGS = FabricBlockSettings.of(Material.STONE).hardness(0.2F);
}
