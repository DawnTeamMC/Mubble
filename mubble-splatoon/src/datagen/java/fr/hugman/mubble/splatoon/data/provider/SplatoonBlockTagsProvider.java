package fr.hugman.mubble.splatoon.data.provider;

import fr.hugman.mubble.splatoon.tags.SplatoonBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class SplatoonBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public SplatoonBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private static ResourceKey<Block> key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // ink floating in the air would give invisible blocks away
        builder(SplatoonBlockTags.UNINKABLE).add(key(Blocks.BARRIER));

        // blocks with collision in these tags (leaves, chorus flowers...) are painted on their faces instead
        builder(SplatoonBlockTags.INK_COATED)
                .addOptionalTag(BlockTags.FLOWERS)
                .addOptionalTag(BlockItemTags.SAPLINGS.block())
                .addOptionalTag(BlockTags.CROPS)
                .addOptionalTag(BlockTags.CAVE_VINES)
                .add(
                        key(Blocks.SHORT_GRASS),
                        key(Blocks.TALL_GRASS),
                        key(Blocks.FERN),
                        key(Blocks.LARGE_FERN),
                        key(Blocks.SHORT_DRY_GRASS),
                        key(Blocks.TALL_DRY_GRASS),
                        key(Blocks.DEAD_BUSH),
                        key(Blocks.BUSH),
                        key(Blocks.FIREFLY_BUSH),
                        key(Blocks.SWEET_BERRY_BUSH),
                        key(Blocks.VINE),
                        key(Blocks.GLOW_LICHEN),
                        key(Blocks.HANGING_ROOTS),
                        key(Blocks.LEAF_LITTER),
                        key(Blocks.WEEPING_VINES),
                        key(Blocks.WEEPING_VINES_PLANT),
                        key(Blocks.TWISTING_VINES),
                        key(Blocks.TWISTING_VINES_PLANT),
                        key(Blocks.SUGAR_CANE),
                        key(Blocks.BROWN_MUSHROOM),
                        key(Blocks.RED_MUSHROOM),
                        key(Blocks.CRIMSON_ROOTS),
                        key(Blocks.WARPED_ROOTS),
                        key(Blocks.NETHER_SPROUTS),
                        key(Blocks.NETHER_WART),
                        key(Blocks.SMALL_DRIPLEAF)
                );
    }
}
