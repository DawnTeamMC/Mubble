package hugman.mubble.objects.block;

public class SuperNoteBlock extends NoteBlock
{    
    public SuperNoteBlock(Properties builder)
    {
		super(builder);
	}

	@Override
    public double getProperLaunchMotion()
    {
    	return 1.5D;
    }
}
