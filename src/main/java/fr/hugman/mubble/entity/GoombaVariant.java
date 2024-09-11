package fr.hugman.mubble.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public class GoombaVariant {
    public static final Codec<GoombaVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("texture").forGetter(goomba -> goomba.texture),
            Identifier.CODEC.fieldOf("surprised_texture").forGetter(goomba -> goomba.texture)
    ).apply(instance, GoombaVariant::new));

    private final Identifier texture;
    private final Identifier surprisedTexture;

    private final Identifier texturePath;
    private final Identifier surprisedTexturePath;

    public GoombaVariant(Identifier texture, Identifier surprisedTexture) {
        this.texture = texture;
        this.surprisedTexture = surprisedTexture;

        this.texturePath = getTexturePath(texture);
        this.surprisedTexturePath = getTexturePath(surprisedTexture);
    }

    public Identifier texture() {
        return texturePath;
    }

    public Identifier surprisedTexture() {
        return surprisedTexturePath;
    }

    //TODO: move to utility class
    private static Identifier getTexturePath(Identifier id) {
        return id.withPath(oldPath -> "textures/" + oldPath + ".png");
    }
}
