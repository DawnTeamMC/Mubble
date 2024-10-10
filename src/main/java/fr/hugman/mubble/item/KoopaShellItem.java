package fr.hugman.mubble.item;

import fr.hugman.mubble.entity.GreenKoopaShellEntity;
import fr.hugman.mubble.entity.KoopaShellEntity;
import fr.hugman.mubble.entity.RedKoopaShellEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class KoopaShellItem extends Item implements ProjectileItem {
    private final boolean isRed; //TODO: i dont like this impl

    public KoopaShellItem(Item.Settings settings, boolean isRed) {
        super(settings);
        this.isRed = isRed;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        //TODO: sound from Mario Kart?
        world.playSound(
                null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            KoopaShellEntity koopaShellEntity = isRed ? new RedKoopaShellEntity(world, user) : new GreenKoopaShellEntity(world, user);
            koopaShellEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.5F, 1.0F);
            world.spawnEntity(koopaShellEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return isRed ? new RedKoopaShellEntity(world, pos.getX(), pos.getY(), pos.getZ()) :
                new GreenKoopaShellEntity(world, pos.getX(), pos.getY(), pos.getZ());
    }
}
