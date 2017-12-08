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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.continuent.appia.protocols.common.InetWithPort;

import org.continuent.appia.core.Channel;

/**
 * Waits and accepts new TCP connections.
 * 
 * @author pedrofrv
 */
public class AcceptReader extends Thread {

  private ServerSocket socket;
  private TcpBasedPerfectP2PSession session;
  private Channel channel;
  private Object lock;

  private boolean running;

  /**
   * Constructor for AcceptReader.
   */
  public AcceptReader(ServerSocket ss, TcpBasedPerfectP2PSession s,
      Channel channel, Object lock) {
    super();
    socket = ss;
    try {
      socket.setSoTimeout(s.param_SOTIMEOUT);
    } catch (SocketException e) {
      e.printStackTrace();
    }
    session = s;
    this.channel = channel;
    this.lock = lock;
    setRunning(true);
  }

  public void run() {
    Socket newSocket;
    int remotePort;

    while (isRunning()) {
      newSocket = null;

      if (TcpBasedPerfectP2PConfig.debugOn)
        debug("accepting connections");

      try {
        newSocket = socket.accept();
        if (TcpBasedPerfectP2PConfig.debugOn)
          debug("new connection");
      } catch (SocketTimeoutException ste) {
      } catch (IOException ex) {
        if (TcpBasedPerfectP2PConfig.debugOn)
          debug("error in accept");
        ex.printStackTrace();
      }
      // check if there is a connection and
      // put new socket in socket list of connected sockets.

      if (newSocket != null) {
        try {
          remotePort = initProto(newSocket);
      //    System.out.println("TCP:"+newSocket.getInetAddress()+" "+remotePort+"##");
          InetWithPort iwp = new InetWithPort(newSocket.getInetAddress(),
              remotePort);

          synchronized (lock) {
            if (session.existsSocket(session.ourReaders, iwp))
              session.addSocket(session.otherReaders, iwp, newSocket, channel);
            else
              session.addSocket(session.ourReaders, iwp, newSocket, channel);
          }
          if (TcpBasedPerfectP2PConfig.debugOn)
            debug("created socket");
        } catch (IOException ex) {
          if (TcpBasedPerfectP2PConfig.debugOn)
            debug("error initiating connection. closing connection.");
          try {
            newSocket.close();
          } catch (IOException ex1) {
          }
        }
      }
    }
  }

  private int initProto(Socket socket) throws IOException {
    int port;
    byte[] bufferPort = new byte[4];

    receive_n(socket.getInputStream(), bufferPort, 4);

    port = session.byteArrayToInt(bufferPort);

    if (TcpBasedPerfectP2PConfig.debugOn)
      debug("received remote port:: " + port);

    return port;
  }

  private int receive_n(InputStream is, byte[] b, int length)
      throws IOException {
    int n = 0, i = 0;
    while (n != length && i != -1) {
      i = is.read(b, n, length - n);
      n += i;
    }
    if (i == -1)
      throw new IOException();
    return n;
  }

  public synchronized void setRunning(boolean r) {
    running = r;
  }

  private synchronized boolean isRunning() {
    return running;
  }

  private void debug(String msg) {
    // System.out.println("[AcceptReader]:: "+msg);
  }
}
