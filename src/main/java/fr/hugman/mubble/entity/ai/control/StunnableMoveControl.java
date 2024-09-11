package fr.hugman.mubble.entity.ai.control;

import fr.hugman.mubble.entity.Stunnable;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;

public class StunnableMoveControl extends MoveControl {
    public StunnableMoveControl(MobEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (this.entity instanceof Stunnable stunnable && !stunnable.isStunned()) {
            super.tick();
        }
    }
}
