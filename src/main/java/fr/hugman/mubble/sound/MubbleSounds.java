package fr.hugman.mubble.sound;

import fr.hugman.mubble.Mubble;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

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
    public static final SoundEvent KOOPA_SHELL_HIT_BLOCK = of("entity.koopa_shell.hit_block");

    private static SoundEvent of(String path) {
        Identifier id = Mubble.id(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
