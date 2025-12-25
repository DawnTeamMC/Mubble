package fr.hugman.mubble.world.power_up;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.entity.ai.attributes.EntityAttributeEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class PowerUpBuilder {
    private Optional<Component> name = Optional.empty();
    private Optional<Identifier> spriteId = Optional.empty();
    private Optional<Holder<PowerUpAction>> action = Optional.empty();
    private List<EntityAttributeEntry> attributesModifiers = new ArrayList<>();
    private Holder<SoundEvent> obtainSound = PowerUp.DEFAULT_OBTAIN_SOUND;
    private Holder<SoundEvent> looseSound = PowerUp.DEFAULT_LOOSE_SOUND;
    private boolean canSprintOnWater = false;

    public PowerUpBuilder name(Component name) {
        this.name = Optional.ofNullable(name);
        return this;
    }

    public PowerUpBuilder name(Identifier id) {
        return this.name(Component.translatable("power_up." + id.getNamespace() + "." + id.getPath()));
    }

    public PowerUpBuilder name(ResourceKey<PowerUp> key) {
        return name(key.identifier());
    }

    public PowerUpBuilder spriteId(Identifier spriteId) {
        this.spriteId = Optional.ofNullable(spriteId);
        return this;
    }

    public PowerUpBuilder action(Holder<PowerUpAction> action) {
        this.action = Optional.ofNullable(action);
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

    public PowerUpBuilder obtainSound(Holder<SoundEvent> obtainSound) {
        this.obtainSound = obtainSound;
        return this;
    }

    public PowerUpBuilder looseSound(Holder<SoundEvent> looseSound) {
        this.looseSound = looseSound;
        return this;
    }

    public PowerUpBuilder canSprintOnWater(boolean canSprintOnWater) {
        this.canSprintOnWater = canSprintOnWater;
        return this;
    }

    public PowerUp build() {
        if(attributesModifiers.isEmpty()) attributesModifiers = null;
        return new PowerUp(
                name,
                spriteId,
                action,
                Optional.ofNullable(attributesModifiers),
                obtainSound,
                looseSound,
                canSprintOnWater
        );
    }
}