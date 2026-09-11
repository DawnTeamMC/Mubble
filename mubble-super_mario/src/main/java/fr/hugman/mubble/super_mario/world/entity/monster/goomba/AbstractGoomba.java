package fr.hugman.mubble.super_mario.world.entity.monster.goomba;

import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioDamageTypeTags;
import fr.hugman.mubble.super_mario.world.entity.monster.SuperMarioEnemy;
import fr.hugman.mubble.world.entity.Stunnable;
import fr.hugman.mubble.world.entity.Surprisable;
import fr.hugman.mubble.world.entity.ai.control.StunnableMoveControl;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * What every member of the goomba family shares: the surprise that doubles as a stun, the crush
 * animation that replaces the vanilla death spin when they are stomped, and the goomba voice.
 * <p>
 * The two entities built on it pull in opposite directions. {@link Goomba} carries a data pack variant
 * and fights by biting; {@link MiniGoomba} has neither, and latches onto whoever it catches instead.
 *
 * @author Hugman
 * @since v4.0.0
 */
abstract public class AbstractGoomba extends SuperMarioEnemy implements Surprisable, Stunnable {
    protected static final EntityDataAccessor<Byte> GOOMBA_FLAGS = SynchedEntityData.defineId(AbstractGoomba.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Integer> SURPRISE_PROGRESS = SynchedEntityData.defineId(AbstractGoomba.class, EntityDataSerializers.INT);

    protected static final int SURPRISED_FLAG = 2;
    protected static final int UNUSED1_FLAG = 4;
    protected static final int UNUSED2_FLAG = 8;
    protected static final int UNUSED3_FLAG = 16;

    /** How long the surprise lasts, which is both the animation length and the stun it amounts to. */
    public static final int SURPRISE_LENGTH = 10;

    public final AnimationState surprisedAnimationState = new AnimationState();
    public final AnimationState crushAnimationState = new AnimationState();

    protected AbstractGoomba(EntityType<? extends AbstractGoomba> type, Level level) {
        super(type, level);
        this.moveControl = new StunnableMoveControl(this);
    }

    // BEHAVIOR

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
    public boolean isStunned() {
        return this.isSurprised();
    }

    @Override
    public void onSurprised() {
        this.playSound(SuperMarioSounds.GOOMBA_FIND_TARGET, 1.0F, this.getGoombaVoicePitch());
        if (null != this.getTarget()) {
            this.lookAt(EntityAnchorArgument.Anchor.EYES, this.getTarget().position());
        }
    }

    @Override
    public AnimationState getStompDeathAnimationState() {
        return this.crushAnimationState;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return super.hurtServer(level, source, source.is(SuperMarioDamageTypeTags.INSTANT_KILLS_GOOMBAS) ? Float.MAX_VALUE : amount);
    }

    // SOUNDS

    /**
     * The pitch the goomba voice is played back at. Smaller members of the family speak higher.
     */
    //TODO: make pitch depend on variant or size
    protected float getGoombaVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isStomped() ? SuperMarioSounds.GOOMBA_STOMP : SuperMarioSounds.GOOMBA_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (this.getTarget() != null) {
            this.playSound(SuperMarioSounds.GOOMBA_RUN_STEP, 1.0F, this.getGoombaVoicePitch());
        } else {
            this.playSound(SuperMarioSounds.GOOMBA_WALK_STEP, 1.0F, this.getGoombaVoicePitch());
        }
    }

    @Override
    protected float nextStep() {
        //TODO: involve entity size or variant maybe? or even add it in the animation directly to make it as accurate as possible
        return this.moveDist + 0.3f;
    }

    // DATA TRACKER

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(GOOMBA_FLAGS, (byte) 0);
        entityData.define(SURPRISE_PROGRESS, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (SURPRISE_PROGRESS.equals(accessor)) {
            if (this.getSurpriseProgress() == 1) {
                this.surprisedAnimationState.start(this.tickCount);
            }
            this.yHeadRotO = this.yHeadRot;
            this.yBodyRot = this.yHeadRot;
            this.yBodyRotO = this.yBodyRot;
        }
        super.onSyncedDataUpdated(accessor);
    }

    protected void setGoombaFlag(int mask, boolean value) {
        byte b = this.entityData.get(GOOMBA_FLAGS);
        if (value) {
            this.entityData.set(GOOMBA_FLAGS, (byte) (b | mask));
        } else {
            this.entityData.set(GOOMBA_FLAGS, (byte) (b & ~mask));
        }
    }

    protected boolean hasGoombaFlag(int bitmask) {
        return (this.entityData.get(GOOMBA_FLAGS) & bitmask) != 0;
    }

    @Override
    public boolean isSurprised() {
        return this.hasGoombaFlag(SURPRISED_FLAG);
    }

    @Override
    public void setSurprised(boolean b) {
        this.setGoombaFlag(SURPRISED_FLAG, b);
        if (!b) {
            this.setSurpriseProgress(0);
        }
    }

    public int getSurpriseProgress() {
        return this.entityData.get(SURPRISE_PROGRESS);
    }

    public void setSurpriseProgress(int i) {
        this.entityData.set(SURPRISE_PROGRESS, i);
    }

    // TEXTURE

    /** The texture to draw this goomba with, which depends on whether it is mid-surprise. */
    abstract public Identifier getTexture();
}
