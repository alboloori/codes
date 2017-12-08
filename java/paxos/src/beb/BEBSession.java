package beb;


/*
 *
 * Hands-On code of the book Introduction to Reliable Distributed Programming
 * by Rachid Guerraoui and Luís Rodrigues
 * Copyright (C) 2005 Luís Rodrigues
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
 *		Departamento de Informática, FCUL, 
 *		Bloco C6, Sala 6.3.12, Campo Grande, 
 *		1749-016 Lisboa, 
 *		PORTUGAL
 * 	Email:
 * 		ler@di.fc.ul.pt
 * 	Web:
 *		http://www.di.fc.ul.pt/~ler/
 * 
 */

import org.continuent.appia.core.*;
import org.continuent.appia.protocols.common.InetWithPort;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.events.SendableEvent;

import ac.Pp2pSendEvent;

import paxos.DecidedEvent;
import process.ProcessInitEvent;
import process.ProcessSet;
import process.Process;



/**
 * Session implementing the Basic Broadcast protocol.
 * 
 * @author nuno
 * 
 */
public class BEBSession extends Session {

  /*
   * State of the protocol: the set of processes in the group
   */
  private ProcessSet processes;

  /**
   * Builds a new BEBSession.
   * 
   * @param layer
   */
  Channel channel;
  public BEBSession(Layer layer) {
    super(layer);
  }

  /**
   * Handles incoming events.
   * 
   * @see appia.Session#handle(appia.Event)
   */
  public void handle(Event event) {
	 // System.out.println("-------BEBSession.handle------");
    // Init events. Channel Init is from Appia and ProcessInitEvent is to know
    // the elements of the group
    if (event instanceof ChannelInit)
      handleChannelInit((ChannelInit) event);
    else
    	if (event instanceof ProcessInitEvent)
      handleProcessInitEvent((ProcessInitEvent) event);
    else if(event instanceof BebBroadcastEvent)
    	handleBebBroadcastEvent((BebBroadcastEvent)event);
    else if(event instanceof Pp2pSendEvent)
    	handlePp2pSendEvent((Pp2pSendEvent)event);
    else if(event instanceof paxos.DecidedEvent)
		handleDecideEvent((paxos.DecidedEvent)event);	    
    else if (event instanceof SendableEvent) 
       handleSendableEvent((SendableEvent)event);
    
    
  }

  /**
   * Gets the process set and forwards the event to other layers.
   * 
   * @param event
   */
  public void handleSendableEvent( SendableEvent event)
  {
	 // System.out.println("-------BEBSession.handleSendableEvent------");
	  {
	      if (event.getDir() == Direction.DOWN)
	      {
	        // UPON event from the above protocol (or application)
	    	  String msg =((SendableEvent) event).getMessage().popString();
	    	 // System.out.println(msg+" delivered to beb");
	    	  ((SendableEvent) event).getMessage().pushString(msg);
	    	  if(msg.equals("H"))
	    	  {
	    		  try {
		    	      event.go();
		    	    } catch (AppiaEventException e) {
		    	      e.printStackTrace();
		    	    }
	    	  }
	    	 
	    	  else
	    	  {
	    		  try {
		    	      event.go();
		    	    } catch (AppiaEventException e) {
		    	      e.printStackTrace();
		    	    }
	    	  }
	    		  
	        /* if(!msg.equals("H"))//ride moooooonnnnnnnnnnn
	         {
	        	 
	         
	           bebBroadcast((SendableEvent) event);
	         }
	         else 
	        	 try{
	        		 msg =((SendableEvent) event).getMessage().popString();
	        		 if(msg.equals("D"))
	        		 {
	        			 Object objArray=((SendableEvent) event).getMessage().popObject();
	        			 int[] array=(int[])objArray;
	        			 int id=array[0];
	        			 int v=array[1];
	        			 DecidedEvent decided=new DecidedEvent(id,v);
	        			 decided.source = processes.getSelfProcess().getInetWithPort();
	        			 decided.dest=processes.getSelfProcess().getInetWithPort();
	        			 decided.setDir(Direction.UP);
	        			 decided.setSource(this);
	        			 decided.setChannel(event.getChannel());
	        			 decided.init();
	        			 decided.go();
	        		 }
	        		 else event.go();
	        	 }
	             catch(AppiaException e)
	             {
	            	 e.printStackTrace();
	             }*/
	        	 
	      }
	      else
	      {
	        // UPON event from the bottom protocol (or perfect point2point links)
	    	  String msg =((SendableEvent) event).getMessage().popString();
    	   //   System.out.println(msg+" delivered to beb");
    	  ((SendableEvent) event).getMessage().pushString(msg);
    	  
 		 if(msg.equals("D"))
 		 {
 			 msg =((SendableEvent) event).getMessage().popString();
 			 Object objArray=((SendableEvent) event).getMessage().popObject();
 			 int[] array=(int[])objArray;
 			 int id=array[0];
 			 int v=array[1];
 			 DecidedEvent decided=new DecidedEvent(id,v);
 			 decided.source = processes.getSelfProcess().getInetWithPort();
 			 decided.dest=processes.getSelfProcess().getInetWithPort();
 			 decided.setDir(Direction.UP);
 			 decided.setSource(this);
 			 decided.setChannel(event.getChannel());
 			 try {
 				decided.init();
 	 			 decided.go();
	    	    } catch (AppiaEventException e) {
	    	      e.printStackTrace();
	    	    }
 			 
 		 }
 		 else 
	    	  try {
	    	      event.go();
	    	    } catch (AppiaEventException e) {
	    	      e.printStackTrace();
	    	    }
	    }
	  }
  }
  public void handlePp2pSendEvent(Pp2pSendEvent e)
  {
	 // System.out.println("-------BEBSession.handlePp2pSendEvent------");
	  try
	  {
		  e.go();
	  }
	  catch(AppiaException ex)
		{
			ex.printStackTrace();
		} 
  }
  public void handleBebBroadcastEvent(BebBroadcastEvent e)
	{
	try
	{
		//System.out.println("-----BEBSession.handleBebBroadcastEvent----");
	    SendableEvent sendingEvent = new SendableEvent();
	    // set source and destination of event message
	    sendingEvent.source = processes.getSelfProcess().getInetWithPort();
	 //   for(int i=0;i<e.contentArray.length;i++)
	   // 	System.out.println(e.contentArray[i]);
	   /*!!!!!!!!!*/ //sendingEvent.getMessage().pushString(e.contentArray.toString());
	   // String strarray=new String();
	    //for(int i=0;i<e.contentArray.length;i++)
	    	//strarray+=e.contentArray[i]+" ";
	    sendingEvent.getMessage().pushObject(e.contentArray);
	    //sendingEvent.getMessage().pushString(strarray);
	    sendingEvent.getMessage().pushString("B");
	    sendingEvent.setSource(this);
	    sendingEvent.setChannel(e.getChannel());
	    sendingEvent.setDir(Direction.DOWN);
	    sendingEvent.init();
	    bebBroadcast(sendingEvent);
	}
	catch(AppiaException ex)
	{
		ex.printStackTrace();
	}
		
	}
  private void handleProcessInitEvent(ProcessInitEvent event) {
	 // System.out.println("-------BEBSession.handleProcessInitEvent------");
    processes = event.getProcessSet();
    
    try {
      event.go();
    } catch (AppiaEventException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the first event that arrives to the protocol session. In this case,
   * just forwards it.
   * 
   * @param init
   */
  private void handleChannelInit(ChannelInit init) {
    try {
    //	System.out.println("-------BEBSession.handleChannelInit------");
    	channel = init.getChannel();
      init.go();
    } catch (AppiaEventException e) {
      e.printStackTrace();
    }
  }

  /**
   * Broadcasts a message.
   * 
   * @param event
   */
  private void bebBroadcast(SendableEvent event) {
	  
	//  System.out.println("-------BEBSession.bebBroadcast------");
   // Debug.print("BEB: broadcasting message.");
    // get an array of processes
	//  System.out.println("bebBroadcast.handleSendable");
	if(event.getDir()==Direction.DOWN)
	{
    Process[] processArray = this.processes.getAllProcesses();
   // Object msg= event.getMessage().popObject();
    
    
    for (int i = 0; i < processes.getSize(); i++)
    	
		//if (isNeighbour(i))
		{
          SendableEvent sendingEvent = null;
      try {
          sendingEvent = (SendableEvent) event.cloneEvent();
        // set source and destination of event message
          sendingEvent.source = processes.getSelfProcess().getInetWithPort();
          sendingEvent.dest = processArray[i].getInetWithPort();
          sendingEvent.setSource(this);
          
        //  sendingEvent.setMessage(event.getMessage());
          //if (i == processes.getSelfRank())
           // sendingEvent.setDir(Direction.UP);
        sendingEvent.init();
        sendingEvent.go();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      } catch (AppiaEventException e) {
        e.printStackTrace();
      }
    
	}
	}
	else
	{
		try {
		event.go();
	   }
		catch (AppiaEventException e) {
	        e.printStackTrace();
	      }
	}
  }
  public boolean isNeighbour(int node) {

		return (processes.getMatrix()[processes.getSelfRank()][node] > 0);
	}


  /**
   * Delivers an incoming message.
   * 
   * @param event
   */
  private void handleDecideEvent( paxos.DecidedEvent event)
	{
		try
		{
		//	  System.out.println("-------BEBSession.handleDecideEvent------");
		 	int id=event.id;
		 	int value=event.result;
		 	SendableEvent sendingEvent = new SendableEvent();
		    // set source and destination of event message
		    sendingEvent.source = processes.getSelfProcess().getInetWithPort();
		    int[]contentArray=new int[5];
		    contentArray[0]=id;
		    contentArray[1]=value;
		    
		    sendingEvent.getMessage().pushObject(contentArray);
		    sendingEvent.getMessage().pushString("D");
		    sendingEvent.setSource(this);
		    sendingEvent.setChannel(event.getChannel());
		    sendingEvent.setDir(Direction.DOWN);
		    sendingEvent.init();
		    bebBroadcast(sendingEvent);
		 	
		}
		catch(AppiaException e)
		{
			e.printStackTrace();
		}
	}

  

}
