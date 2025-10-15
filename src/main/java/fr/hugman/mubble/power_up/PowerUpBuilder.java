package fr.hugman.mubble.power_up;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.attribute.EntityAttributeEntry;
import fr.hugman.mubble.power_up.action.PowerUpAction;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PowerUpBuilder {
    private Optional<Text> name = Optional.empty();
    private Optional<Identifier> spriteId = Optional.empty();
    private Optional<RegistryEntry<PowerUpAction>> action = Optional.empty();
    private List<EntityAttributeEntry> attributesModifiers = new ArrayList<>();
    private RegistryEntry<SoundEvent> obtainSound = PowerUp.DEFAULT_OBTAIN_SOUND;
    private RegistryEntry<SoundEvent> looseSound = PowerUp.DEFAULT_LOOSE_SOUND;
    private boolean canSprintOnWater = false;

    public PowerUpBuilder name(Text name) {
        this.name = Optional.ofNullable(name);
        return this;
    }

    public PowerUpBuilder name(Identifier id) {
        return this.name(Text.translatable("power_up." + id.getNamespace() + "." + id.getPath()));
    }

    public PowerUpBuilder name(RegistryKey<PowerUp> key) {
        return name(key.getValue());
    }

    public PowerUpBuilder spriteId(Identifier spriteId) {
        this.spriteId = Optional.ofNullable(spriteId);
        return this;
    }

    public PowerUpBuilder action(RegistryEntry<PowerUpAction> action) {
        this.action = Optional.ofNullable(action);
        return this;
    }

    public PowerUpBuilder attributesModifier(EntityAttributeEntry attributesModifier) {
        this.attributesModifiers.add(attributesModifier);
        return this;
    }

    public PowerUpBuilder attributesModifier(RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
        var path = attribute.getKey().map(key -> key.getValue().getPath()).orElse("unknown");
        return this.attributesModifier(new EntityAttributeEntry(attribute, new EntityAttributeModifier(Mubble.id("power_up/" + path), value, operation)));
    }

    public PowerUpBuilder obtainSound(RegistryEntry<SoundEvent> obtainSound) {
        this.obtainSound = obtainSound;
        return this;
    }

    public PowerUpBuilder looseSound(RegistryEntry<SoundEvent> looseSound) {
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