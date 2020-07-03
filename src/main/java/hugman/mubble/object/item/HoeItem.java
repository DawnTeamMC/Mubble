package hugman.mubble.object.item;

import net.minecraft.item.ToolMaterial;

public class HoeItem extends net.minecraft.item.HoeItem {
	/* Extension for internal publicity */
	public HoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
}
