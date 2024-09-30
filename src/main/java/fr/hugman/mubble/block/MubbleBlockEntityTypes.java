package fr.hugman.mubble.block;

import com.mojang.datafixers.types.Type;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Util;

public class MubbleBlockEntityTypes {
    public static final BlockEntityType<BumpableBlockEntity> BUMPABLE_BLOCK = of("bumpable_block", FabricBlockEntityTypeBuilder.create(BumpableBlockEntity::new,
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

    private static <T extends BlockEntity> BlockEntityType<T> of(String path, FabricBlockEntityTypeBuilder<T> blockEntityType) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, path);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Mubble.id(path), blockEntityType.build(type));
    }
}
