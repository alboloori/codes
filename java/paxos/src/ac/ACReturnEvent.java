package ac;

import org.continuent.appia.core.events.SendableEvent;



public class ACReturnEvent extends SendableEvent {
	public int id;
	public int result;
public ACReturnEvent(int id,int r)
{
	this.id=id;
	this.result=r;
}

}
