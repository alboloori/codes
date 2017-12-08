/*
 * DeadHostSweeper.java
 *
 * Created on December 17, 2006, 4:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package FishCS;
import java.io.*;
import java.util.*;
import java.net.*;
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
/**
 *
 * @author Ali Javadzadeh
 */
public class DeadHostSweeper extends Thread{
    private Server fishServer;
    private final static int CHECKING_PERIOD=10000;
    private final static int TIMEOUT_PERIOD=60000;
    private boolean alive=true;
    
   
    
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
     /**
     * Creates a new instance of DeadHostSweeper
     */
    public DeadHostSweeper(Server server) 
    {
        this.fishServer=server;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public void run()
    {
        System.out.println("---------------DeadHostSweeper running--------------");
        while(alive)
       {
             ArrayList hostDir=this.fishServer.getHostDirectory();
             Iterator hostIterator=hostDir.iterator();
             try{
               Thread.sleep(CHECKING_PERIOD);
             }
             catch(InterruptedException e)
             {
                 System.out.println("Interrupt Exception");
                 e.printStackTrace();
             }
             while(hostIterator.hasNext())
                 for (int i = 0; i <  hostDir.size(); i++)
		{
		    
                 FishHost host=(FishHost)hostDir.get(i);    
                 if(hostIsAlive(host))
                      System.out.println("Host "+host.getHostIP()+"is valid.");
                 else
                 {
                     System.out.println("Host "+host.getHostIP()+"doesn't respond.It will be removed");
                     this.fishServer.unregisterHost(host);
                     
                 }
                
                    
                  try{
               Thread.sleep(10000);
             }
             catch(InterruptedException e)
             {
                 System.out.println("Interrupt Exception");
                 e.printStackTrace();
             }
                 }
       }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
 public boolean hostIsAlive(FishHost host)
 {
     //System.out.println("---------------checking host-------------");
      ObjectInputStream in;
      ObjectOutputStream out;
          try
	    {
		 Socket hostSocket = new Socket(host.getHostIP(),host.getHostPort());
	          in=new ObjectInputStream(hostSocket.getInputStream());
		  out=new ObjectOutputStream(hostSocket.getOutputStream());
                System.out.println("Sending ping to:"+host.getHostIP()+":"+host.getHostPort());
		 out.writeObject(new String(Protocol.REQ_PING));
             //    in.wait(TIMEOUT_PERIOD);
                 Object obj=in.readObject();
                 String response=obj.toString();
                 System.out.println("pong received"+response);
                 if(response.equals(Protocol.RES_PONG))
		  return true;
	    }
	    catch (Exception e)
	    {
		System.out.println("Could not connect to filehost at " + 
		host.getHostIP() + ":" + 
		host.getHostPort() );
                e.printStackTrace();
	    }
      
           return false;
 }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
 public void halt()
 {
   alive=false;
 }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
}
