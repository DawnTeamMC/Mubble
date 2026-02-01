package fr.hugman.mubble.world.entity.ai.control;

import fr.hugman.mubble.world.entity.Stunnable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

public class StunnableMoveControl extends MoveControl {
    public StunnableMoveControl(Mob entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (this.mob instanceof Stunnable stunnable && !stunnable.isStunned()) {
            super.tick();
        }
    }
}
