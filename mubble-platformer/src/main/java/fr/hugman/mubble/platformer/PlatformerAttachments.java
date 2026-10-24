package fr.hugman.mubble.platformer;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.access.PlatformerSources;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class PlatformerAttachments {
    /**
     * The sources of a player besides their equipment. Only their own client hears about them: it is
     * the one predicting their movement.
     */
    public static final AttachmentType<PlatformerSources> SOURCES = AttachmentRegistry.<PlatformerSources>builder()
            .persistent(PlatformerSources.CODEC)
            .copyOnDeath()
            .syncWith(PlatformerSources.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
            .buildAndRegister(Mubble.id("platformer_sources"));

    /** The moves a player owns. They survive death, like any story progress. */
    public static final AttachmentType<PlatformerUnlocks> UNLOCKS = AttachmentRegistry.<PlatformerUnlocks>builder()
            .persistent(PlatformerUnlocks.CODEC)
            .copyOnDeath()
            .syncWith(PlatformerUnlocks.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
            .buildAndRegister(Mubble.id("platformer_unlocks"));
}
