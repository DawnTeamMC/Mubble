package fr.hugman.mubble.world;

import fr.hugman.mubble.block.BeepBlock;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class MubbleGamerules {
    // SUPER MARIO
    public static GameRules.Key<GameRules.IntRule> BEEP_BLOCK_COOLDOWN;

    public static void init() {
        BEEP_BLOCK_COOLDOWN = GameRuleRegistry.register("beepBlockCooldown", GameRules.Category.UPDATES, GameRuleFactory.createIntRule(BeepBlock.DEFAULT_COOLDOWN));
    }
}
