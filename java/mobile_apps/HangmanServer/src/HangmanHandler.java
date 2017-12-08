/*
 * HangmanHandler.java
 * 2G1529 HomeWork 1         
 * @author Ali Javadzadeh - Arya Vedjdani  
 * Created on November 6, 2006, 6:47 PM
 *
 */

//package HW1;
import java.net.*;
import java.io.*;
import java.util.*;
//////////////////////////////////////////////////////////////////////////////////
/**
  * This class represents the handler for processing each client requests.
  * @see Thread
*/

public class HangmanHandler extends Thread
{
    /**
     * The value of this constant is {@value} 
     * ,which is the number of words in the dictionary file .
     */
    public static final int NO_OF_WORDS=25143;
    /**
     * The value of this constant is {@value}, which is the number of tries
     * the client is allowed to guess a letter.
     */
    public static final int MAX_TRIES = 5;
    /**
     * The value of this constant is {@value}, which is the maximum lenght of word
     */
    public static final int MAX_WORD_LEN = 30;
     /**
     * The value of this constant is c
      * which is the score awarded to the client if it made a right guess
     */
    public static final int LETTER_SCORE=5;
     /**
     * The value of this constant is{@value}
      * the score awarded to the client if it guesses the whole word correctly
     */
    public static final int WORD_SCORE=25;
     /**
     * This field holds a reference to the clientSocket
     */
    private Socket clientSocket;
     /**
     * This field holds a reference to the clientSocket Input buffer
     */
    private BufferedInputStream in;
     /**
     * This field holds a reference to the clientSocket output buffer
     */
    private BufferedOutputStream out;
    /**
     * This field holds the number of tries the client is allowed to guess a letter
     */
    private int failedCounter;
    /**
     * This field holds the total score the client has been awarded
     *if a client wins the score counter is incremented, 
     * if the client loses the score counter is decremented. 
     */
    private int totalScore;
     /**
     * This field holds the view of the word the client has guessed 
     */
    private StringBuffer clientWordView;
    /**
     * This field holds a reference to the word the server has chosen
     */
    private String serverWord;
 private String[]cache;
//////////////////////////////////////////////////////////////////////////////////
     /**
     * Constructor: sets the reference to the client socket  
     */
     public HangmanHandler (Socket clientSocket,String[] cache) throws IOException{

	this.clientSocket = clientSocket;
        this.cache=cache;
       
    }
//////////////////////////////////////////////////////////////////////////////////
    /**
     *selects a random word from the given dictionary file.
     *Each word is suppposed to be on a separate line 
     *@param  fileName The name of file that contains the list of words
     *@return a random word read from the file
     **/
   public   String chooseWord( )
    {
        Random random=new Random();
        int wordNum= random.nextInt(NO_OF_WORDS);
        String word="";
         try {
             word=this.cache[wordNum];
      //        InputStreamReader reader=new InputStreamReader(new FileInputStream(fileName));
        //      BufferedReader bufferedReader=new BufferedReader(reader);
          //     for(int i=0;i<wordNum;i++)
            //        word=bufferedReader.readLine();
              System.out.println(word+" Line No "+String.valueOf(wordNum));
              //reader.close();
              
              }
          
          catch( Exception e) {
               System.out.println("Can not read from cache");
          }
          return word;
     }
//////////////////////////////////////////////////////////////////////////////////
/** 
 * Sends the current view of the secret word and the number of tries remained
 *   seperated by '#' to the client.
 * @throws IOException  If an input or output exception occurred
**/
 public void sendCurrentWordView() throws IOException
 {
      StringBuffer strOut=new StringBuffer(clientWordView.toString());          
      strOut.append('#');
      strOut.append(String.valueOf(failedCounter));
      send(strOut.toString());     
 }
 //////////////////////////////////////////////////////////////////////////////////
 /**
  *Sends an string message to the client
  *@param strMsg The message to be sent to client
  *@throws IOException  If an input or output   exception occurred
  **/
 public void send(String strMsg)throws IOException
 {
      byte[] sentMsg=strMsg.getBytes();
      out.write(sentMsg,0,strMsg.length());
      out.flush();
 }
 //////////////////////////////////////////////////////////////////////////////////
 /**
  *listens to the client's request and responds.
  *It processes client's requests.
  **/
 public void run() {
	try {
              in = new BufferedInputStream(clientSocket.getInputStream());
	      out = new BufferedOutputStream(clientSocket.getOutputStream());
              boolean endGame=false,letterFound=false;
              newGame();
              sendCurrentWordView();   
	      while(!endGame && !this.clientSocket.isClosed())
              {
                byte[] receivedMsg = new byte[MAX_WORD_LEN];
                int n;
                boolean found=false;
                while(!found &&failedCounter>0)
                {
                 if((n = in.read(receivedMsg,0, MAX_WORD_LEN))>0 && !endGame)  
                 {
                  String clientAnswer=(new String(receivedMsg)).substring(0,n);
                  if(clientAnswer.length()==1)
                  {
                    letterFound=false; 
                    //look the letter up in the server word 
                    for(int i=0;i<serverWord.length();i++) 
                        //if the letter is found
                        if( (String.valueOf(serverWord.charAt(i))).equalsIgnoreCase(String.valueOf(clientAnswer.charAt(0))))
                        {
                          letterFound=true;
        //                          char fc=serverWord.charAt(i);
                          //if the letter hasn't been guessed before
                          if(!(String.valueOf(clientWordView.charAt(i))).equalsIgnoreCase(String.valueOf(clientAnswer.charAt(0))))
                         {
                           //update WordView and the total score
                          clientWordView.setCharAt(i,serverWord.charAt(i)); 
                          totalScore+=LETTER_SCORE;
                         }
                           
                        }
                    //if the word is complete
                    if(clientWordView.toString().equalsIgnoreCase(serverWord))
                        found=true;
                    else
                    {
                         if(letterFound==false)
                            failedCounter--;
                         if(failedCounter>0)sendCurrentWordView();
                    }
                  }
                  //if the client sent  a word  or a message
                  else
                      {
                        //if the client requests to end the game
                        if(clientAnswer.equals("end game"))
                        {
                          endGame=true;
                          break;
                        }
                        //if the client requests to continue the game
                        else if(clientAnswer.equals("continue game"))
                         {
                           newGame();
                           sendCurrentWordView();
                         }
                     
                        else
                        //if the clients has guessed a complete word
                          if(clientAnswer.equalsIgnoreCase(serverWord))
                            found=true;
                          else
                            failedCounter=0;
                    }
            }
           } //end of second while loop: for a single game
          
            if(found==true)
            {
                totalScore+=WORD_SCORE;
                send("!Congratulations"+"#"+serverWord+"#"+String.valueOf(totalScore));
            }
            else 
            {
                failedCounter=5;
                totalScore=0;
                send("$game over;you lose"+"#"+serverWord+"#"+String.valueOf(totalScore));
              
            }
           }
              in.close();
              out.close();
            System.out.println("cleint ended game.");
          } catch( IOException e1)
            {
	      System.out.println(e1.toString());
	    }
 }
//////////////////////////////////////////////////////////////////////////////////
 /**
  *Starts a new game by choosing a word randomly from the dictionary.
  *After choosing the word, it resets the client view 
  **/
 public void newGame()
 {
    try{ 
    // totalScore=0;
     failedCounter=MAX_TRIES;
     serverWord=chooseWord();
      if (clientWordView!=null)
         clientWordView.delete(0,clientWordView.length());
      else
     clientWordView=new StringBuffer();
     for(int i=0;i<serverWord.length();i++)
         clientWordView.append('-');
    }
    catch( Exception e)
    {
        System.out.println("An Exception occured.Details;"+e.toString());
    }
    
 }
   
}
