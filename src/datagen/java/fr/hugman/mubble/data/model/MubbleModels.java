package fr.hugman.mubble.data.model;

import fr.hugman.mubble.Mubble;
import java.util.Optional;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

public class MubbleModels {
    public static final ModelTemplate EGG_BLOCK = block("egg_block", TextureSlot.SIDE, TextureSlot.END);
    public static final ModelTemplate CUBE_COLUMN_ALTERNATING = block("cube_column_alternating", MubbleTextureKeys.SIDE_1, MubbleTextureKeys.SIDE_2, TextureSlot.END);

    private static ModelTemplate make(TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.empty(), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Mubble.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate item(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Mubble.id("item/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Mubble.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }
}
