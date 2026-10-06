package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.item.component.PowerUpComponent;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * What the tooltip of an item granting a power-up ends up saying. Forms such as the mini and the mega
 * ones move a dozen attributes at once, so their own description takes the place of the list, and the
 * list stays one advanced tooltip away rather than being lost.
 */
public class PowerUpTooltipTest {
    private static final String FIRST = "the first line";
    private static final String SECOND = "the second line";

    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a described power-up shows its description instead of its modifiers")
    void descriptionTakesThePlaceOfTheModifiers() {
        assertEquals(List.of(FIRST, SECOND), tooltipOf(described(), TooltipFlag.NORMAL));
    }

    @Test
    @DisplayName("the advanced tooltips bring the modifiers back, under the description")
    void advancedTooltipsShowTheModifiers() {
        var lines = tooltipOf(described(), TooltipFlag.ADVANCED);

        assertTrue(lines.size() > 2, () -> "the modifiers should be listed under the description, got " + lines);
        assertEquals(List.of(FIRST, SECOND), lines.subList(0, 2), "the description should still come first");
    }

    @Test
    @DisplayName("a power-up with no description keeps listing its modifiers")
    void undescribedPowerUpKeepsItsModifiers() {
        // What a data pack adding a power-up gets until it writes a description of its own.
        var normal = tooltipOf(undescribed(), TooltipFlag.NORMAL);

        assertTrue(normal.size() > 1, () -> "the modifiers should be listed, got " + normal);
        assertEquals(tooltipOf(undescribed(), TooltipFlag.ADVANCED), normal, "the advanced tooltips should add nothing");
    }

    private static List<String> tooltipOf(PowerUp powerUp, TooltipFlag flag) {
        var lines = new ArrayList<String>();
        PowerUpComponent.buildAutomaticTooltip(powerUp, Item.TooltipContext.EMPTY, line -> lines.add(line.getString()), flag, DataComponentMap.EMPTY);
        return lines;
    }

    private static PowerUp described() {
        return modified(new PowerUpBuilder()
                .description(Component.literal(FIRST))
                .description(Component.literal(SECOND)));
    }

    private static PowerUp undescribed() {
        return modified(new PowerUpBuilder());
    }

    private static PowerUp modified(PowerUpBuilder builder) {
        return builder
                .attributesModifier(Attributes.MAX_HEALTH, 4.0D, AttributeModifier.Operation.ADD_VALUE)
                .attributesModifier(Attributes.MOVEMENT_SPEED, -0.25D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .build();
    }
}
