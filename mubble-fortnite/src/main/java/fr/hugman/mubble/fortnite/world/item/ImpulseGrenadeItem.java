package fr.hugman.mubble.fortnite.world.item;

import fr.hugman.mubble.fortnite.world.entity.projectile.ImpulseGrenade;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

/**
 * Throws an {@link ImpulseGrenade}. It is lobbed rather than hurled like a snowball, so that it comes down a few
 * blocks ahead, where it is meant to be used.
 */
public class ImpulseGrenadeItem extends Item implements ProjectileItem {
    /** As many as a slot holds in Fortnite. */
    public static final int MAX_STACK_SIZE = 10;
    public static final float PROJECTILE_SHOOT_POWER = 1.0F;

    public ImpulseGrenadeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(
                null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(ImpulseGrenade::new, serverLevel, stack, player, 0.0F, PROJECTILE_SHOOT_POWER, 1.0F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return new ImpulseGrenade(level, position.x(), position.y(), position.z(), stack);
    }
}
