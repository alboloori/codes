package paxos;

import java.util.HashSet;
import java.util.Iterator;

import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.AppiaException;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.Direction;
import org.continuent.appia.core.Event;
import org.continuent.appia.core.Layer;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.events.channel.ChannelInit;

import beb.BebBroadcastEvent;

import ac.ACProposeEvent;
import ac.Pp2pSendEvent;
import ac.ValueWithTimestamp;
import eld.Timeout;
import eld.TrustEvent;

import process.ProcessInitEvent;
import process.ProcessSet;

public class PaxosSession extends Session
{
	 
	private ProcessSet processes;

	private Channel channel;
	private HashSet<Integer> seenIds;
	private boolean leader;
	private int[] proposal;
	private boolean[] proposed;
	private boolean[] decided;
	public static int MAX_SIZE=100;
	
	
	  /**
	   * Builds a new BEBSession.
	   * 
	   * @param layer
	   */
//	 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   public PaxosSession(Layer layer) 
   {
	    super(layer);
   }
//	 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	  public void handle(Event e) {
	//	 System.out.println("---------PaxosSession.handle---------");
		if (e instanceof ChannelInit) {
			handleChannelInit((ChannelInit) e);
		}

		else if (e instanceof ProcessInitEvent) 
			handleProcessInitEvent((ProcessInitEvent) e);
			 
		 else if (e instanceof eld.TrustEvent)
		 {
			 handleTrustEvent((eld.TrustEvent)e);
		 }
		 else if (e instanceof ac.ACReturnEvent)
		 {
			 handleACReturnEvent((ac.ACReturnEvent)e);
		 }
		 else if (e instanceof paxos.DecidedEvent)
		 {
			 handleDecidedEvent((paxos.DecidedEvent)e);
		 }
		 else if (e instanceof SendableEvent) 
			handleSendableEvent((SendableEvent) e);
		 
 
	}
	  
//	 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
	  public void handleSendableEvent(SendableEvent e) 
	  {
		//  System.out.println("--------Paxos.handleSendableEvent()--------");
		  if(e.getDir()==Direction.DOWN)//coming from app
		  {
			  int k=processes.getK(); //Integer.parseInt(e.getMessage().popString().trim());
			  for(int i=1;i<=k;i++)
			    ucPropose(i, processes.getSelfRank());//temporary
			 
		  }
	  }
//	 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	  private void handleProcessInitEvent(ProcessInitEvent event) 
	  {
		  try
		  {
		 // System.out.println("------Paxos.handleProcessInitEvent------------");
		  processes = event.getProcessSet();
		  processes.setMatrix(event.getProcessSet().getMatrix());
		  proposal=new int[MAX_SIZE];
		  proposed=new boolean[MAX_SIZE];
		  decided=new boolean[MAX_SIZE];
		  seenIds=new HashSet<Integer> ();
		  leader=false;
		  seenIds.clear();
			event.go();
			//	System.out.println("Init finished");
			} catch (Exception e) {
				e.printStackTrace();
			}
	  }
	  
	  private void handleChannelInit(ChannelInit e) {
		    // System.out.println("--------Paxos.handleChannelInit----------------");
			channel = e.getChannel();

			try {
				e.go();
			} catch (AppiaEventException ex) {
				ex.printStackTrace();
			}

		}

public void	 handleTrustEvent( TrustEvent e)
{
	System.out.println( "*********#"+processes.getSelfRank()+" trusts process #"+e.leaderID+" as leader *************");
	if(e.leaderID==processes.getSelfRank())
	{
		leader=true;
		Iterator< Integer> it=seenIds.iterator();
		while(it.hasNext())
		{
		  int id=(int)it.next();
		  shouldPropose(id);
		}
	}
	else
	{
		leader=false;
	}
}
public void handleACReturnEvent( ac.ACReturnEvent e)
{
	System.out.println("+++++++++++"+(e.result>0?e.result:"NIL")  +" Returned for instance#"+e.id+"+++++++++++");
	if(e.result!=-1)
	{
		trigger_beb_Decided(e.id,e.result);
	}
	else 
	{
		proposed[e.id]=false;
		shouldPropose(e.id);
	}
}

private void CheckInstance(int id) {
	//System.out.println("-------Paxos.CheckInstance------");
	if (!seenIds.contains(id)) {
	    proposal[id]=-1;
	    proposed[id]=decided[id]= false;
		seenIds.add(id);
		

	}
}

private void shouldPropose(int id) {
	System.out.println("---------shouldPropose #"+id+"------");
	if (leader==true && proposed[id]==false && proposal[id]!=-1) {
	    
	    proposed[id]=true;
	    //trigger acPropose
		ACProposeEvent acPropose=new ACProposeEvent(id,proposal[id]);
		 acPropose.source= processes.getSelfProcess().getInetWithPort();
		 acPropose.setSource(this);
		 acPropose.setChannel(this.channel);
		 acPropose.setDir(Direction.DOWN);
		// acPropose.getMessage().pushString(msg);
		    try{
		    	acPropose.init();
		    	 acPropose.go();
		    }
		    catch(AppiaException ex)
		    {
		    	ex.printStackTrace();
		    }
	}

	}
  private void ucPropose(int id, int v)
  {
	 System.out.println("------Paxos.ucPropose----");
	CheckInstance(id);
	proposal[id]=v;
	shouldPropose(id);
  }
  private void trigger_beb_Decided(int id, int v)
  {
	  System.out.println("------Paxos.trigger_beb_Decided----");
	 DecidedEvent de=new DecidedEvent(id,v);
	 de.source= processes.getSelfProcess().getInetWithPort();
	 de.setSource(this);
	 de.setChannel(this.channel);
	 de.setDir(Direction.DOWN);
	 try
	 {
		de.init();
		de.go();
	 }
	 catch(AppiaException ex)
	 {
		 ex.printStackTrace();
	 }
  }
  private void handleDecidedEvent( paxos.DecidedEvent e)
  {
	  //System.out.println("------Paxos.handleDecidedEvent------");
   if(e.getDir()==Direction.UP)
   {
	  CheckInstance(e.id);
	  if(decided[e.id]==false)
	  {
	    decided[e.id]=true;
		  System.out.println("#-#-#-#  UC Decides For Consensus Instance#"+e.id+" With Value= "+e.result+"  #-#-#-#");
	  }
	   
   }
  }

}
