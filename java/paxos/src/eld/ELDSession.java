package eld;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.continuent.appia.protocols.common.InetWithPort;

import beb.BebBroadcastEvent;

import ac.Pp2pSendEvent;

import process.ProcessInitEvent;
import process.ProcessSet;
import process.Process;


public class ELDSession extends Session {

	private ProcessSet processes;

	private Channel channel;

	private HashSet candidateSet;

	

	private long timedelay = 1;
	//private long bigDelta=1;

	private long delta = 1;// all link delays are less than this

	private long period = -1;
	private int leader=-1;
	private int newLeader=-1;
	

	public ELDSession(Layer layer) {
		super(layer);

	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void handle(Event e) {
		// System.out.println("---------ELDSession.handle---------");
		if (e instanceof ChannelInit) {
			handleChannelInit((ChannelInit) e);
		}

		else if (e instanceof ProcessInitEvent) 
			handleProcessInitEvent((ProcessInitEvent) e);
			else if (e instanceof BebBroadcastEvent) 
				handleBebBroadcastEvent((BebBroadcastEvent) e);
			else if (e instanceof Pp2pSendEvent) 
				handlePp2pSendEvent((Pp2pSendEvent) e);
			else if(e instanceof paxos.DecidedEvent)
				handleDecideEvent((paxos.DecidedEvent)e);
		 else if (e instanceof SendableEvent) 
			handleSendableEvent((SendableEvent) e);
		 else if (e instanceof Timeout) 
			handleTimeout((Timeout) e);

		

		else
			debug("Unknown Event");
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void handleSendableEvent(SendableEvent e) {
		// try{
		// System.out.println("ELDSession.handleSendableEvent");
		String msg = e.getMessage().popString();
		e.getMessage().pushString(msg);
        
		if(e.getDir()==Direction.UP && msg.equals("H"))
		{
			
			//System.out.println("HeartBeat received from "
				//	+ getProcessRank((InetWithPort) e.source) + "@ "
					//+ getCurTime());
			int src = getProcessRank((InetWithPort) e.source);
			candidateSet.add(src);
			//Iterator it1 = candidateSet.iterator();
			//System.out.println("candidateset:");
			//while (it1.hasNext()) {
			//	System.out.print(Integer.valueOf(it1.next().toString()) + ",");
			//}
			
		}
		else 
			if(e.getDir()==Direction.UP && (msg.equals("B") || msg.equals("P")))
			{
			//	System.out.println("----"+msg+"---+++++");
				try{
				//	e.getMessage().popString();
				e.go();
				}
				catch(AppiaException ex)
				{
					ex.printStackTrace();
				}
			}
		
		

	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void handleBebBroadcastEvent(BebBroadcastEvent e)
	{
		//System.out.println("--------------ELDSession.handleBebBroadcastEvent--------------");
	try
	{
	 e.go();
	}
	catch(AppiaException ex)
	{
		ex.printStackTrace();
	}
		
	}
	public void handlePp2pSendEvent(Pp2pSendEvent e)
	{
	//	System.out.println("--------------ELDSession.handlePp2pSendEvent----------");
		try
		{
		 e.go();
		}
		catch(AppiaException ex)
		{
			ex.printStackTrace();
		}
		
	}
	private void handleChannelInit(ChannelInit e) {
	//	System.out.println("--------------ELDSession.handleChannelInit-----------");
		channel = e.getChannel();

		try {
			e.go();
		} catch (AppiaEventException ex) {
			ex.printStackTrace();
		}

	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void debug(String msg) {
		System.out.println("DelaySession:: " + msg);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handleProcessInitEvent(ProcessInitEvent event) {
		try {
		//	System.out.println("--------------ELDSession.handleProcessInitEvent----------");
			//System.out.println("ELD.handleProcessInitEvent");
			processes = event.getProcessSet();
			processes.setMatrix(event.getProcessSet().getMatrix());
			candidateSet = new HashSet();
			period = timedelay;
			Process[] prArray = processes.getAllProcesses();
		    this.delta=processes.getDelta();
		    this.timedelay=processes.getTimeDelay();
			int min=processes.getSelfRank();//processes.getProcess(0).getProcessNumber();
			leader=min;
			
			
			trust(leader);
			
			period=timedelay;
			sendHeartbeat();
			candidateSet.clear();
			Timeout checkTimer = new Timeout(period, this.channel, this,
					null);
			checkTimer.init();
			checkTimer.go();
			event.go();
		//	System.out.println("Init finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handleTimeout(Timeout e) {
		//System.out.println("--------------ELDSession.handleTimeout-----------");
		//select process with min process# as leader
		//System.out.println("---------Timeout----------");
		//Process[] processArray=processes.getAllProcesses();
		Iterator it= candidateSet.iterator();
		Object minObj=null;
		//System.out.print("candidateset:");
		int min=processes.getSelfRank();
		while (it.hasNext()) {
			Object obj=it.next();
			int rank=Integer.valueOf(obj.toString());
		//	System.out.print(rank + ",");
			if(rank<min)
			  min=rank;
			
		}
		//System.out.println();
		
		
		
		
		newLeader=min;
		if(leader!=newLeader)
		{
			period+=delta;
			leader=newLeader;
			//System.out.println("*********************Trust to leader "+leader+"*******************");
			trust(leader);
		}	
		sendHeartbeat();
		candidateSet.clear();
		try
		{
		Timeout checkTimer = new Timeout(period, this.channel, this,null);
		checkTimer.init();
		checkTimer.go();
		}
		catch(AppiaException e1)
		{
			e1.printStackTrace();
		}
		
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public int getNumOfNeighbours() {
		int n = 0;
		int[][] matrix = processes.getMatrix();
		int self = processes.getSelfRank();
		for (int i = 0; i < processes.getSize(); i++)
			if (matrix[self][i] > 0)
				n++;
		return n;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
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

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public boolean isNeighbour(int node) {

		return (processes.getMatrix()[processes.getSelfRank()][node] > 0);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public String getCurTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}
	public void sendHeartbeat()
	{
		//System.out.println("--------------ELDSession.sendHeartbeat-----------");
		for (int i = 0; i < processes.getSize(); i++) {
		 SendableEvent he = new SendableEvent();
		 he.source = processes.getSelfProcess().getInetWithPort();
		 he.dest = processes.getProcess(i).getInetWithPort();
		 he.getMessage().pushString("H");
		 he.setChannel(this.channel);
		 he.setSource(this);
		 he.setDir(Direction.DOWN);
		 try
		 {
		 he.init();
		 he.go();
		// System.out.println("HeartBeat Sent to " + i + "@ "
			//	+ getCurTime());
		}
		catch(AppiaException e)
		{
			e.printStackTrace();
		}
	}

	}
	public void trust(int leaderID)
	{
		try
		{
		//	System.out.println("--------------ELDSession.trust---------------");
		 TrustEvent te=new TrustEvent(leaderID);
		 te.source = processes.getSelfProcess().getInetWithPort();
		 te.dest=processes.getSelfProcess().getInetWithPort();
		 te.setDir(Direction.UP);
		 te.setSource(this);
		 te.setChannel(this.channel);
		 te.init();
		 te.go();
	    }
	catch(AppiaException e)
	{
		e.printStackTrace();
	}
		
  }
	private void handleDecideEvent( paxos.DecidedEvent event)
	{
	//	System.out.println("--------------ELDSession.handleDecideEvent----------");
		try
		{
		 event.go();	
		}
		catch(AppiaException e)
		{
			e.printStackTrace();
		}
	}

}
