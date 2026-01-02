package fr.hugman.mubble.world.entity.item;

import fr.hugman.mubble.world.entity.MubbleEntityTypes;
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
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class CollectibleEntity extends Entity {
	private static final String ITEM_TAG = "item";
	private static final String PICKUP_SOUND_TAG = "pickup_sound";
	private static final String PICKUP_SOUND_VOLUME_TAG = "pickup_sound_volume";
	private static final String PICKUP_SOUND_PITCH_TAG = "pickup_sound_pitch";
	private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(CollectibleEntity.class, EntityDataSerializers.ITEM_STACK);

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
	public void tick() {
		if (this.getItem().isEmpty()) {
			this.discard();
			return;
		}

		super.tick();
	}

	@Override
	public boolean ignoreExplosion(final Explosion explosion) {
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
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		if (!this.getItem().isEmpty()) {
			output.store(ITEM_TAG, ItemStack.CODEC, this.getItem());
			output.store(PICKUP_SOUND_TAG, SoundEvent.CODEC, this.pickupSound);
			output.store(PICKUP_SOUND_VOLUME_TAG, FloatProvider.CODEC, this.pickupSoundVolume);
			output.store(PICKUP_SOUND_PITCH_TAG, FloatProvider.CODEC, this.pickupSoundPitch);
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setItem(input.read(ITEM_TAG, ItemStack.CODEC).orElse(ItemStack.EMPTY));
		this.setPickupSound(input.read(PICKUP_SOUND_TAG,SoundEvent.CODEC).orElse(Holder.direct(SoundEvents.ITEM_PICKUP)));
		this.setPickupSoundVolume(input.read(PICKUP_SOUND_VOLUME_TAG, FloatProvider.CODEC).orElse(ConstantFloat.of(1.0F)));
		this.setPickupSoundPitch(input.read(PICKUP_SOUND_PITCH_TAG, FloatProvider.CODEC).orElse(ConstantFloat.of(1.0F)));
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
