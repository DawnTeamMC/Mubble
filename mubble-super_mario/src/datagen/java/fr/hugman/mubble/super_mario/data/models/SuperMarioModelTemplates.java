package fr.hugman.mubble.super_mario.data.models;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

import java.util.Optional;

public class SuperMarioModelTemplates {
    public static final ModelTemplate EGG_BLOCK = create("egg_block", TextureSlot.SIDE, TextureSlot.END);
    public static final ModelTemplate CUBE_COLUMN_ALTERNATING = create("cube_column_alternating", SuperMarioTextureKeys.SIDE_1, SuperMarioTextureKeys.SIDE_2, TextureSlot.END);

    private static ModelTemplate create(TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.empty(), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate create(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(SuperMario.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate create(String parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(SuperMario.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    private static ModelTemplate createItem(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(SuperMario.id("item/" + parent)), Optional.empty(), requiredTextureKeys);
    }
}
