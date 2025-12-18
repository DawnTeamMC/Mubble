package fr.hugman.mubble.item;

import fr.hugman.mubble.component.MubbleDataComponentTypes;
import fr.hugman.mubble.component.PowerUpComponent;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;

public class PowerUpItem extends Item {
    public PowerUpItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        Consumable consumableComponent = stack.get(DataComponents.CONSUMABLE);
        if (null != consumableComponent) {
            return super.use(world, user, hand);
        }
        PowerUpComponent powerUpComponent = stack.get(MubbleDataComponentTypes.POWER_UP);
        if (null != powerUpComponent) {
            user.startUsingItem(hand);
            var opt = powerUpComponent.powerUp().unwrap(world.registryAccess());
            if (opt.isPresent() && PowerUp.canChange(user, opt.get())) {
                if (world instanceof ServerLevel) {
                    user.setPowerUp(opt.get());
                }
                user.awardStat(Stats.ITEM_USED.get(this));
                stack.consume(1, user);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }
}
