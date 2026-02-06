package fr.hugman.mubble.splatoon.world.item;

import fr.hugman.mubble.splatoon.core.component.SplatoonDataComponents;
import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBullet;
import fr.hugman.mubble.splatoon.world.item.weapon.AutomaticShooterConfig;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SplatoonWeaponItem extends Item {
    public SplatoonWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResult.CONSUME.heldItemTransformedTo(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int ticksRemaining) {
        var weaponEntry = stack.get(SplatoonDataComponents.SPLATOON_WEAPON);
        if(weaponEntry == null) {
            return;
        }
        var weapon = weaponEntry.value();

        if(living instanceof Player player) {
            if(weapon instanceof AutomaticShooterConfig config) {
                if(!player.getCooldowns().isOnCooldown(stack)) {
                    this.shootShooterInkBullet(level,player, stack, config);
                }
            }
        }
    }

    private void shootShooterInkBullet(Level level, Player player, ItemStack stack, AutomaticShooterConfig config) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SplatoonSounds.SPLATTERSHOT_SHOOT, SoundSource.PLAYERS, 0.5f, 1.0F);
        player.getCooldowns().addCooldown(stack, (int) config.cooldown());
        if (!level.isClientSide()) {
            float angleDeviation = (player.onGround() ? config.angleDeviation() : config.jumpingAngleDeviation());
            var bullet = new ShooterInkBullet(level, player, config.bulletConfig(), angleDeviation);
            level.addFreshEntity(bullet);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 72000;
    }
}
