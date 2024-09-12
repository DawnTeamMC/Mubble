package fr.hugman.mubble.entity;

import fr.hugman.mubble.entity.ai.control.StunnableMoveControl;
import fr.hugman.mubble.entity.ai.goal.SurprisedActiveTargetGoal;
import fr.hugman.mubble.entity.data.MubbleTrackedData;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Optional;

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
public class GoombaEntity extends BumpableHostileEntity implements Surprisable, Stunnable, VariantHolder<RegistryEntry<GoombaVariant>> {
    public static final String VARIANT_KEY = "variant";

    private static final TrackedData<RegistryEntry<GoombaVariant>> VARIANT = DataTracker.registerData(GoombaEntity.class, MubbleTrackedData.GOOMBA_VARIANT);
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

    // BEHAVIOR

    @Override
    protected Text getDefaultName() {
        return this.getVariant().value().name().orElse(super.getDefaultName());
    }

    public static DefaultAttributeContainer.Builder createGoombaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5);
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
        builder.add(VARIANT, this.getRegistryManager().get(MubbleRegistryKeys.GOOMBA_VARIANT).entryOf(GoombaVariants.NORMAL));
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

    @Override
    public void setVariant(RegistryEntry<GoombaVariant> variant) {
        this.dataTracker.set(VARIANT, variant);
        this.getVariant().value().applyAttributes(this); //TODO: only apply attributes when entity is summoned/spawns
    }

    @Override
    public RegistryEntry<GoombaVariant> getVariant() {
        return this.dataTracker.get(VARIANT);
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

    public int getSurpriseProgress() {
        return this.dataTracker.get(SURPRISE_PROGRESS);
    }

    public void setSurpriseProgress(int i) {
        this.dataTracker.set(SURPRISE_PROGRESS, i);
    }

    // NBT DATA

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString(VARIANT_KEY, (this.getVariant().getKey().orElse(GoombaVariants.NORMAL)).getValue().toString());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        Optional.ofNullable(Identifier.tryParse(nbt.getString(VARIANT_KEY)))
                .map(variantId -> RegistryKey.of(MubbleRegistryKeys.GOOMBA_VARIANT, variantId))
                .flatMap(variantKey -> this.getRegistryManager().get(MubbleRegistryKeys.GOOMBA_VARIANT).getEntry(variantKey))
                .ifPresent(this::setVariant);
    }

    // TEXTURE

    public Identifier getTexture() {
        if(this.isSurprised()) {
            return this.getVariant().value().surprisedTexture();
        }
        return this.getVariant().value().texture();
    }
}
