/*
 * Server.java
 *
 * Created on December 12, 2006, 12:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


package FishCS;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.util.Collections;
/**
 *
 * @author Ali Javadzadeh
 */
public class Server {
    private ServerSocket serverSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
 
    ArrayList hostDirectory;
  
    DeadHostSweeper sweeper;
   
    private boolean listening;
    public final static int DEFAULT_PORT=80;
    /**
     * Creates a new instance of Server
     */
    public Server() {
         listening = true;
         hostDirectory=new ArrayList();
         sweeper=new DeadHostSweeper(this);
      
         
         
    }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public static void main(String[] args) throws IOException {
        int port=DEFAULT_PORT;
	 if(args.length>0)
             try{
             port=Integer.parseInt(args[0]);
             }
             catch(Exception e)
             {
                 System.out.println("Invalid port number format.");
             }
           Server server=new Server();
           try {
                  
            
	    server.serverSocket = new ServerSocket(port);
            
	   } catch (IOException e) {
	       System.err.println("Could not listen on port:  ."+args[0]);
	       System.exit(1);
	    }
          try{
              System.out.println("Initializing server components...");
               server.sweeper.start();
               System.out.println("Server is ready.");
               while(server.listening) {
                 Socket clientSocket = server.serverSocket.accept();
                 System.out.println("Request received from client :"+clientSocket.getInetAddress().toString()); 
                // server.hoststable.put(clientSocket.getInetAddress().getAddress(),clientSocket.getInetAddress().getHostName());
	         (new FishServerHandler(clientSocket,server)).start();
	       }
               server.serverSocket.close();
             }catch(Exception e)
                {
                  e.printStackTrace();
                }
    }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void shutDownServer()
    {
        this.listening=false;
    }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public boolean registerHost(FishHost host/*String ip,int port,ArrayList files*/)
    {
        if (hostDirectory.contains(host))
         return false;
        hostDirectory.add(host);
        return true;
       
    
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
 public ArrayList getHostDirectory()   
 {
     return this.hostDirectory;
 }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
 public boolean addNewFile(SharedFile file,FishHost host)
 {
     Iterator iterator=this.hostDirectory.iterator();
     boolean found=false;
     while(!found && iterator.hasNext())
     {
         FishHost ashost=(FishHost)iterator.next();
         if (ashost.getHostIP().equals(host.getHostIP()))
         {
             ashost.filesList.add(file);
             found=true;
         }
     }
     if(found)return true;
     return false;
 
 }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public boolean unregisterHost(FishHost host)
    {
        if(hostDirectory.contains(host))
        {
            hostDirectory.remove(host);
            return true;
        }
        return false;
    
          
       
      
    }
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    
}
