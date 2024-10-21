package fr.hugman.mubble.item;

import fr.hugman.mubble.component.MubbleComponentTypes;
import fr.hugman.mubble.component.PowerUpComponent;
import fr.hugman.mubble.item.consume.ChangePowerUpConsumeEffect;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

public class PowerUpItem extends Item {
    public PowerUpItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        PowerUpComponent powerUpComponent = stack.get(MubbleComponentTypes.POWER_UP);
        if (powerUpComponent != null) {
            powerUpComponent.buildTooltip(context, tooltip::add, 1.0F, context.getUpdateTickRate());
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
        if (null != consumableComponent) {
            return super.use(world, user, hand);
        }
        PowerUpComponent powerUpComponent = stack.get(MubbleComponentTypes.POWER_UP);
        if (null != powerUpComponent) {
            user.setCurrentHand(hand);
            if (new ChangePowerUpConsumeEffect(powerUpComponent.powerUp()).onConsume(world, stack, user)) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.FAIL;
    }
}
