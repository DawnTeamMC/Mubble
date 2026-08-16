package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioItemTags;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.Stompable;
import fr.hugman.mubble.super_mario.world.entity.item.SuperMarioCollectibles;
import fr.hugman.mubble.super_mario.world.level.storage.loot.SuperMarioBuiltInLootTables;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * A bubble shot by the Bubble Flower power-up.
 * <p>
 * It travels forward, drifts upwards as it slows down, rebounds off blocks and can hold a single entity or item
 * inside of it. Whatever is held is a regular passenger, so vanilla takes care of syncing and saving it.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class Bubble extends Projectile implements Stompable {
    /**
     * The looks a bubble can take. Picked at random on spawn and kept for the bubble's whole life, so that a
     * volley does not look stamped out of the same mould. Deliberately a plain index rather than a registry of
     * variants: nothing else varies with it, and it stays settable from a command.
     */
    private static final List<ClientAsset.ResourceTexture> TEXTURES = IntStream.rangeClosed(1, 4)
            .mapToObj(i -> new ClientAsset.ResourceTexture(SuperMario.id("entity/bubble" + i)))
            .toList();
    public static final int TEXTURE_COUNT = TEXTURES.size();

    /** Ticks before an empty bubble pops on its own. */
    public static final int DEFAULT_LIFETIME = 100;
    /** Ticks before a bubble pops after it caught something. */
    public static final int DEFAULT_FILLED_LIFETIME = 100;
    /** Grace period during which the bubble ignores its owner, so it does not pop right where it spawned. */
    public static final int OWNER_POP_DELAY = 20;
    /**
     * Grace period during which the owner cannot stomp their own bubble. Without it, shooting one while airborne
     * hands out a free bounce straight away, which is enough to fly forever.
     */
    public static final int OWNER_STOMP_DELAY = 10;
    /** Ticks the capture animation lasts before the caught entity turns into its loot. */
    public static final int ABSORB_DURATION = 12;
    /** Ticks the squish animation lasts after rebounding against a block. */
    public static final int SQUISH_DURATION = 6;

    public static final double ATTRACT_RADIUS = 2.0;
    public static final float MAX_TRAPPABLE_SIZE = 2.0f;
    /** Zombies and skeletons sit exactly at 20 HP, and the issue lists them as trappable. */
    public static final float MAX_TRAPPABLE_HEALTH = 20.0f;
    public static final float BASE_SIZE = 0.75f;
    public static final float TRAPPED_PADDING = 0.25f;
    public static final float MAX_SIZE = MAX_TRAPPABLE_SIZE + TRAPPED_PADDING;

    private static final double AIR_FRICTION = 0.97;
    /** Horizontal speed above which the bubble does not rise at all. */
    private static final double FLOAT_SPEED_THRESHOLD = 0.25;
    private static final double FLOAT_MAX_UP = 0.04;
    private static final double FLOAT_LERP = 0.12;
    private static final double REBOUND_RESTITUTION = 0.6;
    /** Below this, a blocked component is just a bubble resting against a block, not a rebound worth animating. */
    private static final double MIN_REBOUND_SPEED = 0.01;
    /** How much of the way towards the target the heading turns each tick. */
    private static final double AIM_ASSIST_STRENGTH = 0.09;
    /** Cosine of the half-angle of the cone the target has to be in. Roughly 55 degrees. */
    private static final double AIM_ASSIST_MIN_DOT = 0.57;
    private static final float STOMP_BOOST = 0.7f;
    /** How fast the drawn size catches up with the real one when the bubble swallows or releases something. */
    private static final float SIZE_LERP = 0.4F;

    // Entity events, well above the vanilla range.
    private static final byte EVENT_SQUISH_X = 100;
    private static final byte EVENT_SQUISH_Y = 101;
    private static final byte EVENT_SQUISH_Z = 102;

    private static final String AGE_KEY = "age";
    private static final String FILLED_AGE_KEY = "filled_age";
    private static final String LIFETIME_KEY = "lifetime";
    private static final String FILLED_LIFETIME_KEY = "filled_lifetime";
    private static final String ITEM_KEY = "item";
    private static final String ABSORB_TICKS_KEY = "absorb_ticks";
    private static final String TRAPPED_NO_AI_KEY = "trapped_no_ai";
    private static final String TEXTURE_KEY = "texture";

    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(Bubble.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_ABSORBING = SynchedEntityData.defineId(Bubble.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_TEXTURE = SynchedEntityData.defineId(Bubble.class, EntityDataSerializers.INT);

    private int age;
    private int filledAge;
    private int lifetime = DEFAULT_LIFETIME;
    private int filledLifetime = DEFAULT_FILLED_LIFETIME;
    private int absorbTicks;
    /** Whether the trapped mob already had its AI disabled before it got caught. */
    private boolean trappedNoAi;

    private int squishTicks;
    private int squishTicksO;
    private Direction.Axis squishAxis = Direction.Axis.Y;
    private int absorbClientTicks;
    private int absorbClientTicksO;
    /** Client-side eased size. Negative until the first tick, which is how the initial snap is detected. */
    private float renderSize = -1.0F;
    private float renderSizeO = -1.0F;

    public Bubble(EntityType<? extends Bubble> type, Level level) {
        super(type, level);
        if (!level.isClientSide()) {
            // Rolled here rather than on the first tick so the choice ships with the spawn packet and the
            // bubble never shows up wearing one texture and swapping to another.
            this.setTextureIndex(this.random.nextInt(TEXTURE_COUNT) + 1);
        }
    }

    public Bubble(Level level, LivingEntity owner) {
        this(SuperMarioEntityTypes.BUBBLE, level);
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ITEM, ItemStack.EMPTY);
        builder.define(DATA_ABSORBING, false);
        builder.define(DATA_TEXTURE, 1);
    }

    // ---------------------------------------------------------------- state

    public ItemStack getItem() {
        return this.entityData.get(DATA_ITEM);
    }

    public void setItem(ItemStack stack) {
        this.entityData.set(DATA_ITEM, stack);
    }

    public boolean isAbsorbing() {
        return this.entityData.get(DATA_ABSORBING);
    }

    /**
     * @return the entity held inside the bubble, if any.
     */
    @Nullable
    public Entity getTrappedEntity() {
        return this.getFirstPassenger();
    }

    /**
     * @return whether the bubble holds neither an entity nor an item.
     */
    public boolean isEmpty() {
        return this.getTrappedEntity() == null && this.getItem().isEmpty();
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getFilledLifetime() {
        return this.filledLifetime;
    }

    public void setFilledLifetime(int filledLifetime) {
        this.filledLifetime = filledLifetime;
    }

    // ---------------------------------------------------------------- ticking

    @Override
    public void tick() {
        if (this.firstTick) {
            this.playSound(SuperMarioSounds.BUBBLE_APPEAR.value(), 0.5F, 1.0F);
        }

        super.tick();

        if (!this.level().isClientSide()) {
            this.age++;
            if (this.isEmpty()) {
                this.aimAtNearbyEntities();
            }
        }

        this.tickMovement();

        if (this.level().isClientSide()) {
            this.tickClientAnimations();
            return;
        }

        if (this.isAbsorbing()) {
            this.tickAbsorption();
        } else {
            this.checkEntityCollisions();
        }
        if (this.isRemoved()) {
            return;
        }
        this.tickLifetime();
    }

    private void tickMovement() {
        Vec3 movement = this.getDeltaMovement().scale(AIR_FRICTION);

        // The slower the bubble travels horizontally, the more it drifts upwards.
        double slowness = 1.0 - Math.clamp(movement.horizontalDistance() / FLOAT_SPEED_THRESHOLD, 0.0, 1.0);
        movement = movement.with(Direction.Axis.Y, Mth.lerp(FLOAT_LERP, movement.y(), FLOAT_MAX_UP * slowness));

        this.setDeltaMovement(movement);
        this.move(MoverType.SELF, movement);
        // move() only records which blocks were crossed; this is what actually runs their "entity inside"
        // behaviour. Without it the bubble ignores portals, pressure plates and every other trigger block.
        this.applyEffectsFromBlocks();
        this.reboundOffBlocks(movement);
        this.needsSync = true;
    }

    /**
     * {@link Entity#move} zeroes out whichever component ran into a block, which is how the rebound axis is found.
     */
    private void reboundOffBlocks(Vec3 requested) {
        if (!this.horizontalCollision && !this.verticalCollision) {
            return;
        }
        Vec3 actual = this.getDeltaMovement();
        Direction.Axis axis = null;
        double x = actual.x();
        double y = actual.y();
        double z = actual.z();

        // Anything slower than MIN_REBOUND_SPEED is a bubble idling against a block — bouncing it would
        // otherwise replay the rebound sound every single tick while it floats under a ceiling.
        if (blocked(requested.x(), actual.x())) {
            x = -requested.x() * REBOUND_RESTITUTION;
            axis = Direction.Axis.X;
        }
        if (blocked(requested.z(), actual.z())) {
            z = -requested.z() * REBOUND_RESTITUTION;
            axis = Direction.Axis.Z;
        }
        if (blocked(requested.y(), actual.y())) {
            y = -requested.y() * REBOUND_RESTITUTION;
            axis = Direction.Axis.Y;
        }
        if (axis == null) {
            return;
        }

        this.setDeltaMovement(x, y, z);
        if (!this.level().isClientSide()) {
            // No sound on rebound: bubbles bounce often enough that it turns into noise. The squish carries it.
            this.level().broadcastEntityEvent(this, squishEvent(axis));
            this.notifyBlockHit(axis, switch (axis) {
                case X -> requested.x();
                case Y -> requested.y();
                case Z -> requested.z();
            });
        }
    }

    /**
     * Lets the block the bubble bounced against react as it would to any thrown projectile. Target blocks, and
     * anything else keyed on projectile hits, come from here.
     */
    private void notifyBlockHit(Direction.Axis axis, double requestedOnAxis) {
        Direction direction = Direction.get(requestedOnAxis > 0.0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE, axis);
        Vec3 from = this.getBoundingBox().getCenter();
        Vec3 to = from.add(Vec3.atLowerCornerOf(direction.getUnitVec3i()).scale(this.getBbWidth() / 2.0 + 0.25));
        BlockHitResult hit = this.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hit.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockState state = this.level().getBlockState(hit.getBlockPos());
        state.onProjectileHit(this.level(), state, hit, this);
    }

    private static boolean blocked(double requested, double actual) {
        return Math.abs(requested) > MIN_REBOUND_SPEED && Math.abs(actual) < 1.0E-7;
    }

    private void tickLifetime() {
        if (this.isEmpty()) {
            if (this.lifetime >= 0 && this.age >= this.lifetime) {
                this.pop();
            }
            return;
        }
        this.filledAge++;
        if (this.filledLifetime >= 0 && this.filledAge >= this.filledLifetime) {
            this.pop();
        }
    }

    /**
     * Nudges the bubble towards a nearby trappable entity. This steers the heading and leaves the speed alone, so
     * it reads as an aim assist on a near miss rather than as a bubble homing in on whatever walks past.
     */
    private void aimAtNearbyEntities() {
        Vec3 velocity = this.getDeltaMovement();
        double speed = velocity.length();
        if (speed < 1.0E-4) {
            return;
        }
        Entity owner = this.getOwner();
        Vec3 heading = velocity.scale(1.0 / speed);
        Vec3 center = this.getBoundingBox().getCenter();

        Vec3 bestDirection = null;
        double bestDistance = Double.MAX_VALUE;
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(ATTRACT_RADIUS), e -> e != owner && this.canTrap(e))) {
            Vec3 direction = entity.getBoundingBox().getCenter().subtract(center).normalize();
            // Only assist towards what the bubble was already flying at.
            if (heading.dot(direction) < AIM_ASSIST_MIN_DOT) {
                continue;
            }
            double distance = this.distanceToSqr(entity);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestDirection = direction;
            }
        }
        if (bestDirection == null) {
            return;
        }
        this.setDeltaMovement(heading.lerp(bestDirection, AIM_ASSIST_STRENGTH).normalize().scale(speed));
    }

    // ---------------------------------------------------------------- interactions

    /**
     * Checks entities overlapping the bubble, in the priority order defined by the Bubble Flower design.
     */
    private void checkEntityCollisions() {
        // A bubble carrying an entity is not fragile: it keeps flying and goes out on its own timer, on a stomp,
        // or when something damages it. Otherwise it bursts the moment it drifts past anything.
        if (this.getTrappedEntity() != null) {
            return;
        }
        Entity owner = this.getOwner();
        boolean holdsItem = !this.getItem().isEmpty();
        List<Entity> entities = this.level().getEntities(this, this.getBoundingBox(), entity -> !entity.isSpectator() && entity.isAlive());

        for (Entity entity : entities) {
            // Bubbles pass through one another: they are in the module tag, but popping or swallowing each other
            // would mean a volley destroys itself.
            if (entity instanceof Bubble) {
                continue;
            }
            // A bubble holding an item is a collectible: whoever touches it takes what is inside.
            if (holdsItem) {
                if (entity instanceof Player player) {
                    this.collectItem(player);
                } else {
                    this.pop();
                }
                return;
            }
            // The owner is checked before the blacklist: players are blacklisted, but the owner still needs its
            // grace period, otherwise every bubble would pop in its shooter's face on the very first tick.
            if (entity == owner) {
                if (this.age < OWNER_POP_DELAY) {
                    continue;
                }
                this.pop();
                return;
            }
            if (entity.is(SuperMarioEntityTypeTags.BUBBLE_CANNOT_TRAP)) {
                this.pop();
                return;
            }
            if (entity.is(SuperMarioEntityTypeTags.ALL)) {
                this.absorb(entity);
                return;
            }
            if (this.canTrap(entity)) {
                this.trap(entity);
                return;
            }
        }
    }

    /**
     * @return whether the bubble is allowed to hold the given entity.
     */
    public boolean canTrap(Entity entity) {
        if (entity == this || entity.isRemoved() || !entity.isAlive() || entity.isSpectator()) {
            return false;
        }
        if (entity.isPassenger() || entity.isVehicle()) {
            return false;
        }
        if (entity.is(SuperMarioEntityTypeTags.BUBBLE_CANNOT_TRAP)) {
            return false;
        }
        if (entity.is(SuperMarioEntityTypeTags.BUBBLE_CAN_TRAP)) {
            return true;
        }
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        // Health is read from the base attribute, not from getMaxHealth(): a leader zombie carries a
        // "leader_zombie_bonus" modifier that can take it past 90 HP, but it is still an ordinary zombie as far
        // as a bubble is concerned. Size stays as measured, since fitting inside really is a physical limit.
        return entity.getBbWidth() < MAX_TRAPPABLE_SIZE
                && entity.getBbHeight() < MAX_TRAPPABLE_SIZE
                && living.getAttributeBaseValue(Attributes.MAX_HEALTH) <= MAX_TRAPPABLE_HEALTH;
    }

    /**
     * Catches a living entity: it stops moving and attacking until the bubble pops.
     */
    public void trap(Entity entity) {
        if (this.level().isClientSide() || !this.hold(entity)) {
            return;
        }
        this.filledAge = 0;
        this.playSound(SuperMarioSounds.BUBBLE_FILL.value(), 0.5F, 1.0F);
    }

    /**
     * Swallows a Super Mario entity: it spins and shrinks away, then gets replaced by its capture loot.
     */
    public void absorb(Entity entity) {
        if (this.level().isClientSide() || !this.hold(entity)) {
            return;
        }
        this.filledAge = 0;
        this.absorbTicks = ABSORB_DURATION;
        this.entityData.set(DATA_ABSORBING, true);
        this.playSound(SuperMarioSounds.BUBBLE_FILL.value(), 0.5F, 1.0F);
    }

    private boolean hold(Entity entity) {
        this.trappedNoAi = entity instanceof Mob mob && mob.isNoAi();
        // Forced, so that mobs which normally refuse to ride anything still get caught.
        if (!entity.startRiding(this, true, true)) {
            return false;
        }
        if (entity instanceof Mob mob) {
            mob.setNoAi(true);
        }
        return true;
    }

    private void tickAbsorption() {
        if (--this.absorbTicks > 0) {
            return;
        }
        this.finishAbsorption();
    }

    /**
     * Turns whatever is being swallowed into its capture loot right away, without waiting for the animation.
     */
    private void finishAbsorption() {
        Entity absorbed = this.getTrappedEntity();
        this.entityData.set(DATA_ABSORBING, false);
        this.absorbTicks = 0;
        // A dying entity stays aboard for the length of its death animation, which outlasts the swallow. If it
        // was killed on the way, there is nothing left in there to turn into loot.
        if (absorbed == null || !absorbed.isAlive()) {
            return;
        }
        ItemStack loot = this.level() instanceof ServerLevel serverLevel ? this.rollCaptureLoot(serverLevel, absorbed) : ItemStack.EMPTY;
        absorbed.stopRiding();
        absorbed.discard();
        this.setItem(loot);
        this.refreshDimensions();
    }

    private ItemStack rollCaptureLoot(ServerLevel level, Entity absorbed) {
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, absorbed)
                .withParameter(LootContextParams.ORIGIN, absorbed.position())
                .create(LootContextParamSets.GIFT);
        var items = level.getServer().reloadableRegistries().getLootTable(SuperMarioBuiltInLootTables.BUBBLE_CAPTURE).getRandomItems(params);
        return items.isEmpty() ? ItemStack.EMPTY : items.getFirst();
    }

    /**
     * Hands the item held inside over to a player, the same way a collectible would.
     */
    private void collectItem(Player player) {
        // Copied, because the inventory shrinks the stack it is handed and the synced one must not be touched.
        ItemStack stack = this.getItem().copy();
        int count = stack.getCount();
        if (player.getInventory().add(stack)) {
            player.take(this, count - stack.getCount());
            SuperMarioCollectibles.collectSound().play(this.random, this.level(), this.getX(), this.getY(), this.getZ(), SoundSource.PLAYERS);
        }
        // Whatever did not fit stays inside and gets spawned back by pop().
        this.setItem(stack);
        this.pop();
    }

    public void pop() {
        if (this.level().isClientSide() || this.isRemoved()) {
            return;
        }
        // Popping a bubble mid-swallow does not rescue the enemy, it just finishes the job early.
        if (this.isAbsorbing()) {
            this.finishAbsorption();
        }
        Entity trapped = this.getTrappedEntity();
        if (trapped != null) {
            trapped.stopRiding();
        }
        this.dropItem();
        this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
        this.playSound(SuperMarioSounds.BUBBLE_POP.value(), 0.5F, 1.0F);
        this.discard();
    }

    /**
     * Spawns whatever the bubble was holding. Items tagged as collectibles come back as such, so that a coin
     * bubble popped by a stray arrow still leaves a coin behind rather than a plain item stack.
     */
    private void dropItem() {
        ItemStack stack = this.getItem();
        if (stack.isEmpty()) {
            return;
        }
        this.setItem(ItemStack.EMPTY);
        Vec3 center = this.getBoundingBox().getCenter();

        if (stack.is(SuperMarioItemTags.SPAWNS_AS_COLLECTIBLE)) {
            CollectibleEntity collectible = new CollectibleEntity(this.level(), center.x(), center.y(), center.z(), stack);
            SuperMarioCollectibles.configure(collectible, stack);
            collectible.setFixed(false);
            collectible.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            this.level().addFreshEntity(collectible);
            return;
        }
        ItemEntity item = new ItemEntity(this.level(), center.x(), center.y(), center.z(), stack);
        item.setDeltaMovement(this.getDeltaMovement().scale(0.5));
        this.level().addFreshEntity(item);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isRemoved()) {
            return false;
        }
        // No health, like paintings: anything that lands a real hit pops it, whether that is an arrow, a sword
        // or a bare fist. Wind charges deal no damage, so they only blow the bubble around.
        if (amount <= 0.0F) {
            return false;
        }
        this.pop();
        return true;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Block rebounds are handled from the actual movement in reboundOffBlocks().
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // Entity interactions are handled from the bounding box in checkEntityCollisions().
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return false;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    // ---------------------------------------------------------------- passengers

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        // Runs on both sides, which keeps the client bounding box (and therefore the sprite size) in sync.
        this.refreshDimensions();
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        // Every way out of a bubble goes through here — popping, being discarded, or the entity dismounting on
        // its own (mobs tagged DISMOUNTS_UNDERWATER do that). Restoring the AI anywhere else leaves mobs frozen.
        if (!this.level().isClientSide() && passenger instanceof Mob mob) {
            mob.setNoAi(this.trappedNoAi);
        }
        this.refreshDimensions();
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        // The passenger's vehicle attachment point is deliberately skipped. Entity types declare it so riders sit
        // properly on boats and horses -- zombies use ridingOffset(-0.7), which vanilla then subtracts, dropping
        // them 0.7 blocks below where they were put. A bubble centres what it holds, whatever the type asks for.
        Vec3 pos = this.getPassengerRidingPosition(passenger);
        callback.accept(passenger, pos.x(), pos.y(), pos.z());
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        // Derived from the passenger rather than from getBbHeight(): the cached dimensions are only refreshed
        // once the passenger list has been applied, and reading them too early drops the rider under the bubble.
        return new Vec3(this.getX(), this.getY() + (sizeFor(passenger) - passenger.getBbHeight()) / 2.0, this.getZ());
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        // Always a cube, whatever is inside.
        float size = sizeFor(this.getFirstPassenger());
        return EntityDimensions.fixed(size, size);
    }

    /**
     * @return the side of the bubble's cube. Read straight from what it holds rather than from the cached
     * dimensions, so the sprite can never be drawn a tick behind the entity it contains.
     */
    public float getSize() {
        return sizeFor(this.getFirstPassenger());
    }

    /**
     * @return the side of the bubble's cube, always a little bigger than what it holds.
     */
    private static float sizeFor(@Nullable Entity trapped) {
        if (trapped == null) {
            return BASE_SIZE;
        }
        return Math.clamp(Math.max(trapped.getBbWidth(), trapped.getBbHeight()) + TRAPPED_PADDING, BASE_SIZE, MAX_SIZE);
    }

    // ---------------------------------------------------------------- stomping

    @Override
    public boolean canBeStomped() {
        return this.isAlive();
    }

    @Override
    public AABB getStompBox() {
        AABB box = this.getBoundingBox();
        return box.setMinY(box.maxY - 0.2D * box.getYsize()).setMaxY(box.maxY + 0.5D);
    }

    @Override
    public Predicate<? super Entity> getStompableBy() {
        Entity trapped = this.getTrappedEntity();
        Entity owner = this.getOwner();
        return EntitySelector.NO_SPECTATORS.and(entity -> entity != trapped
                // Bubbles drift into each other constantly; they must not bounce off one another.
                && !(entity instanceof Bubble)
                && (entity != owner || this.age >= OWNER_STOMP_DELAY)
                && entity.isAlive()
                && !entity.onGround()
                && entity.getDeltaMovement().y() < 0.0D);
    }

    @Override
    public void onStompedBy(Entity entity) {
        if (!(this.level() instanceof ServerLevel)) {
            return;
        }
        // A player's real momentum only lives on their client; the server-side delta is stale, and sending it
        // back would kill the horizontal speed they came in with. getKnownMovement() is what the client reported.
        Vec3 momentum = entity.getKnownMovement();
        entity.setDeltaMovement(momentum.x(), STOMP_BOOST, momentum.z());
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
        entity.fallDistance = 0.0F;
        this.pop();
    }

    // ---------------------------------------------------------------- client animations

    private static byte squishEvent(Direction.Axis axis) {
        return switch (axis) {
            case X -> EVENT_SQUISH_X;
            case Y -> EVENT_SQUISH_Y;
            case Z -> EVENT_SQUISH_Z;
        };
    }

    private void tickClientAnimations() {
        this.tickRenderSize();

        this.squishTicksO = this.squishTicks;
        if (this.squishTicks > 0) {
            this.squishTicks--;
        }

        this.absorbClientTicksO = this.absorbClientTicks;
        if (this.isAbsorbing()) {
            this.absorbClientTicks = Math.min(this.absorbClientTicks + 1, ABSORB_DURATION);
        } else {
            this.absorbClientTicks = 0;
            this.absorbClientTicksO = 0;
        }
    }

    private void tickRenderSize() {
        float target = this.getSize();
        // First tick snaps: a bubble loading in already holding something must not inflate from nothing.
        if (this.renderSize < 0.0F) {
            this.renderSize = target;
            this.renderSizeO = target;
            return;
        }
        this.renderSizeO = this.renderSize;
        this.renderSize = Mth.lerp(SIZE_LERP, this.renderSize, target);
    }

    /**
     * @return the size to draw the bubble at, eased so that swallowing something grows it smoothly. The hitbox
     * itself still changes in one step, since that side is gameplay.
     */
    public float getRenderSize(float partialTicks) {
        if (this.renderSize < 0.0F) {
            return this.getSize();
        }
        return Mth.lerp(partialTicks, this.renderSizeO, this.renderSize);
    }

    /**
     * @return how far along the capture animation is, from 0 (untouched) to 1 (fully swallowed).
     */
    public float getAbsorbProgress(float partialTicks) {
        return Math.clamp(Mth.lerp(partialTicks, this.absorbClientTicksO, this.absorbClientTicks) / ABSORB_DURATION, 0.0F, 1.0F);
    }

    /**
     * @return how squished the bubble currently is, from 0 (round) to 1 (fully flattened).
     */
    public float getSquish(float partialTicks) {
        float ticks = Mth.lerp(partialTicks, this.squishTicksO, this.squishTicks);
        if (ticks <= 0.0F) {
            return 0.0F;
        }
        // Fully squished on impact, easing back to round.
        return Mth.sin((ticks / SQUISH_DURATION) * (Mth.PI / 2.0F));
    }

    public Direction.Axis getSquishAxis() {
        return this.squishAxis;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleEntityEvent(byte state) {
        switch (state) {
            case EVENT_SQUISH_X -> this.startSquish(Direction.Axis.X);
            case EVENT_SQUISH_Y -> this.startSquish(Direction.Axis.Y);
            case EVENT_SQUISH_Z -> this.startSquish(Direction.Axis.Z);
            case EntityEvent.DEATH -> this.spawnPopParticles();
            default -> super.handleEntityEvent(state);
        }
    }

    @Environment(EnvType.CLIENT)
    private void startSquish(Direction.Axis axis) {
        this.squishAxis = axis;
        this.squishTicks = SQUISH_DURATION;
        this.squishTicksO = SQUISH_DURATION;
    }

    @Environment(EnvType.CLIENT)
    private void spawnPopParticles() {
        double radius = this.getBbWidth() / 2.0;
        Vec3 center = this.getBoundingBox().getCenter();
        for (int i = 0; i < 10; i++) {
            this.level().addParticle(
                    ParticleTypes.BUBBLE_POP,
                    center.x() + (this.random.nextDouble() - 0.5) * radius * 2.0,
                    center.y() + (this.random.nextDouble() - 0.5) * radius * 2.0,
                    center.z() + (this.random.nextDouble() - 0.5) * radius * 2.0,
                    (this.random.nextDouble() - 0.5) * 0.1,
                    (this.random.nextDouble() - 0.5) * 0.1,
                    (this.random.nextDouble() - 0.5) * 0.1
            );
        }
    }

    /**
     * @return which of the bubble looks this one wears, from 1 to {@link #TEXTURE_COUNT}.
     */
    public int getTextureIndex() {
        return this.entityData.get(DATA_TEXTURE);
    }

    public void setTextureIndex(int index) {
        this.entityData.set(DATA_TEXTURE, Math.clamp(index, 1, TEXTURE_COUNT));
    }

    public ClientAsset.ResourceTexture getTexture() {
        return TEXTURES.get(this.getTextureIndex() - 1);
    }

    // ---------------------------------------------------------------- serialization

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.age = input.getIntOr(AGE_KEY, 0);
        this.filledAge = input.getIntOr(FILLED_AGE_KEY, 0);
        this.lifetime = input.getIntOr(LIFETIME_KEY, DEFAULT_LIFETIME);
        this.filledLifetime = input.getIntOr(FILLED_LIFETIME_KEY, DEFAULT_FILLED_LIFETIME);
        this.absorbTicks = input.getIntOr(ABSORB_TICKS_KEY, 0);
        this.trappedNoAi = input.getBooleanOr(TRAPPED_NO_AI_KEY, false);
        this.setItem(input.read(ITEM_KEY, ItemStack.CODEC).orElse(ItemStack.EMPTY));
        // Falls back to whatever was rolled on construction, so a bubble summoned without the tag still varies.
        this.setTextureIndex(input.getIntOr(TEXTURE_KEY, this.getTextureIndex()));
        this.entityData.set(DATA_ABSORBING, this.absorbTicks > 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(AGE_KEY, this.age);
        output.putInt(FILLED_AGE_KEY, this.filledAge);
        output.putInt(LIFETIME_KEY, this.lifetime);
        output.putInt(FILLED_LIFETIME_KEY, this.filledLifetime);
        output.putInt(ABSORB_TICKS_KEY, this.absorbTicks);
        output.putBoolean(TRAPPED_NO_AI_KEY, this.trappedNoAi);
        output.putInt(TEXTURE_KEY, this.getTextureIndex());
        if (!this.getItem().isEmpty()) {
            output.store(ITEM_KEY, ItemStack.CODEC, this.getItem());
        }
    }
}
