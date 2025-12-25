package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.references.MubbleEntityTypeKeys;
import fr.hugman.mubble.world.entity.monster.goomba.Goomba;
import fr.hugman.mubble.world.entity.projectile.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class MubbleEntityTypes {
    public static final EntityType<Goomba> GOOMBA = of(MubbleEntityTypeKeys.GOOMBA, EntityType.Builder.of(Goomba::new, MobCategory.CREATURE).sized(0.6f, 0.755f).eyeHeight(0.53125f));
    public static final EntityType<GreenKoopaShell> GREEN_KOOPA_SHELL = of(MubbleEntityTypeKeys.GREEN_KOOPA_SHELL, EntityType.Builder.<GreenKoopaShell>of(GreenKoopaShell::new, MobCategory.MISC).sized(10 / 16f, 7 / 16f));
    public static final EntityType<RedKoopaShell> RED_KOOPA_SHELL = of(MubbleEntityTypeKeys.RED_KOOPA_SHELL, EntityType.Builder.<RedKoopaShell>of(RedKoopaShell::new, MobCategory.MISC).sized(10 / 16f, 7 / 16f));
    public static final EntityType<Fireball> FIREBALL = of(MubbleEntityTypeKeys.FIREBALL, EntityType.Builder.<Fireball>of(Fireball::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));
    public static final EntityType<Iceball> ICEBALL = of(MubbleEntityTypeKeys.ICEBALL, EntityType.Builder.<Iceball>of(Iceball::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));
    public static final EntityType<GoldFireball> GOLD_FIREBALL = of(MubbleEntityTypeKeys.GOLD_FIREBALL, EntityType.Builder.<GoldFireball>of(GoldFireball::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));

    private static <T extends Entity> EntityType<T> of(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GOOMBA, Goomba.createGoombaAttributes());
    }
}
