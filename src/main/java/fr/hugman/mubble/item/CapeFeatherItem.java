package fr.hugman.mubble.item;

import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class CapeFeatherItem extends Item {
    private final boolean infinite;

    public CapeFeatherItem(Item.Settings builder, boolean infinite) {
        super(builder);
        this.infinite = infinite;
    }

    @Override
    public ActionResult use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getStackInHand(handIn);
        Random rand = new Random();
        Vec3d vec3d = playerIn.getVelocity();
        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MubbleSounds.CAPE_FEATHER_USE, SoundCategory.PLAYERS, 0.5F, 1F);
        for (int i = 0; i < rand.nextInt(6) + 1; i++) {
            worldIn.addParticle(ParticleTypes.CLOUD, playerIn.getX() + (rand.nextInt(11) - 5) / 10F, playerIn.getY(), playerIn.getZ() + (rand.nextInt(11) - 5) / 10F, 0.0D, (rand.nextInt(3) + 1) / 10F, 0);
        }
        playerIn.setVelocity(vec3d.x, 0.7D, vec3d.z);
        playerIn.fallDistance = 0f;
        if (!playerIn.isCreative() && !this.infinite) {
            stack.decrement(1);
        }
        playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
        return ActionResult.SUCCESS.withNewHandStack(stack);
    }
}
