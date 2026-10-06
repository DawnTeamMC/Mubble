package fr.hugman.mubble.splatoon.world.item;

import fr.hugman.mubble.splatoon.core.component.SplatoonDataComponents;
import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBullet;
import fr.hugman.mubble.splatoon.world.item.weapon.AutomaticShooterConfig;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * A Splatoon weapon, which fires as long as it is used.
 *
 * <p>Shots are timed in frames, like in Splatoon 3: a weapon firing every 4 frames fires 3 times every 4 ticks, not
 * once per tick. Letting go keeps the weapon cooling down until its next shot was due, so tapping does not fire
 * faster than holding.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class SplatoonWeaponItem extends Item {
    public static final int USE_DURATION = 72000;

    public SplatoonWeaponItem(Properties properties) {
        super(properties);
    }

    @Nullable
    private static AutomaticShooterConfig shooter(ItemStack stack) {
        var weapon = stack.get(SplatoonDataComponents.SPLATOON_WEAPON);
        return weapon != null && weapon.value() instanceof AutomaticShooterConfig config ? config : null;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (shooter(itemStack) == null) {
            return InteractionResult.PASS;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME.heldItemTransformedTo(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int ticksRemaining) {
        var config = shooter(stack);
        if (config == null) {
            return;
        }
        int ticksUsed = this.getUseDuration(stack, living) - ticksRemaining;
        int shots = config.shotsAt(ticksUsed);
        if (shots <= 0) {
            return;
        }
        if (!level.isClientSide()) {
            var ink = stack.getOrDefault(SplatoonDataComponents.INK, InkStyle.DEFAULT);
            int firstShot = config.shotsBefore(ticksUsed);
            for (int i = 0; i < shots; i++) {
                level.addFreshEntity(new ShooterInkBullet(level, living, config, ink, firstShot + i));
            }
            level.playSound(null, living.getX(), living.getY(), living.getZ(), SplatoonSounds.SPLATTERSHOT_SHOOT, SoundSource.PLAYERS, 0.5f, 1.0F);
        }
        if (living instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity living, int remainingTime) {
        var config = shooter(stack);
        if (config != null && living instanceof Player player) {
            int cooldown = config.cooldownAfter(this.getUseDuration(stack, living) - remainingTime);
            if (cooldown > 0) {
                player.getCooldowns().addCooldown(stack, cooldown);
            }
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return USE_DURATION;
    }
}
