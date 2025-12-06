package fr.hugman.mubble.entity;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.component.MubbleDataComponentTypes;
import fr.hugman.mubble.entity.ai.control.StunnableMoveControl;
import fr.hugman.mubble.entity.ai.goal.SurprisedActiveTargetGoal;
import fr.hugman.mubble.tag.MubbleDamageTypeTags;
import fr.hugman.mubble.entity.data.MubbleTrackedData;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Variants;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GoombaEntity extends SuperMarioEnemyEntity implements Surprisable, Stunnable {
    public static final String VARIANT_KEY = "variant";

    public static final MapCodec<RegistryEntry<GoombaVariant>> VARIANT_MAP_CODEC = GoombaVariant.ENTRY_CODEC.fieldOf(VARIANT_KEY);

    protected static final TrackedData<RegistryEntry<GoombaVariant>> VARIANT = DataTracker.registerData(GoombaEntity.class, MubbleTrackedData.GOOMBA_VARIANT);
    protected static final TrackedData<Byte> GOOMBA_FLAGS = DataTracker.registerData(GoombaEntity.class, TrackedDataHandlerRegistry.BYTE);
    protected static final TrackedData<Integer> SURPRISE_PROGRESS = DataTracker.registerData(GoombaEntity.class, TrackedDataHandlerRegistry.INTEGER);

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

    // BEHAVIOR

    @Override
    protected Text getDefaultName() {
        return this.getVariant().value().name().orElse(super.getDefaultName());
    }

    public static DefaultAttributeContainer.Builder createGoombaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.FOLLOW_RANGE, 10.0)
                .add(EntityAttributes.MAX_HEALTH, 4.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.ATTACK_DAMAGE, 1.5);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new PowderSnowJumpGoal(this, this.getEntityWorld()));
        // TODO: add attack animation (bite)
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.6));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(2, new SurprisedActiveTargetGoal<>(this, PlayerEntity.class, true));
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
            this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, this.getTarget().getEntityPos());
        }
    }

    @Override
    public AnimationState getStompDeathAnimationState() {
        return this.crushAnimationState;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return super.damage(world, source, source.isIn(MubbleDamageTypeTags.INSTANT_KILLS_GOOMBAS) ? Float.MAX_VALUE : amount);
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
    protected float calculateNextStepSoundDistance() {
        //TODO: involve entity size or variant maybe? or even add it in the animation directly to make it as accurate as possible
        return this.distanceTraveled + 0.3f;
    }

    // DATA TRACKER

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(GOOMBA_FLAGS, (byte) 0);
        builder.add(SURPRISE_PROGRESS, 0);
        builder.add(VARIANT, this.getRegistryManager().getOrThrow(MubbleRegistryKeys.GOOMBA_VARIANT).getOrThrow(GoombaVariants.NORMAL));
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (SURPRISE_PROGRESS.equals(data)) {
            if (this.getSurpriseProgress() == 1) {
                this.surprisedAnimationState.start(this.age);
            }
            this.lastHeadYaw = this.headYaw;
            this.bodyYaw = this.headYaw;
            this.lastBodyYaw = this.bodyYaw;
        }
        super.onTrackedDataSet(data);
    }

    public void setVariant(RegistryEntry<GoombaVariant> variant) {
        this.dataTracker.set(VARIANT, variant);
        this.getVariant().value().applyAttributes(this); //TODO: only apply attributes when entity is summoned/spawns
    }

    public RegistryEntry<GoombaVariant> getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    private void setGoombaFlag(int mask, boolean value) {
        byte b = this.dataTracker.get(GOOMBA_FLAGS);
        if (value) {
            this.dataTracker.set(GOOMBA_FLAGS, (byte) (b | mask));
        } else {
            this.dataTracker.set(GOOMBA_FLAGS, (byte) (b & ~mask));
        }
    }

    private boolean hasGoombaFlag(int bitmask) {
        return (this.dataTracker.get(GOOMBA_FLAGS) & bitmask) != 0;
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
        return this.dataTracker.get(SURPRISE_PROGRESS);
    }

    public void setSurpriseProgress(int i) {
        this.dataTracker.set(SURPRISE_PROGRESS, i);
    }

    // NBT DATA

	@Override
	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		Variants.writeVariantToNbt(view, this.getVariant());
	}

	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		Variants.readVariantFromNbt(view, MubbleRegistryKeys.GOOMBA_VARIANT).ifPresent(this::setVariant);
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
	public <T> T get(ComponentType<? extends T> type) {
		return type == MubbleDataComponentTypes.GOOMBA_VARIANT ? castComponentValue((ComponentType<T>)type, this.getVariant()) : super.get(type);
	}

	@Override
	protected void copyComponentsFrom(ComponentsAccess from) {
		this.copyComponentFrom(from, MubbleDataComponentTypes.GOOMBA_VARIANT);
		super.copyComponentsFrom(from);
	}

	@Override
	protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
		if (type == MubbleDataComponentTypes.GOOMBA_VARIANT) {
			this.setVariant(castComponentValue(MubbleDataComponentTypes.GOOMBA_VARIANT, value));
			return true;
		} else {
			return super.setApplicableComponent(type, value);
		}
	}
}
