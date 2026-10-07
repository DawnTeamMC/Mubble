package fr.hugman.mubble.test.gametest.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** The exhaustion a player has built up, which vanilla keeps to itself. */
@Mixin(FoodData.class)
public interface FoodDataAccessor {
    @Accessor("exhaustionLevel")
    float mubbleGametest$getExhaustionLevel();
}
