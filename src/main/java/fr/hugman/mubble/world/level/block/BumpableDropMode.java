package fr.hugman.mubble.world.level.block;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum BumpableDropMode implements StringRepresentable {
    ALL(0, "all"),
    ONE(1, "one");

    public static final Codec<BumpableDropMode> CODEC = StringRepresentable.fromEnum(BumpableDropMode::values);
    private final int index;
    private final String id;
    private final Component name;
    private final Component description;

    BumpableDropMode(int index, String id) {
        this.index = index;
        this.id = id;
        this.name = Component.translatable("block.mubble.bumpable.drop." + id);
        this.description = Component.translatable("block.mubble.bumpable.drop." + id + ".description");
    }

    public static BumpableDropMode get(int index) {
        for (BumpableDropMode mode : values()) {
            if (mode.index == index) {
                return mode;
            }
        }
        return ALL;
    }

    public static BumpableDropMode get(String s) {
        for (BumpableDropMode mode : values()) {
            if (mode.id.equals(s)) {
                return mode;
            }
        }
        return ALL;
    }

    public BumpableDropMode next() {
        return get((index + 1) % values().length);
    }

    public int getIndex() {
        return index;
    }

    public Component getName() {
        return name;
    }

    public Component getDescription() {
        return description;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }
}
