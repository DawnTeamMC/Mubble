package fr.hugman.mubble.super_mario.world.level.block.entity;

import com.mojang.datafixers.types.Type;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SuperMarioBlockEntityTypes {
    public static final BlockEntityType<BumpableBlockEntity> BUMPABLE_BLOCK = register("bumpable_block", FabricBlockEntityTypeBuilder.create(BumpableBlockEntity::new,
            SuperMarioBlocks.QUESTION_BLOCK,
            SuperMarioBlocks.BRICK_BLOCK,
            SuperMarioBlocks.CRYSTAL_BLOCK,
            SuperMarioBlocks.GOLD_BLOCK,
            SuperMarioBlocks.BLUE_EXCLAMATION_BLOCK,
            SuperMarioBlocks.GREEN_EXCLAMATION_BLOCK,
            SuperMarioBlocks.YELLOW_EXCLAMATION_BLOCK,
            SuperMarioBlocks.RED_EXCLAMATION_BLOCK,
            SuperMarioBlocks.NOTE_BLOCK,
            SuperMarioBlocks.BLUE_MARIMBA_BLOCK,
            SuperMarioBlocks.GREEN_MARIMBA_BLOCK,
            SuperMarioBlocks.YELLOW_MARIMBA_BLOCK,
            SuperMarioBlocks.RED_MARIMBA_BLOCK,
            SuperMarioBlocks.BLUE_EGG_BLOCK,
            SuperMarioBlocks.CYAN_EGG_BLOCK,
            SuperMarioBlocks.GREEN_EGG_BLOCK,
            SuperMarioBlocks.YELLOW_EGG_BLOCK,
            SuperMarioBlocks.ORANGE_EGG_BLOCK,
            SuperMarioBlocks.RED_EGG_BLOCK,
            SuperMarioBlocks.PINK_EGG_BLOCK,
            SuperMarioBlocks.BLACK_EGG_BLOCK,
            SuperMarioBlocks.WHITE_EGG_BLOCK
    ));

    private static <T extends BlockEntity> BlockEntityType<T> register(String path, FabricBlockEntityTypeBuilder<T> blockEntityType) {
        Util.fetchChoiceType(References.BLOCK_ENTITY, path);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, SuperMario.id(path), blockEntityType.build());
    }
}
