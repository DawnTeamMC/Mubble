package fr.hugman.mubble.data.models;

import fr.hugman.mubble.Mubble;
import java.util.Optional;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

public class MubbleModelTemplates {
    public static final ModelTemplate EGG_BLOCK = create("egg_block", TextureSlot.SIDE, TextureSlot.END);
    public static final ModelTemplate CUBE_COLUMN_ALTERNATING = create("cube_column_alternating", MubbleTextureKeys.SIDE_1, MubbleTextureKeys.SIDE_2, TextureSlot.END);

    private static ModelTemplate create(TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.empty(), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate create(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Mubble.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate create(String parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Mubble.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    private static ModelTemplate createItem(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Mubble.id("item/" + parent)), Optional.empty(), requiredTextureKeys);
    }
}
