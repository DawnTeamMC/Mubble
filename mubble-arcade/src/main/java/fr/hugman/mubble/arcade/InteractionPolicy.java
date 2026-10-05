package fr.hugman.mubble.arcade;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * What the hands of a player under a profile may still do.
 * <p>
 * This is the profile-wide rule. On top of it, every move flagged {@code hands_busy} keeps the hands
 * from attacking and using anything for as long as it lasts, whatever the policy.
 */
public enum InteractionPolicy implements StringRepresentable {
    /** Everything vanilla allows. */
    FULL("full"),
    /** Attacking entities and using items in the air (bows, food, shields); no mining, placing or using blocks and entities. */
    COMBAT_ONLY("combat_only"),
    /** Nothing at all: no attack, no use, no mining, no placing. */
    NONE("none");

    public static final Codec<InteractionPolicy> CODEC = StringRepresentable.fromEnum(InteractionPolicy::values);

    private final String name;

    InteractionPolicy(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean allowsAttackingEntities() {
        return this != NONE;
    }

    public boolean allowsUsingItems() {
        return this != NONE;
    }

    public boolean allowsWorldInteraction() {
        return this == FULL;
    }
}
