package fr.hugman.mubble.super_mario.world.level.gamerules;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.level.block.BeepBlock;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class SuperMarioGameRules {
    public static final GameRule<Integer> BEEP_BLOCK_COOLDOWN = GameRuleBuilder.forInteger(BeepBlock.DEFAULT_COOLDOWN).minValue(1).category(GameRuleCategory.UPDATES).buildAndRegister(SuperMario.id("beep_block_cooldown"));
}
