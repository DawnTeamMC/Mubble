package fr.hugman.mubble.item;

import fr.hugman.mubble.component.MubbleDataComponentTypes;
import fr.hugman.mubble.component.PowerUpComponent;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PowerUpItem extends Item {
    public PowerUpItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
        if (null != consumableComponent) {
            return super.use(world, user, hand);
        }
        PowerUpComponent powerUpComponent = stack.get(MubbleDataComponentTypes.POWER_UP);
        if (null != powerUpComponent) {
            user.setCurrentHand(hand);
            var opt = powerUpComponent.powerUp().resolveEntry(world.getRegistryManager());
            if (opt.isPresent() && PowerUp.canChange(user, opt.get())) {
                if (world instanceof ServerWorld) {
                    user.setPowerUp(opt.get());
                }
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                stack.decrementUnlessCreative(1, user);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL;
    }
}
