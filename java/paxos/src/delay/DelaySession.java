/*
 *
 * APPIA: Protocol composition and execution framework
 * Copyright (C) 2005 Laboratorio de Sistemas Informaticos de Grande Escala (LASIGE)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * 
 * Contact
 * 	Address:
 * 		LASIGE, Departamento de Informatica, Bloco C6
 * 		Faculdade de Ciencias, Universidade de Lisboa
 * 		Campo Grande, 1749-016 Lisboa
 * 		Portugal
 * 	Email:
 * 		appia@di.fc.ul.pt
 * 	Web:
 * 		http://appia.di.fc.ul.pt
 * 
 */
package delay;

import java.util.HashSet;

import org.continuent.appia.core.*;
import org.continuent.appia.protocols.common.InetWithPort;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.message.Message;

import ac.Pp2pSendEvent;

import process.ProcessInitEvent;
import process.ProcessSet;
import process.Process;



// import appia.protocols.group.intra.*;
// import appia.protocols.group.sync.*;
// import appia.protocols.group.*;

// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
public class DelaySession extends Session {
	private ProcessSet processes;

	// private ViewState vs;

	private Channel channel;
    //private String tempHeader;
    //private int[] tempArray;
    //private HashSet<String> timerSet;
	// private LinkedList<InitiateEchoEvent> initEventList=new LinkedList
	// <InitiateEchoEvent>();
	// private LinkedList<EchoEvent> echoEventList = new LinkedList
	// <EchoEvent>();
	public DelaySession(Layer layer) {
		super(layer);

	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void handle(Event e) {
		// System.out.println("---------DelaySession.handle---------");
		try{
		if (e instanceof ChannelInit) {
			handleChannelInit((ChannelInit) e);
		}

		else if (e instanceof delay.DelayTimer) {
			handleDelayTimer((delay.DelayTimer) e);
		} else if (e instanceof ProcessInitEvent) {
			handleProcessInitEvent((ProcessInitEvent) e);
		}
		else if (e instanceof Pp2pSendEvent ) {
			handlePp2pSendEvent((Pp2pSendEvent) e);
		}
		else if (e instanceof SendableEvent) {
			handleSendableEvent((SendableEvent) e);
		}

		else
			debug("Unknown Event");
		}
		catch (Exception e1)
		{
			System.out.println("!!!!!!!!!!!Exception in delay!!!!!!!!!!!");
			e1.printStackTrace();
			
		}
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void handleSendableEvent(SendableEvent e) {
		try {
			//System.out.println("DelaySession.handleSendableEvent");
			String msg = e.getMessage().popString();
			e.getMessage().pushString(msg);
			
			
	//		System.out.println(msg+" delivered to delay");
		if (e.getDir() == Direction.DOWN) {
			
			if (!msg.equals("H")) {
		//		System.out.println("!!!??????????"+msg);
			//System.out.println("DelaySession.handleSendableEvent");
				//    System.out.println(msg);
				
				
					String msg1 = e.getMessage().popString();
					//tempHeader=msg1;
					
					Object obj=e.getMessage().popObject();
					
					e.getMessage().pushObject(obj);
					e.getMessage().pushString(msg1);
					//tempArray=(int[])(obj);
				
					int[][] matrix = processes.getMatrix();// e.getProcessSet().getSelfProcess().getMatrix();
					int selfRank = processes.getSelfRank();// e.getProcessSet().getSelfRank();
					InetWithPort destAddr = (InetWithPort) e.dest;
					//
					int destIndex = getProcessRank(destAddr);
					int n = matrix[selfRank][destIndex];
					
				/*!!!!!*/	DelayTimer dt = new DelayTimer(n, e.getChannel(), this, e,
							selfRank, destIndex);
					//dt.init();
					
				//	System.out.println("Sending message<"+msg1+"> from:" + selfRank
					//		+ " to " + destIndex + " delayed for: " + n
						//	+ " Seconds ");
				//	dt.setArray((int[])(obj));
					//dt.setHeader(msg1);
					dt.init();
					//timerSet.add(dt.timerID);
			//		System.out.println("------Timer "+dt.timerID+"set------");
					dt.go();

					// e.init();o

				} else
				{
					
					e.go();
				}

			}

			else
			{
			//	if(!msg.equals("H"))
				//if(msg.equals("P"))
				//	System.out.println("DelaySession.handleSendableEvent--"+msg+"*********");
				e.go();
			}
		} catch (AppiaEventException ex) {
			ex.printStackTrace();
		} catch (AppiaException et) {
			et.printStackTrace();
		}
		catch (Exception e1)
		{
			System.out.println("!!!!!!!!!!!Exception in delay!!!!!!!!!!!");
			e1.printStackTrace();
			
		}
	}

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handlePp2pSendEvent(Pp2pSendEvent e)
	{
		try
		{
		//	System.out.println("-----DelaySession.handlePp2pSendEvent---------Dir:"+e.getDir());
			if(e.getDir()==Direction.DOWN)
			{
		    SendableEvent sendingEvent = new SendableEvent();
		    // set source and destination of event message
		    sendingEvent.source = processes.getSelfProcess().getInetWithPort();
		    sendingEvent.setChannel(e.getChannel());
		    sendingEvent.dest=e.dest;
		    sendingEvent.getMessage().pushObject(e.contentArray);
		    sendingEvent.getMessage().pushString("P");
		    sendingEvent.setSource(this);
		    sendingEvent.setDir(Direction.DOWN);
		    sendingEvent.init();
		    int[][] matrix = processes.getMatrix();// e.getProcessSet().getSelfProcess().getMatrix();
			int selfRank = processes.getSelfRank();// e.getProcessSet().getSelfRank();
			InetWithPort destAddr = (InetWithPort) e.dest;
			//
			int destIndex = getProcessRank(destAddr);
			int n = matrix[selfRank][destIndex];
		    DelayTimer dt = new DelayTimer(n, e.getChannel(), this, sendingEvent,
					selfRank, destIndex);
			//dt.init();
			
		//	System.out.println("Sending message<"+msg1+"> from:" + selfRank
			//		+ " to " + destIndex + " delayed for: " + n
				//	+ " Seconds ");
		//	dt.setArray((int[])(obj));
			//dt.setHeader(msg1);
			dt.init();
			//timerSet.add(dt.timerID);
	//		System.out.println("------Timer "+dt.timerID+"set------");
			dt.go();
		    //sendingEvent.go();
		  
			}
			else e.go();
		}
		catch(AppiaException ex)
		{
			ex.printStackTrace();
		}
				
	}
//	 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handleDelayTimer(delay.DelayTimer e) {
		try {
              
		
		
			
			//System.out.println("@@@@@@@DelaySession.handleDelayTimer---timerID:"+e.timerID);
			
			//String msg1 = e.event.getMessage().popString();
			
			//tempHeader=msg1;
			
			//Object obj=e.event.getMessage().popObject();
			//int[] array=(int[])(obj);
			//e.event.getMessage().pushObject(obj);
			//e.event.getMessage().pushString(msg1);
			
		 // e.event.getMessage().pushObject(tempArray);
			//e.event.getMessage().pushString(tempHeader);
			//e.event.getMessage().pushObject(e.getArray());
			//e.event.getMessage().pushString(e.getHeader());
			
			// System.out.println("Token sent from:"+e.source+" to "+e.dest);//+
			// " for: "+e.delay+ " Seconds ");
		//	e.event.setMessage(message);
			//System.out.println("After Delay Timeout:<"+msg1+",id:"+array[0]+",val="+array[1]+"> sent to"+(InetWithPort)e.event.dest);
			e.event.go();
			//timerSet.remove(e.timerID);
		
		} catch (AppiaEventException ex) {
			ex.printStackTrace();
		}
		catch (Exception e1)
		{
			System.out.println("!!!!!!!!!!!Exception in delay!!!!!!!!!!!");
			e1.printStackTrace();
			
		}
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handleChannelInit(ChannelInit e) {
		this.channel = e.getChannel();

		try {
			e.go();
		} catch (AppiaEventException ex) {
			ex.printStackTrace();
		}
		catch (Exception e1)
		{
			System.out.println("!!!!!!!!!!!Exception in delay!!!!!!!!!!!");
			e1.printStackTrace();
			
		}

	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void debug(String msg) {
		System.out.println("DelaySession:: " + msg);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handleProcessInitEvent(ProcessInitEvent event) {
		//System.out.println("DelaySession.handleProcessInitEvent");
		try {

			processes = event.getProcessSet();
			
		//	timerSet=new HashSet <String>(); 
			/*Process[] pr=processes.getAllProcesses();
			for(int i=0;i<pr.length;i++)
				System.out.println(pr[i].getProcessNumber()+" "+pr[i].getInetWithPort());*/
			
			//int[][]
			event.go();

		} catch (Exception e) {
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
