package fr.hugman.mubble.world.item;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.world.item.component.PowerUpComponent;
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
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        Consumable consumableComponent = stack.get(DataComponents.CONSUMABLE);
        if (null != consumableComponent) {
            return super.use(level, user, hand);
        }
        PowerUpComponent powerUpComponent = stack.get(MubbleDataComponents.POWER_UP);
        if (null != powerUpComponent) {
            user.startUsingItem(hand);
            var opt = powerUpComponent.powerUp().unwrap(level.registryAccess());
            if (opt.isPresent() && PowerUp.canChange(user, opt.get())) {
                if (level instanceof ServerLevel) {
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
