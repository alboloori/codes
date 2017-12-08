package eld;

import org.continuent.appia.core.Direction;
import org.continuent.appia.core.events.SendableEvent;

public class TrustEvent extends SendableEvent {
	public int leaderID;
public TrustEvent(int id)
{
	leaderID=id;
	
	
}

}
