package fr.hugman.mubble.block;

import com.mojang.datafixers.types.Type;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Util;

public class MubbleBlockEntityTypes {
    // SUPER MARIO
    public static final BlockEntityType<BumpableBlockEntity> BUMPABLE_BLOCK = of("bumpable_block", BlockEntityType.Builder.create(BumpableBlockEntity::new, MubbleBlocks.QUESTION_BLOCK, MubbleBlocks.BRICK_BLOCK, MubbleBlocks.GOLD_BLOCK, MubbleBlocks.NOTE_BLOCK, MubbleBlocks.EXCLAMATION_BLOCK));

    private static <T extends BlockEntity> BlockEntityType<T> of(String path, BlockEntityType.Builder<T> blockEntityType) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, path);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Mubble.id(path), blockEntityType.build(type));
    }
}
