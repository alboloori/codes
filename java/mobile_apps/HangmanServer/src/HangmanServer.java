/*
 * HangmanServer.java
 *
 * @author Ali Javadzadeh - Arya Vedjdani
 * Created on November 6, 2006, 3:46 PM
 * @since 
 */
//package HW1;

import java.net.*;
import java.io.*;
import java.util.*;

//////////////////////////////////////////////////////////////////////////////////
/**
  * This class represents the server part of the application.
  * The server waits for a start game request from the client and creates a handler thread.
  
*/
public class HangmanServer {
    private String[] cache;
    
    /** Creates a new instance of HangmanServer */
    public HangmanServer() {
cache=new String[HangmanHandler.NO_OF_WORDS];
     
    }
 private void fillCache(String fileName) 
    {
        try{
       InputStreamReader reader=new InputStreamReader(new FileInputStream(fileName));
         BufferedReader bufferedReader=new BufferedReader(reader);
        for(int i=0;i<HangmanHandler.NO_OF_WORDS;i++)
            cache[i]=bufferedReader.readLine();   
        }
        catch(IOException e)
        {
           System.out.println("Unable to read from file");
        }
    }
    /** Creates a ServerSocket and waits for clients to send requests
     *  If the clients request for Start Game, the server creates a handler
     *  for each client
     *@throws IOException 
     */
    public static void main(String[] args) throws IOException {
	boolean listening = true;
	ServerSocket serverSocket = null;
        HangmanServer server=new HangmanServer();
        server.fillCache("words.txt");
        
	try {
            
	    serverSocket = new ServerSocket(Integer.parseInt(args[0]));
	} catch (IOException e) {
	    System.err.println("Could not listen on port:  ."+args[0]);
	    System.exit(1);
	}
       try{
           System.out.println("Server is ready.");
	while(listening) {
            
         
            Socket clientSocket = serverSocket.accept();
            System.out.println("Request Accepted"); 
            BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
            byte[] msg = new byte[200];
	    int bytesRead = 0;
	    int n;
	    n = in.read(msg, bytesRead, 60);
            String strMsg=new String(msg);
            System.out.println(strMsg.substring(0,n));
            if((strMsg.substring(0,n).equals("start game")))
	    (new HangmanHandler(clientSocket,server.cache)).start();
            
	}

	serverSocket.close();
   }catch(Exception e1)
   {
       System.out.println(e1.toString());
   }
    }
   
}
