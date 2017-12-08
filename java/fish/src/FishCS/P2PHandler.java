/*
 * P2PHandler.java
 *
 * Created on December 16, 2006, 2:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package FishCS;
import java.io.*;
import java.util.ArrayList;
import java.net.*;
/**
 *
 * @author Ali Javadzadeh
 */
public class P2PHandler extends Thread{
    
 //   private  Socket clientSocket = null;
    
    private ObjectInputStream in=null;
    private ObjectOutputStream out=null;
    boolean listening=true;
    private ServerSocket listenSocket;
    private int port;
    /**
     * Creates a new instance of P2PHandler
     */
    public P2PHandler(int port/*Socket socket*/)
    {
      //this.clientSocket=socket;
       this.port=port;
    }
    public void run()
    {
       try{ 
           listenSocket=new ServerSocket(port);
           while(listening &&!listenSocket.isClosed())
           {
           Socket clientSocket =  listenSocket.accept();  
            out=new ObjectOutputStream(clientSocket.getOutputStream());
           in= new ObjectInputStream (clientSocket.getInputStream());
           Object objMsg=in.readObject();
           if(objMsg.toString().equals(Protocol.REQ_DOWNLOAD))
           {
               System.out.println("------Download request received by P2P handler------ ");
               SharedFile file=(SharedFile)in.readObject();
               DownloadFile dlManager=new DownloadFile(file.getFilename(),file.getPath());
              if(dlManager.readFile())
              {
                System.out.println("------File sent to client------ ");    
               out.writeObject(new String(Protocol.RES_START_DOWNLOAD));
               out.writeObject(dlManager);
              }
              else out.writeObject(new String(Protocol.RES_DOWNLOAD_FAILED));
           }
           else if(objMsg.toString().equals(Protocol.REQ_PING))
           {
            System.out.println("----------Ping Received---------");
           out.writeObject(new String(Protocol.RES_PONG));
           System.out.println("----------Pong Sent---------");
           }
		else
		   System.out.println("Unknown command recieved: "+objMsg.toString());
           }
         
       }
        catch(IOException e)
        {
           System.out.println("IO failed in P2P handler") ;
           e.printStackTrace();
           System.out.println(e.toString());
        }
       catch(Exception e)
       {
           
       }
    }
    public void stopRunning()
    {
        listening=false;
    }
}
