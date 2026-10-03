package fr.hugman.mubble.world.arcade;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.arcade.access.ArcadeSources;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class ArcadeAttachments {
    /**
     * The sources of a player besides their equipment. Only their own client hears about them: it is
     * the one predicting their movement.
     */
    public static final AttachmentType<ArcadeSources> SOURCES = AttachmentRegistry.<ArcadeSources>builder()
            .persistent(ArcadeSources.CODEC)
            .copyOnDeath()
            .syncWith(ArcadeSources.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
            .buildAndRegister(Mubble.id("arcade_sources"));

    /** The moves a player owns. They survive death, like any story progress. */
    public static final AttachmentType<ArcadeUnlocks> UNLOCKS = AttachmentRegistry.<ArcadeUnlocks>builder()
            .persistent(ArcadeUnlocks.CODEC)
            .copyOnDeath()
            .syncWith(ArcadeUnlocks.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
            .buildAndRegister(Mubble.id("arcade_unlocks"));
}
