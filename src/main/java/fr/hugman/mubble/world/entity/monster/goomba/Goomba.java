package fr.hugman.mubble.world.entity.monster.goomba;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.tags.MubbleDamageTypeTags;
import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.world.entity.Stunnable;
import fr.hugman.mubble.world.entity.Surprisable;
import fr.hugman.mubble.world.entity.ai.control.StunnableMoveControl;
import fr.hugman.mubble.world.entity.ai.goal.target.SurprisedActiveTargetGoal;
import fr.hugman.mubble.world.entity.monster.SuperMarioEnemy;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class Goomba extends SuperMarioEnemy implements Surprisable, Stunnable {
    public static final String VARIANT_KEY = "variant";

    public static final MapCodec<Holder<GoombaVariant>> VARIANT_MAP_CODEC = GoombaVariant.ENTRY_CODEC.fieldOf(VARIANT_KEY);

    protected static final EntityDataAccessor<Holder<GoombaVariant>> VARIANT = SynchedEntityData.defineId(Goomba.class, MubbleEntityDataSerializers.GOOMBA_VARIANT);
    protected static final EntityDataAccessor<Byte> GOOMBA_FLAGS = SynchedEntityData.defineId(Goomba.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Integer> SURPRISE_PROGRESS = SynchedEntityData.defineId(Goomba.class, EntityDataSerializers.INT);

    private static final int SURPRISED_FLAG = 2;
    private static final int UNUSED1_FLAG = 4;
    private static final int UNUSED2_FLAG = 8;
    private static final int UNUSED3_FLAG = 16;

    public static final int SURPRISE_LENGTH = 10; // animation length

    public final AnimationState surprisedAnimationState = new AnimationState();
    public final AnimationState crushAnimationState = new AnimationState();

    public Goomba(EntityType<? extends Goomba> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new StunnableMoveControl(this);
    }

    // BEHAVIOR

    @Override
    protected Component getTypeName() {
        return this.getVariant().value().name().orElse(super.getTypeName());
    }

    public static AttributeSupplier.Builder createGoombaAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0)
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        // TODO: add attack animation (bite)
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new SurprisedActiveTargetGoal<>(this, Player.class, true));
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
    public boolean isStunned() {
        return this.isSurprised();
    }

    @Override
    public void onSurprised() {
        this.playSound(MubbleSounds.GOOMBA_FIND_TARGET, 1.0F, 1.0F);
        if(null != this.getTarget()) {
            this.lookAt(EntityAnchorArgument.Anchor.EYES, this.getTarget().position());
        }
    }

    @Override
    public AnimationState getStompDeathAnimationState() {
        return this.crushAnimationState;
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return super.hurtServer(world, source, source.is(MubbleDamageTypeTags.INSTANT_KILLS_GOOMBAS) ? Float.MAX_VALUE : amount);
    }

    // SOUNDS

    @Override
    protected SoundEvent getDeathSound() {
        return this.isStomped() ? MubbleSounds.GOOMBA_STOMP : MubbleSounds.GOOMBA_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        //TODO: make pitch depend on variant or size
        if (this.getTarget() != null) {
            this.playSound(MubbleSounds.GOOMBA_RUN_STEP, 1.0F, 1.0F);
        } else {
            this.playSound(MubbleSounds.GOOMBA_WALK_STEP, 1.0F, 1.0F);
        }
    }

    @Override
    protected float nextStep() {
        //TODO: involve entity size or variant maybe? or even add it in the animation directly to make it as accurate as possible
        return this.moveDist + 0.3f;
    }

    // DATA TRACKER

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GOOMBA_FLAGS, (byte) 0);
        builder.define(SURPRISE_PROGRESS, 0);
        builder.define(VARIANT, this.registryAccess().lookupOrThrow(MubbleRegistries.GOOMBA_VARIANT).getOrThrow(GoombaVariants.NORMAL));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (SURPRISE_PROGRESS.equals(data)) {
            if (this.getSurpriseProgress() == 1) {
                this.surprisedAnimationState.start(this.tickCount);
            }
            this.yHeadRotO = this.yHeadRot;
            this.yBodyRot = this.yHeadRot;
            this.yBodyRotO = this.yBodyRot;
        }
        super.onSyncedDataUpdated(data);
    }

    public void setVariant(Holder<GoombaVariant> variant) {
        this.entityData.set(VARIANT, variant);
        this.getVariant().value().applyAttributes(this); //TODO: only apply attributes when entity is summoned/spawns
    }

    public Holder<GoombaVariant> getVariant() {
        return this.entityData.get(VARIANT);
    }

    private void setGoombaFlag(int mask, boolean value) {
        byte b = this.entityData.get(GOOMBA_FLAGS);
        if (value) {
            this.entityData.set(GOOMBA_FLAGS, (byte) (b | mask));
        } else {
            this.entityData.set(GOOMBA_FLAGS, (byte) (b & ~mask));
        }
    }

    private boolean hasGoombaFlag(int bitmask) {
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

    // NBT DATA

	@Override
	protected void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		VariantUtils.writeVariant(view, this.getVariant());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		VariantUtils.readVariant(view, MubbleRegistries.GOOMBA_VARIANT).ifPresent(this::setVariant);
	}

    // TEXTURE

    public Identifier getTexture() {
        if (this.isSurprised()) {
            return this.getVariant().value().assetInfo().surprised().texturePath();
        }
        return this.getVariant().value().assetInfo().texture().texturePath();
    }

	@Nullable
	@Override
	public <T> T get(DataComponentType<? extends T> type) {
		return type == MubbleDataComponents.GOOMBA_VARIANT ? castComponentValue((DataComponentType<T>)type, this.getVariant()) : super.get(type);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter from) {
		this.applyImplicitComponentIfPresent(from, MubbleDataComponents.GOOMBA_VARIANT);
		super.applyImplicitComponents(from);
	}

	@Override
	protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
		if (type == MubbleDataComponents.GOOMBA_VARIANT) {
			this.setVariant(castComponentValue(MubbleDataComponents.GOOMBA_VARIANT, value));
			return true;
		} else {
			return super.applyImplicitComponent(type, value);
		}
	}
}
