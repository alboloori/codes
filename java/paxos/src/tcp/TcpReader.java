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

package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.continuent.appia.core.*;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.protocols.common.InetWithPort;



/**
 * Waits and reads incoming data from a TCP connection.
 * 
 * @author pedrofrv
 */
public class TcpReader extends Thread {

  private Socket s;
  private InputStream is = null;
  private TcpBasedPerfectP2PSession parentSession;
  private int remotePort;
  private int originalPort; // port of the accept socket
  private Channel channel;

  private int inactiveCounter = 0;

  private boolean running;

  public TcpReader(Socket socket, TcpBasedPerfectP2PSession session,
      int originalPort, int remotePort, Channel channel) {
    super();
    s = socket;
    try {
      socket.setSoTimeout(session.param_SOTIMEOUT);
    } catch (SocketException e) {
      e.printStackTrace();
    }
    parentSession = session;
    this.originalPort = originalPort;
    this.remotePort = remotePort;
    this.channel = channel;
    setRunning(true);
  }

  public void run() {
    SendableEvent event = null;

    try {
      is = s.getInputStream();
    } catch (IOException ex) {
      InetWithPort iwp = new InetWithPort(s.getInetAddress(), remotePort);

      if (TcpBasedPerfectP2PConfig.debugOn)
        debug("ia receber dados e o no " + iwp.host + " no porto " + iwp.port
            + " falhou");

      try {
        TcpUndeliveredEvent undelivered = new TcpUndeliveredEvent(iwp);
        undelivered.asyncGo(channel, Direction.UP);
        parentSession.removeSocket(iwp);
      } catch (AppiaEventException exception) {
        exception.printStackTrace();
      }
      return;
    }
    while (isRunning()) {
      try {
        event = receiveAndFormat();
        clearInactiveCounter();

        if (event != null) {
          if (TcpBasedPerfectP2PConfig.debugOn)
            debug("received an event sending it to the appia stack: " + event
                + " Channel: " + event.getChannel());
          event.asyncGo(event.getChannel(), Direction.UP);
        }
      } catch (AppiaEventException ex) {
        ex.printStackTrace();
      } catch (SocketTimeoutException ste) {
      } catch (IOException ex) {
        InetWithPort iwp = new InetWithPort(s.getInetAddress(), remotePort);
        if (TcpBasedPerfectP2PConfig.debugOn)
          debug("was about to receive data but node on host " + iwp.host
              + " port " + iwp.port + " failed");
        parentSession.removeSocket(iwp);

        // send_undelivered :(
        try {
          TcpUndeliveredEvent undelivered = new TcpUndeliveredEvent(iwp);
          undelivered.asyncGo(channel, Direction.UP);
        } catch (AppiaEventException e) {
          if (TcpBasedPerfectP2PConfig.debugOn)
            e.printStackTrace();
        }
      }
    }
  }

  private int receive_n(byte[] b, int length) throws IOException {
    int n = 0, i = 0, x = 0;
    while (n != length && i != -1) {
      i = is.read(b, n, length - n);
      n += i;
      x++;
    }
    if (i == -1)
      throw new IOException();

    return n;
  }

  /*
   * Event deserialization. Returns the event or null if something happened.
   */
  private SendableEvent receiveAndFormat() throws IOException {
    SendableEvent e = null;
    try {
      byte bTotal[] = new byte[4];
      int total;
      receive_n(bTotal, 4);
      total = parentSession.byteArrayToInt(bTotal);

      byte data[] = new byte[total];
      receive_n(data, total);

      int curPos = 0;

      /* Extract event class name */
      // size of class name
      int sLength = parentSession.byteArrayToInt(data, curPos);
      // the class name
      String className = new String(data, curPos + 4, sLength);

      // updating curPos
      curPos += sLength + 4;

      /* Create event */
      Class c = Class.forName(className);
      e = (SendableEvent) c.newInstance();

      /* Extract channel name and put event in it */
      sLength = parentSession.byteArrayToInt(data, curPos);
      String channelName = new String(data, curPos + 4, sLength);

      Channel msgChannel = parentSession.getChannel(channelName);

      if (msgChannel == null)
        return null;

      e.setChannel(msgChannel);
      curPos += sLength + 4;

      /* Extract the addresses and put them on the event */

      // msg's source
      e.source = new InetWithPort();
      ((InetWithPort) e.source).host = s.getInetAddress();
      ((InetWithPort) e.source).port = remotePort;

      e.dest = new InetWithPort();
      ((InetWithPort) e.dest).host = s.getLocalAddress();
      ((InetWithPort) e.dest).port = originalPort;

      e.getMessage().setByteArray(data, curPos, total - curPos);

    } catch (IOException ste) {
      throw ste;
    } catch (Exception ex) {

      if (TcpBasedPerfectP2PConfig.debugOn) {
        ex.printStackTrace();
        System.err.println("Exception catched while processing message from "
            + s.getInetAddress().getHostName() + ":" + remotePort
            + ". Continuing operation.");
      }
      throw new IOException();
    }

    return e;
  }

  public synchronized void setRunning(boolean r) {
    running = r;
  }

  private synchronized boolean isRunning() {
    return running;
  }

  private void debug(String msg) {
    System.out.println("[TCPREADER]:: " + msg);
  }

  public Socket getSocket() {
    return s;
  }

  public synchronized int getInactiveCounter() {
    return inactiveCounter;
  }

  public synchronized int sumInactiveCounter() {
    return (++this.inactiveCounter);
  }

  public synchronized void clearInactiveCounter() {
    this.inactiveCounter = 0;
  }

}
