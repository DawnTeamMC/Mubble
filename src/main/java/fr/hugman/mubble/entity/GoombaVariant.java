package fr.hugman.mubble.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class GoombaVariant {
    public static final Codec<GoombaVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TextCodecs.CODEC.optionalFieldOf("name").forGetter(v -> v.name),
            Identifier.CODEC.fieldOf("texture").forGetter(v -> v.texture),
            Identifier.CODEC.fieldOf("surprised_texture").forGetter(v -> v.surprisedTexture),
            Codec.unboundedMap(EntityAttribute.CODEC, Codec.DOUBLE).optionalFieldOf("base_attribute_values", Map.of()).forGetter(v -> v.baseAttributes)
    ).apply(instance, GoombaVariant::new));

    private final Optional<Text> name;
    private final Identifier texture;
    private final Identifier surprisedTexture;
    private final Map<RegistryEntry<EntityAttribute>, Double> baseAttributes;

    private final Identifier texturePath;
    private final Identifier surprisedTexturePath;

    public GoombaVariant(Optional<Text> name, Identifier texture, Identifier surprisedTexture, Map<RegistryEntry<EntityAttribute>, Double> baseAttributes) {
        this.name = name;
        this.texture = texture;
        this.surprisedTexture = surprisedTexture;
        this.baseAttributes = baseAttributes;

        this.texturePath = getTexturePath(texture);
        this.surprisedTexturePath = getTexturePath(surprisedTexture);
    }

    public Optional<Text> name() {
        return name;
    }

    public Identifier texture() {
        return texturePath;
    }

    public Identifier surprisedTexture() {
        return surprisedTexturePath;
    }

    public Map<RegistryEntry<EntityAttribute>, Double> baseAttributes() {
        return this.baseAttributes;
    }

    public void applyAttributes(LivingEntity livingEntity) {
        this.baseAttributes.forEach((attribute, value) -> livingEntity.getAttributeInstance(attribute).setBaseValue(value));
    }

    //TODO: move to utility class
    private static Identifier getTexturePath(Identifier id) {
        return id.withPath(oldPath -> "textures/" + oldPath + ".png");
    }
}
