package app;

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

//import echo.ProcessSet;
//import appia.protocols.tutorialDA.utils.ProcessSet;
import org.continuent.appia.core.*;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.protocols.common.RegisterSocketEvent;
import org.continuent.appia.core.events.channel.ChannelClose;

import paxos.DecidedEvent;
import process.ProcessInitEvent;
import process.ProcessSet;
import process.SampleSendableEvent;


/**
 * Session implementing the sample application.
 * 
 * @author nuno
 */
public class ApplSession extends Session {

	Channel channel;

	private ProcessSet processes;

	private AppCommandReader reader;

	private boolean blocked = false;

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public ApplSession(Layer layer) {
		super(layer);

	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void init(ProcessSet processes) {
		this.processes = processes;
		this.processes.setMatrix(processes.getMatrix());
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void handle(Event event) {
		System.out	.println("-----------ApplSession.handle()---------------------");
		// System.out.println("Received event: "+event.getClass().getName());
		if (event instanceof SampleSendableEvent)
			handleSampleSendableEvent((SampleSendableEvent) event);
		else if (event instanceof SendableEvent)
			handleSendableEvent((SendableEvent) event);
		else if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof ChannelClose)
			handleChannelClose((ChannelClose) event);
		else if (event instanceof RegisterSocketEvent)
			handleRegisterSocket((RegisterSocketEvent) event);

	}

	/**
	 * @param event
	 */
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handleRegisterSocket(RegisterSocketEvent event) {
		if (event.error) {
			System.out.println("Address already in use!");
			System.exit(2);
		}
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	/**
	 * @param init
	 */

	private void handleChannelInit(ChannelInit init) {
		try {
			channel = init.getChannel();
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
		

		try {
			// sends this event to open a socket in the layer that is used has
			// perfect
			// point to point
			// channels or unreliable point to point channels.
			//System.out.println("ApplSession.handleChannelInit");
			RegisterSocketEvent rse = new RegisterSocketEvent(channel,
					Direction.DOWN, this);
			rse.port = processes.getSelfProcess().getInetWithPort().port;
			rse.localHost = processes.getSelfProcess().getInetWithPort().host;
			rse.go();
			ProcessInitEvent processInit = new ProcessInitEvent(channel,
					Direction.DOWN, this);
			processInit.setProcessSet(processes);
			// processInit.setMatrix(buildMatrix);
			processInit.go();
		} catch (AppiaEventException e1) {
			e1.printStackTrace();
		}
		System.out.println("Channel is open.");
		// starts the thread that reads from the keyboard.
		
        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!*/trigger_ucPropose();
		reader = new AppCommandReader(this);
		reader.start();
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * @param close
	 */
	private void handleChannelClose(ChannelClose close) {
		channel = null;
		System.out.println("Channel is closed.");
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * @param event
	 */
	private void handleSampleSendableEvent(SampleSendableEvent event) {
		
		if (event.getDir() == Direction.DOWN)
			handleOutgoingEvent(event);
		else
			handleIncomingEvent(event);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	/**
	 * @param event
	 */
	private void handleOutgoingEvent(SampleSendableEvent event) {
   System.out.println("---------ApplSession.handleOutgoingEvent---------");
		String command = event.getCommand();
		
		if ("UC".equalsIgnoreCase(command))
		// handleSend(event);
		  handleACCommand(event);

	}

	/**
	 * @param event
	 */
	private void handleACCommand(SampleSendableEvent event) {
		System.out.println("--------ApplSession.handleACCommand---------");
		 
		try {

			SendableEvent ievent = new SendableEvent();
			ievent.source = this.processes.getSelfProcess().getInetWithPort();
			ievent.setSource(this);
			ievent.setDir(event.getDir());
			ievent.setChannel(event.getChannel());
			String msg=event.getMessage().popString();
			ievent.getMessage().pushString(msg);
			ievent.init();
			ievent.go();
			
		//	System.out.println("---------event.go--------------");
		}   catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	private void handleSendableEvent(SendableEvent event) {
		// try {
		System.out.println("---------ApplSesion.handleSendable--------------");
		if (event.getDir() == Direction.UP)
			System.out.println("Message " + event.getMessage().popString()
					+ "received");

		 
	}

	private void handleIncomingEvent(SampleSendableEvent event) {

		System.out.print("Received event  \n");
	}
	private void trigger_ucPropose()
	  {
		  System.out.println("------app.trigger_ucPropose----");
		 SendableEvent de=new SendableEvent();
		 de.source= processes.getSelfProcess().getInetWithPort();
		 de.setSource(this);
		 de.setChannel(this.channel);
		 de.setDir(Direction.DOWN);
		 de.getMessage().pushString("U");
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

}
