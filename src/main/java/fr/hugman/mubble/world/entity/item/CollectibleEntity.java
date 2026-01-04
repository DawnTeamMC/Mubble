package fr.hugman.mubble.world.entity.item;

import fr.hugman.mubble.sounds.MubbleSounds;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CollectibleEntity extends Entity {
    private static final String TAG_ITEM = "item";
    private static final String TAG_PICKUP_SOUND = "pickup_sound";
    private static final String TAG_PICKUP_SOUND_VOLUME = "pickup_sound_volume";
    private static final String TAG_PICKUP_SOUND_PITCH = "pickup_sound_pitch";
    private static final String TAG_IS_FIXED = "is_fixed";
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(CollectibleEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_IS_FIXED = SynchedEntityData.defineId(CollectibleEntity.class, EntityDataSerializers.BOOLEAN);

    protected Holder<SoundEvent> pickupSound;
    protected FloatProvider pickupSoundVolume;
    protected FloatProvider pickupSoundPitch;

    public CollectibleEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public CollectibleEntity(Level level, double x, double y, double z, ItemStack stack) {
        this(MubbleEntityTypes.COLLECTIBLE, level);
        this.setPos(x, y, z);
        this.setItem(stack);
        this.setFixed(true);
    }

    public static CollectibleEntity coin(Level level, double x, double y, double z, ItemStack stack) {
        var collectible = new CollectibleEntity(level, x, y, z, stack);
        collectible.setPickupSound(MubbleSounds.COIN_COLLECT);
        collectible.setPickupSoundVolume(ConstantFloat.of(0.2f));
        collectible.setPickupSoundPitch(ConstantFloat.of(1.0f));
        return collectible;
    }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public void setItem(final ItemStack itemStack) {
        this.getEntityData().set(DATA_ITEM, itemStack);
    }

    public Holder<SoundEvent> getPickupSound() {
        return pickupSound;
    }

    public void setPickupSound(Holder<SoundEvent> pickupSound) {
        this.pickupSound = pickupSound;
    }

    public FloatProvider getPickupSoundVolume() {
        return pickupSoundVolume;
    }

    public void setPickupSoundVolume(FloatProvider pickupSoundVolume) {
        this.pickupSoundVolume = pickupSoundVolume;
    }

    public FloatProvider getPickupSoundPitch() {
        return pickupSoundPitch;
    }

    public void setPickupSoundPitch(FloatProvider pickupSoundPitch) {
        this.pickupSoundPitch = pickupSoundPitch;
    }

    /**
     * Determines if the entity is fixed and does not move at all. This differs from NoGravity, which only disables gravity effects.
     */
    public boolean isFixed() {
        return this.getEntityData().get(DATA_IS_FIXED);
    }

    public void setFixed(boolean fixed) {
        this.getEntityData().set(DATA_IS_FIXED, fixed);
    }

    @Override
    public Component getName() {
        Component name = this.getCustomName();
        return name != null ? name : this.getItem().getItemName();
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public void tick() {
        if (this.getItem().isEmpty()) {
            this.discard();
            return;
        }

        if (!this.isFixed()) {
            this.xo = this.getX();
            this.yo = this.getY();
            this.zo = this.getZ();
            Vec3 oldMovement = this.getDeltaMovement();
            if (this.isInWater() && this.getFluidHeight(FluidTags.WATER) > 0.1F) {
                this.setFluidMovement(0.99F);
            } else if (this.isInLava() && this.getFluidHeight(FluidTags.LAVA) > 0.1F) {
                this.setFluidMovement(0.95F);
            } else {
                this.applyGravity();
            }

            if (this.level().isClientSide()) {
                this.noPhysics = false;
            } else {
                this.noPhysics = !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-7));
                if (this.noPhysics) {
                    this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
                }
            }

            var wasOnGround = this.onGround();
            if (!wasOnGround || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
                var oldY = this.getDeltaMovement().y;
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.applyEffectsFromBlocks();

                float friction = 0.98F;
                if (this.onGround()) {
                    if (!wasOnGround) {
                        var y = oldY * -0.85D;
                        if (y > 0.025D) {
                            Vec3 movement = this.getDeltaMovement().add(0.0D, y, 0.0D);
                            this.setDeltaMovement(movement);
                            this.setOnGround(false);
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), MubbleSounds.COIN_BOUNCE, this.getSoundSource(), Math.min(1.0f, (float) y), 1.0f);
                            this.needsSync = true;
                        }
                    } else {
                        friction = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
                    }
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply(friction, 0.98, friction));
                if (this.onGround()) {
                    Vec3 movement = this.getDeltaMovement();
                    if (movement.y < 0.0) {
                        this.setDeltaMovement(movement.multiply(1.0, -0.5, 1.0));
                    }
                }
            }

            this.needsSync = this.needsSync | this.updateInWaterStateAndDoFluidPushing();
            if (!this.level().isClientSide()) {
                double movementLength = this.getDeltaMovement().subtract(oldMovement).lengthSqr();
                if (movementLength > 0.01) {
                    this.needsSync = true;
                }
            }
        }

        super.tick();
    }

    @Override
    public void onExplosionHit(@Nullable Entity explosionCausedBy) {
        super.onExplosionHit(explosionCausedBy);
        this.setFixed(false);
    }

    @Override
    public BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }


    private void setFluidMovement(final double multiplier) {
        Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(movement.x * multiplier, movement.y + (movement.y < 0.06F ? 5.0E-4F : 0.0F), movement.z * multiplier);
    }

    @Override
    public boolean ignoreExplosion(final Explosion explosion) {
        if (!this.isFixed()) {
            return false;
        }
        return explosion.shouldAffectBlocklikeEntities() ? super.ignoreExplosion(explosion) : true;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_ITEM, ItemStack.EMPTY);
        entityData.define(DATA_IS_FIXED, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        if (!this.getItem().isEmpty()) {
            output.store(TAG_ITEM, ItemStack.CODEC, this.getItem());
            output.store(TAG_PICKUP_SOUND, SoundEvent.CODEC, this.pickupSound);
            output.store(TAG_PICKUP_SOUND_VOLUME, FloatProvider.CODEC, this.pickupSoundVolume);
            output.store(TAG_PICKUP_SOUND_PITCH, FloatProvider.CODEC, this.pickupSoundPitch);
            output.putBoolean(TAG_IS_FIXED, this.isFixed());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.setItem(input.read(TAG_ITEM, ItemStack.CODEC).orElse(ItemStack.EMPTY));
        this.setPickupSound(input.read(TAG_PICKUP_SOUND, SoundEvent.CODEC).orElse(Holder.direct(SoundEvents.ITEM_PICKUP)));
        this.setPickupSoundVolume(input.read(TAG_PICKUP_SOUND_VOLUME, FloatProvider.CODEC).orElse(ConstantFloat.of(1.0F)));
        this.setPickupSoundPitch(input.read(TAG_PICKUP_SOUND_PITCH, FloatProvider.CODEC).orElse(ConstantFloat.of(1.0F)));
        this.setFixed(input.getBooleanOr(TAG_IS_FIXED, false));
        if (this.getItem().isEmpty()) {
            this.discard();
        }
    }

    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_ITEM.equals(accessor)) {
            this.getItem().setEntityRepresentation(this);
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Nullable
    @Override
    public SlotAccess getSlot(final int slot) {
        return slot == 0 ? SlotAccess.of(this::getItem, this::setItem) : super.getSlot(slot);
    }

    public void collect(final Player player) {
        if (!this.level().isClientSide()) {
            ItemStack itemStack = this.getItem();
            Item item = itemStack.getItem();
            int orgCount = itemStack.getCount();
            if (player.getInventory().add(itemStack)) {
                player.take(this, orgCount);
                if (itemStack.isEmpty()) {
                    this.discard();
                    itemStack.setCount(orgCount);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), orgCount);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), getPickupSound(), SoundSource.PLAYERS, this.getPickupSoundVolume().sample(this.random), this.getPickupSoundPitch().sample(this.random));
            }
        }
    }
}
