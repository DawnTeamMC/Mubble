package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.entity.FlyingBlockEntity;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.InkBlock;
import fr.hugman.mubble.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.entity.projectile.ShooterInkBulletEntity;
import fr.hugman.mubble.item.weapon.AutomaticShooterItem;
import fr.hugman.mubble.item.weapon.AutomaticShooterConfig;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;

public class Splatoon {
	// ------------------
	//      GENERIC
	// ------------------

	// ITEM GROUP
	public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Mubble.id("splatoon"));

	// BLOCKS
	public static final InkBlock INK_BLOCK = new InkBlock(AbstractBlock.Settings.create().mapColor(MapColor.WATER_BLUE).replaceable().noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().sounds(BlockSoundGroup.field_44608));

	// ------------------
	// AUTOMATIC SHOOTERS
	// ------------------

	// WEAPONS
	public static final AutomaticShooterItem SPLATTERSHOT = new AutomaticShooterItem(AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(40, 8, 360, 180, 1.4495F, 4, 2.2F, 0.016f), 6, 12.0F, 6.0F));
	public static final AutomaticShooterItem DOT_96_GAL = new AutomaticShooterItem(AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(25, 9, 620, 350, 2.377F, 5, 2.45F, 0.016f), 12, 11.3511F, 4.0F));

	// ENTITIES
	public static final EntityType<ShooterInkBulletEntity> SHOOTER_INK_BULLET = FabricEntityTypeBuilder.<ShooterInkBulletEntity>create(SpawnGroup.MISC, ShooterInkBulletEntity::new)
			.dimensions(EntityDimensions.fixed(0.5f, 0.5f))
			.trackRangeChunks(4)
			.trackedUpdateRate(20)
			.forceTrackedVelocityUpdates(true)
			.build();

	public static void init(Registrar r) {
		r.add("ink", INK_BLOCK);

		r.add("splattershot", SPLATTERSHOT);
		r.add("dot_96_gal", DOT_96_GAL);

		r.add("shooter_ink_bullet", SHOOTER_INK_BULLET);

		appendItemGroups();
	}

	public static void appendItemGroups() {
		Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
				.displayName(Text.translatable("item_group.mubble.splatoon"))
				.icon(() -> new ItemStack(SPLATTERSHOT))
				.entries((displayContext, entries) -> {
					entries.add(SPLATTERSHOT);
					entries.add(DOT_96_GAL);
				}).build());
	}
}
