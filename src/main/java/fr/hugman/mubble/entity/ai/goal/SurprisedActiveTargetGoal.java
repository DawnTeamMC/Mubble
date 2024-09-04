package fr.hugman.mubble.entity.ai.goal;

import java.util.function.Predicate;

import fr.hugman.mubble.entity.GoombaEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;

public class SurprisedActiveTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
	public SurprisedActiveTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
		super(mob, targetClass, 10, checkVisibility, false, null);
	}

	public SurprisedActiveTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility, Predicate<LivingEntity> targetPredicate) {
		super(mob, targetClass, 10, checkVisibility, false, targetPredicate);
	}

	public SurprisedActiveTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate) {
		super(mob, targetClass, 10, checkVisibility, checkCanNavigate, null);
	}

	public SurprisedActiveTargetGoal(
			MobEntity mob,
			Class<T> targetClass,
			int reciprocalChance,
			boolean checkVisibility,
			boolean checkCanNavigate,
			@Nullable Predicate<LivingEntity> targetPredicate
	) {
		super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
	}


	@Override
	public void start() {
		super.start();
		if(this.mob instanceof GoombaEntity goomba) {
			goomba.setSurprised(true);
			if(goomba.getTarget() != null) {
				goomba.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, goomba.getTarget().getPos());
			}
		}
	}
}