package fr.hugman.mubble.data.model;

import fr.hugman.mubble.Mubble;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;

import java.util.Optional;

public class PromenadeModels {
    public static final Model EGG_BLOCK = block("egg_block", TextureKey.SIDE, TextureKey.END);

    private static Model make(TextureKey... requiredTextureKeys) {
        return new Model(Optional.empty(), Optional.empty(), requiredTextureKeys);
    }

    private static Model block(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Mubble.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static Model item(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Mubble.id("item/" + parent)), Optional.empty(), requiredTextureKeys);
    }


    private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Mubble.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }
}
