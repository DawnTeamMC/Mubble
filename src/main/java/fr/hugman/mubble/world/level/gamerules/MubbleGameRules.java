package fr.hugman.mubble.world.level.gamerules;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.BeepBlock;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class MubbleGameRules {
    // SUPER MARIO
    public static final GameRule<Integer> BEEP_BLOCK_COOLDOWN = GameRuleBuilder.forInteger(BeepBlock.DEFAULT_COOLDOWN).minValue(1).category(GameRuleCategory.UPDATES).buildAndRegister(Mubble.id("beep_block_cooldown"));
}
