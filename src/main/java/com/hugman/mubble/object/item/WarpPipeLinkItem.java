package com.hugman.mubble.object.item;

import net.minecraft.item.Item;

public class WarpPipeLinkItem extends Item {

    public WarpPipeLinkItem(Settings settings) {
        super(settings);
    }

    public int originX = 0;
    public int originY = -65;
    public int originZ = 0;

    public int getOriginX() { return this.originX; }
    public int getOriginY() { return this.originY; }
    public int getOriginZ() { return this.originZ; }


    public void setOriginX(int newPos) { this.originX = newPos; }
    public void setOriginY(int newPos) { this.originY = newPos; }
    public void setOriginZ(int newPos) { this.originZ = newPos; }

}
