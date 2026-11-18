package fr.hugman.mubble.splatoon.world.level.attachment;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.world.level.ink.ChunkInk;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class SplatoonAttachmentTypes {
    /**
     * The ink painted on a chunk. It is saved with the chunk, but sent to clients by {@link fr.hugman.mubble.splatoon.world.level.ink.InkSync}
     * rather than by the attachment API, so that painting a few cells does not resend the whole chunk.
     */
    public static final AttachmentType<ChunkInk> CHUNK_INK = AttachmentRegistry.create(Splatoon.id("ink"), builder -> builder
            .persistent(ChunkInk.CODEC)
            .initializer(ChunkInk::new));
}
