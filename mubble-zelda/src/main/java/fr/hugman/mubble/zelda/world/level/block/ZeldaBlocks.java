package fr.hugman.mubble.zelda.world.level.block;

import fr.hugman.mubble.zelda.references.ZeldaBlockItemIds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class ZeldaBlocks {
    // Each stage points at the next, and the last one points nowhere: that is what hatches.
    public static final LoftwingEggBlock HATCHING_LOFTWING_EGG = registerEgg(ZeldaBlockItemIds.HATCHING_LOFTWING_EGG, null);
    public static final LoftwingEggBlock CRACKED_LOFTWING_EGG = registerEgg(ZeldaBlockItemIds.CRACKED_LOFTWING_EGG, () -> HATCHING_LOFTWING_EGG);
    public static final LoftwingEggBlock LOFTWING_EGG = registerEgg(ZeldaBlockItemIds.LOFTWING_EGG, () -> CRACKED_LOFTWING_EGG);

    private static LoftwingEggBlock registerEgg(BlockItemId id, Supplier<Block> nextStage) {
        return register(id, properties -> new LoftwingEggBlock(properties, nextStage), BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG).mapColor(MapColor.SAND));
    }

    private static <B extends Block> B register(BlockItemId id, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties blockSettings) {
        B block = Registry.register(BuiltInRegistries.BLOCK, id.block(), factory.apply(blockSettings.setId(id.block())));
        Registry.register(BuiltInRegistries.ITEM, id.item(), new BlockItem(block, new Item.Properties().setId(id.item()).useBlockDescriptionPrefix()));
        return block;
    }
}
