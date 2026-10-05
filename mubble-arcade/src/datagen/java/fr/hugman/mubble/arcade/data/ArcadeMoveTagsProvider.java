package fr.hugman.mubble.arcade.data;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import static fr.hugman.mubble.arcade.references.ArcadeMoveIds.*;
import static fr.hugman.mubble.arcade.tags.ArcadeMoveTags.*;

import fr.hugman.mubble.arcade.move.ArcadeMove;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

public class ArcadeMoveTagsProvider extends FabricTagsProvider<ArcadeMove> {
    public ArcadeMoveTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, ArcadeRegistries.ARCADE_MOVE, registriesFuture);
    }

    @Override
    protected TagAppender<ArcadeMove> builder(TagKey<ArcadeMove> tag) {
        return TagAppender.forBuilder(this.getOrCreateRawBuilder(tag));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        // every move of its own; the parts of a move (a ledge climb, a rollout) follow it
        this.builder(ALL).add(
                RUN, SKID, JUMP, DOUBLE_JUMP, TRIPLE_JUMP, CROUCH, GROUND_POUND, GROUND_POUND_JUMP, LEDGE_GRAB, WALL_SLIDE, WALL_JUMP,
                ROLL, ROLL_JUMP, LONG_JUMP, BACKFLIP, SIDE_SOMERSAULT, DIVE,
                SPIN, VAULT, SLIDE);
        this.builder(AERIAL).add(JUMP, DOUBLE_JUMP, TRIPLE_JUMP, GROUND_POUND, GROUND_POUND_JUMP, WALL_JUMP, ROLL_JUMP, LONG_JUMP, BACKFLIP, SIDE_SOMERSAULT, DIVE, SPIN);
        this.builder(WALL).add(WALL_SLIDE, WALL_JUMP);
        this.builder(LEDGE).add(LEDGE_GRAB, VAULT);
        this.builder(SPEED).add(RUN, ROLL, ROLL_JUMP, LONG_JUMP, DIVE, SLIDE);
        this.builder(GROUND).add(RUN, SKID, CROUCH, ROLL, SLIDE, VAULT);
    }
}
