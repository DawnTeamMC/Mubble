package hugman.mubble.objects.item;

import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public class InstrumentItem extends Item
{
	protected final SoundEvent sound;
	
    public InstrumentItem(Item.Properties builder, SoundEvent soundIn)
    {
        super(builder);
        this.sound = soundIn;
    }
    
    public SoundEvent getInstrumentSound()
    {
		return sound;
	}
}
