package ac;

import org.continuent.appia.core.events.SendableEvent;

public class ACProposeEvent extends SendableEvent {
   public int id;
   public int value;
   public ACProposeEvent(int id,int v)
   {
	   this.id=id;
	   this.value=v;
   }
}
