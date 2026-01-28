package fr.hugman.mubble.world.entity.item.collectible;

import fr.hugman.mubble.network.protocol.common.custom.CollectCollectiblePayload;
import fr.hugman.mubble.sounds.SoundConfig;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.level.GoldenServerExplosion;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CollectibleEntity extends Entity {
    private static final String TAG_ITEM = "item";
    private static final String TAG_COLLECT_SOUND = "collect_sound";
    private static final String TAG_BOUNCE_SOUND = "bounce_sound";
    private static final String TAG_IS_FIXED = "is_fixed";
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(CollectibleEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_IS_FIXED = SynchedEntityData.defineId(CollectibleEntity.class, EntityDataSerializers.BOOLEAN);

    protected SoundConfig collectSound;
    protected SoundConfig bounceSound;

    private float clientXRot;
    private float clientXRotO;
    private float clientYRot;
    private float clientYRotO;
    private float clientZRot;
    private float clientZRotO;

    public CollectibleEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public CollectibleEntity(Level level, double x, double y, double z, ItemStack stack) {
        this(MubbleEntityTypes.COLLECTIBLE, level);
        this.setPos(x, y, z);
        this.setItem(stack);
        this.setFixed(true);
    }

    @Nullable
    public static Vec3 placePos(Level level, BlockPos blockPos) {
        var dimensions = MubbleEntityTypes.COLLECTIBLE.getDimensions();
        Vec3 pos = Vec3.atBottomCenterOf(blockPos).add(0, Math.clamp((1 - dimensions.height()) / 2, 0.0f, 0.5f), 0);
        AABB box = dimensions.makeBoundingBox(pos.x(), pos.y(), pos.z());
        if (level.noCollision(null, box) && level.getEntities(null, box).isEmpty()) {
            return pos;
        }
        return null;
    }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public void setItem(final ItemStack itemStack) {
        this.getEntityData().set(DATA_ITEM, itemStack);
    }

    @Nullable
    public SoundConfig getCollectSound() {
        return collectSound;
    }

    public void setCollectSound(@Nullable SoundConfig collectSound) {
        this.collectSound = collectSound;
    }

    @Nullable
    public SoundConfig getBounceSound() {
        return bounceSound;
    }

    public void setBounceSound(@Nullable SoundConfig bounceSound) {
        this.bounceSound = bounceSound;
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

    public float getClientXRot() {
        return clientXRot;
    }

    public float getClientYRot() {
        return clientYRot;
    }

    public float getClientZRot() {
        return this.clientZRot;
    }

    public void setClientXRot(float rot) {
        if (!Float.isFinite(rot)) {
            Util.logAndPauseIfInIde("Invalid entity rotation: " + rot + ", discarding.");
        } else {
            this.clientXRot = rot  % 360.0F;
        }
    }

    public void setClientYRot(float rot) {
        if (!Float.isFinite(rot)) {
            Util.logAndPauseIfInIde("Invalid entity rotation: " + rot + ", discarding.");
        } else {
            this.clientYRot = rot  % 360.0F;
        }
    }

    public void setClientZRot(final float rot) {
        if (!Float.isFinite(rot)) {
            Util.logAndPauseIfInIde("Invalid entity rotation: " + rot + ", discarding.");
        } else {
            this.clientZRot = rot  % 360.0F;
        }
    }

    public float getClientXRot(final float partialTicks) {
        return partialTicks == 1.0F ? this.getClientXRot() : Mth.rotLerp(partialTicks, this.clientXRotO, this.getClientXRot());
    }

    public float getClientYRot(final float partialTicks) {
        return partialTicks == 1.0F ? this.getClientYRot() : Mth.rotLerp(partialTicks, this.clientYRotO, this.getClientYRot());
    }

    public float getClientZRot(final float partialTicks) {
        return partialTicks == 1.0F ? this.getClientZRot() : Mth.rotLerp(partialTicks, this.clientZRotO, this.getClientZRot());
    }

    public void updateClientRotation() {
        this.clientXRotO = this.getClientXRot();
        this.clientYRotO = this.getClientYRot();
        this.clientZRotO = this.getClientZRot();

        var rotateSpeed = 10;
        var freeRotateSpeed = 20;

        this.setClientYRot(this.getClientYRot() + rotateSpeed);
        var movement = this.getDeltaMovement();
        var speed = movement.lengthSqr();
        if(this.isFixed() || this.onGround() || speed < 0.001) {
            this.setClientXRot(Mth.rotLerp(0.2f, this.getClientXRot(), 0.0f));
            this.setClientZRot(Mth.rotLerp(0.2f, this.getClientZRot(), 0.0f));
        }
        else {
            this.setClientXRot((this.getClientXRot() + (float) (movement.length() * freeRotateSpeed)));
            this.setClientZRot((this.getClientZRot() + (float) (movement.length() * freeRotateSpeed * 1.3)));
        }
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
                            if(!this.level().isClientSide()) {
                                Vec3 movement = this.getDeltaMovement().add(0.0D, y, 0.0D);
                                this.setDeltaMovement(movement);
                                this.setOnGround(false);
                                if(this.getBounceSound() != null) {
                                    this.getBounceSound().volume(ConstantFloat.of(Math.min(1.0f, (float) y)));
                                    this.getBounceSound().play(this.random, this.level(), this.getX(), this.getY(), this.getZ(), this.getSoundSource());
                                }
                            }
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

        if(this.level().isClientSide()) {
            updateClientRotation();
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
        if(explosion instanceof GoldenServerExplosion) {
            //TODO: make this more dynamic...
            return true;
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
            output.storeNullable(TAG_COLLECT_SOUND, SoundConfig.CODEC, this.getCollectSound());
            output.storeNullable(TAG_BOUNCE_SOUND, SoundConfig.CODEC, this.getBounceSound());
            output.putBoolean(TAG_IS_FIXED, this.isFixed());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.setItem(input.read(TAG_ITEM, ItemStack.CODEC).orElse(ItemStack.EMPTY));
        this.setCollectSound(input.read(TAG_COLLECT_SOUND, SoundConfig.CODEC).orElse(new SoundConfig(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F)));
        this.setBounceSound(input.read(TAG_BOUNCE_SOUND, SoundConfig.CODEC).orElse(null));
        this.setFixed(input.getBooleanOr(TAG_IS_FIXED, true));
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
                if (!this.isRemoved()) {
                    for(var tracking : PlayerLookup.tracking(this)) {
                        ServerPlayNetworking.send(tracking, new CollectCollectiblePayload(this.getId(), orgCount));
                    }
                }
                if (itemStack.isEmpty()) {
                    this.discard();
                    itemStack.setCount(orgCount);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), orgCount);
                if(this.getCollectSound() != null) {
                    this.getCollectSound().play(this.random, this.level(), this.getX(), this.getY(), this.getZ(), SoundSource.PLAYERS);
                }
            }
        }
    }
}
