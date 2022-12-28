package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.weapon.AutomaticShooterItem;
import fr.hugman.mubble.item.weapon.stats.AutomaticShooterConfig;
import fr.hugman.mubble.item.weapon.stats.bullet.ShooterBulletConfig;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class SplatoonContent {
	public static final AutomaticShooterItem SPLATTERSHOT = new AutomaticShooterItem(AutomaticShooterConfig.ofSplat(ShooterBulletConfig.ofSplat(40, 8, 360, 180, 1.4495F, 4, 2.2F), 6, 12.0F, 6.0F));
	public static final AutomaticShooterItem DOT_96_GAL = new AutomaticShooterItem(AutomaticShooterConfig.ofSplat(ShooterBulletConfig.ofSplat(25, 9, 620, 350, 2.377F, 5, 2.45F), 12, 11.3511F, 4.0F));

	public static void init() {
		Registrar.add(Mubble.id("splattershot"), SPLATTERSHOT);
		Registrar.add(Mubble.id("dot_96_gal"), DOT_96_GAL);
	}

	public static final ItemGroup GROUP = FabricItemGroup.builder(Mubble.id("splatoon"))
			.displayName(Text.translatable("item_group.mubble.splatoon"))
			.icon(() -> new ItemStack(SPLATTERSHOT))
			.entries((enabledFeatures, entries, operatorEnabled) -> {
				entries.add(SPLATTERSHOT);
				entries.add(DOT_96_GAL);
			})
			.build();
}
