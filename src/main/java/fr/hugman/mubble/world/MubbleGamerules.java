package fr.hugman.mubble.world;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.BeepBlock;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleCategory;

public class MubbleGamerules {
    // SUPER MARIO
    public static final GameRule<Integer> BEEP_BLOCK_COOLDOWN = GameRuleBuilder.forInteger(BeepBlock.DEFAULT_COOLDOWN).minValue(1).category(GameRuleCategory.UPDATES).buildAndRegister(Mubble.id("beep_block_cooldown"));
}
