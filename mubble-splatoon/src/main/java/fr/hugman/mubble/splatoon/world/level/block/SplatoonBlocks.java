package fr.hugman.mubble.splatoon.world.level.block;

import fr.hugman.mubble.splatoon.references.SplatoonBlockKeys;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class SplatoonBlocks {
    public static final InkBlock INK_BLOCK = register(SplatoonBlockKeys.INK, InkBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollision().strength(100.0f).pushReaction(PushReaction.DESTROY).noLootTable().sound(SoundType.EMPTY));

    private static <B extends Block> B noItem(ResourceKey<Block> key, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties blockSettings) {
        B block = factory.apply(blockSettings.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    private static <B extends Block> B register(ResourceKey<Block> key, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties blockSettings, Item.Properties itemSettings) {
        B block = noItem(key, factory, blockSettings);
        var itemRegistryKey = ResourceKey.create(Registries.ITEM, key.identifier());
        Registry.register(BuiltInRegistries.ITEM, itemRegistryKey, new BlockItem(block, itemSettings.setId(itemRegistryKey).useBlockDescriptionPrefix()));
        return block;
    }

    private static <O extends Block> O register(ResourceKey<Block> key, Function<BlockBehaviour.Properties, O> factory, BlockBehaviour.Properties settings) {
        return register(key, factory, settings, new Item.Properties());
    }
}
