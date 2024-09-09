package fr.hugman.mubble.entity;

import com.google.common.collect.Lists;
import fr.hugman.mubble.entity.ai.goal.SurprisedActiveTargetGoal;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

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
public class GoombaEntity extends HostileEntity {
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
        this.moveControl = new GoombaEntity.GoombaMoveControl(this);
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

    @Override
    public void tickMovement() {
        super.tickMovement();

        // TODO: implement this behaviour in an interface or abstract class
        // TODO: disable all this if the entity is being ridden
        // TODO: stack eachother ontop?
        if (this.getHealth() > 0.0F && !this.isSpectator()) {
            Box hitBox = this.getBoundingBox();
            hitBox = hitBox.withMinY(hitBox.maxY - (0.2D * (hitBox.maxY - hitBox.minY)));
            hitBox = hitBox.withMaxY(hitBox.maxY + 0.5D);

            if (!this.getWorld().isClient) {
                List<Entity> list = this.getWorld().getOtherEntities(this, hitBox, EntityPredicates.EXCEPT_SPECTATOR.and(entity -> entity.getType().isIn(MubbleEntityTypeTags.CAN_JUMP_BUMP)));
                if (!list.isEmpty()) {
                    List<Entity> list2 = Lists.newArrayList();

                    for (Entity entity : list) {
                        if (!entity.isOnGround() && entity.getVelocity().getY() < 0.3D && entity.isAlive()) {
                            list2.add(entity);
                        }
                    }

                    if (!list2.isEmpty()) {
                        // TODO: set damage source to first entity in list
                        this.damage(this.getDamageSources().genericKill(), Float.MAX_VALUE);
                        for (Entity entity : list) {
                            entity.setVelocity(entity.getVelocity().x, 0.5D, entity.getVelocity().z);
                            if (entity instanceof PlayerEntity player) {
                                ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                            }
                            entity.fallDistance = 0.0F;
                        }
                    }
                }
            }
        }
    }

    public boolean canMove() {
        return !this.isSurprised();
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

    //TODO: implement this in a standalone class with an interface for the entity
    static class GoombaMoveControl extends MoveControl {
        private final GoombaEntity goomba;

        public GoombaMoveControl(GoombaEntity goomba) {
            super(goomba);
            this.goomba = goomba;
        }

        @Override
        public void tick() {
            if (this.goomba.canMove()) {
                super.tick();
            }
        }
    }

}
