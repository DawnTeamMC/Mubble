package fr.hugman.mubble.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public class BeanstalkEntity extends MobEntity {
    public BeanstalkEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }
}