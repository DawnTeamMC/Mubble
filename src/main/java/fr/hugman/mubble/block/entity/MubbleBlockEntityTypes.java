package fr.hugman.mubble.block.entity;

import com.mojang.datafixers.types.Type;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.MubbleBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MubbleBlockEntityTypes {
    public static final BlockEntityType<BumpableBlockEntity> BUMPABLE_BLOCK = register("bumpable_block", FabricBlockEntityTypeBuilder.create(BumpableBlockEntity::new,
            MubbleBlocks.QUESTION_BLOCK,
            MubbleBlocks.BRICK_BLOCK,
            MubbleBlocks.CRYSTAL_BLOCK,
            MubbleBlocks.GOLD_BLOCK,
            MubbleBlocks.BLUE_EXCLAMATION_BLOCK,
            MubbleBlocks.GREEN_EXCLAMATION_BLOCK,
            MubbleBlocks.YELLOW_EXCLAMATION_BLOCK,
            MubbleBlocks.RED_EXCLAMATION_BLOCK,
            MubbleBlocks.NOTE_BLOCK,
            MubbleBlocks.BLUE_MARIMBA_BLOCK,
            MubbleBlocks.GREEN_MARIMBA_BLOCK,
            MubbleBlocks.YELLOW_MARIMBA_BLOCK,
            MubbleBlocks.RED_MARIMBA_BLOCK,
            MubbleBlocks.BLUE_EGG_BLOCK,
            MubbleBlocks.CYAN_EGG_BLOCK,
            MubbleBlocks.GREEN_EGG_BLOCK,
            MubbleBlocks.YELLOW_EGG_BLOCK,
            MubbleBlocks.ORANGE_EGG_BLOCK,
            MubbleBlocks.RED_EGG_BLOCK,
            MubbleBlocks.PINK_EGG_BLOCK,
            MubbleBlocks.BLACK_EGG_BLOCK,
            MubbleBlocks.WHITE_EGG_BLOCK
    ));

    private static <T extends BlockEntity> BlockEntityType<T> register(String path, FabricBlockEntityTypeBuilder<T> blockEntityType) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, path);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Mubble.id(path), blockEntityType.build());
    }
}
