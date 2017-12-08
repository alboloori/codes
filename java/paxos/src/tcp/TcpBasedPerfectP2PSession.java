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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.continuent.appia.core.events.AppiaMulticast;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.events.channel.ChannelClose;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.message.Message;
import org.continuent.appia.core.message.MsgBuffer;


import org.continuent.appia.core.*;
import org.continuent.appia.protocols.common.InetWithPort;
import org.continuent.appia.protocols.common.RegisterSocketEvent;
import org.continuent.appia.protocols.utils.HostUtils;
import org.continuent.appia.xml.utils.SessionProperties;


/**
 * Session implementing the
 * TCP-Based Perfect Point-to-Point Links protocol.
 * 
 * @author pedrofrv
 * 
 */
public class TcpBasedPerfectP2PSession extends Session
    implements
    org.continuent.appia.xml.interfaces.InitializableSession {

  private static final int DEST_TIMEOUT = 150000; // 2,5 minutes
  private static final int MAX_INACTIVITY = 2;
  private static final int SOTIMEOUT = 5000;

  protected int param_DEST_TIMEOUT = DEST_TIMEOUT,
      param_MAX_INACTIVITY = MAX_INACTIVITY, param_SOTIMEOUT = SOTIMEOUT;

  // Channels
  protected HashMap channels;

  // Open Sockets created by this node
  // protected HashMap ourSockets;
  protected HashMap ourReaders;

  // Sockets opened to us
  // protected HashMap otherSockets;
  protected HashMap otherReaders;

  // Accept Thread
  protected AcceptReader acceptThread;

  protected int ourPort = -1;

  protected Object socketLock;
  protected Object channelLock;

  private Channel timerChannel = null;

  /**
   * Constructor for NewTcpSession.
   * 
   * @param layer
   */
  public TcpBasedPerfectP2PSession(Layer layer) {
    super(layer);

    // init all
    channels = new HashMap();
    // ourSockets = new HashMap();
    ourReaders = new HashMap();
    // otherSockets = new HashMap();
    otherReaders = new HashMap();

    socketLock = new Object();
    channelLock = new Object();
  }

  /**
   * Possible parameters:
   * <ul>
   * <li><b>dest_timeout</b> timeout to verify of there are unused open
   * connections.
   * <li><b>max_inactivity</b> number of times that the dest_timeout expires
   * without closing the connection..
   * <li><b>reader_sotimeout</b> the timeout of the threads that listen UDP
   * sockets. Default is 5 seconds.
   * </ul>
   * 
   * @param params
   *          The parameters.
   */
  public void init(SessionProperties params) {
    if (params.containsKey("reader_sotimeout"))
      param_SOTIMEOUT = params.getInt("reader_sotimeout");
    if (params.containsKey("dest_timeout"))
      param_DEST_TIMEOUT = params.getInt("dest_timeout");
    if (params.containsKey("max_inactivity"))
      param_MAX_INACTIVITY = params.getInt("max_inactivity");
  }

  public void handle(Event e) {
    if (e instanceof SendableEvent)
      handleSendable((SendableEvent) e);
    else if (e instanceof RegisterSocketEvent)
      handleRegisterSocket((RegisterSocketEvent) e);
    else if (e instanceof ChannelInit)
      handleChannelInit((ChannelInit) e);
    else if (e instanceof ChannelClose)
      handleChannelClose((ChannelClose) e);
    else if (e instanceof TcpTimer)
      handleTcpTimer((TcpTimer) e);
  }

  private void handleSendable(SendableEvent e) {
   
    if (e.getDir() == Direction.UP) {
      try {
        e.go();
      } catch (AppiaEventException e1) {
        e1.printStackTrace();
      }
      return;
    }

    if (TcpBasedPerfectP2PConfig.debugOn)
      debug("preparing to send ::" + e + " CHANNEL: "
          + e.getChannel().getChannelID());

    byte[] data = format(e);

    if (e.dest instanceof AppiaMulticast) {
      Object[] dests = ((AppiaMulticast) e.dest).getDestinations();
      for (int i = 0; i < dests.length; i++) {
        if (dests[i] instanceof InetWithPort)
          send(data, (InetWithPort) dests[i], e.getChannel());
        else
          sendUndelivered(e.getChannel(), dests[i]);
      }
    } else if (e.dest instanceof InetWithPort) {
      send(data, (InetWithPort) e.dest, e.getChannel());
    } else {
      sendUndelivered(e.getChannel(), e.dest);
    }

    try {
      e.go();
    } catch (AppiaEventException ex) {
      ex.printStackTrace();
    }
  }

  private void handleRegisterSocket(RegisterSocketEvent e) {
    if (TcpBasedPerfectP2PConfig.debugOn)
      debug("received RSE");
    ServerSocket ss = null;

    if (ourPort < 0) {
      if (e.port == RegisterSocketEvent.FIRST_AVAILABLE) {
        try {
          ss = new ServerSocket(0);
        } catch (IOException ex) {
          if (TcpBasedPerfectP2PConfig.debugOn)
            ex.printStackTrace();
        }
      } else if (e.port == RegisterSocketEvent.RANDOMLY_AVAILABLE) {
        Random rand = new Random();
        int p;
        boolean done = false;

        while (!done) {
          p = rand.nextInt(Short.MAX_VALUE);

          try {
            ss = new ServerSocket(p);
            done = true;
          } catch (IllegalArgumentException ex) {
          } catch (IOException ex) {
          }
        }
      } else {
        try {
          ss = new ServerSocket(e.port);
        } catch (IOException ex) {
          if (TcpBasedPerfectP2PConfig.debugOn)
            ex.printStackTrace();
        }
      }
    }
    if (ss != null) {
      ourPort = ss.getLocalPort();
      if (TcpBasedPerfectP2PConfig.debugOn)
        debug("Our port is " + ourPort);

      // create accept thread int the request port.
      acceptThread = new AcceptReader(ss, this, e.getChannel(), socketLock);
      acceptThread.start();

      e.localHost = HostUtils.getLocalAddress();
      e.port = ourPort;
      e.error = false;
    } else {
      e.error = true;
    }

    // send RegisterSocketEvent
    e.setDir(Direction.invert(e.getDir()));
    e.setSource(this);

    try {
      e.init();
      e.go();
    } catch (AppiaEventException ex) {
      ex.printStackTrace();
    }
  }

  private void handleChannelInit(ChannelInit e) {
    // add channel to hash map
    putChannel(e.getChannel());
    try {
      e.go();
    } catch (AppiaEventException ex) {
      ex.printStackTrace();
    }

    if (timerChannel == null) {
      try {
        TcpTimer timer = new TcpTimer(param_DEST_TIMEOUT, e.getChannel(), this,
            EventQualifier.ON);
        timer.go();
        timerChannel = timer.getChannel();
      } catch (AppiaEventException ex) {
        ex.printStackTrace();
      } catch (AppiaException ex) {
        ex.printStackTrace();
      }
    }
  }

  private void handleChannelClose(ChannelClose e) {

    // remove channel.
    removeChannel(e.getChannel());

    if (channels.size() == 0) {
      acceptThread.setRunning(false);
      Iterator it = ourReaders.values().iterator();
      while (it.hasNext()) {
        ((TcpReader) (it.next())).setRunning(false);
        it.remove();
      }
      it = otherReaders.values().iterator();
      while (it.hasNext()) {
        ((TcpReader) (it.next())).setRunning(false);
        it.remove();
      }
    } else if (e.getChannel() == timerChannel) {
      try {
        timerChannel = (Channel) channels.values().iterator().next();
        TcpTimer timer = new TcpTimer(param_DEST_TIMEOUT, timerChannel, this,
            EventQualifier.ON);
        timer.go();
      } catch (Exception ex) {
        timerChannel = null;
        ex.printStackTrace();
      }
    }

  }

  private void handleTcpTimer(TcpTimer e) {
    try {
      e.go();
    } catch (AppiaEventException e1) {
      e1.printStackTrace();
    }

    Iterator it = ourReaders.values().iterator();
    while (it.hasNext()) {
      TcpReader reader = (TcpReader) it.next();
      if (reader.sumInactiveCounter() > param_MAX_INACTIVITY) {
        reader.setRunning(false);
        it.remove();
      }
    }
    it = otherReaders.values().iterator();
    while (it.hasNext()) {
      TcpReader reader = (TcpReader) it.next();
      if (reader.sumInactiveCounter() > param_MAX_INACTIVITY) {
        reader.setRunning(false);
        it.remove();
      }
    }
  }

  protected void send(byte[] data, InetWithPort dest, Channel channel) {
    Socket s = null;

    try {
      // check if the socket exist int the opensockets created by us
      if (existsSocket(ourReaders, dest)) {
        // if so use that socket
        s = getSocket(ourReaders, dest);
        if (TcpBasedPerfectP2PConfig.debugOn)
          debug("our socket, sending...");
      } else {// if not
        // check if socket exist in sockets created by the other
        if (existsSocket(otherReaders, dest)) {
          // if so use that socket
          s = getSocket(otherReaders, dest);
          if (TcpBasedPerfectP2PConfig.debugOn)
            debug("other socket, sending...");
        } else {// if not
          // create new socket and put it opensockets created by us
          s = createSocket(ourReaders, dest, channel);
          if (TcpBasedPerfectP2PConfig.debugOn)
            debug("created new socket, sending...");
        }
      }
      // send event by the chosen socket -> formatAndSend()
      if (TcpBasedPerfectP2PConfig.debugOn)
        debug("Sending through " + s);
      s.getOutputStream().write(data);
      s.getOutputStream().flush();
    } catch (IOException ex) {
      if (TcpBasedPerfectP2PConfig.debugOn) {
        ex.printStackTrace();
        debug("o no " + dest.host + " no porto " + dest.port + " falhou");
      }
      sendUndelivered(channel, dest);
      removeSocket(dest);
    }
  }

  protected boolean existsSocket(HashMap hr, InetWithPort iwp) {
    synchronized (socketLock) {
      if (hr.containsKey(iwp))
        return true;
      else
        return false;
    }
  }

  protected Socket getSocket(HashMap hm, InetWithPort iwp) {
    synchronized (socketLock) {
      TcpReader reader = (TcpReader) hm.get(iwp);
      reader.clearInactiveCounter();
      return reader.getSocket();
    }
  }

  // create socket, put in hashmap and create thread
  protected Socket createSocket(HashMap hr, InetWithPort iwp, Channel channel)
      throws IOException {
    synchronized (socketLock) {
      Socket newSocket = null;

      // create socket

      newSocket = new Socket(iwp.host, iwp.port);
      newSocket.setTcpNoDelay(true);

      byte bPort[] = intToByteArray(ourPort);

      newSocket.getOutputStream().write(bPort);
      if (TcpBasedPerfectP2PConfig.debugOn)
        debug("Sending our original port " + ourPort);

      addSocket(hr, iwp, newSocket, channel);

      return newSocket;
    }
  }

  protected void addSocket(HashMap hr, InetWithPort iwp, Socket socket,
      Channel channel) {
    synchronized (socketLock) {
      TcpReader reader = new TcpReader(socket, this, ourPort, iwp.port, channel);
      reader.start();

      // hm.put(iwp,socket);
      hr.put(iwp, reader);
    }
  }

  protected void removeSocket(InetWithPort iwp) {
    synchronized (socketLock) {
      if (existsSocket(ourReaders, iwp))
        ((TcpReader) (ourReaders.remove(iwp))).setRunning(false);
      if (existsSocket(otherReaders, iwp))
        ((TcpReader) (otherReaders.remove(iwp))).setRunning(false);
    }
  }

  protected Channel getChannel(String channelName) {
    synchronized (channelLock) {
      return (Channel) channels.get(channelName);
    }
  }

  protected void putChannel(Channel channel) {
    synchronized (channelLock) {
      channels.put(channel.getChannelID(), channel);
    }
  }

  protected void removeChannel(Channel channel) {
    synchronized (channelLock) {
      channels.remove(channel.getChannelID());
    }
  }

  protected byte[] format(SendableEvent e) {
    MsgBuffer mbuf = new MsgBuffer();
    Message msg = e.getMessage();

    byte[] eventType = e.getClass().getName().getBytes();
    byte[] channelID = e.getChannel().getChannelID().getBytes();

    mbuf.len = channelID.length;
    msg.push(mbuf);
    System.arraycopy(channelID, 0, mbuf.data, mbuf.off, mbuf.len);

    mbuf.len = 4;
    msg.push(mbuf);
    intToByteArray(channelID.length, mbuf.data, mbuf.off);

    mbuf.len = eventType.length;
    msg.push(mbuf);
    System.arraycopy(eventType, 0, mbuf.data, mbuf.off, mbuf.len);

    mbuf.len = 4;
    msg.push(mbuf);
    intToByteArray(eventType.length, mbuf.data, mbuf.off);

    mbuf.len = 4;
    msg.push(mbuf);
    intToByteArray(msg.length() - 4, mbuf.data, mbuf.off);

    return msg.toByteArray();
  }

  protected byte[] intToByteArray(int i) {
    byte[] ret = new byte[4];

    ret[0] = (byte) ((i & 0xff000000) >> 24);
    ret[1] = (byte) ((i & 0x00ff0000) >> 16);
    ret[2] = (byte) ((i & 0x0000ff00) >> 8);
    ret[3] = (byte) (i & 0x000000ff);

    return ret;
  }

  protected void intToByteArray(int i, byte[] a, int o) {
    a[o + 0] = (byte) ((i & 0xff000000) >> 24);
    a[o + 1] = (byte) ((i & 0x00ff0000) >> 16);
    a[o + 2] = (byte) ((i & 0x0000ff00) >> 8);
    a[o + 3] = (byte) (i & 0x000000ff);
  }

  protected int byteArrayToInt(byte[] b) {
    int ret = 0;

    ret |= b[0] << 24;
    ret |= (b[1] << 24) >>> 8; // must be done this way because of java's
    ret |= (b[2] << 24) >>> 16; // sign extension of <<
    ret |= (b[3] << 24) >>> 24;

    return ret;
  }

  protected int byteArrayToInt(byte[] b, int off) {
    int ret = 0;

    ret |= b[off] << 24;
    ret |= (b[off + 1] << 24) >>> 8; // must be done this way because of
    ret |= (b[off + 2] << 24) >>> 16; // java's sign extension of <<
    ret |= (b[off + 3] << 24) >>> 24;

    return ret;
  }

  protected void sendUndelivered(Channel channel, Object who) {
    try {
      TcpUndeliveredEvent undelivered = new TcpUndeliveredEvent(channel,
          Direction.UP, this, who);
      undelivered.go();
    } catch (AppiaEventException exception) {
      exception.printStackTrace();
    }
  }

  private void debug(String msg) {
    if (TcpBasedPerfectP2PConfig.debugOn)
      System.out.println("[TcpComplete] ::" + msg);
  }
}
