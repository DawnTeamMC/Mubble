package fr.hugman.mubble.world.item;

import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.item.CollectibleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CollectibleItem extends Item {
	public CollectibleItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(final UseOnContext context) {
		Direction clickedFace = context.getClickedFace();
		if (clickedFace == Direction.DOWN) {
			return InteractionResult.FAIL;
		} else {
			Level level = context.getLevel();
			BlockPlaceContext placeContext = new BlockPlaceContext(context);
			BlockPos blockPos = placeContext.getClickedPos();
			ItemStack itemStack = context.getItemInHand();
			var dimensions = MubbleEntityTypes.COLLECTIBLE.getDimensions();
			Vec3 pos = Vec3.atBottomCenterOf(blockPos).add(0, Math.max(0.0f, 1.0f - dimensions.height()), 0);
			AABB box = dimensions.makeBoundingBox(pos.x(), pos.y(), pos.z());
			if (level.noCollision(null, box) && level.getEntities(null, box).isEmpty()) {
				if (level instanceof ServerLevel serverLevel) {
					CollectibleEntity entity = new CollectibleEntity(serverLevel, pos.x(), pos.y(), pos.z(), itemStack.copyWithCount(1));
					EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer()).accept(entity);
					if (entity == null) {
						return InteractionResult.FAIL;
					}
					entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), 0.0f, 0.0F);
					serverLevel.addFreshEntityWithPassengers(entity);
					level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
					entity.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
				}

				itemStack.shrink(1);
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		}
	}
}
