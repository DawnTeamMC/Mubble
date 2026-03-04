package fr.hugman.mubble.entity;

import fr.hugman.mubble.registry.MubbleSounds;
import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * The Bubble entity, shot by a player holding the Bubble Flower power-up.
 * <p>
 * An empty bubble travels forward (with air friction) and slightly floats upward
 * when moving slowly horizontally. It can trap small living entities as passengers.
 * Bubbles rebound off walls, and living entities can stomp them for a vertical boost.
 * Bubbles can also carry an item (as a collectible).
 *
 * @author Copilot
 * @since v4.0.0
 */
public class BubbleEntity extends Entity {
	// =====================
	//   DataTracker keys
	// =====================
	private static final TrackedData<ItemStack> TRAPPED_ITEM =
			DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<Float> BUBBLE_SIZE =
			DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.FLOAT);

	// =====================
	//   Entity tag IDs
	// =====================
	public static final TagKey<EntityType<?>> TAG_ALL =
			TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mubble", "all"));
	public static final TagKey<EntityType<?>> TAG_BUBBLE_CANNOT_TRAP =
			TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mubble", "bubble_cannot_trap"));
	public static final TagKey<EntityType<?>> TAG_BUBBLE_CAN_TRAP =
			TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mubble", "bubble_can_trap"));

	// =====================
	//   Constants
	// =====================
	/** Ticks before an empty bubble naturally pops. (5 s) */
	public static final int MAX_EMPTY_AGE = 100;
	/** Ticks after catching an entity before the bubble naturally pops. (5 s) */
	public static final int MAX_CAUGHT_AGE = 100;
	/** Ticks after spawn during which the bubble cannot pop when touching the owner. */
	public static final int OWNER_IMMUNITY_TICKS = 20;
	/** Radius (blocks) in which an empty bubble is attracted to trappable entities. */
	public static final float ATTRACT_RADIUS = 2.0f;
	/** Horizontal speed below which the bubble floats upward. */
	public static final float FLOAT_THRESHOLD = 0.1f;
	/** Upward acceleration applied when floating. */
	public static final float FLOAT_FORCE = 0.007f;
	/** Per-tick horizontal air friction multiplier. */
	public static final float AIR_FRICTION = 0.99f;
	/** Velocity multiplier on wall rebound. */
	public static final float BOUNCE_FACTOR = 0.65f;
	/** Extra padding (blocks) added to a trapped entity's dimensions for bubble size. */
	public static final float TRAPPED_PADDING = 0.25f;
	/** Default bubble half-size (radius). */
	public static final float DEFAULT_BUBBLE_SIZE = 0.5f;
	/** Minimum velocity magnitude considered "moving" for collision rebound detection. */
	private static final double MIN_VELOCITY_THRESHOLD = 0.01;
	/** Fraction of intended movement below which an axis is considered fully blocked. */
	private static final double COLLISION_MOVEMENT_THRESHOLD = 0.5;

	// =====================
	//   Instance fields
	// =====================
	/** Total ticks this entity has existed. */
	private int lifetime;
	/** World time (in ticks) when an entity was caught; -1 if none caught yet. */
	private int caughtTime = -1;
	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;

	// =====================
	//   Constructor
	// =====================
	public BubbleEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	/** Convenience constructor used when spawning from {@link fr.hugman.mubble.item.BubbleFlowerItem}. */
	public BubbleEntity(World world, LivingEntity owner, Vec3d velocity) {
		this(SuperMario.BUBBLE_ENTITY_TYPE, world);
		this.ownerUUID = owner.getUuid();
		this.cachedOwner = owner;
		// Position bubble slightly in front of owner eyes
		this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
		this.setVelocity(velocity);
	}

	// =====================
	//   Initialisation
	// =====================
	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(TRAPPED_ITEM, ItemStack.EMPTY);
		this.dataTracker.startTracking(BUBBLE_SIZE, DEFAULT_BUBBLE_SIZE);
	}

	// =====================
	//   Dimension / hitbox
	// =====================
	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		float size = this.getBubbleSize() * 2.0f;
		return EntityDimensions.fixed(size, size);
	}

	// =====================
	//   Getters / setters
	// =====================

	public float getBubbleSize() {
		return this.dataTracker.get(BUBBLE_SIZE);
	}

	private void setBubbleSize(float size) {
		this.dataTracker.set(BUBBLE_SIZE, size);
		this.calculateDimensions();
	}

	/** The item stored inside this bubble, or {@link ItemStack#EMPTY}. */
	public ItemStack getTrappedItem() {
		return this.dataTracker.get(TRAPPED_ITEM);
	}

	public void setTrappedItem(ItemStack stack) {
		this.dataTracker.set(TRAPPED_ITEM, stack.copy());
	}

	public boolean hasTrappedItem() {
		return !this.getTrappedItem().isEmpty();
	}

	public boolean hasTrappedEntity() {
		return !this.getPassengerList().isEmpty();
	}

	@Nullable
	public Entity getTrappedEntity() {
		return this.getFirstPassenger();
	}

	@Nullable
	public Entity getOwner() {
		if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
			return this.cachedOwner;
		}
		if (this.ownerUUID != null && this.world instanceof net.minecraft.server.world.ServerWorld sw) {
			this.cachedOwner = sw.getEntity(this.ownerUUID);
			return this.cachedOwner;
		}
		return null;
	}

	// =====================
	//   Passenger helpers
	// =====================
	@Override
	protected void updatePassengerPosition(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			double cx = this.getX();
			double cy = this.getY() + (this.getHeight() - passenger.getHeight()) / 2.0;
			double cz = this.getZ();
			passenger.setPosition(cx, cy, cz);
			// Spin the passenger slowly
			float newYaw = (passenger.getYaw() + 4.0f) % 360.0f;
			passenger.setYaw(newYaw);
			passenger.setHeadYaw(newYaw);
		}
	}

	// =====================
	//   Tick
	// =====================
	@Override
	public void tick() {
		super.tick();
		if (this.world.isClient) {
			tickClient();
			return;
		}
		this.lifetime++;

		if (this.hasTrappedEntity()) {
			tickWithTrappedEntity();
		} else if (this.hasTrappedItem()) {
			tickWithTrappedItem();
		} else {
			tickEmpty();
		}

		tickMovement();
	}

	private void tickClient() {
		// Bubble particles on client
		if (this.age % 10 == 0) {
			this.world.addParticle(ParticleTypes.BUBBLE_POP,
					this.getX() + (this.random.nextDouble() - 0.5) * this.getWidth(),
					this.getY() + this.random.nextDouble() * this.getHeight(),
					this.getZ() + (this.random.nextDouble() - 0.5) * this.getWidth(),
					0, 0, 0);
		}
	}

	private void tickEmpty() {
		// Natural pop after MAX_EMPTY_AGE
		if (this.lifetime > MAX_EMPTY_AGE) {
			pop();
			return;
		}

		// Attract towards trappable entities
		List<Entity> nearby = this.world.getOtherEntities(this, this.getBoundingBox().expand(ATTRACT_RADIUS));
		Entity attractTarget = null;
		double nearestDist = Double.MAX_VALUE;
		for (Entity e : nearby) {
			if (canTrap(e)) {
				double d = this.squaredDistanceTo(e);
				if (d < nearestDist) {
					nearestDist = d;
					attractTarget = e;
				}
			}
		}
		if (attractTarget != null) {
			Vec3d dir = attractTarget.getPos().subtract(this.getPos()).normalize().multiply(0.02);
			this.setVelocity(this.getVelocity().add(dir));
		}

		// Check entity intersections (in priority order)
		List<Entity> colliding = this.world.getOtherEntities(this, this.getBoundingBox().contract(0.05));
		for (Entity e : colliding) {
			if (e == this) continue;

			// 1. Boss / blacklist → pop
			if (e.getType().isIn(TAG_BUBBLE_CANNOT_TRAP)) {
				pop();
				return;
			}
			// 2. Owner (after immunity) → pop
			if (e == getOwner() && this.lifetime > OWNER_IMMUNITY_TICKS) {
				pop();
				return;
			}
			// 3. Mubble entity (super_mario:all) → coin inside bubble
			// NOTE: Gold nugget is used as a placeholder until a dedicated coin item/entity
			// is added to the super_mario module.
			if (e.getType().isIn(TAG_ALL)) {
				ItemStack coin = new ItemStack(Items.GOLD_NUGGET);
				setTrappedItem(coin);
				this.world.playSound(null, this.getX(), this.getY(), this.getZ(),
						MubbleSounds.BUBBLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
				return;
			}
			// 4. Can be trapped → trap it
			if (canTrap(e)) {
				trapEntity(e);
				return;
			}
		}
	}

	private void tickWithTrappedEntity() {
		Entity passenger = getTrappedEntity();
		if (passenger == null || passenger.isRemoved()) {
			pop();
			return;
		}

		// Resize bubble to match trapped entity
		float neededSize = Math.max(passenger.getWidth(), passenger.getHeight()) / 2.0f + TRAPPED_PADDING;
		if (Math.abs(this.getBubbleSize() - neededSize) > 0.01f) {
			setBubbleSize(neededSize);
		}

		// Natural pop after MAX_CAUGHT_AGE
		if (this.caughtTime >= 0 && (this.lifetime - this.caughtTime) > MAX_CAUGHT_AGE) {
			pop();
			return;
		}

		// Check stomp from above
		checkStomped();

		// Any other entity touching the filled bubble → pop (except the passenger)
		List<Entity> touching = this.world.getOtherEntities(this, this.getBoundingBox().contract(0.05));
		for (Entity e : touching) {
			if (e == passenger || e == this) continue;
			pop();
			return;
		}
	}

	private void tickWithTrappedItem() {
		// Check stomp from above
		checkStomped();

		// Any entity touching a bubble with item → give item, pop
		List<Entity> touching = this.world.getOtherEntities(this, this.getBoundingBox().contract(0.05));
		for (Entity e : touching) {
			if (e == this) continue;
			ItemStack item = getTrappedItem();
			if (!item.isEmpty() && e instanceof PlayerEntity player) {
				player.giveItemStack(item.copy());
				setTrappedItem(ItemStack.EMPTY);
			}
			pop();
			return;
		}
	}

	/** Checks whether an entity is stomping (landing on top of) this bubble. */
	private void checkStomped() {
		Box bb = this.getBoundingBox();
		Box topSlice = new Box(bb.minX, bb.maxY - 0.1, bb.minZ, bb.maxX, bb.maxY + 0.4, bb.maxZ);
		List<Entity> above = this.world.getOtherEntities(this, topSlice);
		for (Entity e : above) {
			if (e == this || (hasTrappedEntity() && e == getTrappedEntity())) continue;
			// Entity must be moving downward or just landed
			if (e.getVelocity().y <= 0.05) {
				// Vertical boost for the stomper
				Vec3d vel = e.getVelocity();
				e.setVelocity(vel.x, 0.5, vel.z);
				e.velocityDirty = true;
				e.velocityModified = true;
				this.world.playSound(null, this.getX(), this.getY(), this.getZ(),
						MubbleSounds.BUBBLE_POP, SoundCategory.NEUTRAL, 1.0f, 1.2f);
				if (!this.world.isClient) {
					spawnPopParticles();
					// If filled bubble had item, drop it
					if (hasTrappedItem()) {
						ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(),
								getTrappedItem().copy());
						this.world.spawnEntity(itemEntity);
					}
				}
				this.discard();
				return;
			}
		}
	}

	private void tickMovement() {
		Vec3d vel = this.getVelocity();

		// Air friction (horizontal)
		double nx = vel.x * AIR_FRICTION;
		double nz = vel.z * AIR_FRICTION;
		double ny = vel.y;

		// Upward float when moving slowly horizontally
		double horizontalSpeed = Math.sqrt(nx * nx + nz * nz);
		if (horizontalSpeed < FLOAT_THRESHOLD) {
			ny += FLOAT_FORCE;
		}

		// Small downward gravity so the bubble arcs
		ny -= 0.005;

		this.setVelocity(nx, ny, nz);

		// Record position before move to detect wall hits
		double beforeX = this.getX();
		double beforeY = this.getY();
		double beforeZ = this.getZ();
		Vec3d beforeVel = this.getVelocity();

		this.move(MovementType.SELF, this.getVelocity());

		// Detect blocked axes and rebound
		double movedX = this.getX() - beforeX;
		double movedY = this.getY() - beforeY;
		double movedZ = this.getZ() - beforeZ;

		boolean xBlocked = Math.abs(beforeVel.x) > MIN_VELOCITY_THRESHOLD && Math.abs(movedX) < Math.abs(beforeVel.x) * COLLISION_MOVEMENT_THRESHOLD;
		boolean yBlocked = Math.abs(beforeVel.y) > MIN_VELOCITY_THRESHOLD && Math.abs(movedY) < Math.abs(beforeVel.y) * COLLISION_MOVEMENT_THRESHOLD;
		boolean zBlocked = Math.abs(beforeVel.z) > MIN_VELOCITY_THRESHOLD && Math.abs(movedZ) < Math.abs(beforeVel.z) * COLLISION_MOVEMENT_THRESHOLD;

		if (xBlocked || yBlocked || zBlocked) {
			Vec3d bounced = this.getVelocity();
			double bx = xBlocked ? -beforeVel.x * BOUNCE_FACTOR : bounced.x;
			double by = yBlocked ? -beforeVel.y * BOUNCE_FACTOR : bounced.y;
			double bz = zBlocked ? -beforeVel.z * BOUNCE_FACTOR : bounced.z;
			this.setVelocity(bx, by, bz);

			if (!this.world.isClient) {
				this.world.playSound(null, this.getX(), this.getY(), this.getZ(),
						MubbleSounds.BUBBLE_REBOUND, SoundCategory.NEUTRAL, 0.7f, 1.0f);
			}
		}
	}

	// =====================
	//   Trapping logic
	// =====================
	private boolean canTrap(Entity entity) {
		if (entity.getType().isIn(TAG_BUBBLE_CANNOT_TRAP)) return false;
		if (entity == getOwner()) return false;
		if (entity.getType().isIn(TAG_BUBBLE_CAN_TRAP)) return true;
		if (!(entity instanceof LivingEntity living)) return false;
		// Small entity: less than 2 blocks in each dimension, less than 20 max HP
		return living.getWidth() < 2.0f
				&& living.getHeight() < 2.0f
				&& living.getMaxHealth() < 20.0f;
	}

	private void trapEntity(Entity entity) {
		entity.startRiding(this, true);
		this.caughtTime = this.lifetime;
		this.world.playSound(null, this.getX(), this.getY(), this.getZ(),
				MubbleSounds.BUBBLE_FILL, SoundCategory.NEUTRAL, 1.0f, 0.8f);
	}

	// =====================
	//   Pop
	// =====================
	public void pop() {
		if (this.isRemoved()) return;
		if (!this.world.isClient) {
			this.world.playSound(null, this.getX(), this.getY(), this.getZ(),
					MubbleSounds.BUBBLE_POP, SoundCategory.NEUTRAL, 1.0f, 1.0f);
			spawnPopParticles();
			// If the bubble had an item, drop it
			if (hasTrappedItem()) {
				ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(),
						getTrappedItem().copy());
				this.world.spawnEntity(itemEntity);
			}
		}
		this.discard();
	}

	private void spawnPopParticles() {
		if (this.world instanceof ServerWorld serverWorld) {
			serverWorld.spawnParticles(ParticleTypes.BUBBLE_POP,
					this.getX(), this.getY() + this.getHeight() / 2.0, this.getZ(),
					8, this.getWidth() * 0.5, this.getHeight() * 0.5, this.getWidth() * 0.5, 0.1);
		}
	}

	// =====================
	//   Damage / interaction
	// =====================
	@Override
	public boolean isAttackable() {
		return true;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (this.isRemoved()) return false;
		// Wind charges push but don't pop (they deal no damage per vanilla)
		if (source.isExplosive() && amount <= 0.0f) return false;
		// Projectile hits or explosions pop the bubble
		if (source.isProjectile() || source.isExplosive()) {
			if (!this.world.isClient) {
				this.world.playSound(null, this.getX(), this.getY(), this.getZ(),
						MubbleSounds.BUBBLE_POP, SoundCategory.NEUTRAL, 1.0f, 1.0f);
				spawnPopParticles();
				if (hasTrappedItem()) {
					ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(),
							getTrappedItem().copy());
					this.world.spawnEntity(itemEntity);
				}
			}
			this.discard();
			return true;
		}
		return false;
	}

	// =====================
	//   NBT persistence
	// =====================
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putInt("Lifetime", this.lifetime);
		nbt.putInt("CaughtTime", this.caughtTime);
		if (this.ownerUUID != null) {
			nbt.putUuid("Owner", this.ownerUUID);
		}
		if (hasTrappedItem()) {
			nbt.put("TrappedItem", getTrappedItem().writeNbt(new NbtCompound()));
		}
		nbt.putFloat("BubbleSize", this.getBubbleSize());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		this.lifetime = nbt.getInt("Lifetime");
		this.caughtTime = nbt.getInt("CaughtTime");
		if (nbt.containsUuid("Owner")) {
			this.ownerUUID = nbt.getUuid("Owner");
		}
		if (nbt.contains("TrappedItem")) {
			this.setTrappedItem(ItemStack.fromNbt(nbt.getCompound("TrappedItem")));
		}
		if (nbt.contains("BubbleSize")) {
			this.setBubbleSize(nbt.getFloat("BubbleSize"));
		}
	}
}
