package fr.hugman.mubble.world.entity.ai.goal.target;

import fr.hugman.mubble.world.entity.Surprisable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;

public class SurprisedActiveTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public SurprisedActiveTargetGoal(Mob mob, Class<T> targetClass, boolean checkVisibility) {
        super(mob, targetClass, 10, checkVisibility, false, null);
    }

    public SurprisedActiveTargetGoal(Mob mob, Class<T> targetClass, boolean checkVisibility, TargetingConditions.Selector targetPredicate) {
        super(mob, targetClass, 10, checkVisibility, false, targetPredicate);
    }

    public SurprisedActiveTargetGoal(Mob mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate) {
        super(mob, targetClass, 10, checkVisibility, checkCanNavigate, null);
    }

    public SurprisedActiveTargetGoal(
            Mob mob,
            Class<T> targetClass,
            int reciprocalChance,
            boolean checkVisibility,
            boolean checkCanNavigate,
            @Nullable TargetingConditions.Selector targetPredicate
    ) {
        super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
    }


    @Override
    public void start() {
        super.start();
        if (this.mob instanceof Surprisable surprisable) {
            if (mob.getTarget() != null) {
                surprisable.setSurprised(true);
                surprisable.onSurprised();
            }
        }
    }
}