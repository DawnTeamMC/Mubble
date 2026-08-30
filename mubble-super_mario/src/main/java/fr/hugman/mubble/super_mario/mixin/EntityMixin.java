package fr.hugman.mubble.super_mario.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioPowerUpTags;
import fr.hugman.mubble.super_mario.world.entity.FallGraced;
import fr.hugman.mubble.super_mario.world.entity.Stompable;
import fr.hugman.mubble.super_mario.world.level.block.HittableBlock;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Entity.class)
public class EntityMixin implements Stompable, FallGraced {
	@Unique
	private double super_mario$fallGrace;
	@Unique
	private boolean super_mario$fallGraceArmed;

	/**
	 * Bumps whatever the entity hits with the top of its hitbox, right before it is moved there.
	 * <p>
	 * The movement is captured from {@code move()} rather than collided for a second time: {@code collide()}
	 * sweeps every block and entity shape along the way, which makes it the most expensive part of moving an
	 * entity, and calling it again here doubled that cost for every entity moving in the world.
	 *
	 * @param delta    the movement that was requested, which collisions may have cut short
	 * @param movement the movement {@code move()} resolved out of {@code delta} and is about to apply. It is
	 *                 the second {@link Vec3} local at this point, {@code delta} itself being the first.
	 */
	@Inject(method="move", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/Entity;setPos(Lnet/minecraft/world/phys/Vec3;)V", ordinal=0))
	private void mubble$move(MoverType type, Vec3 delta, CallbackInfo ci, @Local(ordinal = 1) Vec3 movement) {
		if (movement.y() <= 0) {
			return;
		}
		Entity this_ = (Entity) (Object) this;
		Level level = this_.level();
		Vec3 headPos = this_.position().add(0, this_.getBbHeight(), 0);
		BlockHitResult hit = level.clip(new ClipContext(headPos, headPos.add(movement).add(0, HittableBlock.HIT_Y_OFFSET, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this_));
		if (hit.getType() == HitResult.Type.BLOCK && hit.getDirection() == Direction.DOWN) {
			BlockPos blockPos = hit.getBlockPos();
			BlockState state = level.getBlockState(blockPos);
			if (state.getBlock() instanceof HittableBlock hittableBlock) {
				hittableBlock.onHit(level, state, this_, hit);
			}
		}
	}

	@Inject(method="tick", at=@At("HEAD"))
	private void mubble$tick(CallbackInfo ci) {
		Entity this_ = (Entity) (Object) this;
		this.super_mario$tickFallGrace(this_);
		if (this.canBeStomped()) {
			AABB hitBox = this.getStompBox();
			if (hitBox != null) {
				List<Entity> list = this_.level().getEntities(this_, hitBox, this.getStompableBy());
				if (! list.isEmpty()) {
					this.onStompedBy(list.getFirst());
				}
			}
		}
	}

	@Override
	public void grantFallGrace(double blocks) {
		this.super_mario$fallGrace = Math.max(this.super_mario$fallGrace, blocks);
		this.super_mario$fallGraceArmed = false;
	}

	/**
	 * Spends granted fall grace on the way down.
	 * <p>
	 * It waits for the launch to show up in the entity's movement first: a player's fall distance is reset by
	 * the server on every movement packet that gains height, so a discount written at the moment of the bounce
	 * is wiped before the fall even starts. Once the entity is heading down again nothing resets it any more,
	 * which is where the grace can safely be taken off.
	 */
	@Unique
	private void super_mario$tickFallGrace(Entity entity) {
		if (this.super_mario$fallGrace <= 0.0) {
			return;
		}
		if (entity.onGround()) {
			// Back on the ground without ever falling: the grace does not carry over to a later fall.
			this.super_mario$fallGrace = 0.0;
			return;
		}
		// getKnownMovement() rather than the delta, which is stale for players.
		double climb = entity.getKnownMovement().y();
		if (!this.super_mario$fallGraceArmed) {
			this.super_mario$fallGraceArmed = climb > 0.0;
			return;
		}
		if (climb < 0.0 && entity.fallDistance > 0.0) {
			entity.fallDistance -= this.super_mario$fallGrace;
			this.super_mario$fallGrace = 0.0;
		}
	}

	@Override
	public boolean canBeStomped() {
		var this_ = ((Entity) (Object) this);
		return this_.is(SuperMarioEntityTypeTags.STOMPABLE) && ! this_.isSpectator() && ! this_.isVehicle() && this_.isAlive();
	}

	@Override
	public AABB getStompBox() {
		var this_ = ((Entity) (Object) this);
		AABB hitBox = this_.getBoundingBox();
		hitBox = hitBox.setMinY(hitBox.maxY - (0.2D * (hitBox.maxY - hitBox.minY)));
		hitBox = hitBox.setMaxY(hitBox.maxY + 0.5D);

		return hitBox;
	}

	@Override
	public Predicate<? super Entity> getStompableBy() {
		return EntitySelector.NO_SPECTATORS.and(entity ->
				entity.is(SuperMarioEntityTypeTags.CAN_STOMP) &&
						! entity.onGround() &&
						entity.getDeltaMovement().y() < 0.3D &&
						entity.isAlive()
		);
	}

	@Override
	public void onStompedBy(Entity entity) {
		var this_ = ((Entity) (Object) this);
		//TODO: display particles!
		if (this_.level() instanceof ServerLevel serverLevel) {
			float damage = 2.0F; //TODO: calculate damage using boots?
			if (entity instanceof PowerUpHolder powerUpHolder
					&& powerUpHolder.getPowerUp().isPresent()
					&& powerUpHolder.getPowerUp().get().is(SuperMarioPowerUpTags.DISABLES_STOMPING)) {
				damage = 0.0f;
			}
			if(damage > 0) {
				this_.hurtServer(serverLevel, this_.damageSources().source(SuperMarioDamageTypeIds.STOMP, entity), 2.0F);
			}
			else {
				// TODO: play sound
			}
			// A player's real momentum only lives on their client; the server-side delta is stale, and sending it
			// back would kill the horizontal speed they came in with. getKnownMovement() is what the client reported.
			Vec3 momentum = entity.getKnownMovement();
			entity.setDeltaMovement(momentum.x(), 0.5D, momentum.z());
			if (entity instanceof Player player) {
				((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
			}
			entity.fallDistance = 0.0F;
		}
	}
}