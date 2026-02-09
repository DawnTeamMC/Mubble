package fr.hugman.mubble.super_mario.sounds;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class SuperMarioSounds {
    public static final SoundEvent BUMPABLE_BLOCK_DESTROY = register("block.bumpable_block.destroy");
    public static final SoundEvent BUMPABLE_BLOCK_BUMP = register("block.bumpable_block.bump");
    public static final SoundEvent BUMPABLE_BLOCK_CHANGE_LOOT = register("block.bumpable_block.change_loot");
    public static final SoundEvent BUMPABLE_BLOCK_LOOT = register("block.bumpable_block.loot");
    public static final SoundEvent BUMPABLE_BLOCK_LOOT_COIN = register("block.bumpable_block.loot_coin");

    public static final SoundEvent NOTE_BLOCK_JUMP_LOW = register("block.note_block.jump.low");
    public static final SoundEvent NOTE_BLOCK_JUMP_HIGH = register("block.note_block.jump.high");

    public static final Holder.Reference<SoundEvent> COIN_COLLECT = registerForHolder("item.coin.collect");
    public static final Holder.Reference<SoundEvent> COIN_BOUNCE = registerForHolder("item.coin.bounce");
    public static final SoundEvent CAPE_FEATHER_USE = register("item.cape_feather.use");

    public static final Holder.Reference<SoundEvent> GOLDEN_EXPLOSION = registerForHolder("entity.generic.golden_explosion");

    public static final SoundEvent GOOMBA_WALK_STEP = register("entity.goomba.walk_step");
    public static final SoundEvent GOOMBA_RUN_STEP = register("entity.goomba.run_step");
    public static final SoundEvent GOOMBA_FIND_TARGET = register("entity.goomba.find_target");
    public static final SoundEvent GOOMBA_DEATH = register("entity.goomba.death");
    public static final SoundEvent GOOMBA_STOMP = register("entity.goomba.stomp");

    public static final SoundEvent KOOPA_SHELL_SLIDE = register("entity.koopa_shell.slide");
    public static final SoundEvent KOOPA_SHELL_HOMING = register("entity.koopa_shell.homing");
    public static final SoundEvent KOOPA_SHELL_HIT_BLOCK = register("entity.koopa_shell.hit_block");
    public static final SoundEvent KOOPA_SHELL_BREAK = register("entity.koopa_shell.break");
    public static final SoundEvent KOOPA_SHELL_KICK = register("entity.koopa_shell.kick");

    public static final SoundEvent FIREBALL_HIT_BLOCK = register("entity.fireball.hit.block");
    public static final SoundEvent FIREBALL_HIT_ENTITY = register("entity.fireball.hit.entity");
    public static final Holder.Reference<SoundEvent> FIREBALL_MELT_BLOCK = registerForHolder("entity.fireball.hit.melt_block");
    public static final Holder.Reference<SoundEvent> FIREBALL_THROW = registerForHolder("entity.fireball.throw");

    public static final Holder.Reference<SoundEvent> ICEBALL_HIT_BLOCK = registerForHolder("entity.iceball.hit.block");
    public static final SoundEvent ICEBALL_HIT_ENTITY = register("entity.iceball.hit.entity");
    public static final Holder.Reference<SoundEvent> ICEBALL_THROW = registerForHolder("entity.iceball.throw");

    public static final Holder.Reference<SoundEvent> GOLD_FIREBALL_THROW = registerForHolder("entity.gold_fireball.throw");

	public static final Holder.Reference<SoundEvent> POWER_UP_OBTAIN = registerForHolder("power_up.obtain");
	public static final Holder.Reference<SoundEvent> POWER_UP_OBTAIN_MINI = registerForHolder("power_up.obtain.mini");
	public static final Holder.Reference<SoundEvent> POWER_UP_OBTAIN_SUPER_STAR = registerForHolder("power_up.obtain.super_star");
	public static final Holder.Reference<SoundEvent> POWER_UP_OBTAIN_GOLD = registerForHolder("power_up.obtain.gold");
    public static final Holder.Reference<SoundEvent> POWER_UP_EMIT_GOLD = registerForHolder("power_up.emit.gold");
    public static final Holder.Reference<SoundEvent> POWER_UP_LOOSE = registerForHolder("power_up.loose");

    private static SoundEvent register(String path) {
        Identifier id = SuperMario.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    private static Holder.Reference<SoundEvent> registerForHolder(String path) {
        Identifier id = SuperMario.id(path);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
