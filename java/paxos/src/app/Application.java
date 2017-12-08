/*
 *
 * Hands-On code of the book Introduction to Reliable Distributed Programming
 * by Rachid Guerraoui and Luيs Rodrigues
 * Copyright (C) 2005 Luيs Rodrigues
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
 *		Departamento de Informلtica, FCUL, 
 *		Bloco C6, Sala 6.3.12, Campo Grande, 
 *		1749-016 Lisboa, 
 *		PORTUGAL
 * 	Email:
 * 		ler@di.fc.ul.pt
 * 	Web:
 *		http://www.di.fc.ul.pt/~ler/
 * 
 */

package app;

// import appia.protocols.tutorialDA.utils.ProcessSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.StringTokenizer;
import org.continuent.appia.protocols.common.InetWithPort;

import tcp.TcpBasedPerfectP2PLayer;
import org.continuent.appia.core.*;

import beb.BEBLayer;

import ac.ACLayer;

import paxos.PaxosLayer;
import process.ProcessSet;
import process.Process;

import delay.DelayLayer;
import eld.ELDLayer;


// import appia.protocols.tutorialDA.delay.DelayLayer;
/**
 * This class is the MAIN class to run the Reliable Broadcast protocols.
 * 
 * @author nuno
 */
public class Application {

	/**
	 * Builds the Process set, using the information in the specified file.
	 * 
	 * @param filename
	 *            the location of the file
	 * @param selfProc
	 *            the number of the self process
	 * @return a new ProcessSet
	 */
	private static ProcessSet buildProcessSet(String filename, int selfProc) {
		int n;
		ProcessSet set = new ProcessSet();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

		String line = null;
		StringTokenizer st = null;
		String s = "";
		// read number of nodes
		try {
			s = reader.readLine();

		} catch (IOException e) {
		}
		n = Integer.parseInt(s);
		System.out.print(n);
		// reads lines of type: <process number> <IP address> <port>
		for (int i = 0; i < n; i++) {
			try {
				line = reader.readLine();
				if (line == null)
					break;
				st = new StringTokenizer(line);
				if (st.countTokens() != 3) {
					System.err.println("Wrong line in file.");
					continue;
				}
				int procNumber = Integer.parseInt(st.nextToken());
				InetAddress addr = InetAddress.getByName(st.nextToken());
				int portNumber = Integer.parseInt(st.nextToken());
				boolean self = (procNumber == selfProc);
				Process process = new Process(new InetWithPort(
						addr, portNumber), procNumber, self);
				// process.setMatrix(buildAdjacentMatrix(filename));
				set.addProcess(process, procNumber);
			} catch (IOException e) {
				// hasMoreLines = false;
			} catch (NumberFormatException e) {
				System.err.println("Wrong line in file.");
				continue;
			}
		} // end of while
		set.setMatrix(buildAdjacentMatrix(filename));
				
		Process[] pr = set.getAllProcesses();
		System.out.println("buildProcessSet..." + pr);
		
//		read timedelay, delta, k
		try
		{
			for (int i = 0; i < n; i++) 
				reader.readLine();
			line = reader.readLine();
			//System.out.println(line);
			if (line != null)
			{
			
			 st = new StringTokenizer(line);
			 if (st.countTokens() != 3) {
				System.err.println("Wrong line in file for timedelay, delta, k.");
				
			 }
			 else
			 {
				 int timedelay = Integer.parseInt(st.nextToken());
				 int delta = Integer.parseInt(st.nextToken());
				 int k=Integer.parseInt(st.nextToken());
				 set.setTimeDelay(timedelay);
				 set.setDelta(delta);
				 set.setK(k);
			 }
			}
			
		}
		catch(IOException ei)
		{
			ei.printStackTrace();
		}

		 /* int[][] mat= set.getMatrix();
		  for(int i=0;i<mat.length;i++) {
		  for(int j=0;j<mat.length;j++) System.out.print(mat[i][j]+" ");
		   System.out.println(); 
		   }
		  System.out.println("\n "+set.getTimeDelay()+" "+set.getDelta()+" "+set.getK());*/
		 

		return set;
	}

	// ========================================================================
	private static int[][] buildAdjacentMatrix(String filename) {

		int n;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

		String line = null;
		StringTokenizer st = null;
		int[][] array;
		// boolean hasMoreLines = true;
		String s = "";
		// read number of nodes
		try {
			s = reader.readLine();

		} catch (IOException e) {
		}
		n = Integer.parseInt(s);
		array = new int[n][n];
		for (int i = 0; i < n; i++) {
			try {
				reader.readLine();
			} catch (IOException e) {
			}

		}

		// reads lines of type: <process number> <IP address> <port>
		try {
			for (int i = 0; i < n; i++) {
				line = reader.readLine();
				st = new StringTokenizer(line);
				if (st.countTokens() != n) {
					System.err.println("Wrong line in file.");
				}
				for (int j = 0; j < n; j++)
					array[i][j] = Integer.parseInt(st.nextToken());
			}
		} catch (IOException e) {
			// hasMoreLines = false;
		} catch (NumberFormatException e) {
			System.err.println("Wrong line in file.");
		}
		
		return array;

		// end of while

	}

	/**
	 * Builds an Appia channel with the specified QoS
	 * 
	 * @param set
	 *            the ProcessSet
	 * @param qos
	 *            the specified QoS
	 * @return a new uninitialized channel
	 */
	private static Channel getChannel(ProcessSet set, String qos) {

		if (qos.equals("Paxos"))
			return getPaxosChannel(set);
		return null;

	}

	/**
	 * Builds a new Channel with Probabilistic Broadcast.
	 * 
	 * @param processes
	 *            set of proccesses
	 * @param fanout
	 *            fanout to use in the protocol
	 * @param rounds
	 *            number of rounds to use in the protocol
	 * @return a new uninitialized Channel
	 */

	/**
	 * Builds a new Appia Channel with Best Effort Broadcast
	 * 
	 * @param processes
	 *            set of processes
	 * @return a new uninitialized Channel
	 */
	

	// =======================================================================
	
	private static Channel getPaxosChannel(ProcessSet processes) {
		/* Create layers and put them on a array */
		Layer[] qos = { new TcpBasedPerfectP2PLayer(), new DelayLayer(),new BEBLayer(),new ELDLayer(),new ACLayer(),
				new PaxosLayer(), new ApplLayer() };

		/* Create a QoS */
		QoS myQoS = null;
		try {
			//System.out	.println("---------Application.getOPFDChannel-----------");
			myQoS = new QoS("Paxos QoS", qos);
			// System.out.println("qos cre");
		} catch (AppiaInvalidQoSException ex) {
			System.err.println("Invalid QoS");
			System.err.println(ex.getMessage());
			System.exit(1);
		} catch (Exception e) {
			// System.out.print()
			e.printStackTrace();
		}
		/* Create a channel. Uses default event scheduler. */
		Channel channel = myQoS.createUnboundChannel("Paxos Channel");
		/*
		 * Application Session requires special arguments: filename and . A
		 * session is created and binded to the stack. Remaining ones are
		 * created by default
		 */
		ApplSession sas = (ApplSession) qos[qos.length - 1].createSession();
		// qos[qos.length - 2].createSession();
		sas.init(processes);
		ChannelCursor cc = channel.getCursor();
		/*
		 * Application is the last session of the array. Positioning in it is
		 * simple
		 */
		try {
			cc.top();
			cc.setSession(sas);
		} catch (AppiaCursorException ex) {
			System.err.println("Unexpected exception in main. Type code:"
					+ ex.type);
			System.exit(1);
		}
		return channel;
	}

	// ===================================================================

	private static final int NUM_ARGS = 8;

	/**
	 * Main!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < (NUM_ARGS - 2)) {
			invalidArgs();
		}

		/* Parse arguments */
		int arg = 0, self = -1;
		String filename = null, qos = null;
		try {
			while (arg < args.length) {
				if (args[arg].equals("-f")) {
					arg++;
					filename = args[arg];
					System.out.println("Reading from file: " + filename);
				} else if (args[arg].equals("-n")) {
					arg++;
					try {
						self = Integer.parseInt(args[arg]);
						System.out.println("Process number: " + self);
					} catch (NumberFormatException e) {
						invalidArgs();
					}
				} else if (args[arg].equals("-qos")) {
					arg++;
					qos = args[arg];
					// if (qos.equals("pb")) {
					// qos = qos + " " + args[++arg] + " " + args[++arg];
					// }
					System.out.println("Starting with QoS: " + qos);
				} else
					invalidArgs();
				arg++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			invalidArgs();
		}

		/*
		 * gets a new uninitialized Channel with the specified QoS and the Appl
		 * session created. Remaining sessions are created by default. Just tell
		 * the channel to start.
		 */
		Channel channel = getChannel(buildProcessSet(filename, self), qos);

		/*
		 * int ar[][]=buildAdjacentMatrix(filename); for(int i=0;i<ar.length;i++) {
		 * for(int j=0;j<ar.length;j++) System.out.print(ar[i][j]+" ");
		 * System.out.println(); }
		 */
		try {

			channel.start();
		} catch (AppiaDuplicatedSessionsException ex) {
			// catch (Exception ex) {
			System.err.println("Sessions binding strangely resulted in "
					+ "one single sessions occurring more than "
					+ "once in a channel");
			System.exit(1);
		}

		/* All set. Appia main class will handle the rest */
		// System.out.println("In Main...");
		System.out.println("Starting Appia...");
		Appia.run();
	}

	/**
	 * Prints a error message and exit.
	 */
	private static void invalidArgs() {
		System.out.println("Invalid args.");

		System.exit(1);
	}
}
