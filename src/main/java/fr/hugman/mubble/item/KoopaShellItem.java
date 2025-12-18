package fr.hugman.mubble.item;

import fr.hugman.mubble.entity.GreenKoopaShellEntity;
import fr.hugman.mubble.entity.KoopaShellEntity;
import fr.hugman.mubble.entity.RedKoopaShellEntity;
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
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        //TODO: sound from Mario Kart?
        world.playSound(
                null, user.getX(), user.getY(), user.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClientSide()) {
            KoopaShellEntity koopaShellEntity = isRed ? new RedKoopaShellEntity(world, user) : new GreenKoopaShellEntity(world, user);
            koopaShellEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 0.5F, 1.0F);
            world.addFreshEntity(koopaShellEntity);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, user);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
        return isRed ? new RedKoopaShellEntity(world, pos.x(), pos.y(), pos.z()) :
                new GreenKoopaShellEntity(world, pos.x(), pos.y(), pos.z());
    }
}
