/**
 * 
 */
package ac;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.AppiaException;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.Direction;
import org.continuent.appia.core.Event;
import org.continuent.appia.core.Layer;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.message.Message;
import org.continuent.appia.protocols.common.InetWithPort;

import beb.BebBroadcastEvent;

import eld.TrustEvent;

import process.Process;
import process.ProcessInitEvent;
import process.ProcessSet;

/**
 * @author Ali Javadzadeh
 * 
 */
public class ACSession extends Session {
	private ProcessSet processes;
	private Channel channel;
	private int[] tempValue;

	private int[] val;

	private int[] wAcks;

	private int[] rts;

	private int[] wts;

	private int[] tstamp;

	private HashSet<Integer> seenIds;

	private Hashtable<Integer, HashSet<ValueWithTimestamp>> readSet;

	public static int MAX_SIZE = 100;
 //   int CONTENTARRAY_SIZE=5;
	int N;

	/*
	 * Builds a new BEBSession.
	 * 
	 * @param layer
	 */
	public ACSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		// Init events. Channel Init is from Appia and ProcessInitEvent is to
		// know
		// the elements of the group
	//	System.out.println("-------AC.handle------");
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof ProcessInitEvent)
			handleProcessInitEvent((ProcessInitEvent) event);
		else if (event instanceof ACProposeEvent)
			handleProposeEvent((ACProposeEvent) event);
		else if(event instanceof TrustEvent)
			handleTrustEvent(( TrustEvent)event);
		else if(event instanceof paxos.DecidedEvent)
			handleDecideEvent((paxos.DecidedEvent)event);
		else if (event instanceof SendableEvent)
			handleSendableEvent((SendableEvent) event);
	}

	private void CheckInstance(int id) {
		//System.out.println("-------AC.CheckInstance------");
		if (!seenIds.contains(id)) {
			tempValue[id] = val[id] = -1;// NULL
			wAcks[id] = rts[id] = wts[id] = 0;
			tstamp[id] = processes.getSelfRank();
			if(readSet.get(id)!=null)
				readSet.get(id).clear();
			else
			{
				//HashSet<ValueWithTimestamp> temp=new HashSet<ValueWithTimestamp>();
				readSet.put(new Integer(id), new HashSet<ValueWithTimestamp>());
			}
				//((HashSet<ValueWithTimestamp>) readSet..get(id))=new HashSet<ValueWithTimestamp>;
			seenIds.add(id);
			

		}
	}

	private void handleProcessInitEvent(ProcessInitEvent event) {
	//	System.out.println("-------AC.handleProcessInitEvent------");
		processes = event.getProcessSet();
		tempValue = new int[MAX_SIZE];
		val = new int[MAX_SIZE];
		wAcks = new int[MAX_SIZE];
		rts = new int[MAX_SIZE];
		wts = new int[MAX_SIZE];
		tstamp = new int[MAX_SIZE];
		seenIds = new HashSet<Integer>();
		readSet = new Hashtable<Integer, HashSet<ValueWithTimestamp>>();
		N = processes.getSize();
		seenIds.clear();
		try {
			event.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

	private void handleProposeEvent(ACProposeEvent event) {
		//System.out.println("---------AC. handleProposeEvent----------");
		CheckInstance(event.id);
		tstamp[event.id] = tstamp[event.id] + N;
		tempValue[event.id] = event.value;

		// trigger bebBroadcast READ
		BebBroadcastEvent bebEvent = new BebBroadcastEvent();
		bebEvent.contentArray[0] =2;
		bebEvent.contentArray[1] = event.id;
		bebEvent.contentArray[2] = tstamp[event.id];
		bebEvent.source = processes.getSelfProcess().getInetWithPort();
		bebEvent.setSource(this);
		bebEvent.setChannel(event.getChannel());
		
		bebEvent.setDir(Direction.DOWN);
		try {
			bebEvent.init();
			bebEvent.go();
		} catch (AppiaException e) {
			e.printStackTrace();
		}

	}
	private void handleTrustEvent( TrustEvent event)
	{
	//	System.out.println("-------AC.handleTrustEvent------");
		try
		{
			event.go();
		}
		catch(AppiaException ex)
		{
			ex.printStackTrace();
		}
	}

	private void handleSendableEvent(SendableEvent event) {
     //   System.out.println("----------ACSession.handleSendableEvent-----------");
		if (event.getDir() == Direction.UP) {
			
			Message msg =(Message) event.getMessage();
			Object objmsg=null;
			 
			String strmsg=msg.popString();// throw string header away
			objmsg= msg.popObject();	
			/*String array=msg.popString();
			 System.out.println("***************"+array+"***************");
			 StringTokenizer st=new StringTokenizer(array);
			 int[] msgContentArray=new int [MAX_SIZE];
			 int i=0;
			 while (st.hasMoreTokens())
			   msgContentArray[i++]=Integer.parseInt(st.nextToken());*/
			 int[] msgContentArray = (int[])objmsg;
			//int[] msgContentArray = (int[])objmsg;
			/*for(int i=0;i<msgContentArray.length;i++)
			ing str	System.out.print(msgContentArray[i]+",");*/
			int id = msgContentArray[1];
			int ts = msgContentArray[2];
			int v = msgContentArray[3];
			if (msgContentArray[0] == 1)// pp2pDeliver NACK
			{

				if(readSet.get(id)!=null) readSet.get(id).clear();
				wAcks[id] = 0;
			//	System.out.println("ACReturn NIL  for  " + id);// ACReturn
																// event
																// triggered
				acReturn(id, -1);//-1 for NIL
			} else if (msgContentArray[0] == 2)// bebDeliver READ
			{
				CheckInstance(id);
				System.out.println("bebDeliver READ "+"for id#"+id);
				if (rts[id] >= ts || wts[id] >= ts) {
					// trigger pp2pSend NACK
					Pp2pSendEvent p2pEvent=new Pp2pSendEvent();
					p2pEvent.contentArray[0]=1;
					p2pEvent.contentArray[1]=id;
					p2pEvent.source= processes.getSelfProcess().getInetWithPort();
					p2pEvent.dest=event.source;
					p2pEvent.setSource(this);
					p2pEvent.setChannel(event.getChannel());
					p2pEvent.setDir(Direction.DOWN);
				    try{
				    	p2pEvent.init();
				    	p2pEvent.go();
				    }
				    catch(AppiaException e)
				    {
				    	e.printStackTrace();
				    }
				} else {
					System.out.println("pp2pSend READACK to #"+getProcessRank((InetWithPort)(event.source))+" for id#"+id);
					rts[id] = ts;
					// trigger pp2pSend READACK
					Pp2pSendEvent p2pEvent=new Pp2pSendEvent();
					
					p2pEvent.contentArray[0]=4;
					p2pEvent.contentArray[1]=id;
					p2pEvent.contentArray[2]=ts;
					p2pEvent.contentArray[3]=v;
					p2pEvent.source= processes.getSelfProcess().getInetWithPort();
					p2pEvent.dest=event.source;
					p2pEvent.setSource(this);
					p2pEvent.setChannel(event.getChannel());
					
					p2pEvent.setDir(Direction.DOWN);
				    try{
				    	p2pEvent.init();
				    	p2pEvent.go();
				    }
				    catch(AppiaException e)
				    {
				    	e.printStackTrace();
				    }
				}

			} else if (msgContentArray[0] == 3)// bebDeliver WRITE
			{

				CheckInstance(id);
				if (rts[id] > ts || wts[id] > ts) {
					// trigger pp2pSend NACK
					System.out.println("pp2pSend NACK to"+getProcessRank((InetWithPort)(event.source))+" for id#"+id);
					Pp2pSendEvent p2pEvent=new Pp2pSendEvent();
					p2pEvent.contentArray[0]=1;
					p2pEvent.contentArray[1]=id;
					p2pEvent.setChannel(event.getChannel());
					p2pEvent.source= processes.getSelfProcess().getInetWithPort();
					p2pEvent.dest=event.source;
					p2pEvent.setSource(this);
					p2pEvent.setDir(Direction.DOWN);
				    try{
				    	p2pEvent.init();
				    	p2pEvent.go();
				    }
				    catch(AppiaException e)
				    {
				    	e.printStackTrace();
				    }
				} else {
					val[id] = v;
					wts[id] = ts;

					// trigger pp2pSend WRITEACK
					System.out.println("pp2pSend WRITEACK to #"+getProcessRank((InetWithPort)(event.source))+" for id#"+id);
					Pp2pSendEvent p2pEvent=new Pp2pSendEvent();
					p2pEvent.contentArray[0]=5;
					p2pEvent.contentArray[1]=id;
					
					p2pEvent.source= processes.getSelfProcess().getInetWithPort();
					p2pEvent.dest=event.source;
					p2pEvent.setSource(this);
					p2pEvent.setChannel(event.getChannel());
					
					p2pEvent.setDir(Direction.DOWN);
				    try{
				    	p2pEvent.init();
				    	p2pEvent.go();
				    }
				    catch(AppiaException e)
				    {
				    	e.printStackTrace();
				    }

				}

			} else if (msgContentArray[0] == 4) // pp2pDeliver READACK
			{
				System.out.println("pp2pDeliver READACK from #"+getProcessRank((InetWithPort)(event.source))+" for id#"+id);
				readSet.get(id).add(new ValueWithTimestamp(ts, v));
				if (readSet.get(id).size() > N / 2) {
					ValueWithTimestamp highestValuewithTimestamp = highestTS(id);
					if (highestValuewithTimestamp.v == -1) {
						tempValue[id] = v;
					}
					// trigger bebBroadcast WRITE
					System.out.println("trigger bebBroadcast WRITE "+" for id#"+id);
					BebBroadcastEvent bebEvent=new BebBroadcastEvent();
				    bebEvent.contentArray[0]=3;
				    bebEvent.contentArray[1]=id;
				    bebEvent.contentArray[2]=ts;
				    bebEvent.contentArray[3]=v;
				    bebEvent.source= processes.getSelfProcess().getInetWithPort();
				    bebEvent.setSource(this);
				    bebEvent.setChannel(event.getChannel());
				    bebEvent.setDir(Direction.DOWN);
				    try{
				    bebEvent.init();
				    bebEvent.go();
				    }
				    catch(AppiaException e)
				    {
				    	e.printStackTrace();
				    }
					
				}
			} else if (msgContentArray[0] == 5)// pp2pDeliver WRITEACK
			{
				System.out.println("pp2pDeliver WRITEACK from #"+getProcessRank((InetWithPort)(event.source))+" for id#"+id);
				wAcks[id] = wAcks[id] + 1;
				if (wAcks[id] > N / 2) {
					if(readSet.get(id)!=null)
					   readSet.get(id).clear();
					wAcks[id] = 0;
					//System.out
						//	.print("ACReturn " + tempValue[id] + " for " + id);// trigger
																				// acReturn
																				// event
					acReturn(id, tempValue[id]);

				}
			}
			 
		} else {
			try {
				event.go();
			} catch (AppiaException ex) {
				ex.printStackTrace();
			}
		}
		
	}

	//
	//

	/**
	 * Handles the first event that arrives to the protocol session. In this
	 * case, just forwards it.
	 * 
	 * @param init
	 */
	private void handleChannelInit(ChannelInit e) {
	//	System.out.println("-------AC.handleChannelInit------");
		channel = e.getChannel();

		try {
			e.go();
		} catch (AppiaEventException ex) {
			ex.printStackTrace();
		}

	}
	public ValueWithTimestamp highestTS(int id) {
		Iterator it = readSet.get(id).iterator();
		ValueWithTimestamp maxvtmp = null;
		if (it.hasNext()) {
			maxvtmp = (ValueWithTimestamp) it.next();

			while (it.hasNext()) {
				ValueWithTimestamp temp = (ValueWithTimestamp) it.next();
				if (temp.ts > maxvtmp.ts) {
					maxvtmp = temp;
				}

			}

		}
		return maxvtmp;
	}
	public void acReturn(int id,int result)
	{
	//	System.out.println("-------AC.acReturn------");
		try
		{
		 ACReturnEvent ar=new ACReturnEvent(id,result);
		 ar.source = processes.getSelfProcess().getInetWithPort();
		 ar.dest=processes.getSelfProcess().getInetWithPort();
		 ar.setDir(Direction.UP);
		 ar.setSource(this);
		 ar.setChannel(this.channel);
		 ar.init();
		 ar.go();
	    }
	catch(AppiaException e)
	{
		e.printStackTrace();
	}
		
  }
	private void handleDecideEvent( paxos.DecidedEvent event)
	{
		//System.out.println("-------AC.handleDecideEvent------");
		try
		{
		 event.go();	
		}
		catch(AppiaException e)
		{
			e.printStackTrace();
		}
	}
	public int getProcessRank(InetWithPort srcAddr) {
		Process[] prArray = processes.getAllProcesses();
		int sRank = -1;
		for (int i = 0; i < processes.getSize(); i++) {
			if (prArray[i].getInetWithPort().host.equals(srcAddr.host)
					&& prArray[i].getInetWithPort().port == srcAddr.port) {
				sRank = i;
				break;
			}
		}
		return sRank;
	}

}
