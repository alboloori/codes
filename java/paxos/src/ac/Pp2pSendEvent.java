package ac;

import org.continuent.appia.core.events.SendableEvent;

public class Pp2pSendEvent extends SendableEvent {
    int ARRAY_SIZE=5;
	public int [] contentArray;
	public Pp2pSendEvent()
	{
		 contentArray=new int[ARRAY_SIZE];
	}
}
