package fr.hugman.mubble.test.gametest.support;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The blocks only the tests use. They are registered with the mods, as the registries freeze before
 * the tests load.
 */
public final class TestBlocks implements ModInitializer {
    private static final ResourceKey<Block> POUND_COUNTING_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("mubble-gametest", "pound_counting_block"));
    public static final PoundCountingBlock POUND_COUNTING_BLOCK = Registry.register(BuiltInRegistries.BLOCK, POUND_COUNTING_BLOCK_KEY, new PoundCountingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).setId(POUND_COUNTING_BLOCK_KEY)));

    @Override
    public void onInitialize() {
        // loading the class registers the blocks
    }
}
