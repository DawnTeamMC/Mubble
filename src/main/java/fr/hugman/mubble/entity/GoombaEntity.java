package fr.hugman.mubble.entity;

import fr.hugman.mubble.entity.ai.control.StunnableMoveControl;
import fr.hugman.mubble.entity.ai.goal.SurprisedActiveTargetGoal;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

//TODO: add mini variant
//TODO: add paragoomba variant
//TODO: add blue, gray variants
//TODO: add spiky variant
//TODO: add jack O'Goomba variant
//TODO: add tanoomba variant
//TODO: add goombrat variant
//TODO: add tail goomba variant
//TODO: add gold goomba variant
//TODO: add bone goomba variant
//TODO: add octoomba variant
//TODO: add choomba variant
//TODO: add Goomdiver variant
//TODO: add Gritty Goomba variant
//TODO: add Dreamy Goomba variant
//TODO: add Headbonk Goomba variant
//TODO: add Goombeetle variant

//TODO: add Goombette variant
//TODO: add Goombario variant
//TODO: add Goombella variant
//TODO: add Professor Frankly variant
//TODO: add Private Goomp variant

//TODO: add hat support
//TODO: add shoe support

//TODO: add swim navigation and animation
//TODO: add sleeping behavior
public class GoombaEntity extends BumpableHostileEntity implements Surprisable, Stunnable {
    private static final TrackedData<Byte> GOOMBA_FLAGS = DataTracker.registerData(GoombaEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Integer> SURPRISE_PROGRESS = DataTracker.registerData(GoombaEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final int SURPRISED_FLAG = 2;
    private static final int UNUSED1_FLAG = 4;
    private static final int UNUSED2_FLAG = 8;
    private static final int UNUSED3_FLAG = 16;

    public static final int SURPRISE_LENGTH = 10; // animation length

    public AnimationState surprisedAnimationState = new AnimationState();
    public AnimationState crushAnimationState = new AnimationState();

    protected GoombaEntity(EntityType<? extends GoombaEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new StunnableMoveControl(this);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new PowderSnowJumpGoal(this, this.getWorld()));
        // TODO: add attack animation (bite)
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(2, new SurprisedActiveTargetGoal<>(this, PlayerEntity.class, true));
    }


    public static DefaultAttributeContainer.Builder createGoombaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isSurprised()) {
            this.setSurpriseProgress(this.getSurpriseProgress() + 1);
            if (this.getSurpriseProgress() > SURPRISE_LENGTH) {
                this.setSurprised(false);
            }
        }
    }

    public boolean isStunned() {
        return this.isSurprised();
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        // TODO: check for damage source
        this.crushAnimationState.start(this.age);
    }

    // DATA TRACKER

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(GOOMBA_FLAGS, (byte)0);
        builder.add(SURPRISE_PROGRESS, 0);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (SURPRISE_PROGRESS.equals(data)) {
            if (this.getSurpriseProgress() == 1) {
                this.surprisedAnimationState.start(this.age);
            }
        }

        super.onTrackedDataSet(data);
    }

    private void setGoombaFlag(int mask, boolean value) {
        byte b = this.dataTracker.get(GOOMBA_FLAGS);
        if (value) {
            this.dataTracker.set(GOOMBA_FLAGS, (byte)(b | mask));
        } else {
            this.dataTracker.set(GOOMBA_FLAGS, (byte)(b & ~mask));
        }
    }

    private boolean hasGoombaFlag(int bitmask) {
        return (this.dataTracker.get(GOOMBA_FLAGS) & bitmask) != 0;
    }

    public boolean isSurprised() {
        return this.hasGoombaFlag(SURPRISED_FLAG);
    }

    public void setSurprised(boolean b) {
        this.setGoombaFlag(SURPRISED_FLAG, b);
        if (!b) {
            this.setSurpriseProgress(0);
        }
    }

    public int getSurpriseProgress() {
        return this.dataTracker.get(SURPRISE_PROGRESS);
    }

    public void setSurpriseProgress(int i) {
        this.dataTracker.set(SURPRISE_PROGRESS, i);
    }
}
