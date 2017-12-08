/*
 * FishServerHandler.java
 *
 * Created on December 12, 2006, 12:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package FishCS;
import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author Ali Javadzadeh
 */
public class FishServerHandler extends Thread{
    
    /**
     * Creates a new instance of FishServerHandler
     */
      private Socket clientSocket;
      private Server refFishServer;
      private ObjectInputStream in;
      private ObjectOutputStream out;
      private FishHost host;
      private boolean listening=true;
      //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
      public FishServerHandler(Socket cSocket,Server server) 
      {
        this.clientSocket=cSocket;
        this.refFishServer=server;
      }
      //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
      public void run() 
      {
         System.out.println("-------------FishServerHandler.run-------------------------");
         try {
              in  = new ObjectInputStream(clientSocket.getInputStream());
               System.out.println("-------------InputStream Created-------------------------");
	       out = new ObjectOutputStream(clientSocket.getOutputStream());
               System.out.println("-------------OutputStream Created-------------------------");
             //  sendData(new String("$Connect$"));
             //  boolean listening=true;
               while(listening)
                {
                  Object request=this.receiveData();
                  
                  if(request!=null)
                  handleRequest(request);
                }
        }
        catch(IOException e)
        {
           System.out.println("IO failed in handler") ;
           e.printStackTrace();
           System.out.println(e.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void handleRequest(Object msg)
    {   System.out.println("---------------handleRequest--------------");
        if(msg!=null)
        {     
         String strMsg=msg.toString();
         System.out.println("Requeste "+msg+" received from:"+clientSocket.getInetAddress().getHostAddress().toString());
        if(strMsg.toString().equals(Protocol.REQ_REGISTER))
        {
            FishHost fhost=( FishHost) this.receiveData();
            this.host=fhost;
            this.refFishServer.registerHost(host);
            int i=0;
             ArrayList fileList=fhost.getFileList();
            while(fileList.iterator().hasNext() &&i < fileList.size())
               System.out.println(((SharedFile)fileList.get(i++)).toString());
            return;
        }
         
         if(strMsg.toString().equals(Protocol.REQ_UNREGISTER))
        {
             if(this.host!=null)
               refFishServer.unregisterHost(host);
             listening=false;
            return;
        }
        if(strMsg.toString().equals(Protocol.REQ_SEARCH))
        {
            String keyword=(String)this.receiveData();
            System.out.println(keyword);
            ArrayList list=search(keyword);
            if(list!=null)
            {
             sendData(new String(Protocol.RES_FOUND));
             System.out.println("Found Message sent");
             Iterator it=list.iterator();
             while(it.hasNext())
             {
                 System.out.println(it.next().toString());
             }
             sendData(list);
             System.out.println("list sent successfully");
             
            }
            else sendData(new String(Protocol.RES_NOTFOUND));
            return;
         }
          if(strMsg.toString().equals(Protocol.REQ_ADDFILE))
          {
             SharedFile file=(SharedFile) this.receiveData();
             this.host.filesList.add(file);
          }
      }
    }
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     private void sendData(Object objData)
    {
        System.out.println("---------------sendData--------------");
        try
        {
            out.writeObject(objData);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Unable to send data.");
        }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    private Object receiveData()
    {
        System.out.println("---------------receiveData--------------");
        Object obj=null;
        try
        {   
            obj=in.readObject();
        }
        catch(EOFException e)
        {
            System.out.println("EOF");
        }
        catch(IOException e)
        {
            System.out.println(e.toString());
            System.out.println("IO Exception occured");
            e.printStackTrace();
        }
        
          catch( Exception e)
        {
            e.printStackTrace();
            System.out.println("Unable to read data.");
        }
      return obj;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public ArrayList search(String keyword)
    {
        System.out.println("---------------Search-------------");
        ArrayList<SharedFile> result= new ArrayList();
        ArrayList directory=refFishServer.getHostDirectory();
        Iterator itrDir=directory.iterator();
        CharSequence key= keyword.subSequence(0,keyword.length());
        while(itrDir.hasNext())
        {
          FishHost host=(FishHost)(itrDir.next());
          ArrayList files=host.getFileList();
          Iterator itrFiles=files.iterator();
          while(itrFiles.hasNext())
          {
             SharedFile sh=(SharedFile)itrFiles.next();
             if(sh.getFilename().contains(key))
                 result.add(sh);
          }
        }
       if(result.size()>0) return result;
        return null;
    }
}
