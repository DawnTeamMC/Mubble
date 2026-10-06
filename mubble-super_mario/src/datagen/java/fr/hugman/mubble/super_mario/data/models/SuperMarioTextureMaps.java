package fr.hugman.mubble.super_mario.data.models;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

public class SuperMarioTextureMaps {
    public static TextureMapping all(Identifier all) {
        return new TextureMapping().put(TextureSlot.ALL, new Material(all.withPrefix("block/")));
    }

    public static TextureMapping sideEnd(Identifier side, Identifier end) {
        return new TextureMapping()
                .put(TextureSlot.SIDE, new Material(side.withPrefix("block/")))
                .put(TextureSlot.END, new Material(end.withPrefix("block/")));
    }
}
