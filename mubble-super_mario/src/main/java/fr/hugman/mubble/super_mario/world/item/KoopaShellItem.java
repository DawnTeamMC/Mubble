package fr.hugman.mubble.super_mario.world.item;

import fr.hugman.mubble.super_mario.world.entity.projectile.GreenKoopaShell;
import fr.hugman.mubble.super_mario.world.entity.projectile.KoopaShell;
import fr.hugman.mubble.super_mario.world.entity.projectile.RedKoopaShell;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
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

public class KoopaShellItem extends Item implements ProjectileItem {
    private final boolean isRed; //TODO: i dont like this impl

    public KoopaShellItem(Item.Properties settings, boolean isRed) {
        super(settings);
        this.isRed = isRed;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        //TODO: sound from Mario Kart?
        level.playSound(
                null, user.getX(), user.getY(), user.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!level.isClientSide()) {
            KoopaShell koopaShell = isRed ? new RedKoopaShell(level, user) : new GreenKoopaShell(level, user);
            koopaShell.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 0.5F, 1.0F);
            level.addFreshEntity(koopaShell);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, user);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return isRed ? new RedKoopaShell(level, pos.x(), pos.y(), pos.z()) :
                new GreenKoopaShell(level, pos.x(), pos.y(), pos.z());
    }
}
