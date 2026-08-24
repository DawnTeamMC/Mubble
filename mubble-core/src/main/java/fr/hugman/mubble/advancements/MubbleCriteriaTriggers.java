package fr.hugman.mubble.advancements;

import fr.hugman.mubble.Mubble;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class MubbleCriteriaTriggers {
    public static final TrialCompletedTrigger TRIAL_COMPLETED = register("trial_completed", new TrialCompletedTrigger());

    private static <T extends CriterionTrigger<?>> T register(String path, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Mubble.id(path), trigger);
    }
}
