package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.MubbleItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class GreenKoopaShellEntity extends KoopaShellEntity {
    private static final Identifier TEXTURE = Mubble.id("textures/entity/koopa_shell/green.png");

    public GreenKoopaShellEntity(EntityType<? extends GreenKoopaShellEntity> entityType, World world) {
        super(entityType, world);
    }

    public GreenKoopaShellEntity(World world, double x, double y, double z) {
        super(MubbleEntityTypes.GREEN_KOOPA_SHELL, world, x, y, z);
    }

    public GreenKoopaShellEntity(World world, LivingEntity owner) {
        super(MubbleEntityTypes.GREEN_KOOPA_SHELL, world, owner);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(MubbleItems.GREEN_KOOPA_SHELL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }
}
