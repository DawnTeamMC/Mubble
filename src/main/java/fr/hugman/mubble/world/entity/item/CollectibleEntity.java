package fr.hugman.mubble.world.entity.item;

import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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
	private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(CollectibleEntity.class, EntityDataSerializers.ITEM_STACK);

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
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setItem(input.read(ITEM_TAG, ItemStack.CODEC).orElse(ItemStack.EMPTY));
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

	//TODO: make a custom touch in player#aistep
	@Override
	public void playerTouch(final Player player) {
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
			}
		}
	}
}
