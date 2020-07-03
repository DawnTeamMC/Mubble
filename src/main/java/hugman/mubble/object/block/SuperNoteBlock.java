package hugman.mubble.object.block;

public class SuperNoteBlock extends NoteBlock {
	public SuperNoteBlock(Settings builder) {
		super(builder);
	}

	@Override
	public double getProperLaunchMotion() {
		return 1.5D;
	}
}
