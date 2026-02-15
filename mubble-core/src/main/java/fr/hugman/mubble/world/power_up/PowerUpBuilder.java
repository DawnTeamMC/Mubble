package fr.hugman.mubble.world.power_up;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.entity.ai.attributes.EntityAttributeEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jspecify.annotations.Nullable;

public class PowerUpBuilder {
    private @Nullable Component name = null;
    private @Nullable Identifier spriteId = null;
    private @Nullable Holder<PowerUpAction> action = null;
    private final List<EntityAttributeEntry> attributesModifiers = new ArrayList<>();
    private @Nullable Holder<SoundEvent> obtainSound = null;
    private @Nullable Holder<SoundEvent> emitSound = null;
    private @Nullable Holder<SoundEvent> looseSound = null;
    private @Nullable ParticleOptions particle = null;
    private @Nullable Identifier humanoidOverlayAssetId = null;
    private boolean emissiveOverlay = false;

    public PowerUpBuilder name(Component name) {
        this.name = name;
        return this;
    }

    public PowerUpBuilder name(Identifier id) {
        return this.name(Component.translatable("power_up." + id.getNamespace() + "." + id.getPath()));
    }

    public PowerUpBuilder name(ResourceKey<PowerUp> key) {
        return name(key.identifier());
    }

    public PowerUpBuilder spriteId(Identifier spriteId) {
        this.spriteId = spriteId;
        return this;
    }

    public PowerUpBuilder action(Holder<PowerUpAction> action) {
        this.action = action;
        return this;
    }

    public PowerUpBuilder attributesModifier(EntityAttributeEntry attributesModifier) {
        this.attributesModifiers.add(attributesModifier);
        return this;
    }

    public PowerUpBuilder attributesModifier(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
        var path = attribute.unwrapKey().map(key -> key.identifier().getPath()).orElse("unknown");
        return this.attributesModifier(new EntityAttributeEntry(attribute, new AttributeModifier(Mubble.id("power_up/" + path), value, operation)));
    }

    public PowerUpBuilder particle(ParticleOptions particle) {
        this.particle = particle;
        return this;
    }

    public PowerUpBuilder obtainSound(Holder<SoundEvent> obtainSound) {
        this.obtainSound = obtainSound;
        return this;
    }

    public PowerUpBuilder emitSound(Holder<SoundEvent> emitSound) {
        this.emitSound = emitSound;
        return this;
    }

    public PowerUpBuilder looseSound(Holder<SoundEvent> looseSound) {
        this.looseSound = looseSound;
        return this;
    }

    public PowerUpBuilder humanoidOverlay(Identifier assetId) {
        this.humanoidOverlayAssetId = assetId;
        return this;
    }

    public PowerUpBuilder humanoidOverlay(ResourceKey<PowerUp> key) {
        return this.humanoidOverlay(key.identifier().withPath(s -> "entity/power_up/humanoid/" + s));
    }

    public PowerUpBuilder emissiveOverlay() {
        this.emissiveOverlay = true;
        return this;
    }

    public PowerUp build() {
        return new PowerUp(
                Optional.ofNullable(name),
                Optional.ofNullable(spriteId),
                Optional.ofNullable(action),
                Optional.ofNullable(attributesModifiers.isEmpty() ? null : attributesModifiers),
                new PowerUpCosmectics(
                        Optional.ofNullable(this.particle),
                        Optional.ofNullable(this.obtainSound),
                        Optional.ofNullable(this.emitSound),
                        Optional.ofNullable(this.looseSound),
                        Optional.ofNullable(this.humanoidOverlayAssetId),
                        this.emissiveOverlay
                )
        );
    }
}