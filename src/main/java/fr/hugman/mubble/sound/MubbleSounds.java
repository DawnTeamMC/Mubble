package fr.hugman.mubble.sound;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class MubbleSounds {
    // SUPER MARIO
    public static final SoundEvent BUMPABLE_BLOCK_DESTROY = of("block.bumpable_block.destroy");
    public static final SoundEvent BUMPABLE_BLOCK_BUMP = of("block.bumpable_block.bump");
    public static final SoundEvent BUMPABLE_BLOCK_CHANGE_LOOT = of("block.bumpable_block.change_loot");
    public static final SoundEvent BUMPABLE_BLOCK_LOOT = of("block.bumpable_block.loot");
    public static final SoundEvent BUMPABLE_BLOCK_LOOT_COIN = of("block.bumpable_block.loot_coin");

    public static final SoundEvent NOTE_BLOCK_JUMP_LOW = of("block.note_block.jump.low");
    public static final SoundEvent NOTE_BLOCK_JUMP_HIGH = of("block.note_block.jump.high");
    public static final SoundEvent CAPE_FEATHER_USE = of("item.cape_feather.use");

    public static final SoundEvent GOOMBA_WALK_STEP = of("entity.goomba.walk_step");
    public static final SoundEvent GOOMBA_RUN_STEP = of("entity.goomba.run_step");
    public static final SoundEvent GOOMBA_FIND_TARGET = of("entity.goomba.find_target");
    public static final SoundEvent GOOMBA_DEATH = of("entity.goomba.death");
    public static final SoundEvent GOOMBA_STOMP = of("entity.goomba.stomp");

    public static final SoundEvent KOOPA_SHELL_SLIDE = of("entity.koopa_shell.slide");
    public static final SoundEvent KOOPA_SHELL_HOMING = of("entity.koopa_shell.homing");
    public static final SoundEvent KOOPA_SHELL_HIT_BLOCK = of("entity.koopa_shell.hit_block");
    public static final SoundEvent KOOPA_SHELL_BREAK = of("entity.koopa_shell.break");
    public static final SoundEvent KOOPA_SHELL_KICK = of("entity.koopa_shell.kick");

    public static final SoundEvent FIREBALL_HIT_BLOCK = of("entity.fireball.hit.block");
    public static final SoundEvent FIREBALL_HIT_ENTITY = of("entity.fireball.hit.entity");
    public static final Holder.Reference<SoundEvent> FIREBALL_MELT_BLOCK = ofRef("entity.fireball.hit.melt_block");
    public static final Holder.Reference<SoundEvent> FIREBALL_THROW = ofRef("entity.fireball.throw");

    public static final Holder.Reference<SoundEvent> ICEBALL_HIT_BLOCK = ofRef("entity.iceball.hit.block");
    public static final SoundEvent ICEBALL_HIT_ENTITY = of("entity.iceball.hit.entity");
    public static final Holder.Reference<SoundEvent> ICEBALL_THROW = ofRef("entity.iceball.throw");

	public static final Holder.Reference<SoundEvent> POWER_UP_OBTAIN = ofRef("power_up.obtain");
	public static final Holder.Reference<SoundEvent> POWER_UP_OBTAIN_MINI = ofRef("power_up.obtain.mini");
	public static final Holder.Reference<SoundEvent> POWER_UP_OBTAIN_SUPER_STAR = ofRef("power_up.obtain.super_star");
	public static final Holder.Reference<SoundEvent> POWER_UP_LOOSE = ofRef("power_up.loose");


    private static SoundEvent of(String path) {
        Identifier id = Mubble.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    private static Holder.Reference<SoundEvent> ofRef(String path) {
        Identifier id = Mubble.id(path);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
