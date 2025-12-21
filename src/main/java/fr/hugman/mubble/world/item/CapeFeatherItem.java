package fr.hugman.mubble.world.item;

import fr.hugman.mubble.sound.MubbleSounds;
import java.util.Random;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CapeFeatherItem extends Item {
    private final boolean infinite;

    public CapeFeatherItem(Item.Properties builder, boolean infinite) {
        super(builder);
        this.infinite = infinite;
    }

    @Override
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        Random rand = new Random();
        Vec3 vec3d = playerIn.getDeltaMovement();
        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MubbleSounds.CAPE_FEATHER_USE, SoundSource.PLAYERS, 0.5F, 1F);
        for (int i = 0; i < rand.nextInt(6) + 1; i++) {
            worldIn.addParticle(ParticleTypes.CLOUD, playerIn.getX() + (rand.nextInt(11) - 5) / 10F, playerIn.getY(), playerIn.getZ() + (rand.nextInt(11) - 5) / 10F, 0.0D, (rand.nextInt(3) + 1) / 10F, 0);
        }
        playerIn.setDeltaMovement(vec3d.x, 0.7D, vec3d.z);
        playerIn.fallDistance = 0f;
        if (!playerIn.isCreative() && !this.infinite) {
            stack.shrink(1);
        }
        playerIn.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
    }
}
